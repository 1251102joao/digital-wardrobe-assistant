package org.Controller;

import java.util.List;
import org.Model.Caracteristica;

/** UC10: definição de características com confirmação e unicidade. */
public final class DefinirCaracteristica_Controller {
    private final ContextoAplicacao contexto;
    private Caracteristica rascunho;
    /**
     * Inicia o caso de uso administrativo.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public DefinirCaracteristica_Controller(ContextoAplicacao contexto) {
        this.contexto = contexto;
        contexto.getAssistente().exigirAdministrador();
    }
    /**
     * Valida os dados e mantém uma característica ainda não registada.
     *
     * @param designacao designação obrigatória da referência
     * @param descricao descrição obrigatória da característica
     */
    public void setDados(String designacao, String descricao) {
        rascunho = null;
        var a = contexto.getAssistente();
        Caracteristica candidata = a.novaCaracteristica(designacao, descricao);
        a.validarReferencia(candidata, a.obterListaCaracteristicas(), List.of());
        rascunho = candidata;
    }
    /**
     * Dados para confirmação.
     *
     * @return dados para confirmação.
     */
    public String obterDados() {
        if (rascunho == null) throw new IllegalStateException("Introduza os dados primeiro.");
        return rascunho.toString();
    }
    /** Regista depois da confirmação. */
    public void registarCaracteristica() {
        obterDados();
        contexto.alterar(a -> { a.registarCaracteristica(rascunho); return null; });
        rascunho = null;
    }
}
