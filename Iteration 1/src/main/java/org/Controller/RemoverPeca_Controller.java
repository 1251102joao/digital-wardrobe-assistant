package org.Controller;

import java.util.UUID;

/** UC2: remoção de uma peça pertencente ao utilizador atual. */
public final class RemoverPeca_Controller extends Pecas_Controller {
    /**
     * Recebe o contexto da aplicação.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public RemoverPeca_Controller(ContextoAplicacao contexto) { super(contexto); }
    /**
     * Executa depois da confirmação apresentada pela UI.
     *
     * @param id identificador da peça no guarda-roupa do utilizador atual
     */
    public void removerPeca(UUID id) {
        contexto.alterar(a -> { a.getGuardaRoupaAtual().removerPeca(id); return null; });
    }
}
