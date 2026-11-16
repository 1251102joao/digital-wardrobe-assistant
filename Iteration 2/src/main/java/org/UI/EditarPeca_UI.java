package org.UI;

import org.Utils.Idioma;
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
        var peca = Utils.apresentaESeleciona(controller.obterListaPecas(), Idioma.t("Peça a editar"));
        controller.selecionarPeca(peca.getId());
        System.out.println(Idioma.t("Dados atuais:\n") + peca.obterDados());
        System.out.println(Idioma.t("Introduza os dados completos que devem ficar na peça."));
        var dados = FormularioPeca_UI.ler(controller);
        controller.setDados(dados.nome(), dados.tipoRoupa(), dados.cores(), dados.partesCorpo(), dados.estacoes(), dados.ocasioes(), dados.caracteristicas());
        System.out.println(controller.obterDados());
        if (Utils.confirma(Idioma.t("Confirma os dados?"))) {
            controller.guardarAlteracoes();
            System.out.println(Idioma.t("Alterações guardadas com sucesso."));
        } else Utils.cancelada();
    }
}
