package org.Model;

import org.Utils.Idioma;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.Utils.PalavraPasse;
import org.Utils.Validacao;

/** Conta com credenciais protegidas; Creator do seu guarda-roupa e dos seus pedidos. */
public final class Utilizador implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Nome validado. */
    private final String nome;
    /** Email validado e normalizado. */
    private final String email;
    /** Hash e salt; nunca contém a palavra-passe em texto simples. */
    private final PalavraPasse palavraPasse;
    /** Papel administrativo atribuído na configuração inicial. */
    private final boolean administrador;
    /** Guarda-roupa pertencente a esta conta. */
    private final GuardaRoupa guardaRoupa = new GuardaRoupa();
    /** Histórico dos pedidos com resultado. */
    private final List<PedidoOutfit> pedidos = new ArrayList<>();

    /** Valida os campos locais e cria um guarda-roupa vazio. */
    Utilizador(String nome, String email, String password, boolean administrador) {
        this.nome = Validacao.obrigatorio(nome, Idioma.t("Nome"));
        this.email = Validacao.obrigatorio(email, Idioma.t("Email")).toLowerCase(Locale.ROOT);
        if (!this.email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+")) {
            throw new IllegalArgumentException(Idioma.t("Introduza um email válido, por exemplo nome@exemplo.pt."));
        }
        this.palavraPasse = new PalavraPasse(password);
        this.administrador = administrador;
    }
    /**
     * Devolve o nome.
     *
     * @return devolve o nome.
     */
    public String getNome() { return nome; }
    /**
     * Devolve o email normalizado.
     *
     * @return devolve o email normalizado.
     */
    public String getEmail() { return email; }
    /**
     * Indica o papel da conta.
     *
     * @return indica o papel da conta.
     */
    public boolean isAdministrador() { return administrador; }
    /** Valida uma tentativa sem devolver o hash ou o salt. */
    boolean autentica(String tentativa) { return palavraPasse.corresponde(tentativa); }
    /**
     * Devolve o guarda-roupa que pertence à conta.
     *
     * @return devolve o guarda-roupa que pertence à conta.
     */
    public GuardaRoupa getGuardaRoupa() { return guardaRoupa; }
    /**
     * Devolve os pedidos guardados sem expor a coleção interna.
     *
     * @return devolve os pedidos guardados sem expor a coleção interna.
     */
    public List<PedidoOutfit> getPedidos() { return List.copyOf(pedidos); }
    /**
     * Cria um pedido com as duas escolhas e o resultado, sem o registar ainda.
     * @param ocasiao ocasião selecionada
     * @param estacao estação selecionada
     * @param outfit combinação válida gerada
     * @return pedido em preparação
     */
    public PedidoOutfit criarPedido(Ocasiao ocasiao, Estacao estacao, Outfit outfit) {
        return new PedidoOutfit(ocasiao, estacao, outfit);
    }
    /**
     * Regista o pedido apenas depois de obter uma combinação do próprio guarda-roupa.
     * @param pedido pedido completo do utilizador
     */
    public void registarPedido(PedidoOutfit pedido) {
        for (PecaVestuario peca : pedido.getOutfit().getPecas()) {
            PecaVestuario atual = guardaRoupa.obterPeca(peca.getId());
            if (!atual.isAdequada(pedido.getOcasiao(), pedido.getEstacao())) {
                throw new IllegalArgumentException(Idioma.t("O pedido contém uma peça indisponível ou inadequada."));
            }
        }
        pedidos.add(pedido);
    }
    /**
     * Pedidos ainda sem avaliação, na ordem em que foram feitos.
     * @return lista imutável de pedidos por avaliar
     */
    public List<PedidoOutfit> getPedidosPorAvaliar() { return pedidos.stream().filter(p -> !p.isAvaliado()).toList(); }
    /**
     * Atribui uma nota a um pedido deste utilizador (Information Expert: é ele quem guarda os seus pedidos).
     * @param pedido pedido do próprio utilizador
     * @param nota nota de 0 a 10
     */
    public void avaliarPedido(PedidoOutfit pedido, int nota) {
        if (!pedidos.contains(pedido)) throw new IllegalArgumentException(Idioma.t("O pedido não pertence ao seu histórico."));
        pedido.avaliar(nota);
    }
    /**
     * Dados apresentáveis, excluindo sempre a palavra-passe.
     *
     * @return dados apresentáveis, excluindo sempre a palavra-passe.
     */
    public String obterDados() { return Idioma.t("Nome: ") + nome + Idioma.t("\nEmail: ") + email; }
}
