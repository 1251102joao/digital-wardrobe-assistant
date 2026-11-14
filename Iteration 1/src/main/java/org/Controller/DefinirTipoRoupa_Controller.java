package org.Controller;

import java.util.ArrayList;
import java.util.List;
import org.Model.TipoRoupa;

/** UC12: mantém o lote de tipos de roupa até existir confirmação. */
public final class DefinirTipoRoupa_Controller {
    private final ContextoAplicacao contexto;
    private final List<TipoRoupa> listagem = new ArrayList<>();

    /**
     * Inicia um lote vazio para o administrador autenticado.
     * @param contexto contexto partilhado da aplicação
     */
    public DefinirTipoRoupa_Controller(ContextoAplicacao contexto) {
        this.contexto = contexto;
        contexto.getAssistente().exigirAdministrador();
    }

    /** Descarta os candidatos sem modificar o catálogo. */
    public void novaListagem() { listagem.clear(); }

    /**
     * Valida a designação perante o catálogo e o próprio lote.
     * @param designacao nome obrigatório do tipo de roupa
     */
    public void adicionarTipoRoupa(String designacao) {
        var a = contexto.getAssistente();
        TipoRoupa tipo = a.novoTipoRoupa(designacao);
        a.validarReferencia(tipo, a.obterListaTiposRoupa(), listagem);
        listagem.add(tipo);
    }

    /**
     * Lista imutável dos candidatos ainda não registados.
     * @return listagem em preparação
     */
    public List<TipoRoupa> obterListagem() { return List.copyOf(listagem); }

    /** Valida novamente e regista o lote inteiro depois da confirmação. */
    public void registarListagem() {
        contexto.alterar(a -> { a.registarTiposRoupa(listagem); return null; });
        listagem.clear();
    }
}
