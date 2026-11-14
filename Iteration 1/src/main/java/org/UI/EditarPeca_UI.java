package org.UI;

import org.Controller.*;
import org.Utils.Utils;

/** Interface de consola do UC3, adaptada do fluxo de confirmação do código base. */
public final class EditarPeca_UI {
    private final EditarPeca_Controller controller;
    /**
     * Liga a interface ao controlador do caso de uso.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public EditarPeca_UI(ContextoAplicacao contexto) { controller = new EditarPeca_Controller(contexto); }
    /** Executa seleção, introdução dos dados e confirmação. */
    public void run() {
        var peca = Utils.apresentaESeleciona(controller.obterListaPecas(), "Peça a editar");
        controller.selecionarPeca(peca.getId());
        System.out.println("Dados atuais:\n" + peca.obterDados());
        System.out.println("Introduza os dados completos que devem ficar na peça.");
        var dados = FormularioPeca_UI.ler(controller);
        controller.setDados(dados.nome(), dados.tipoRoupa(), dados.cor(), dados.estacoes(), dados.ocasioes(), dados.caracteristicas());
        System.out.println(controller.obterDados());
        if (Utils.confirma("Confirma os dados?")) {
            controller.guardarAlteracoes();
            System.out.println("Alterações guardadas com sucesso.");
        } else Utils.cancelada();
    }
}
