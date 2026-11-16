package org.Controller;

import java.util.ArrayList;
import java.util.List;
import org.Model.Cor;

/** Mantém a listagem em construção; só a regista como um todo após confirmação. */
public final class DefinirCor_Controller {
    private final ContextoAplicacao contexto;
    private final List<Cor> listagem = new ArrayList<>();
    /**
     * Inicia uma listagem vazia.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public DefinirCor_Controller(ContextoAplicacao contexto) {
        this.contexto = contexto;
        contexto.getAssistente().exigirAdministrador();
    }
    /** Limpa o rascunho, sem modificar o catálogo. */
    public void novaListagem() { listagem.clear(); }
    /**
     * Valida perante catálogo e rascunho e adiciona apenas ao rascunho.
     *
     * @param designacao designação obrigatória da referência
     */
    public void adicionarCor(String designacao) {
        var a = contexto.getAssistente();
        Cor novo = a.novaCor(designacao);
        a.validarReferencia(novo, a.obterListaCores(), listagem);
        listagem.add(novo);
    }
    /**
     * Lista temporária imutável para confirmação.
     *
     * @return lista temporária imutável para confirmação.
     */
    public List<Cor> obterListagem() { return List.copyOf(listagem); }
    /** Regista e grava a listagem completa, voltando a validar. */
    public void registarListagem() {
        contexto.alterar(a -> { a.registarCores(listagem); return null; });
        listagem.clear();
    }
}
