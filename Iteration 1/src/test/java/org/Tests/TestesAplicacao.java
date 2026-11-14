package org.Tests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.Controller.*;
import org.Model.*;
import org.Persistence.RepositorioDados;
import org.Utils.DadosDemonstracao;

/** Testes de regras e integração dos doze UC, sem dependências externas. */
public final class TestesAplicacao {
    private static int verificacoes;
    private TestesAplicacao() { }

    /**
     * Executa os testes, incluindo a geração pendente por decisão expressa do grupo.
     * @param args argumentos não utilizados
     * @throws Exception se um teste falhar ou o sistema de ficheiros não estiver disponível
     */
    public static void main(String[] args) throws Exception {
        testarContas();
        testarCatalogos();
        testarPredefinidos();
        testarPecas();
        testarOutfits();
        testarPersistencia();
        System.out.println("PASSOU: " + verificacoes + " verificações.");
    }

    private static ContextoAplicacao demoUtilizador() {
        var c = DadosDemonstracao.criar();
        new Autenticar_Controller(c).autenticar("utilizador@demo.pt", "Demo123!");
        return c;
    }
    private static void testarContas() {
        var c = DadosDemonstracao.criar();
        var auth = new Autenticar_Controller(c);
        falha(() -> new AdicionarPeca_Controller(c), "Autenticação obrigatória");
        falha(() -> auth.autenticar("utilizador@demo.pt", "errada"), "Palavra-passe inválida");
        falha(() -> c.getAssistente().getUtilizadorAtual(), "Tentativa falhada não inicia sessão");
        auth.autenticar("UTILIZADOR@DEMO.PT", "Demo123!");
        igual(3, pecas(c).size(), "Email sem distinção de maiúsculas");
        falha(() -> new DefinirTipoRoupa_Controller(c), "UC12 reservado ao administrador");
        falha(() -> new DefinirCaracteristica_Controller(c), "UC10 reservado ao administrador");
        var registo = new RegistarUtilizador_Controller(c);
        falha(() -> registo.setDados("Nome", "utilizador@demo.pt", "123"), "UC11 email único");
        falha(() -> registo.setDados("Nome", "sem-email", "123"), "UC11 email válido");
        falha(() -> registo.setDados(" ", "nova@teste.pt", "123"), "UC11 nome obrigatório");
        falha(() -> registo.setDados("Nome", "nova@teste.pt", " "), "UC11 palavra-passe obrigatória");
        registo.setDados("Nova", "nova@teste.pt", "Segredo123!");
        ok(!registo.obterDados().contains("Segredo123!"), "Confirmação não apresenta palavra-passe");
        falha(() -> auth.autenticar("nova@teste.pt", "Segredo123!"), "Rascunho de conta não é registado");
        registo.registarUtilizador();
        auth.autenticar("nova@teste.pt", "Segredo123!");
        igual(0, pecas(c).size(), "Conta nova tem guarda-roupa vazio");
        falha(registo::registarUtilizador, "Não confirma o mesmo rascunho duas vezes");
        auth.terminarSessao();
        falha(() -> c.getAssistente().getUtilizadorAtual(), "Termina a sessão");
        auth.autenticar("admin@demo.pt", "Admin123!");
        falha(() -> new ConsultarGuardaRoupa_Controller(c), "Administrador não acede ao guarda-roupa dos utilizadores");
        falha(() -> c.getAssistente().criarAdministradorInicial("Outro", "outro@teste.pt", "123"), "Configuração administrativa inicial única");
    }

