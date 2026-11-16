package org.UI;

import org.Utils.Idioma;
import org.Controller.*;
import org.Utils.Utils;

/** Interface de consola do UC1, adaptada do fluxo de confirmação do código base. */
public final class AdicionarPeca_UI {
    private final AdicionarPeca_Controller controller;
    /**
     * Liga a interface ao controlador do caso de uso.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public AdicionarPeca_UI(ContextoAplicacao contexto) { controller = new AdicionarPeca_Controller(contexto); }
    /** Executa seleção, introdução dos dados e confirmação. */
    public void run() {

        var dados = FormularioPeca_UI.ler(controller);
        controller.setDados(dados.nome(), dados.tipoRoupa(), dados.cores(), dados.partesCorpo(), dados.estacoes(), dados.ocasioes(), dados.caracteristicas());
        System.out.println(controller.obterDados());
        if (Utils.confirma(Idioma.t("Confirma os dados?"))) {
            controller.registarPeca();
            System.out.println(Idioma.t("Peça registada com sucesso."));
        } else Utils.cancelada();
    }
}
