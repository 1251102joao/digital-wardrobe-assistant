package org.Model;

import java.io.Serializable;
import org.Utils.Validacao;

/** Informação de referência imutável, identificada pela classe e designação normalizada. */
public abstract class Referencia implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Designação validada da referência. */
    private final String designacao;

    /**
     * Valida a designação obrigatória.
     *
     * @param designacao designação obrigatória da referência
     */
    protected Referencia(String designacao) {
        this.designacao = Validacao.obrigatorio(designacao, "Designação");
    }

    /**
     * Devolve a designação apresentada nos menus.
     *
     * @return devolve a designação apresentada nos menus.
     */
    public final String getDesignacao() { return designacao; }

    @Override
    public final boolean equals(Object outro) {
        return outro != null && getClass() == outro.getClass()
                && Validacao.chave(designacao).equals(Validacao.chave(((Referencia) outro).designacao));
    }

    @Override
    public final int hashCode() { return 31 * getClass().hashCode() + Validacao.chave(designacao).hashCode(); }

    @Override
    public String toString() { return designacao; }
}
