package org.Model;

import java.io.Serializable;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.UUID;
import org.Utils.Validacao;

/** Peça imutável: uma edição produz uma nova versão com o mesmo identificador. */
public final class PecaVestuario implements Serializable {
    private static final long serialVersionUID = 2L;
    /** Identificador estável da peça. */
    private final UUID id;
    /** Nome validado. */
    private final String nome;
    /** Tipo de roupa selecionado no catálogo (UC12). */
    private final TipoRoupa tipoRoupa;
    /** Cor selecionada no catálogo. */
    private final Cor cor;
    /** Estações registadas ou associadas à peça. */
    private final List<Estacao> estacoes;
    /** Ocasiões registadas ou associadas à peça. */
    private final List<Ocasiao> ocasioes;
    /** Características registadas ou associadas à peça. */
    private final List<Caracteristica> caracteristicas;
    /** Estado de disponibilidade para sugestões. */
    private final boolean disponivel;

    /** Constrói e valida os dados mínimos de uma nova peça disponível. */
    PecaVestuario(String nome, TipoRoupa tipoRoupa, Cor cor, List<Estacao> estacoes, List<Ocasiao> ocasioes,
                 List<Caracteristica> caracteristicas) {
        this(UUID.randomUUID(), nome, tipoRoupa, cor, estacoes, ocasioes, caracteristicas, true);
    }

    private PecaVestuario(UUID id, String nome, TipoRoupa tipoRoupa, Cor cor, List<Estacao> estacoes,
                         List<Ocasiao> ocasioes, List<Caracteristica> caracteristicas, boolean disponivel) {
        this.id = id;
        this.nome = Validacao.obrigatorio(nome, "Nome da peça");
        if (tipoRoupa == null) throw new IllegalArgumentException("O tipo de roupa é obrigatório.");
        this.tipoRoupa = tipoRoupa;
        if (cor == null) throw new IllegalArgumentException("A cor é obrigatória.");
        if (estacoes == null || estacoes.isEmpty() || estacoes.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException("Selecione pelo menos uma estação válida.");
        }
        if (ocasioes == null || ocasioes.isEmpty() || ocasioes.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException("Selecione pelo menos uma ocasião válida.");
        }
        if (caracteristicas == null || caracteristicas.stream().anyMatch(java.util.Objects::isNull)) {
            throw new IllegalArgumentException("A lista de características é inválida.");
        }
        this.cor = cor;
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
     * Devolve a cor.
     *
     * @return devolve a cor.
     */
    public Cor getCor() { return cor; }
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
        return new PecaVestuario(id, dados.nome, dados.tipoRoupa, dados.cor, dados.estacoes, dados.ocasioes,
                dados.caracteristicas, disponivel);
    }
    /** Cria uma versão indisponível, preservando os restantes dados (UC4). */
    PecaVestuario comIndisponibilidade() {
        return new PecaVestuario(id, nome, tipoRoupa, cor, estacoes, ocasioes, caracteristicas, false);
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
    public String obterDados() {
        return "Nome: " + nome + "\nTipo de roupa: " + tipoRoupa + "\nCor: " + cor + "\nEstações: " + estacoes
                + "\nOcasiões: " + ocasioes + "\nCaracterísticas: " + caracteristicas
                + "\nDisponível: " + (disponivel ? "sim" : "não");
    }
    @Override
    public String toString() { return nome + " | " + tipoRoupa + " | " + cor + (disponivel ? " | disponível" : " | indisponível"); }
}
