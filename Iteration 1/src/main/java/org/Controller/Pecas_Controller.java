package org.Controller;

import java.util.List;
import java.util.UUID;
import org.Model.*;

/** Consultas comuns aos UC1 a UC5; evita repetir a navegação pelo domínio. */
public abstract class Pecas_Controller {
    /** Contexto usado pelo controlador concreto do caso de uso. */
    protected final ContextoAplicacao contexto;
    /**
     * Exige uma sessão de utilizador final.
     *
     * @param contexto contexto partilhado da aplicação
     */
    protected Pecas_Controller(ContextoAplicacao contexto) {
        this.contexto = contexto;
        contexto.getAssistente().exigirUtilizador();
    }
    /**
     * Peças exclusivamente do utilizador atual.
     *
     * @return peças exclusivamente do utilizador atual.
     */
    public List<PecaVestuario> obterListaPecas() { return contexto.getAssistente().getGuardaRoupaAtual().obterListaPecas(); }
    /**
     * Consulta protegida por pertença ao guarda-roupa.
     *
     * @param id identificador da peça no guarda-roupa do utilizador atual
     * @return consulta protegida por pertença ao guarda-roupa.
     */
    public PecaVestuario obterPeca(UUID id) { return contexto.getAssistente().getGuardaRoupaAtual().obterPeca(id); }
    /**
     * Tipos de roupa definidos no UC12.
     * @return lista imutável de tipos registados
     */
    public List<TipoRoupa> obterListaTiposRoupa() { return contexto.getAssistente().obterListaTiposRoupa(); }
    /**
     * Cores definidas.
     *
     * @return cores definidas.
     */
    public List<Cor> obterListaCores() { return contexto.getAssistente().obterListaCores(); }
    /**
     * Estações definidas.
     *
     * @return estações definidas.
     */
    public List<Estacao> obterListaEstacoes() { return contexto.getAssistente().obterListaEstacoes(); }
    /**
     * Ocasiões definidas.
     *
     * @return ocasiões definidas.
     */
    public List<Ocasiao> obterListaOcasioes() { return contexto.getAssistente().obterListaOcasioes(); }
    /**
     * Características definidas.
     *
     * @return características definidas.
     */
    public List<Caracteristica> obterListaCaracteristicas() { return contexto.getAssistente().obterListaCaracteristicas(); }
    /**
     * Cria um rascunho válido sem o inserir no guarda-roupa.
     *
     * @param nome nome obrigatório
     * @param tipoRoupa tipo selecionado no catálogo
     * @param cor cor selecionada no catálogo
     * @param estacoes estações selecionadas, pelo menos uma
     * @param ocasioes ocasiões selecionadas, pelo menos uma
     * @param caracteristicas características selecionadas, podendo ser uma lista vazia
     * @param ignorar identificador a excluir da verificação, ou null numa adição
     * @return cria um rascunho válido sem o inserir no guarda-roupa.
     */
    protected PecaVestuario preparar(String nome, TipoRoupa tipoRoupa, Cor cor, List<Estacao> estacoes,
                                     List<Ocasiao> ocasioes, List<Caracteristica> caracteristicas, UUID ignorar) {
        var a = contexto.getAssistente();
        var g = a.getGuardaRoupaAtual();
        if (a.obterListaTiposRoupa().isEmpty() || a.obterListaCores().isEmpty() || a.obterListaEstacoes().isEmpty()
                || a.obterListaOcasioes().isEmpty() || a.obterListaCaracteristicas().isEmpty()) {
            throw new IllegalArgumentException("O administrador deve definir todos os catálogos antes de registar peças.");
        }
        PecaVestuario peca = g.novaPeca(nome, tipoRoupa, cor, estacoes, ocasioes, caracteristicas);
        a.validarReferencias(peca);
        g.validarNome(nome, ignorar);
        return peca;
    }
}
