package org.UI;

import org.Utils.Idioma;
import org.Controller.*;
import org.Utils.Utils;

/** Menu de visitante, adaptado do menu correspondente do código base. */
public final class MenuUNR_UI {
    private final ContextoAplicacao contexto;
    /**
     * Partilha o contexto com as interfaces dos casos de uso.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public MenuUNR_UI(ContextoAplicacao contexto) { this.contexto = contexto; }
    /** Apresenta as operações até o utilizador regressar. */
    public void run() {
        while (true) {
            System.out.println(Idioma.t("\n###### VISITANTE ######"));
            System.out.println(Idioma.t("1. Fazer registo (UC11)"));
            System.out.println(Idioma.t("0. Voltar"));
            switch (Utils.opcao(Idioma.t("Escolha:"), 0, 1)) {
                case 0 -> { return; }
                case 1 -> Utils.executar(() -> new RegistarUtilizador_UI(contexto).run());
                default -> throw new IllegalStateException(Idioma.t("Opção inesperada."));
            }
        }
    }
}
