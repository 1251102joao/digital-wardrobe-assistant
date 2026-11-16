package org.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Utilitários de consola adaptados do código base de Paulo Maio e Dulce Mota.
 * Usa um único leitor e valida opções, cancelamentos e fim da entrada.
 * Em qualquer pergunta de texto, escrever 0 cancela a operação em curso.
 */
public final class Utils {
    private static final BufferedReader ENTRADA = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    private Utils() { }

    /**
     * Lê uma linha em bruto; o fim da entrada (EOF) termina a aplicação.
     * O 0 só é interpretado como cancelamento pelos métodos que o declaram.
     *
     * @param mensagem texto apresentado na consola
     * @return linha lida, sem tratamento
     */
    public static String readLineFromConsole(String mensagem) {
        System.out.print(mensagem + " ");
        System.out.flush();
        try {
            String linha = ENTRADA.readLine();
            if (linha == null) throw new FimEntradaException();
            return linha;
        } catch (IOException e) { throw new IllegalStateException(Idioma.t("Não foi possível ler a entrada."), e); }
    }
    /**
     * Lê e apara um campo obrigatório, permitindo correção imediata; 0 cancela a operação.
     *
     * @param mensagem texto apresentado na consola
     * @return texto não vazio
     */
    public static String obrigatorio(String mensagem) {
        while (true) {
            String texto = readLineFromConsole(mensagem).strip();
            if (texto.equals("0")) throw new OperacaoCanceladaException();
            if (!texto.isBlank()) return texto;
            System.out.println(Idioma.t("O campo é obrigatório. Escreva 0 para cancelar."));
        }
    }
    /**
     * Como {@link #obrigatorio(String)}, mas 0 não cancela: devolve null para sinalizar o fim de uma listagem.
     *
     * @param mensagem texto apresentado na consola
     * @return texto não vazio, ou null se o utilizador escreveu 0
     */
    public static String obrigatorioOuFim(String mensagem) {
        while (true) {
            String texto = readLineFromConsole(mensagem).strip();
            if (texto.equals("0")) return null;
            if (!texto.isBlank()) return texto;
            System.out.println(Idioma.t("O campo é obrigatório. Escreva 0 para terminar."));
        }
    }
    /**
     * Oculta a palavra-passe se existir consola nativa; no IntelliJ usa a entrada normal.
     *
     * @param mensagem texto apresentado na consola
     * @return oculta a palavra-passe se existir consola nativa; no IntelliJ usa a entrada normal.
     */
    public static String password(String mensagem) {
        if (System.console() == null) return obrigatorio(mensagem);
        while (true) {
            char[] chars = System.console().readPassword("%s ", mensagem);
            if (chars == null) throw new FimEntradaException();
            String texto = new String(chars);
            java.util.Arrays.fill(chars, '\0');
            if (texto.strip().equals("0")) throw new OperacaoCanceladaException();
            if (!texto.isBlank()) return texto;
            System.out.println(Idioma.t("A palavra-passe é obrigatória. Escreva 0 para cancelar."));
        }
    }
    /**
     * Lê uma opção inteira dentro do intervalo fechado.
     *
     * @param mensagem texto apresentado na consola
     * @param minimo menor opção aceite
     * @param maximo maior opção aceite
     * @return lê uma opção inteira dentro do intervalo fechado.
     */
    public static int opcao(String mensagem, int minimo, int maximo) {
        while (true) {
            try {
                int valor = Integer.parseInt(readLineFromConsole(mensagem).strip());
                if (valor >= minimo && valor <= maximo) return valor;
            } catch (NumberFormatException e) { /* Pede novamente abaixo. */ }
            System.out.println(String.format(Idioma.t("Introduza um número entre %d e %d."), minimo, maximo));
        }
    }
    /**
     * Lê uma nota inteira de 0 a 10; aqui o 0 é uma nota válida, por isso o cancelamento é feito com c.
     *
     * @param mensagem texto apresentado na consola
     * @return nota entre 0 e 10
     */
    public static int nota(String mensagem) {
        while (true) {
            String texto = readLineFromConsole(mensagem).strip();
            if (texto.equalsIgnoreCase("c")) throw new OperacaoCanceladaException();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= 0 && valor <= 10) return valor;
            } catch (NumberFormatException e) { /* Pede novamente abaixo. */ }
            System.out.println(Idioma.t("Introduza um número inteiro entre 0 e 10 (ou c para cancelar)."));
        }
    }
    /**
     * Confirma explicitamente com S/N; 0 também cancela.
     *
     * @param mensagem texto apresentado na consola
     * @return true se a resposta for S, false se for N
     */
    public static boolean confirma(String mensagem) {
        while (true) {
            String resposta = readLineFromConsole(mensagem + " " + Idioma.t("(S/N):")).strip();
            if (resposta.equalsIgnoreCase("s") || resposta.equalsIgnoreCase(Idioma.t("s"))) return true;
            if (resposta.equalsIgnoreCase("n")) return false;
            if (resposta.equals("0")) throw new OperacaoCanceladaException();
            System.out.println(Idioma.t("Responda S ou N (ou 0 para cancelar)."));
        }
    }
    /**
     * Apresenta uma lista numerada.
     *
     * @param lista elementos disponíveis para apresentação ou seleção
     * @param titulo título da listagem
     */
    public static void apresentaLista(List<?> lista, String titulo) {
        System.out.println("\n" + titulo);
        for (int i = 0; i < lista.size(); i++) System.out.println((i + 1) + ". " + lista.get(i));
    }
    /**
     * Seleciona um elemento ou cancela com 0.
     *
     * @param lista elementos disponíveis para apresentação ou seleção
     * @param titulo título da listagem
     * @param <T> tipo dos elementos ou do resultado
     * @return seleciona um elemento ou cancela com 0.
     */
    public static <T> T apresentaESeleciona(List<T> lista, String titulo) {
        if (lista.isEmpty()) throw new IllegalArgumentException(Idioma.t("Não existem elementos para selecionar: ") + titulo);
        apresentaLista(lista, titulo);
        System.out.println(Idioma.t("0. Cancelar"));
        int indice = opcao(Idioma.t("Escolha:"), 0, lista.size());
        if (indice == 0) throw new OperacaoCanceladaException();
        return lista.get(indice - 1);
    }
    /**
     * Seleciona vários elementos sem duplicados, por exemplo 1,2,3.
     *
     * @param lista elementos disponíveis para apresentação ou seleção
     * @param titulo título da listagem
     * @param obrigatorio true se for necessário selecionar pelo menos um elemento
     * @param <T> tipo dos elementos ou do resultado
     * @return seleciona vários elementos sem duplicados, por exemplo 1,2,3.
     */
    public static <T> List<T> selecionarVarios(List<T> lista, String titulo, boolean obrigatorio) {
        if (lista.isEmpty()) {
            if (obrigatorio) throw new IllegalArgumentException(Idioma.t("Não existem elementos definidos: ") + titulo);
            return List.of();
        }
        apresentaLista(lista, titulo);
        System.out.println(obrigatorio
                ? Idioma.t("Selecione uma ou mais opções, separadas por vírgulas, ou 0 para cancelar.")
                : Idioma.t("Selecione opções separadas por vírgulas, ou 0 para nenhuma."));
        while (true) {
            String texto = readLineFromConsole(Idioma.t("Opções:")).strip();
            if (texto.equals("0")) {
                if (obrigatorio) throw new OperacaoCanceladaException();
                return List.of();
            }
            try {
                LinkedHashSet<T> escolhidos = new LinkedHashSet<>();
                for (String parte : texto.split(",", -1)) {
                    int indice = Integer.parseInt(parte.strip());
                    if (indice < 1 || indice > lista.size()) throw new NumberFormatException();
                    escolhidos.add(lista.get(indice - 1));
                }
                return new ArrayList<>(escolhidos);
            } catch (NumberFormatException e) { System.out.println(Idioma.t("Seleção inválida. Use números da lista, por exemplo 1,2.")); }
        }
    }
    /**
     * Contém erros esperados e permite repetir o caso de uso com novos dados.
     *
     * @param operacao operação a executar sobre a cópia de trabalho ou na consola
     */
    public static void executar(Runnable operacao) {
        while (true) {
            try { operacao.run(); return; }
            catch (OperacaoCanceladaException e) { System.out.println(e.getMessage()); return; }
            catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println(Idioma.t("Não foi possível concluir: ") + e.getMessage());
                try { if (!confirma(Idioma.t("Pretende repetir a operação com novos dados?"))) return; }
                catch (OperacaoCanceladaException cancelada) { System.out.println(cancelada.getMessage()); return; }
            }
        }
    }
    /** Pergunta o idioma da interface (português, inglês ou francês) e aplica-o. */
    public static void escolherIdioma() {
        System.out.println("\nIdioma / Language / Langue:\n1. Português\n2. English\n3. Français");
        Idioma.definir(new String[] {"pt", "en", "fr"}[opcao("1-3:", 1, 3) - 1]);
    }
    /** Indica a ausência de confirmação sem modificar o domínio. */
    public static void cancelada() { System.out.println(Idioma.t("Operação cancelada. Os dados não foram alterados.")); }
}