    private static void testarCatalogos() {
        var c = DadosDemonstracao.criar();
        new Autenticar_Controller(c).autenticar("admin@demo.pt", "Admin123!");
        var tipos = new DefinirTipoRoupa_Controller(c);
        int antes = c.getAssistente().obterListaTiposRoupa().size();
        tipos.adicionarTipoRoupa("Camisa");
        igual(antes, c.getAssistente().obterListaTiposRoupa().size(), "UC12 lote ainda não confirmado");
        falha(() -> tipos.adicionarTipoRoupa(" CAMISA "), "UC12 duplicado no lote");
        falha(() -> tipos.adicionarTipoRoupa("calças"), "UC12 duplicado no catálogo");
        falha(() -> tipos.adicionarTipoRoupa(" "), "UC12 designação obrigatória");
        tipos.registarListagem();
        igual(antes + 1, c.getAssistente().obterListaTiposRoupa().size(), "UC12 regista lote completo");
        falha(tipos::registarListagem, "UC12 lote vazio rejeitado");
        tipos.adicionarTipoRoupa("Saia");
        tipos.novaListagem();
        igual(antes + 1, c.getAssistente().obterListaTiposRoupa().size(), "UC12 descartar lote não altera catálogo");
        var cores = new DefinirCor_Controller(c);
        cores.adicionarCor("Verde");
        cores.adicionarCor("Roxo");
        falha(() -> cores.adicionarCor(" verde "), "UC7 duplicado no lote");
        falha(() -> cores.adicionarCor("Branco"), "UC7 duplicado no catálogo");
        igual(3, c.getAssistente().obterListaCores().size(), "UC7 lote sem confirmação não modifica estado");
        cores.registarListagem();
        igual(5, c.getAssistente().obterListaCores().size(), "UC7 regista várias cores");
        var ocasioes = new DefinirOcasiao_Controller(c);
        ocasioes.adicionarOcasiao("Casamento");
        falha(() -> ocasioes.adicionarOcasiao("CASAMENTO"), "UC8 duplicado no lote");
        ocasioes.registarListagem();
        igual(4, c.getAssistente().obterListaOcasioes().size(), "UC8 regista ocasião");
        var estacoes = new DefinirEstacao_Controller(c);
        estacoes.adicionarEstacao("Meia-estação");
        falha(() -> estacoes.adicionarEstacao("meia-estação"), "UC9 duplicado no lote");
        falha(() -> estacoes.adicionarEstacao("Inverno"), "UC9 duplicado no catálogo");
        falha(() -> estacoes.adicionarEstacao(" "), "UC9 designação obrigatória");
        igual(4, c.getAssistente().obterListaEstacoes().size(), "UC9 lote sem confirmação não altera catálogo");
        estacoes.registarListagem();
        igual(5, c.getAssistente().obterListaEstacoes().size(), "UC9 regista apenas a designação");
        igual("Inverno", c.getAssistente().obterListaEstacoes().get(3).toString(), "Estação não apresenta datas");
        var caracteristica = new DefinirCaracteristica_Controller(c);
        falha(() -> caracteristica.setDados("Algodão", "Outra descrição"), "UC10 designação única");
        falha(() -> caracteristica.setDados("Lã", " "), "UC10 descrição obrigatória");
        caracteristica.setDados("Lã", "Tecido quente");
        igual(1, c.getAssistente().obterListaCaracteristicas().size(), "UC10 rascunho não registado");
        caracteristica.registarCaracteristica();
        igual(2, c.getAssistente().obterListaCaracteristicas().size(), "UC10 confirma característica");
        int quantidade = c.getAssistente().obterListaTiposRoupa().size();
        falha(() -> c.alterar(a -> { a.registarTiposRoupa(List.of(new TipoRoupa("Boné"), new TipoRoupa("Calças"))); return null; }), "UC12 lote com um duplicado é rejeitado integralmente");
        igual(quantidade, c.getAssistente().obterListaTiposRoupa().size(), "UC12 não regista lote parcial");
        falha(() -> c.getAssistente().obterListaTiposRoupa().add(new TipoRoupa("Injetado")), "Catálogo devolvido não pode ser alterado externamente");
    }

    private static void testarPredefinidos() {
        var contexto = new ContextoAplicacao(new AssistenteDigital(), null);
        ok(contexto.getAssistente().catalogosEmFalta(), "Domínio novo tem catálogos em falta");
        contexto.alterar(a -> { a.carregarPredefinidos(); return null; });
        ok(!contexto.getAssistente().catalogosEmFalta(), "Predefinidos preenchem os cinco catálogos");
        var auth = new Autenticar_Controller(contexto);
        auth.configurarAdministrador("Admin", "admin@teste.pt", "Admin123!");
        auth.autenticar("admin@teste.pt", "Admin123!");
        var a = contexto.getAssistente();
        igual(CatalogosPredefinidos.TIPOS_ROUPA.size(), a.obterListaTiposRoupa().size(), "Tipos de roupa predefinidos");
        igual(CatalogosPredefinidos.CORES.size(), a.obterListaCores().size(), "Cores predefinidas");
        igual(4, a.obterListaEstacoes().size(), "Quatro estações predefinidas");
        igual(CatalogosPredefinidos.OCASIOES.size(), a.obterListaOcasioes().size(), "Ocasiões predefinidas");
        igual(CatalogosPredefinidos.CARACTERISTICAS.size(), a.obterListaCaracteristicas().size(), "Características predefinidas");
        falha(() -> new DefinirCor_Controller(contexto).adicionarCor("branco"), "Predefinida conta como existente");
        var cores = new DefinirCor_Controller(contexto);
        cores.adicionarCor("Dourado");
        cores.registarListagem();
        int antes = contexto.getAssistente().obterListaCores().size();
        contexto.alterar(x -> { x.carregarPredefinidos(); return null; });
        igual(antes, contexto.getAssistente().obterListaCores().size(), "Recarregar não altera catálogo já definido");
    }

