package org.Controller;

import java.util.List;
import org.Model.*;

/** UC6: coordena as escolhas, a geração e o registo apenas depois de confirmar. */
public final class SolicitarOutfit_Controller {
    private final ContextoAplicacao contexto;
    private Ocasiao ocasiao;
    private Estacao estacao;

    /**
     * Inicia um pedido para o utilizador autenticado.
     * @param contexto contexto partilhado da execução
     */
    public SolicitarOutfit_Controller(ContextoAplicacao contexto) {
        this.contexto = contexto;
        contexto.getAssistente().exigirUtilizador();
    }
    /**
     * Ocasiões para seleção.
     * @return catálogo de ocasiões registadas
     */
    public List<Ocasiao> obterListaOcasioes() { return contexto.getAssistente().obterListaOcasioes(); }
    /**
     * Todas as estações do catálogo, sem restrições de calendário.
     * @return catálogo de estações registadas
     */
    public List<Estacao> obterListaEstacoes() { return contexto.getAssistente().obterListaEstacoes(); }
    /**
     * Seleciona uma ocasião que continua a existir no catálogo.
     * @param ocasiao ocasião escolhida pelo utilizador
     */
    public void selecionarOcasiao(Ocasiao ocasiao) {
        this.ocasiao = null;
        this.estacao = null;
        if (ocasiao == null || !obterListaOcasioes().contains(ocasiao)) {
            throw new IllegalArgumentException("Selecione uma ocasião definida.");
        }
        this.ocasiao = ocasiao;
    }
    /**
     * Seleciona uma estação explícita, sem pedir nem calcular uma data.
     * @param estacao estação escolhida pelo utilizador
     */
    public void selecionarEstacao(Estacao estacao) {
        this.estacao = null;
        contexto.getAssistente().validarEscolhasOutfit(ocasiao, estacao);
        this.estacao = estacao;
    }
    /**
     * Valida e apresenta o pedido ainda em preparação.
     * @return ocasião e estação para confirmação
     */
    public String obterDadosPedido() {
        contexto.getAssistente().validarEscolhasOutfit(ocasiao, estacao);
        return "Ocasião: " + ocasiao + "\nEstação: " + estacao;
    }
    /**
     * Executa o fluxo de confirmação do diagrama de design do UC6.
     * @return pedido registado com o outfit resultante
     */
    public PedidoOutfit confirmar() {
        obterDadosPedido();
        PedidoOutfit pedido = contexto.alterar(a -> {
            a.validarEscolhasOutfit(ocasiao, estacao);
            Utilizador utilizador = a.exigirUtilizador();
            var candidatas = utilizador.getGuardaRoupa().filtrarElegiveis(ocasiao, estacao);
            Outfit outfit = contexto.getGeradorOutfits().gerar(candidatas, ocasiao, estacao);
            PedidoOutfit novo = utilizador.criarPedido(ocasiao, estacao, outfit);
            utilizador.registarPedido(novo);
            return novo;
        });
        ocasiao = null;
        estacao = null;
        return pedido;
    }
}
