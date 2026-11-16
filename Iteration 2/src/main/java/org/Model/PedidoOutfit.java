package org.Model;

import org.Utils.Idioma;
import java.io.Serializable;
import java.util.Objects;

/** Escolhas do utilizador, resultado recomendado (UC6) e avaliação posterior; não contém data. */
public final class PedidoOutfit implements Serializable {
    private static final long serialVersionUID = 3L;
    /** Ocasião escolhida explicitamente. */
    private final Ocasiao ocasiao;
    /** Estação escolhida explicitamente, sem cálculo pelo calendário. */
    private final Estacao estacao;
    /** Combinação válida associada ao pedido. */
    private final Outfit outfit;
    /** Nota de 0 a 10 dada pelo utilizador, ou null enquanto não avaliado. */
    private Integer avaliacao;

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
    /**
     * Nota atribuída pelo utilizador.
     * @return nota de 0 a 10, ou null se o pedido ainda não foi avaliado
     */
    public Integer getAvaliacao() { return avaliacao; }
    /**
     * Indica se já existe avaliação.
     * @return true se o utilizador já atribuiu uma nota
     */
    public boolean isAvaliado() { return avaliacao != null; }
    /** Regista a nota; só o utilizador dono do pedido a invoca, através de {@link Utilizador#avaliarPedido}. */
    void avaliar(int nota) {
        if (nota < 0 || nota > 10) throw new IllegalArgumentException(Idioma.t("A avaliação deve estar entre 0 e 10."));
        if (avaliacao != null) throw new IllegalStateException(Idioma.t("Este outfit já foi avaliado."));
        avaliacao = nota;
    }
    /**
     * Resumo para o histórico.
     * @return ocasião, estação, peças e avaliação
     */
    public String obterDados() {
        return Idioma.t("Ocasião: ") + ocasiao + Idioma.t(" | Estação: ") + estacao + "\n" + outfit.obterDados()
                + Idioma.t("\nAvaliação: ") + (avaliacao == null ? Idioma.t("ainda não avaliado") : avaliacao + "/10");
    }
    @Override
    public String toString() {
        return outfit.getPecas().stream().map(PecaVestuario::getNome).collect(java.util.stream.Collectors.joining(" + "))
                + " (" + ocasiao + ", " + estacao + ")";
    }
}