    private static void testarPecas() {
        var c = demoUtilizador();
        var original = pecas(c).get(0);
        var adicionar = new AdicionarPeca_Controller(c);
        falha(() -> preencher(adicionar, original, " CAMISOLA BRANCA "), "UC1 nome único por guarda-roupa");
        falha(() -> adicionar.setDados("Nome", null, original.getCor(), original.getEstacoes(), original.getOcasioes(), List.of()), "UC1 tipo obrigatório");
        falha(() -> adicionar.setDados("Nome", new TipoRoupa("Desconhecido"), original.getCor(), original.getEstacoes(), original.getOcasioes(), List.of()), "UC1 tipo tem de existir no catálogo");
        falha(() -> adicionar.setDados("Nome", original.getTipoRoupa(), null, original.getEstacoes(), original.getOcasioes(), List.of()), "UC1 cor obrigatória");
        falha(() -> adicionar.setDados("Nome", original.getTipoRoupa(), original.getCor(), List.of(), original.getOcasioes(), List.of()), "UC1 pelo menos uma estação");
        falha(() -> adicionar.setDados("Nome", original.getTipoRoupa(), original.getCor(), original.getEstacoes(), List.of(), List.of()), "UC1 pelo menos uma ocasião");
        preencher(adicionar, original, "Segunda camisola");
        igual(3, pecas(c).size(), "UC1 rascunho não altera guarda-roupa");
        adicionar.registarPeca();
        igual(4, pecas(c).size(), "UC1 confirmação regista peça");
        var nova = pecas(c).get(3);
        igual(original.getTipoRoupa(), nova.getTipoRoupa(), "UC1 conserva o tipo escolhido");
        ok(nova.isDisponivel(), "UC1 nova peça fica disponível");
        ok(nova.obterDados().contains("Tipo de roupa: Camisola"), "UC5 apresenta o tipo de roupa");
        falha(adicionar::registarPeca, "UC1 impede confirmação repetida");
        var marcar = new MarcarIndisponivel_Controller(c);
        marcar.marcarIndisponivel(original.getId());
        ok(!consulta(c).obterPeca(original.getId()).isDisponivel(), "UC4 marca indisponível");
        var editar = new EditarPeca_Controller(c);
        editar.selecionarPeca(original.getId());
        var calcas = c.getAssistente().obterListaTiposRoupa().get(1);
        editar.setDados("Peça editada", calcas, original.getCor(), original.getEstacoes(), original.getOcasioes(), List.of());
        igual("Camisola branca", consulta(c).obterPeca(original.getId()).getNome(), "UC3 cancelamento mantém dados anteriores");
        editar.guardarAlteracoes();
        var editada = consulta(c).obterPeca(original.getId());
        igual("Peça editada", editada.getNome(), "UC3 altera o nome");
        igual(calcas, editada.getTipoRoupa(), "UC3 altera o tipo de roupa");
        ok(!editada.isDisponivel(), "UC3 preserva a indisponibilidade");
        igual(original.getId(), editada.getId(), "UC3 preserva o identificador da peça");
        editar.selecionarPeca(original.getId());
        falha(() -> editar.setDados("Calças azuis", calcas, original.getCor(), original.getEstacoes(), original.getOcasioes(), List.of()), "UC3 nome de outra peça rejeitado");
        igual("Peça editada", consulta(c).obterPeca(original.getId()).getNome(), "UC3 dados inválidos não modificam a peça");
        var registo = new RegistarUtilizador_Controller(c);
        registo.setDados("Outra", "outra@teste.pt", "Outra123!");
        registo.registarUtilizador();
        new Autenticar_Controller(c).autenticar("outra@teste.pt", "Outra123!");
        igual(0, pecas(c).size(), "Guarda-roupa separado por utilizador");
        falha(() -> consulta(c).obterPeca(original.getId()), "UC5 não consulta peça alheia");
        falha(() -> new RemoverPeca_Controller(c).removerPeca(original.getId()), "UC2 não remove peça alheia");
        falha(() -> new EditarPeca_Controller(c).selecionarPeca(original.getId()), "UC3 não edita peça alheia");
        falha(() -> new MarcarIndisponivel_Controller(c).marcarIndisponivel(original.getId()), "UC4 não altera peça alheia");
        preencher(new AdicionarPeca_Controller(c), original, "Peça editada");
        igual(0, pecas(c).size(), "Outro rascunho cancelado não regista peça");
        new Autenticar_Controller(c).autenticar("utilizador@demo.pt", "Demo123!");
        new RemoverPeca_Controller(c).removerPeca(original.getId());
        igual(3, pecas(c).size(), "UC2 remove a peça selecionada");
        falha(() -> consulta(c).obterPeca(original.getId()), "UC2 peça removida deixa o guarda-roupa");
    }

