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
        var caracteristica = new DefinirCaracteristica_Controller(contexto);
        caracteristica.setDados("Algodão", "Peça de algodão");
        caracteristica.registarCaracteristica();
        auth.terminarSessao();
        var registo = new RegistarUtilizador_Controller(contexto);
        registo.setDados("Utilizador demo", "utilizador@demo.pt", "Demo123!");
        registo.registarUtilizador();
        auth.autenticar("utilizador@demo.pt", "Demo123!");
        for (int i = 0; i < 3; i++) {
            var a = contexto.getAssistente();
            var adicionar = new AdicionarPeca_Controller(contexto);
            adicionar.setDados(List.of("Camisola branca", "Calças azuis", "Sapatilhas pretas").get(i),
                    a.obterListaTiposRoupa().get(i), a.obterListaCores().get(i), a.obterListaEstacoes(), a.obterListaOcasioes(), a.obterListaCaracteristicas());
            adicionar.registarPeca();
        }
        auth.terminarSessao();
        return contexto;
    }
}
