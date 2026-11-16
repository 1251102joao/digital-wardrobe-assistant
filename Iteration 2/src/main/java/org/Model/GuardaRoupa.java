package org.Model;

import org.Utils.Idioma;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.Utils.Validacao;

/** Creator das peças e dos outfits; garante unicidade dentro de um guarda-roupa. */
public final class GuardaRoupa implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Versões das peças contidas na coleção. */
    private final List<PecaVestuario> pecas = new ArrayList<>();

    /** Cria um guarda-roupa vazio. */
    public GuardaRoupa() { }
    /**
     * Lista de peças sem permitir alterações externas à coleção.
     *
     * @return lista de peças sem permitir alterações externas à coleção.
     */
    public List<PecaVestuario> obterListaPecas() { return List.copyOf(pecas); }
    /**
     * Cria um rascunho validado, sem o registar antes da confirmação.
     *
     * @param nome nome obrigatório
     * @param tipoRoupa tipo selecionado no catálogo
     * @param cores cores selecionadas no catálogo, pelo menos uma
     * @param partesCorpo partes do corpo associadas, pelo menos uma
     * @param estacoes estações selecionadas, pelo menos uma
     * @param ocasioes ocasiões selecionadas, pelo menos uma
     * @param caracteristicas características selecionadas, pelo menos uma
     * @return cria um rascunho validado, sem o registar antes da confirmação.
     */
    public PecaVestuario novaPeca(String nome, TipoRoupa tipoRoupa, List<Cor> cores, List<ParteCorpo> partesCorpo,
                                  List<Estacao> estacoes, List<Ocasiao> ocasioes, List<Caracteristica> caracteristicas) {
        return new PecaVestuario(nome, tipoRoupa, cores, partesCorpo, estacoes, ocasioes, caracteristicas);
    }
    /**
     * Encontra exclusivamente uma peça deste guarda-roupa.
     *
     * @param id identificador da peça no guarda-roupa do utilizador atual
     * @return encontra exclusivamente uma peça deste guarda-roupa.
     */
    public PecaVestuario obterPeca(UUID id) {
        return pecas.stream().filter(p -> p.getId().equals(id)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(Idioma.t("A peça não pertence ao seu guarda-roupa.")));
    }
    /**
     * Valida nome único, ignorando a própria peça numa edição.
     *
     * @param nome nome obrigatório
     * @param ignorar identificador a excluir da verificação, ou null numa adição
     */
    public void validarNome(String nome, UUID ignorar) {
        String chave = Validacao.chave(nome);
        if (pecas.stream().anyMatch(p -> !p.getId().equals(ignorar)
                && Validacao.chave(p.getNome()).equals(chave))) {
            throw new IllegalArgumentException(Idioma.t("Já existe uma peça com esse nome no seu guarda-roupa."));
        }
    }
    /**
     * Regista apenas depois de verificar a regra global.
     *
     * @param peca peça validada a registar ou verificar
     */
    public void registarPeca(PecaVestuario peca) {
        validarNome(peca.getNome(), null);
        if (pecas.stream().anyMatch(p -> p.getId().equals(peca.getId()))) {
            throw new IllegalArgumentException(Idioma.t("A peça já foi registada."));
        }
        pecas.add(peca);
    }
    /**
     * Substitui atomicamente a versão atual, preservando disponibilidade e ID.
     *
     * @param id identificador da peça no guarda-roupa do utilizador atual
     * @param dados rascunho com os novos dados da peça
     */
    public void editarPeca(UUID id, PecaVestuario dados) {
        PecaVestuario atual = obterPeca(id);
        validarNome(dados.getNome(), id);
        pecas.set(pecas.indexOf(atual), atual.comDados(dados));
    }
    /**
     * Remove uma peça do guarda-roupa; os outfits antigos retêm a versão histórica.
     *
     * @param id identificador da peça no guarda-roupa do utilizador atual
     */
    public void removerPeca(UUID id) { pecas.remove(obterPeca(id)); }
    /**
     * Marca uma peça do utilizador como indisponível (UC4).
     *
     * @param id identificador da peça no guarda-roupa do utilizador atual
     */
    public void marcarIndisponivel(UUID id) {
        PecaVestuario atual = obterPeca(id);
        pecas.set(pecas.indexOf(atual), atual.comIndisponibilidade());
    }
    /**
     * Filtra as peças disponíveis adequadas às escolhas, sem compor um outfit.
     * @param ocasiao ocasião escolhida pelo utilizador
     * @param estacao estação escolhida pelo utilizador
     * @return lista imutável de peças elegíveis, eventualmente vazia
     */
    public List<PecaVestuario> filtrarElegiveis(Ocasiao ocasiao, Estacao estacao) {
        return pecas.stream().filter(p -> p.isAdequada(ocasiao, estacao)).toList();
    }
}
