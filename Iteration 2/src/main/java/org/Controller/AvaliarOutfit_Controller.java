package org.Controller;

import org.Utils.Idioma;
import java.util.List;
import org.Model.*;

/** UC13: o utilizador atribui uma nota de 0 a 10 a um outfit que lhe foi recomendado. */
public final class AvaliarOutfit_Controller {
    private final ContextoAplicacao contexto;
    private int indice = -1;
    private Integer nota;

    /**
     * Exige uma sessão de utilizador final.
     * @param contexto contexto partilhado da aplicação
     */
    public AvaliarOutfit_Controller(ContextoAplicacao contexto) {
        this.contexto = contexto;
        contexto.getAssistente().exigirUtilizador();
    }
    /**
     * Pedidos recomendados que ainda não foram avaliados.
     * @return lista imutável de pedidos por avaliar
     */
    public List<PedidoOutfit> obterPedidosPorAvaliar() { return contexto.getAssistente().exigirUtilizador().getPedidosPorAvaliar(); }
    /**
     * Seleciona o pedido a avaliar.
     * @param pedido pedido da lista de pedidos por avaliar
     */
    public void selecionarPedido(PedidoOutfit pedido) {
        indice = -1;
        nota = null;
        int i = contexto.getAssistente().exigirUtilizador().getPedidos().indexOf(pedido);
        if (i < 0 || pedido.isAvaliado()) throw new IllegalArgumentException(Idioma.t("Selecione um outfit por avaliar."));
        indice = i;
    }
    /**
     * Valida a nota sem a registar.
     * @param nota valor inteiro de 0 a 10
     */
    public void setNota(int nota) {
        this.nota = null;
        if (indice < 0) throw new IllegalStateException(Idioma.t("Selecione primeiro um outfit."));
        if (nota < 0 || nota > 10) throw new IllegalArgumentException(Idioma.t("A avaliação deve estar entre 0 e 10."));
        this.nota = nota;
    }
    /**
     * Dados para confirmação.
     * @return outfit e nota escolhida
     */
    public String obterDados() {
        if (indice < 0 || nota == null) throw new IllegalStateException(Idioma.t("Introduza a avaliação primeiro."));
        return contexto.getAssistente().exigirUtilizador().getPedidos().get(indice).obterDados() + Idioma.t("\nNova avaliação: ") + nota + "/10";
    }
    /** Confirma e grava a avaliação de forma transacional. */
    public void registarAvaliacao() {
        obterDados();
        final int i = indice;
        final int n = nota;
        contexto.alterar(a -> {
            Utilizador u = a.exigirUtilizador();
            u.avaliarPedido(u.getPedidos().get(i), n);
            return null;
        });
        indice = -1;
        nota = null;
    }
}
