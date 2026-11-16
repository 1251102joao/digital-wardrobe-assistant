package org.Controller;

import java.util.List;
import org.Model.PedidoOutfit;

/** UC14: apresenta o histórico de recomendações e respetivas avaliações do utilizador atual. */
public final class ConsultarHistorico_Controller {
    private final ContextoAplicacao contexto;

    /**
     * Exige uma sessão de utilizador final.
     * @param contexto contexto partilhado da aplicação
     */
    public ConsultarHistorico_Controller(ContextoAplicacao contexto) {
        this.contexto = contexto;
        contexto.getAssistente().exigirUtilizador();
    }
    /**
     * Pedidos do próprio utilizador, do mais antigo para o mais recente.
     * @return lista imutável com recomendações e avaliações
     */
    public List<PedidoOutfit> obterHistorico() { return contexto.getAssistente().exigirUtilizador().getPedidos(); }
}
