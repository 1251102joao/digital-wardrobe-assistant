package org.Model;

import org.Utils.Validacao;

/** Característica livre com designação única e descrição obrigatória (UC10). */
public final class Caracteristica extends Referencia {
    private static final long serialVersionUID = 1L;
    /** Descrição validada da característica. */
    private final String descricao;

    /**
     * Valida os dados locais da característica.
     *
     * @param designacao designação obrigatória da referência
     * @param descricao descrição obrigatória da característica
     */
    public Caracteristica(String designacao, String descricao) {
        super(designacao);
        this.descricao = Validacao.obrigatorio(descricao, "Descrição");
    }

    /**
     * Devolve a descrição.
     *
     * @return devolve a descrição.
     */
    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return getDesignacao() + " - " + descricao; }
}
