package org.Controller;

import java.util.UUID;

/** UC4: coordena a marcação de uma peça como indisponível. */
public final class MarcarIndisponivel_Controller extends Pecas_Controller {
    /**
     * Recebe o contexto da aplicação.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public MarcarIndisponivel_Controller(ContextoAplicacao contexto) { super(contexto); }
    /**
     * Marca a peça como indisponível.
     *
     * @param id identificador da peça no guarda-roupa do utilizador atual
     */
    public void marcarIndisponivel(UUID id) {
        contexto.alterar(a -> { a.getGuardaRoupaAtual().marcarIndisponivel(id); return null; });
    }
}
