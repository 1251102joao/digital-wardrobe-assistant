package org.Model;

import org.Utils.Idioma;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.Persistence.Serializacao;

/**
 * Ponto de acesso da aplicação, adaptado de AssistenteDigital do código base.
 * Conhece contas, sessão e catálogos; aplica as validações globais do relatório.
 * A estrutura UI/Controller/Model deriva do código base de Dulce Mota.
 */
public final class AssistenteDigital implements Serializable {
    private static final long serialVersionUID = 3L;
    /** Tipos de roupa registados no UC12. */
    private final List<TipoRoupa> tiposRoupa = new ArrayList<>();
    /** Catálogo das cores registadas. */
    private final List<Cor> cores = new ArrayList<>();
    /** Ocasiões registadas ou associadas à peça. */
    private final List<Ocasiao> ocasioes = new ArrayList<>();
    /** Estações registadas ou associadas à peça. */
    private final List<Estacao> estacoes = new ArrayList<>();
    /** Características fixas do sistema, associadas às peças. */
    private final List<Caracteristica> caracteristicas = new ArrayList<>();
    /** Partes do corpo fixas do sistema, associadas às peças. */
    private final List<ParteCorpo> partesCorpo = new ArrayList<>();
    /** Contas registadas, incluindo o administrador. */
    private final List<Utilizador> utilizadores = new ArrayList<>();
    private transient Utilizador utilizadorAtual;

    /** Cria um domínio com as listas fixas (características e partes do corpo); a primeira execução configura o administrador. */
    public AssistenteDigital() {
        for (String[] c : CatalogosPredefinidos.CARACTERISTICAS) caracteristicas.add(new Caracteristica(c[0], c[1]));
        CatalogosPredefinidos.PARTES_CORPO.forEach(n -> partesCorpo.add(new ParteCorpo(n)));
    }
    /**
     * Indica se ainda é necessário configurar a conta inicial.
     *
     * @return indica se ainda é necessário configurar a conta inicial.
     */
    public boolean necessitaAdministrador() { return utilizadores.isEmpty(); }
    /**
     * Permite criar o primeiro administrador uma única vez, antes de existirem contas.
     *
     * @param nome nome obrigatório
     * @param email endereço de email da conta
     * @param password palavra-passe; é convertida em hash antes de ser guardada
     */
    public void criarAdministradorInicial(String nome, String email, String password) {
        if (!necessitaAdministrador()) throw new IllegalStateException(Idioma.t("O administrador já foi configurado."));
        utilizadores.add(new Utilizador(nome, email, password, true));
    }
    /**
     * Creator de um rascunho de utilizador comum (UC11).
     *
     * @param nome nome obrigatório
     * @param email endereço de email da conta
     * @param password palavra-passe; é convertida em hash antes de ser guardada
     * @return creator de um rascunho de utilizador comum (UC11).
     */
    public Utilizador novoUtilizador(String nome, String email, String password) {
        return new Utilizador(nome, email, password, false);
    }
    /**
     * Validação global do email único, incluindo a conta do administrador.
     *
     * @param utilizador conta candidata ao registo
     */
    public void validarUtilizador(Utilizador utilizador) {
        if (utilizadores.stream().anyMatch(u -> u.getEmail().equals(utilizador.getEmail()))) {
            throw new IllegalArgumentException(Idioma.t("Já existe uma conta com esse email."));
        }
    }
    /**
     * Regista uma conta comum depois da confirmação.
     *
     * @param utilizador conta candidata ao registo
     */
    public void registarUtilizador(Utilizador utilizador) {
        if (necessitaAdministrador()) throw new IllegalStateException(Idioma.t("Configure primeiro o administrador."));
        if (utilizador.isAdministrador()) throw new IllegalArgumentException(Idioma.t("O registo público cria apenas utilizadores."));
        validarUtilizador(utilizador);
        utilizadores.add(utilizador);
    }
    /**
     * Inicia uma sessão; uma tentativa falhada elimina qualquer sessão anterior.
     *
     * @param email endereço de email da conta
     * @param password palavra-passe; é convertida em hash antes de ser guardada
     */
    public void autenticar(String email, String password) {
        terminarSessao();
        String normalizado = email == null ? "" : email.strip();
        Utilizador conta = utilizadores.stream().filter(u -> u.getEmail().equalsIgnoreCase(normalizado))
                .findFirst().orElse(null);
        if (conta == null || !conta.autentica(password)) {
            throw new IllegalArgumentException(Idioma.t("Email ou palavra-passe incorretos."));
        }
        utilizadorAtual = conta;
    }
    /** Termina a sessão atual. */
    public void terminarSessao() { utilizadorAtual = null; }
    /**
     * Obtém a conta autenticada ou rejeita acesso anónimo.
     *
     * @return obtém a conta autenticada ou rejeita acesso anónimo.
     */
    public Utilizador getUtilizadorAtual() {
        if (utilizadorAtual == null) throw new IllegalStateException(Idioma.t("É necessário iniciar sessão."));
        return utilizadorAtual;
    }
    /** Impõe o papel de administrador também no domínio, além do menu. */
    public void exigirAdministrador() {
        if (!getUtilizadorAtual().isAdministrador()) {
            throw new IllegalStateException(Idioma.t("Esta operação exige uma conta de administrador."));
        }
    }
    /**
     * Impõe o papel de utilizador final para operações do guarda-roupa.
     *
     * @return impõe o papel de utilizador final para operações do guarda-roupa.
     */
    public Utilizador exigirUtilizador() {
        Utilizador conta = getUtilizadorAtual();
        if (conta.isAdministrador()) throw new IllegalStateException(Idioma.t("Inicie sessão como utilizador final."));
        return conta;
    }
    /**
     * Devolve apenas o guarda-roupa do utilizador autenticado.
     *
     * @return devolve apenas o guarda-roupa do utilizador autenticado.
     */
    public GuardaRoupa getGuardaRoupaAtual() { return exigirUtilizador().getGuardaRoupa(); }
    /**
     * Lista imutável das cores.
     *
     * @return lista imutável das cores.
     */
    public List<Cor> obterListaCores() { getUtilizadorAtual(); return List.copyOf(cores); }
    /**
     * Lista imutável das ocasiões.
     *
     * @return lista imutável das ocasiões.
     */
    public List<Ocasiao> obterListaOcasioes() { getUtilizadorAtual(); return List.copyOf(ocasioes); }
    /**
     * Lista imutável das categorias sazonais definidas.
     *
     * @return lista imutável das categorias sazonais definidas.
     */
    public List<Estacao> obterListaEstacoes() { getUtilizadorAtual(); return List.copyOf(estacoes); }
    /**
     * Lista imutável das características.
     *
     * @return lista imutável das características.
     */
    public List<Caracteristica> obterListaCaracteristicas() { getUtilizadorAtual(); return List.copyOf(caracteristicas); }
    /**
     * Lista imutável das partes do corpo.
     *
     * @return partes do corpo a que uma peça pode estar associada
     */
    public List<ParteCorpo> obterListaPartesCorpo() { getUtilizadorAtual(); return List.copyOf(partesCorpo); }

