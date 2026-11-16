package org.Model;

import org.Utils.Idioma;
import java.io.Serializable;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.UUID;
import org.Utils.Validacao;

/** Peça imutável: uma edição produz uma nova versão com o mesmo identificador. */
public final class PecaVestuario implements Serializable {
    private static final long serialVersionUID = 3L;
    /** Identificador estável da peça. */
    private final UUID id;
    /** Nome validado. */
    private final String nome;
    /** Tipo de roupa selecionado no catálogo (UC12). */
    private final TipoRoupa tipoRoupa;
    /** Uma ou mais cores selecionadas no catálogo. */
    private final List<Cor> cores;
    /** Uma ou mais partes do corpo a que a peça está associada. */
    private final List<ParteCorpo> partesCorpo;
    /** Estações registadas ou associadas à peça. */
    private final List<Estacao> estacoes;
    /** Ocasiões registadas ou associadas à peça. */
    private final List<Ocasiao> ocasioes;
    /** Características registadas ou associadas à peça. */
    private final List<Caracteristica> caracteristicas;
    /** Estado de disponibilidade para sugestões. */
    private final boolean disponivel;

    /** Constrói e valida os dados mínimos de uma nova peça disponível. */
    PecaVestuario(String nome, TipoRoupa tipoRoupa, List<Cor> cores, List<ParteCorpo> partesCorpo, List<Estacao> estacoes,
                 List<Ocasiao> ocasioes, List<Caracteristica> caracteristicas) {
        this(UUID.randomUUID(), nome, tipoRoupa, cores, partesCorpo, estacoes, ocasioes, caracteristicas, true);
    }

    private PecaVestuario(UUID id, String nome, TipoRoupa tipoRoupa, List<Cor> cores, List<ParteCorpo> partesCorpo,
                         List<Estacao> estacoes, List<Ocasiao> ocasioes, List<Caracteristica> caracteristicas, boolean disponivel) {
        this.id = id;
        this.nome = Validacao.obrigatorio(nome, Idioma.t("Nome da peça"));
        if (tipoRoupa == null) throw new IllegalArgumentException(Idioma.t("O tipo de roupa é obrigatório."));
        this.tipoRoupa = tipoRoupa;
        if (cores == null || cores.isEmpty() || cores.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException(Idioma.t("Selecione pelo menos uma cor válida."));
        }
        if (partesCorpo == null || partesCorpo.isEmpty() || partesCorpo.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException(Idioma.t("Selecione pelo menos uma parte do corpo válida."));
        }
        if (estacoes == null || estacoes.isEmpty() || estacoes.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException(Idioma.t("Selecione pelo menos uma estação válida."));
        }
        if (ocasioes == null || ocasioes.isEmpty() || ocasioes.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException(Idioma.t("Selecione pelo menos uma ocasião válida."));
        }
        if (caracteristicas == null || caracteristicas.isEmpty() || caracteristicas.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException(Idioma.t("Selecione pelo menos uma característica válida."));
        }
        this.cores = List.copyOf(new LinkedHashSet<>(cores));
        this.partesCorpo = List.copyOf(new LinkedHashSet<>(partesCorpo));
        this.estacoes = List.copyOf(new LinkedHashSet<>(estacoes));
        this.ocasioes = List.copyOf(new LinkedHashSet<>(ocasioes));
        this.caracteristicas = List.copyOf(new LinkedHashSet<>(caracteristicas));
        this.disponivel = disponivel;
    }

    /**
     * Identificador estável, usado para selecionar peças sem expor a lista interna.
     *
     * @return identificador estável, usado para selecionar peças sem expor a lista interna.
     */
    public UUID getId() { return id; }
    /**
     * Devolve o nome.
     *
     * @return devolve o nome.
     */
    public String getNome() { return nome; }
    /**
     * Devolve as cores da peça.
     *
     * @return lista imutável de cores
     */
    public List<Cor> getCores() { return cores; }
    /**
     * Devolve as partes do corpo associadas à peça.
     *
     * @return lista imutável de partes do corpo
     */
    public List<ParteCorpo> getPartesCorpo() { return partesCorpo; }
    /**
     * Devolve o tipo de roupa selecionado no catálogo.
     * @return tipo da peça
     */
    public TipoRoupa getTipoRoupa() { return tipoRoupa; }
    /**
     * Devolve uma lista imutável de estações.
     *
     * @return devolve uma lista imutável de estações.
     */
    public List<Estacao> getEstacoes() { return estacoes; }
    /**
     * Devolve uma lista imutável de ocasiões.
     *
     * @return devolve uma lista imutável de ocasiões.
     */
    public List<Ocasiao> getOcasioes() { return ocasioes; }
    /**
     * Devolve uma lista imutável de características.
     *
     * @return devolve uma lista imutável de características.
     */
    public List<Caracteristica> getCaracteristicas() { return caracteristicas; }
    /**
     * Indica se a peça pode integrar sugestões.
     *
     * @return indica se a peça pode integrar sugestões.
     */
    public boolean isDisponivel() { return disponivel; }

    /** Valida e cria uma versão editada, mantendo identificador e disponibilidade. */
    PecaVestuario comDados(PecaVestuario dados) {
        return new PecaVestuario(id, dados.nome, dados.tipoRoupa, dados.cores, dados.partesCorpo, dados.estacoes, dados.ocasioes,
                dados.caracteristicas, disponivel);
    }
    /** Cria uma versão indisponível, preservando os restantes dados (UC4). */
    PecaVestuario comIndisponibilidade() {
        return new PecaVestuario(id, nome, tipoRoupa, cores, partesCorpo, estacoes, ocasioes, caracteristicas, false);
    }
    /**
     * Regras de elegibilidade do UC6, atribuídas à peça pelo Information Expert.
     *
     * @param ocasiao ocasião selecionada no catálogo
     * @param estacao estação escolhida explicitamente pelo utilizador
     * @return regras de elegibilidade do UC6, atribuídas à peça pelo Information Expert.
     */
    public boolean isAdequada(Ocasiao ocasiao, Estacao estacao) {
        return disponivel && ocasioes.contains(ocasiao) && estacoes.contains(estacao);
    }
    /**
     * Dados completos para consulta e confirmação.
     *
     * @return dados completos para consulta e confirmação.
     */
    public String obterDados() { return obterDados(disponivel); }
    /**
     * Dados completos com a disponibilidade indicada, usada ao confirmar a edição de uma peça.
     *
     * @param estadoDisponivel disponibilidade a apresentar
     * @return dados completos para consulta e confirmação
     */
    public String obterDados(boolean estadoDisponivel) {
        return Idioma.t("Nome: ") + nome + Idioma.t("\nTipo de roupa: ") + tipoRoupa + Idioma.t("\nCores: ") + cores + Idioma.t("\nPartes do corpo: ") + partesCorpo + Idioma.t("\nEstações: ") + estacoes
                + Idioma.t("\nOcasiões: ") + ocasioes + Idioma.t("\nCaracterísticas: ") + caracteristicas
                + Idioma.t("\nDisponível: ") + (estadoDisponivel ? Idioma.t("sim") : Idioma.t("não"));
    }
    @Override
    public String toString() { return nome + " | " + tipoRoupa + " | " + cores + (disponivel ? Idioma.t(" | disponível") : Idioma.t(" | indisponível")); }
}
