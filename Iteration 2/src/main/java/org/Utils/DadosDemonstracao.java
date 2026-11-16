package org.Utils;

import java.util.List;
import org.Controller.*;
import org.Model.*;

/** Cenário de demonstração isolado, construído com as mesmas regras dos casos de uso. */
public final class DadosDemonstracao {
    private DadosDemonstracao() { }
    /**
     * Cria contas e catálogos de exemplo; não grava ficheiros.
     *
     * @return cria contas e catálogos de exemplo; não grava ficheiros.
     */
    public static ContextoAplicacao criar() {
        var contexto = new ContextoAplicacao(new AssistenteDigital(), null);
        var auth = new Autenticar_Controller(contexto);
        auth.configurarAdministrador("Administrador demo", "admin@demo.pt", "Admin123!");
        auth.autenticar("admin@demo.pt", "Admin123!");
        var cores = new DefinirCor_Controller(contexto);
        for (String cor : List.of("Branco", "Azul", "Preto")) cores.adicionarCor(cor);
        cores.registarListagem();
        var ocasioes = new DefinirOcasiao_Controller(contexto);
        for (String ocasiao : List.of("Aulas", "Trabalho", "Jantar")) ocasioes.adicionarOcasiao(ocasiao);
        ocasioes.registarListagem();
        var estacoes = new DefinirEstacao_Controller(contexto);
        for (String estacao : List.of("Primavera", "Verão", "Outono", "Inverno")) estacoes.adicionarEstacao(estacao);
        estacoes.registarListagem();
        var tipos = new DefinirTipoRoupa_Controller(contexto);
        for (String tipo : List.of("Camisola", "Calças", "Sapatilhas", "Vestido", "Casaco")) tipos.adicionarTipoRoupa(tipo);
        tipos.registarListagem();
        auth.terminarSessao();
        var registo = new RegistarUtilizador_Controller(contexto);
        registo.setDados("Utilizador demo", "utilizador@demo.pt", "Demo123!");
        registo.registarUtilizador();
        auth.autenticar("utilizador@demo.pt", "Demo123!");
        var a = contexto.getAssistente();
        String[] nomes = {"Camisola branca", "Calças azuis", "Sapatilhas pretas", "Vestido preto"};
        int[] tipo = {0, 1, 2, 3};
        String[][] partes = {{"Tronco", "Braços"}, {"Pernas"}, {"Pés"}, {"Tronco", "Pernas"}};
        for (int i = 0; i < nomes.length; i++) {
            var adicionar = new AdicionarPeca_Controller(contexto);
            final int k = i;
            var partesPeca = a.obterListaPartesCorpo().stream()
                    .filter(p -> List.of(partes[k]).contains(p.getDesignacao())).toList();
            adicionar.setDados(nomes[i], a.obterListaTiposRoupa().get(tipo[i]), List.of(a.obterListaCores().get(i % 3)),
                    partesPeca, a.obterListaEstacoes(), a.obterListaOcasioes(), List.of(a.obterListaCaracteristicas().get(0)));
            adicionar.registarPeca();
        }
        auth.terminarSessao();
        return contexto;
    }
}