    /**
     * Creator de uma cor; apenas validação local.
     *
     * @param designacao designação obrigatória da referência
     * @return creator de uma cor; apenas validação local.
     */
    public Cor novaCor(String designacao) { exigirAdministrador(); return new Cor(designacao); }
    /**
     * Creator de uma ocasião; apenas validação local.
     *
     * @param designacao designação obrigatória da referência
     * @return creator de uma ocasião; apenas validação local.
     */
    public Ocasiao novaOcasiao(String designacao) { exigirAdministrador(); return new Ocasiao(designacao); }
    /**
     * Cria uma estação sem datas associadas.
     * @param designacao nome obrigatório da estação
     * @return categoria sazonal ainda não registada
     */
    public Estacao novaEstacao(String designacao) {
        exigirAdministrador(); return new Estacao(designacao);
    }
    /**
     * Cria um tipo de roupa para a listagem em construção (UC12).
     * @param designacao designação obrigatória do tipo
     * @return tipo ainda não registado
     */
    public TipoRoupa novoTipoRoupa(String designacao) {
        exigirAdministrador(); return new TipoRoupa(designacao);
    }
    /**
     * Disponibiliza os tipos de roupa definidos pelo administrador.
     * @return lista imutável do catálogo
     */
    public List<TipoRoupa> obterListaTiposRoupa() {
        getUtilizadorAtual(); return List.copyOf(tiposRoupa);
    }
    /**
     * Regista o lote completo de tipos de roupa após confirmação.
     * @param novos tipos de roupa a validar e registar
     */
    public void registarTiposRoupa(List<TipoRoupa> novos) { registarLote(novos, tiposRoupa); }
    /**
     * Verifica um candidato perante o catálogo e a listagem ainda não confirmada.
     *
     * @param nova referência candidata a registo
     * @param existentes referências já registadas
     * @param pendentes referências válidas ainda em construção
     * @param <T> tipo dos elementos ou do resultado
     */
    public <T extends Referencia> void validarReferencia(T nova, List<T> existentes, List<T> pendentes) {
        exigirAdministrador();
        if (existentes.contains(nova) || pendentes.contains(nova)) {
            throw new IllegalArgumentException(Idioma.t("A designação já existe no sistema ou na listagem em construção."));
        }
    }
    private <T extends Referencia> void registarLote(List<T> novos, List<T> destino) {
        exigirAdministrador();
        if (novos == null || novos.isEmpty()) throw new IllegalArgumentException(Idioma.t("A listagem está vazia."));
        List<T> validados = new ArrayList<>();
        for (T novo : novos) {
            java.util.Objects.requireNonNull(novo, Idioma.t("Referência em falta."));
            validarReferencia(novo, destino, validados);
            validados.add(novo);
        }
        destino.addAll(validados); // Só altera o catálogo se o lote inteiro for válido.
    }
    /**
     * Regista o lote inteiro do UC7.
     *
     * @param novas listagem completa a confirmar
     */
    public void registarCores(List<Cor> novas) { registarLote(novas, cores); }
    /**
     * Regista o lote inteiro do UC8.
     *
     * @param novas listagem completa a confirmar
     */
    public void registarOcasioes(List<Ocasiao> novas) { registarLote(novas, ocasioes); }
    /**
     * Regista o lote inteiro de estações do UC9, sem designações duplicadas.
     *
     * @param novas listagem completa a confirmar
     */
    public void registarEstacoes(List<Estacao> novas) { registarLote(novas, estacoes); }
    /**
     * Garante que a peça usa exclusivamente informação dos catálogos registados.
     *
     * @param peca peça validada a registar ou verificar
     */
    public void validarReferencias(PecaVestuario peca) {
        exigirUtilizador();
        if (!tiposRoupa.contains(peca.getTipoRoupa()) || !cores.containsAll(peca.getCores())
                || !partesCorpo.containsAll(peca.getPartesCorpo()) || !estacoes.containsAll(peca.getEstacoes())
                || !ocasioes.containsAll(peca.getOcasioes()) || !caracteristicas.containsAll(peca.getCaracteristicas())) {
            throw new IllegalArgumentException(Idioma.t("A peça contém referências que não estão definidas no sistema."));
        }
    }
    /**
     * Verifica se as duas escolhas do pedido continuam nos catálogos.
     * @param ocasiao ocasião escolhida pelo utilizador
     * @param estacao estação escolhida pelo utilizador, em qualquer altura do ano
     */
    public void validarEscolhasOutfit(Ocasiao ocasiao, Estacao estacao) {
        exigirUtilizador();
        if (ocasiao == null || !ocasioes.contains(ocasiao)) {
            throw new IllegalArgumentException(Idioma.t("Selecione uma ocasião definida."));
        }
        if (estacao == null || !estacoes.contains(estacao)) {
            throw new IllegalArgumentException(Idioma.t("Selecione uma estação definida."));
        }
    }
    /**
     * Indica se algum dos catálogos definidos pelo administrador ainda está vazio.
     *
     * @return true se faltar carregar valores iniciais em pelo menos um catálogo
     */
    public boolean catalogosEmFalta() {
        return tiposRoupa.isEmpty() || cores.isEmpty() || ocasioes.isEmpty() || estacoes.isEmpty();
    }
    /**
     * Preenche com os valores iniciais apenas os catálogos que ainda estão vazios; nunca altera um catálogo já
     * definido pelo administrador. Usado no arranque normal, antes de existir sessão.
     */
    public void carregarPredefinidos() {
        if (tiposRoupa.isEmpty()) CatalogosPredefinidos.TIPOS_ROUPA.forEach(n -> tiposRoupa.add(new TipoRoupa(n)));
        if (cores.isEmpty()) CatalogosPredefinidos.CORES.forEach(n -> cores.add(new Cor(n)));
        if (ocasioes.isEmpty()) CatalogosPredefinidos.OCASIOES.forEach(n -> ocasioes.add(new Ocasiao(n)));
        if (estacoes.isEmpty()) CatalogosPredefinidos.ESTACOES.forEach(n -> estacoes.add(new Estacao(n)));
    }
    /**
     * Cópia de trabalho para uma transação; a sessão não é gravada em disco.
     *
     * @return cópia de trabalho para uma transação; a sessão não é gravada em disco.
     */
    public AssistenteDigital copiar() {
        AssistenteDigital copia = Serializacao.copiar(this, AssistenteDigital.class);
        if (utilizadorAtual != null) {
            copia.utilizadorAtual = copia.utilizadores.stream()
                    .filter(u -> u.getEmail().equals(utilizadorAtual.getEmail())).findFirst().orElseThrow();
        }
        return copia;
    }
}