    private static void testarOutfits() {
        var c = demoUtilizador();
        var a = c.getAssistente();
        var ocasiao = a.obterListaOcasioes().get(0);
        var inverno = a.obterListaEstacoes().get(3);
        var pedido = new SolicitarOutfit_Controller(c);
        falha(() -> pedido.selecionarOcasiao(null), "UC6 ocasião obrigatória");
        falha(() -> pedido.selecionarOcasiao(new Ocasiao("Ausente")), "UC6 ocasião definida");
        pedido.selecionarOcasiao(ocasiao);
        falha(() -> pedido.selecionarEstacao(null), "UC6 estação obrigatória");
        falha(() -> pedido.selecionarEstacao(new Estacao("Ausente")), "UC6 estação definida");
        pedido.selecionarEstacao(inverno);
        igual("Ocasião: Aulas\nEstação: Inverno", pedido.obterDadosPedido(), "UC6 estação escolhida sem data");
        igual(0, c.getAssistente().exigirUtilizador().getPedidos().size(), "UC6 preparação não regista pedido");
        List<PecaVestuario> antes = pecas(c);
        boolean pendente = false;
        try { pedido.confirmar(); } catch (ComposicaoPendenteException e) { pendente = true; }
        ok(pendente, "UC6 geração pendente até decisão do docente");
        igual(0, c.getAssistente().exigirUtilizador().getPedidos().size(), "UC6 pendente não regista pedido nem outfit");
        igual(antes.stream().map(PecaVestuario::getId).toList(), pecas(c).stream().map(PecaVestuario::getId).toList(), "UC6 pendente preserva o guarda-roupa");
        ok(pecas(c).stream().allMatch(PecaVestuario::isDisponivel), "UC6 não altera disponibilidade");
        var verao = a.obterListaEstacoes().get(1);
        pedido.selecionarOcasiao(ocasiao);
        pedido.selecionarEstacao(verao);
        ok(pedido.obterDadosPedido().contains("Verão"), "UC6 permite selecionar outra estação livremente");
        new MarcarIndisponivel_Controller(c).marcarIndisponivel(pecas(c).get(0).getId());
        igual(2, c.getAssistente().getGuardaRoupaAtual().filtrarElegiveis(ocasiao, inverno).size(), "Filtragem exclui peças indisponíveis");
        // Política de teste: apenas verifica o encadeamento do serviço, não define uma regra do produto.
        var tecnico = new ContextoAplicacao(c.getAssistente(), null, new GeradorOutfits(List::copyOf));
        var teste = new SolicitarOutfit_Controller(tecnico);
        teste.selecionarOcasiao(ocasiao);
        teste.selecionarEstacao(inverno);
        PedidoOutfit resultado = teste.confirmar();
        igual(2, resultado.getOutfit().getPecas().size(), "Serviço usa apenas as candidatas da filtragem");
        igual(inverno, resultado.getEstacao(), "Pedido conserva a estação escolhida");
        igual(1, tecnico.getAssistente().exigirUtilizador().getPedidos().size(), "Fluxo de registo funciona quando é injetada uma política de teste");
        falha(teste::confirmar, "Confirmação repetida rejeitada");
        var vazia = new ContextoAplicacao(c.getAssistente(), null, new GeradorOutfits(lista -> List.of()));
        var impossivel = new SolicitarOutfit_Controller(vazia);
        impossivel.selecionarOcasiao(ocasiao);
        impossivel.selecionarEstacao(inverno);
        falha(impossivel::confirmar, "UC6 distingue peças elegíveis de combinação impossível");
        igual(0, vazia.getAssistente().exigirUtilizador().getPedidos().size(), "Combinação impossível não regista pedido");
        for (var peca : pecas(c)) new MarcarIndisponivel_Controller(c).marcarIndisponivel(peca.getId());
        falha(pedido::confirmar, "UC6 sem peças disponíveis termina sem gerar");
        igual(0, c.getAssistente().exigirUtilizador().getPedidos().size(), "Falta de peças não regista pedido");
        var fonte = demoUtilizador();
        var candidatas = pecas(fonte);
        var fora = candidatas.get(0);
        new RemoverPeca_Controller(fonte).removerPeca(fora.getId());
        falha(() -> new GeradorOutfits(lista -> List.of(fora)).gerar(pecas(fonte), ocasiao, inverno), "Gerador rejeita peça fora das candidatas");
        falha(() -> new GeradorOutfits(lista -> List.of(lista.get(0), lista.get(0))).gerar(pecas(fonte), ocasiao, inverno), "Gerador rejeita repetição da mesma peça");
        var g = fonte.getAssistente().getGuardaRoupaAtual();
        var amostra = pecas(fonte).get(0);
        var soVerao = g.novaPeca("Só verão", amostra.getTipoRoupa(), amostra.getCor(), List.of(verao), List.of(ocasiao), List.of());
        ok(!soVerao.isAdequada(ocasiao, inverno), "Adequação exige estação selecionada");
        ok(!amostra.isAdequada(new Ocasiao("Outra ocasião"), inverno), "Adequação exige ocasião selecionada");
    }

