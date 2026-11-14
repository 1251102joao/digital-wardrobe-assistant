package org.Utils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Guarda apenas salt aleatório e hash PBKDF2, nunca a palavra-passe original. */
public final class PalavraPasse implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ITERACOES = 210_000;
    /** Valor aleatório único utilizado na derivação. */
    private final byte[] salt;
    /** Chave derivada da palavra-passe por PBKDF2. */
    private final byte[] hash;

    /**
     * Cria o hash de uma palavra-passe não vazia; a política está em aberto no relatório.
     *
     * @param texto texto introduzido pelo utilizador
     */
    public PalavraPasse(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("A palavra-passe é obrigatória.");
        }
        salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        hash = derivar(texto);
    }

    private byte[] derivar(String texto) {
        char[] chars = texto.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, salt, ITERACOES, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
        } catch (java.security.GeneralSecurityException e) {
            throw new IllegalStateException("Não foi possível proteger a palavra-passe.", e);
        } finally {
            spec.clearPassword();
            Arrays.fill(chars, '\0');
        }
    }

    /**
     * Compara a tentativa com o hash guardado.
     *
     * @param tentativa palavra-passe introduzida na autenticação
     * @return compara a tentativa com o hash guardado.
     */
    public boolean corresponde(String tentativa) {
        return tentativa != null && MessageDigest.isEqual(hash, derivar(tentativa));
    }
}
