package org.Utils;

/** Termina a aplicação sem ciclos infinitos quando a entrada padrão é fechada. */
public final class FimEntradaException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /** Cria o sinal de fim da entrada. */
    public FimEntradaException() { super("Entrada terminada."); }
}