    private static void testarPersistencia() throws Exception {
        Path pasta = Files.createTempDirectory("anams-atualizado-");
        try {
            var c = demoUtilizador();
            Path dados = pasta.resolve("dados.dat");
            var repositorio = new RepositorioDados(dados);
            repositorio.guardar(c.getAssistente());
            var carregado = repositorio.carregar();
            falha(carregado::getUtilizadorAtual, "Sessão não é persistida");
            carregado.autenticar("utilizador@demo.pt", "Demo123!");
            igual(3, carregado.getGuardaRoupaAtual().obterListaPecas().size(), "Gravação opcional recupera peças");
            igual("Camisola", carregado.getGuardaRoupaAtual().obterListaPecas().get(0).getTipoRoupa().getDesignacao(), "Gravação opcional recupera tipo de roupa");
            igual("Inverno", carregado.obterListaEstacoes().get(3).getDesignacao(), "Gravação opcional recupera estação sem datas");
            String bruto = new String(Files.readAllBytes(dados), StandardCharsets.ISO_8859_1);
            ok(!bruto.contains("Demo123!") && !bruto.contains("Admin123!"), "Passwords não ficam em texto simples");
            Path bloqueio = pasta.resolve("nao-e-pasta");
            Files.writeString(bloqueio, "bloqueio");
            var semEscrita = new ContextoAplicacao(carregado, new RepositorioDados(bloqueio.resolve("dados.dat")));
            var id = pecas(semEscrita).get(0).getId();
            falha(() -> new RemoverPeca_Controller(semEscrita).removerPeca(id), "Erro de escrita comunicado");
            igual(3, pecas(semEscrita).size(), "Erro de escrita não aplica remoção em memória");
            Files.writeString(dados, "danificado");
            boolean rejeitado = false;
            try { repositorio.carregar(); } catch (IOException e) { rejeitado = true; }
            ok(rejeitado, "Ficheiro danificado rejeitado");
            igual("danificado", Files.readString(dados), "Ficheiro danificado preservado");
        } finally {
            try (var caminhos = Files.walk(pasta)) {
                for (var caminho : caminhos.sorted(java.util.Comparator.reverseOrder()).toList()) Files.deleteIfExists(caminho);
            }
        }
    }

    private static ConsultarGuardaRoupa_Controller consulta(ContextoAplicacao c) { return new ConsultarGuardaRoupa_Controller(c); }
    private static List<PecaVestuario> pecas(ContextoAplicacao c) { return consulta(c).obterListaPecas(); }
    private static void preencher(AdicionarPeca_Controller ctrl, PecaVestuario peca, String nome) {
        ctrl.setDados(nome, peca.getTipoRoupa(), peca.getCor(), peca.getEstacoes(), peca.getOcasioes(), peca.getCaracteristicas());
    }
    private static void igual(Object esperado, Object atual, String mensagem) {
        ok(java.util.Objects.equals(esperado, atual), mensagem + ": esperado=" + esperado + ", atual=" + atual);
    }
    private static void ok(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
        verificacoes++;
    }
    private static void falha(Runnable acao, String mensagem) {
        boolean falhou = false;
        try { acao.run(); }
        catch (IllegalArgumentException | IllegalStateException | UnsupportedOperationException e) { falhou = true; }
        ok(falhou, mensagem);
    }
}
