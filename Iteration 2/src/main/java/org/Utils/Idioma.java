package org.Utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Idioma da interface (português, inglês ou francês). Os textos do código estão escritos em português
 * e funcionam como chave de tradução; um texto sem tradução é apresentado tal como está.
 * As traduções estão reunidas em {@link Traducoes}.
 */
public final class Idioma {
    /** Idiomas suportados, pela ordem das colunas de {@link Traducoes}. */
    public enum Lingua {
        /** Português. */ PT,
        /** Inglês. */ EN,
        /** Francês. */ FR
    }

    private static final Map<String, String[]> TABELA = new HashMap<>();
    private static volatile Lingua atual = Lingua.PT;
    static { for (String[] linha : Traducoes.TABELA) TABELA.put(linha[0], new String[] {linha[1], linha[2]}); }
    private Idioma() { }

    /**
     * Define o idioma a partir de um código (pt, en ou fr); códigos desconhecidos mantêm o português.
     *
     * @param codigo código do idioma, sem distinção de maiúsculas
     */
    public static void definir(String codigo) {
        try { atual = Lingua.valueOf(codigo.strip().toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException | NullPointerException e) { atual = Lingua.PT; }
    }
    /**
     * Idioma em uso.
     *
     * @return idioma atual
     */
    public static Lingua atual() { return atual; }
    /**
     * Traduz um texto escrito em português para o idioma atual.
     *
     * @param texto texto em português, que serve de chave
     * @return tradução, ou o próprio texto se estiver em português ou não houver tradução
     */
    public static String t(String texto) {
        if (atual == Lingua.PT || texto == null) return texto;
        String[] traducao = TABELA.get(texto);
        if (traducao == null) return texto;
        String resultado = traducao[atual.ordinal() - 1];
        return resultado.isEmpty() ? texto : resultado;
    }
}
