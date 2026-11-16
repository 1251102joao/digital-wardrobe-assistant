package org.Model;

import org.Utils.Idioma;
import java.io.Serializable;
import java.util.List;

/** Sugestão com uma ou mais peças; retém as versões existentes no momento do pedido. */
public final class Outfit implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Versões das peças contidas na coleção. */
    private final List<PecaVestuario> pecas;

    /** Cria uma sugestão não vazia. */
    Outfit(List<PecaVestuario> pecas) {
        if (pecas.isEmpty()) throw new IllegalArgumentException(Idioma.t("Um outfit não pode estar vazio."));
        this.pecas = List.copyOf(pecas);
    }
    /**
     * Lista imutável das versões das peças incluídas.
     *
     * @return lista imutável das versões das peças incluídas.
     */
    public List<PecaVestuario> getPecas() { return pecas; }
    /**
     * Texto da sugestão.
     *
     * @return texto da sugestão.
     */
    public String obterDados() {
        return pecas.stream().map(PecaVestuario::toString).collect(java.util.stream.Collectors.joining("\n"));
    }
}
