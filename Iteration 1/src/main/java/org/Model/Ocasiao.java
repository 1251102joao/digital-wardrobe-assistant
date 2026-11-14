package org.Model;

/** Contexto de utilização de uma peça ou pedido de outfit (UC8). */
public final class Ocasiao extends Referencia {
    private static final long serialVersionUID = 1L;
    /**
     * Cria uma referência com designação obrigatória.
     *
     * @param designacao designação obrigatória da referência
     */
    public Ocasiao(String designacao) { super(designacao); }
}
