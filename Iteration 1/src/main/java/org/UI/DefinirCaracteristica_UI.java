package org.UI;

import org.Controller.*;
import org.Utils.Utils;

/** Interface do UC10: define uma característica. */
public final class DefinirCaracteristica_UI {
    private final DefinirCaracteristica_Controller controller;
    /**
     * Liga a UI ao controlador.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public DefinirCaracteristica_UI(ContextoAplicacao contexto) { controller = new DefinirCaracteristica_Controller(contexto); }
    /** Solicita, valida e confirma antes de registar. */
    public void run() {
        String nome = Utils.obrigatorio("Designação:");
        String descricao = Utils.obrigatorio("Descrição:");
        controller.setDados(nome, descricao);
        System.out.println(controller.obterDados());
        if (Utils.confirma("Confirma a característica?")) {
            controller.registarCaracteristica();
            System.out.println("Característica registada com sucesso.");
        } else Utils.cancelada();
    }
}
