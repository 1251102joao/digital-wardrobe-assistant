package org.UI;

import org.Utils.Idioma;
import org.Controller.*;

/** Interface do UC14: mostra no ecrã o histórico de recomendações e avaliações. */
public final class ConsultarHistorico_UI {
    private final ConsultarHistorico_Controller controller;
    /**
     * Liga a interface ao controlador.
     * @param contexto contexto partilhado da aplicação
     */
    public ConsultarHistorico_UI(ContextoAplicacao contexto) { controller = new ConsultarHistorico_Controller(contexto); }
    /** Lista todos os pedidos, com as peças e a avaliação de cada um. */
    public void run() {
        var historico = controller.obterHistorico();
        if (historico.isEmpty()) { System.out.println(Idioma.t("Ainda não tem recomendações de outfits.")); return; }
        System.out.println(Idioma.t("\nHistórico de recomendações e avaliações"));
        for (int i = 0; i < historico.size(); i++) System.out.println("\n#" + (i + 1) + "\n" + historico.get(i).obterDados());
    }
}
