package org.UI;

import org.Utils.Idioma;
import org.Controller.*;
import org.Utils.Utils;

/** Interface do UC2, com confirmação obrigatória antes da remoção. */
public final class RemoverPeca_UI {
    private final RemoverPeca_Controller controller;
    /**
     * Liga a UI ao controlador.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public RemoverPeca_UI(ContextoAplicacao contexto) { controller = new RemoverPeca_Controller(contexto); }
    /** Seleciona e remove uma peça depois de confirmar os seus dados. */
    public void run() {
        var peca = Utils.apresentaESeleciona(controller.obterListaPecas(), Idioma.t("Peça a remover"));
        System.out.println(peca.obterDados());
        if (Utils.confirma(Idioma.t("Confirma a remoção desta peça?"))) {
            controller.removerPeca(peca.getId());
            System.out.println(Idioma.t("Peça removida com sucesso."));
        } else Utils.cancelada();
    }
}
