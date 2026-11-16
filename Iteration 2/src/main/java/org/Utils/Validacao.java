package org.Utils;

import java.text.Normalizer;
import java.util.Locale;

/** Regras comuns de texto; não contém regras específicas dos casos de uso. */
public final class Validacao {
    private Validacao() { }

    /**
     * Devolve texto aparado ou rejeita um campo obrigatório em falta.
     *
     * @param valor valor a validar ou novo estado solicitado
     * @param campo nome do campo para a mensagem de erro
     * @return devolve texto aparado ou rejeita um campo obrigatório em falta.
     */
    public static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + Idioma.t(" é obrigatório."));
        }
        return valor.strip();
    }

    /**
     * Normaliza espaços, Unicode e maiúsculas para verificar unicidade.
     *
     * @param texto texto introduzido pelo utilizador
     * @return normaliza espaços, Unicode e maiúsculas para verificar unicidade.
     */
    public static String chave(String texto) {
        return Normalizer.normalize(obrigatorio(texto, Idioma.t("Designação")), Normalizer.Form.NFC)
                .replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }
}
