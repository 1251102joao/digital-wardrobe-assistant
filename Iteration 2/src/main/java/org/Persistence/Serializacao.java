package org.Persistence;

import org.Utils.Idioma;
import java.io.*;

/** Conversão do domínio para bytes e leitura limitada às classes usadas pela aplicação. */
public final class Serializacao {
    private Serializacao() { }
    /**
     * Serializa um objeto sem alterar o original.
     *
     * @param objeto objeto a converter ou copiar
     * @return serializa um objeto sem alterar o original.
     * @throws IOException se não for possível ler ou gravar o conteúdo local
     */
    public static byte[] escrever(Serializable objeto) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) { out.writeObject(objeto); }
        return bytes.toByteArray();
    }
    /**
     * Lê apenas os tipos autorizados e limita dimensão/profundidade do grafo.
     *
     * @param bytes conteúdo serializado a validar
     * @param tipo classe esperada no resultado
     * @param <T> tipo dos elementos ou do resultado
     * @return lê apenas os tipos autorizados e limita dimensão/profundidade do grafo.
     * @throws IOException se não for possível ler ou gravar o conteúdo local
     */
    public static <T> T ler(byte[] bytes, Class<T> tipo) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            in.setObjectInputFilter(ObjectInputFilter.Config.createFilter(
                    "maxdepth=60;maxrefs=500000;maxarray=500000;maxbytes=52428800;"
                    + "org.Model.*;org.Utils.PalavraPasse;java.base/java.lang.*;"
                    + "java.base/java.util.*;java.base/java.time.*;!*"));
            return tipo.cast(in.readObject());
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException(Idioma.t("O ficheiro não é compatível com esta versão."), e);
        }
    }
    /**
     * Cria uma cópia independente para executar alterações antes de as confirmar.
     *
     * @param objeto objeto a converter ou copiar
     * @param tipo classe esperada no resultado
     * @param <T> tipo dos elementos ou do resultado
     * @return cria uma cópia independente para executar alterações antes de as confirmar.
     */
    public static <T extends Serializable> T copiar(T objeto, Class<T> tipo) {
        try { return ler(escrever(objeto), tipo); }
        catch (IOException e) { throw new IllegalStateException(Idioma.t("Não foi possível preparar a alteração."), e); }
    }
}
