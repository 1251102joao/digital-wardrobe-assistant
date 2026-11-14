package org.Utils;

/** Sinaliza cancelamento voluntário, sem tratar a situação como erro. */
public final class OperacaoCanceladaException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /** Cria o sinal de cancelamento. */
    public OperacaoCanceladaException() { super("Operação cancelada."); }
}
