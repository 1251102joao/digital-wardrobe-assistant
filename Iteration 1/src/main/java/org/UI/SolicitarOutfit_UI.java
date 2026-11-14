package org.UI;

import org.Controller.*;
import org.Model.ComposicaoPendenteException;
import org.Utils.Utils;

/** Interface do UC6: ocasião e estação escolhidas explicitamente pelo utilizador. */
public final class SolicitarOutfit_UI {
    private final SolicitarOutfit_Controller controller;
    /**
     * Liga a interface ao controlador.
     * @param contexto contexto partilhado da aplicação
     */
    public SolicitarOutfit_UI(ContextoAplicacao contexto) { controller = new SolicitarOutfit_Controller(contexto); }
    /** Solicita as escolhas e confirma antes de gerar ou registar o resultado. */
    public void run() {
        var ocasioes = controller.obterListaOcasioes();
        if (ocasioes.isEmpty()) { System.out.println("Não existem ocasiões definidas."); return; }
        var ocasiao = Utils.apresentaESeleciona(ocasioes, "Ocasião do outfit");
        controller.selecionarOcasiao(ocasiao);
        var estacoes = controller.obterListaEstacoes();
        if (estacoes.isEmpty()) { System.out.println("Não existem estações definidas."); return; }
        var estacao = Utils.apresentaESeleciona(estacoes, "Estação pretendida (escolha livre)");
        controller.selecionarEstacao(estacao);
        System.out.println(controller.obterDadosPedido());
        if (Utils.confirma("Confirma o pedido?")) {
            try {
                var pedido = controller.confirmar();
                System.out.println("Pedido e outfit registados com sucesso.\nSugestão:\n" + pedido.getOutfit().obterDados());
            } catch (ComposicaoPendenteException e) {
                System.out.println(e.getMessage());
            }
        } else Utils.cancelada();
    }
}
