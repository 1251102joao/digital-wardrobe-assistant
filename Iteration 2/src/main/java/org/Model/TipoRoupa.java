package org.Model;

/** Categoria de roupa definida pelo administrador no UC12, como calças ou camisola. */
public final class TipoRoupa extends Referencia {
    private static final long serialVersionUID = 1L;

    /**
     * Cria um tipo de roupa com designação obrigatória.
     * @param designacao nome do tipo de roupa
     */
    public TipoRoupa(String designacao) { super(designacao); }
}
