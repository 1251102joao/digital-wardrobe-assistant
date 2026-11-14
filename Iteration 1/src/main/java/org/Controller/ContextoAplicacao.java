package org.Controller;

import java.io.IOException;
import java.util.function.Function;
import org.Model.AssistenteDigital;
import org.Model.GeradorOutfits;
import org.Model.PoliticaComposicao;
import org.Persistence.RepositorioDados;

/** Liga o domínio à gravação; uma falha deixa o estado anterior intacto. */
public final class ContextoAplicacao {
    private AssistenteDigital assistente;
    private final RepositorioDados repositorio;
    private final GeradorOutfits gerador;

    /**
     * Usa repositório nulo para demonstrações e testes exclusivamente em memória.
     *
     * @param assistente raiz do domínio a carregar ou guardar
     * @param repositorio repositório local, ou null para trabalhar apenas em memória
     */
    public ContextoAplicacao(AssistenteDigital assistente, RepositorioDados repositorio) {
        this(assistente, repositorio, new GeradorOutfits(PoliticaComposicao.pendente()));
    }
    /**
     * Permite fornecer a política de composição sem a gravar como estado do domínio.
     * @param assistente ponto de acesso aos catálogos e utilizadores
     * @param repositorio gravação opcional, ou null para estado apenas em memória
     * @param gerador serviço que aplica a política de composição
     */
    public ContextoAplicacao(AssistenteDigital assistente, RepositorioDados repositorio, GeradorOutfits gerador) {
        this.assistente = java.util.Objects.requireNonNull(assistente);
        this.repositorio = repositorio;
        this.gerador = java.util.Objects.requireNonNull(gerador);
    }
    /**
     * Serviço de composição partilhado pelos pedidos de outfit.
     * @return gerador configurado nesta execução
     */
    public GeradorOutfits getGeradorOutfits() { return gerador; }
    /**
     * Devolve a versão atual do domínio; os controladores consultam-na em cada operação.
     *
     * @return devolve a versão atual do domínio; os controladores consultam-na em cada operação.
     */
    public AssistenteDigital getAssistente() { return assistente; }
    /**
     * Executa numa cópia, grava e só então publica o novo estado.
     *
     * @param operacao operação a executar sobre a cópia de trabalho ou na consola
     * @param <T> tipo dos elementos ou do resultado
     * @return executa numa cópia, grava e só então publica o novo estado.
     */
    public <T> T alterar(Function<AssistenteDigital, T> operacao) {
        AssistenteDigital trabalho = assistente.copiar();
        T resultado = operacao.apply(trabalho);
        try { if (repositorio != null) repositorio.guardar(trabalho); }
        catch (IOException e) {
            throw new IllegalStateException("Não foi possível gravar os dados. A alteração não foi aplicada. "
                    + e.getMessage(), e);
        }
        assistente = trabalho;
        return resultado;
    }
}
