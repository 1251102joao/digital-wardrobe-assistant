package org.Model;

import java.io.Serializable;
import java.util.Objects;

/** Escolhas do utilizador e resultado registado no UC6; não contém data. */
public final class PedidoOutfit implements Serializable {
    private static final long serialVersionUID = 2L;
    /** Ocasião escolhida explicitamente. */
    private final Ocasiao ocasiao;
    /** Estação escolhida explicitamente, sem cálculo pelo calendário. */
    private final Estacao estacao;
    /** Combinação válida associada ao pedido. */
    private final Outfit outfit;

    /** Cria um pedido completo, pronto a ser registado pelo seu utilizador. */
    PedidoOutfit(Ocasiao ocasiao, Estacao estacao, Outfit outfit) {
        this.ocasiao = Objects.requireNonNull(ocasiao);
        this.estacao = Objects.requireNonNull(estacao);
        this.outfit = Objects.requireNonNull(outfit);
    }
    /**
     * Ocasião do pedido.
     * @return ocasião selecionada
     */
    public Ocasiao getOcasiao() { return ocasiao; }
    /**
     * Estação do pedido.
     * @return estação selecionada
     */
    public Estacao getEstacao() { return estacao; }
    /**
     * Resultado registado.
     * @return outfit gerado
     */
    public Outfit getOutfit() { return outfit; }
}
