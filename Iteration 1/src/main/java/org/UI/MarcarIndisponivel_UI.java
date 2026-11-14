package org.UI;

import org.Controller.*;
import org.Utils.Utils;

/** UC4: seleciona uma peça disponível e marca-a como indisponível após confirmação. */
public final class MarcarIndisponivel_UI {
    private final MarcarIndisponivel_Controller controller;
    /**
     * Liga a UI ao controlador.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public MarcarIndisponivel_UI(ContextoAplicacao contexto) { controller = new MarcarIndisponivel_Controller(contexto); }
    /** Executa o UC4. */
    public void run() {
        var candidatas = controller.obterListaPecas().stream()
                .filter(p -> p.isDisponivel()).toList();
        if (candidatas.isEmpty()) {
            System.out.println("Não existem peças disponíveis.");
            return;
        }
        var peca = Utils.apresentaESeleciona(candidatas, "Selecione a peça");
        System.out.println(peca.obterDados());
        if (Utils.confirma("Confirma marcar como indisponível?")) {
            controller.marcarIndisponivel(peca.getId());
            System.out.println("Disponibilidade atualizada com sucesso.");
        } else Utils.cancelada();
    }
}
