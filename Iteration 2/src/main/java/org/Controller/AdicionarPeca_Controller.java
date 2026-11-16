package org.Controller;

import org.Utils.Idioma;
import java.util.List;
import java.util.UUID;
import org.Model.*;

/** UC1: coordena a criação e confirmação de uma peça. */
public final class AdicionarPeca_Controller extends Pecas_Controller {
    private PecaVestuario rascunho;

    /**
     * Recebe o contexto da aplicação.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public AdicionarPeca_Controller(ContextoAplicacao contexto) { super(contexto); }


    /**
     * Valida um rascunho sem alterar a peça ou o guarda-roupa.
     *
     * @param nome nome obrigatório
     * @param tipoRoupa tipo selecionado no catálogo
     * @param cores cores selecionadas no catálogo, pelo menos uma
     * @param partesCorpo partes do corpo associadas, pelo menos uma
     * @param estacoes estações selecionadas, pelo menos uma
     * @param ocasioes ocasiões selecionadas, pelo menos uma
     * @param caracteristicas características selecionadas, pelo menos uma
     */
    public void setDados(String nome, TipoRoupa tipoRoupa, List<Cor> cores, List<ParteCorpo> partesCorpo, List<Estacao> estacoes,
                         List<Ocasiao> ocasioes,
                         List<Caracteristica> caracteristicas) {
        rascunho = null;

        rascunho = preparar(nome, tipoRoupa, cores, partesCorpo, estacoes, ocasioes, caracteristicas, null);
    }
    /**
     * Dados completos para confirmação.
     *
     * @return dados completos para confirmação.
     */
    public String obterDados() {
        if (rascunho == null) throw new IllegalStateException(Idioma.t("Introduza os dados primeiro."));
        return rascunho.obterDados();
    }
    /** Confirma a operação, valida novamente e grava. */
    public void registarPeca() {
        obterDados();
        contexto.alterar(a -> {
            a.validarReferencias(rascunho);
            a.getGuardaRoupaAtual().registarPeca(rascunho);
            return null;
        });
        rascunho = null;
    }
}
