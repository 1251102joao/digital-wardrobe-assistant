package org.Controller;

import java.util.List;
import java.util.UUID;
import org.Model.*;

/** UC3: substitui a peça apenas após validar e confirmar os novos dados. */
public final class EditarPeca_Controller extends Pecas_Controller {
    private PecaVestuario rascunho;
    private UUID selecionada;
    /**
     * Recebe o contexto da aplicação.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public EditarPeca_Controller(ContextoAplicacao contexto) { super(contexto); }
    /**
     * Seleciona uma peça do utilizador atual.
     *
     * @param id identificador da peça no guarda-roupa do utilizador atual
     */
    public void selecionarPeca(UUID id) { obterPeca(id); selecionada = id; rascunho = null; }
    /**
     * Valida um rascunho sem alterar a peça ou o guarda-roupa.
     *
     * @param nome nome obrigatório
     * @param tipoRoupa tipo selecionado no catálogo
     * @param cor cor selecionada no catálogo
     * @param estacoes estações selecionadas, pelo menos uma
     * @param ocasioes ocasiões selecionadas, pelo menos uma
     * @param caracteristicas características selecionadas, podendo ser uma lista vazia
     */
    public void setDados(String nome, TipoRoupa tipoRoupa, Cor cor, List<Estacao> estacoes, List<Ocasiao> ocasioes,
                         List<Caracteristica> caracteristicas) {
        rascunho = null;
        if (selecionada == null) throw new IllegalStateException("Selecione uma peça primeiro.");
        rascunho = preparar(nome, tipoRoupa, cor, estacoes, ocasioes, caracteristicas, selecionada);
    }
    /**
     * Dados completos para confirmação.
     *
     * @return dados completos para confirmação.
     */
    public String obterDados() {
        if (rascunho == null) throw new IllegalStateException("Introduza os dados primeiro.");
        return obterPeca(selecionada).isDisponivel() ? rascunho.obterDados() : rascunho.obterDados().replace("Disponível: sim", "Disponível: não");
    }
    /** Confirma a operação, valida novamente e grava. */
    public void guardarAlteracoes() {
        obterDados();
        contexto.alterar(a -> {
            a.validarReferencias(rascunho);
            a.getGuardaRoupaAtual().editarPeca(selecionada, rascunho);
            return null;
        });
        rascunho = null;
    }
}
