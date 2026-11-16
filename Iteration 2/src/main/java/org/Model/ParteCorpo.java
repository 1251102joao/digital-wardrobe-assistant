package org.Model;

/** Parte do corpo humano coberta por uma peça de vestuário (por exemplo, tronco ou pernas). */
public final class ParteCorpo extends Referencia {
    private static final long serialVersionUID = 1L;
    /**
     * Cria uma parte do corpo com designação obrigatória.
     *
     * @param designacao designação obrigatória da referência
     */
    public ParteCorpo(String designacao) { super(designacao); }
}
