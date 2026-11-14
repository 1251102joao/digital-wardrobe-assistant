package org.UI;

import org.Controller.ContextoAplicacao;
import org.Controller.DefinirTipoRoupa_Controller;
import org.Utils.Utils;

/** Interface do UC12: definição de tipos de roupa com confirmação do lote. */
public final class DefinirTipoRoupa_UI {
    private final DefinirTipoRoupa_Controller controller;

    /**
     * Liga a interface ao controlador administrativo.
     * @param contexto contexto partilhado da aplicação
     */
    public DefinirTipoRoupa_UI(ContextoAplicacao contexto) {
        controller = new DefinirTipoRoupa_Controller(contexto);
    }

    /** Solicita designações, permite correções e confirma o lote antes do registo. */
    public void run() {
        System.out.println("Introduza os tipos de roupa, um de cada vez. Escreva 0 para terminar; no fim confirma ou descarta a listagem (N).");
        while (true) {
            String designacao = Utils.obrigatorioOuFim("Designação do tipo de roupa:");
            if (designacao == null) break;
            try {
                controller.adicionarTipoRoupa(designacao);
                System.out.println("Tipo adicionado à listagem em construção.");
            } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }
        if (controller.obterListagem().isEmpty()) {
            System.out.println("A listagem está vazia. Nenhum tipo de roupa foi registado.");
            return;
        }
        Utils.apresentaLista(controller.obterListagem(), "Tipos de roupa para confirmação");
        if (Utils.confirma("Confirma a listagem completa?")) {
            controller.registarListagem();
            System.out.println("Tipos de roupa registados com sucesso.");
        } else Utils.cancelada();
    }
}
