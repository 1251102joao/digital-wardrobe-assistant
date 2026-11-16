package org.UI;

import org.Utils.Idioma;
import org.Controller.*;
import org.Utils.Utils;

/** Interface do registo de utilizadores (UC11). */
public final class RegistarUtilizador_UI {
    private final RegistarUtilizador_Controller controller;
    /**
     * Liga a UI ao controlador.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public RegistarUtilizador_UI(ContextoAplicacao contexto) { controller = new RegistarUtilizador_Controller(contexto); }
    /** Solicita os dados sem apresentar a palavra-passe na confirmação. */
    public void run() {
        String nome = Utils.obrigatorio(Idioma.t("Nome:"));
        String email = Utils.obrigatorio(Idioma.t("Email:"));
        String password = Utils.password(Idioma.t("Palavra-passe:"));
        controller.setDados(nome, email, password);
        System.out.println(controller.obterDados());
        if (Utils.confirma(Idioma.t("Confirma o registo?"))) {
            controller.registarUtilizador();
            System.out.println(Idioma.t("Utilizador registado com sucesso. Pode agora iniciar sessão."));
        } else Utils.cancelada();
    }
}
