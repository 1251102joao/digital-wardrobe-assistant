package org.UI;

import org.Utils.Idioma;
import org.Controller.*;
import org.Utils.Utils;

/** Interface do UC13: escolher um outfit recomendado e dar-lhe uma nota de 0 a 10. */
public final class AvaliarOutfit_UI {
    private final AvaliarOutfit_Controller controller;
    /**
     * Liga a interface ao controlador.
     * @param contexto contexto partilhado da aplicação
     */
    public AvaliarOutfit_UI(ContextoAplicacao contexto) { controller = new AvaliarOutfit_Controller(contexto); }
    /** Seleciona o outfit, lê a nota e confirma antes de gravar. */
    public void run() {
        var pedidos = controller.obterPedidosPorAvaliar();
        if (pedidos.isEmpty()) { System.out.println(Idioma.t("Não tem outfits por avaliar. Solicite primeiro um outfit.")); return; }
        controller.selecionarPedido(Utils.apresentaESeleciona(pedidos, Idioma.t("Outfits por avaliar")));
        controller.setNota(Utils.nota(Idioma.t("Avaliação de 0 a 10 (c para cancelar):")));
        System.out.println(controller.obterDados());
        if (Utils.confirma(Idioma.t("Confirma a avaliação?"))) {
            controller.registarAvaliacao();
            System.out.println(Idioma.t("Avaliação registada com sucesso."));
        } else Utils.cancelada();
    }
}
