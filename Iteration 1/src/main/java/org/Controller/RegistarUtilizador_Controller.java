package org.Controller;

import org.Model.Utilizador;

/** UC11: mantém o rascunho fora do domínio registado até existir confirmação. */
public final class RegistarUtilizador_Controller {
    private final ContextoAplicacao contexto;
    private Utilizador rascunho;
    /**
     * Inicia o caso de uso.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public RegistarUtilizador_Controller(ContextoAplicacao contexto) { this.contexto = contexto; }
    /**
     * Cria e valida um rascunho sem alterar as contas registadas.
     *
     * @param nome nome obrigatório
     * @param email endereço de email da conta
     * @param password palavra-passe; é convertida em hash antes de ser guardada
     */
    public void setDados(String nome, String email, String password) {
        rascunho = null;
        Utilizador candidato = contexto.getAssistente().novoUtilizador(nome, email, password);
        contexto.getAssistente().validarUtilizador(candidato);
        rascunho = candidato;
    }
    /**
     * Dados para confirmação; não apresenta a palavra-passe.
     *
     * @return dados para confirmação; não apresenta a palavra-passe.
     */
    public String obterDados() { return candidato().obterDados(); }
    private Utilizador candidato() {
        if (rascunho == null) throw new IllegalStateException("Introduza primeiro os dados válidos.");
        return rascunho;
    }
    /** Confirma o registo e grava. */
    public void registarUtilizador() {
        Utilizador novo = candidato();
        contexto.alterar(a -> { a.registarUtilizador(novo); return null; });
        rascunho = null;
    }
}
