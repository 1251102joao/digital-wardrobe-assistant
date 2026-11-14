package org.Model;

/** A regra de composição não foi validada pelo cliente; não existe um outfit para registar. */
public final class ComposicaoPendenteException extends IllegalStateException {
    private static final long serialVersionUID = 1L;

    /** Informa a decisão de âmbito tomada pelo grupo para esta versão. */
    public ComposicaoPendenteException() {
        super("A geração de outfits está pendente da definição da regra de composição pelo docente. "
                + "Não foram registados um pedido ou um outfit.");
    }
}
