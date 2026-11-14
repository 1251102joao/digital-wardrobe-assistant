package org.UI;

import org.Controller.*;
import org.Utils.Utils;

/** Interface de consulta do UC5. */
public final class ConsultarGuardaRoupa_UI {
    private final ConsultarGuardaRoupa_Controller controller;
    /**
     * Liga a UI ao controlador.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public ConsultarGuardaRoupa_UI(ContextoAplicacao contexto) { controller = new ConsultarGuardaRoupa_Controller(contexto); }
    /** Mostra a listagem e os detalhes da peça selecionada. */
    public void run() {
        var pecas = controller.obterListaPecas();
        if (pecas.isEmpty()) { System.out.println("O seu guarda-roupa está vazio."); return; }
        var peca = Utils.apresentaESeleciona(pecas, "O seu guarda-roupa");
        System.out.println(peca.obterDados());
    }
}
