package org.Controller;

import org.Model.Utilizador;

/** Coordena autenticação e configuração inicial, como suporte aos UC autenticados. */
public final class Autenticar_Controller {
    private final ContextoAplicacao contexto;
    /**
     * Recebe o contexto partilhado pelos menus.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public Autenticar_Controller(ContextoAplicacao contexto) { this.contexto = contexto; }
    /**
     * Autentica sem persistir a sessão.
     *
     * @param email endereço de email da conta
     * @param password palavra-passe; é convertida em hash antes de ser guardada
     * @return autentica sem persistir a sessão.
     */
    public Utilizador autenticar(String email, String password) {
        contexto.getAssistente().autenticar(email, password);
        return contexto.getAssistente().getUtilizadorAtual();
    }
    /** Termina a sessão. */
    public void terminarSessao() { contexto.getAssistente().terminarSessao(); }
    /**
     * Configura e grava a primeira conta administrativa.
     *
     * @param nome nome obrigatório
     * @param email endereço de email da conta
     * @param password palavra-passe; é convertida em hash antes de ser guardada
     */
    public void configurarAdministrador(String nome, String email, String password) {
        contexto.alterar(a -> { a.criarAdministradorInicial(nome, email, password); return null; });
    }
}
