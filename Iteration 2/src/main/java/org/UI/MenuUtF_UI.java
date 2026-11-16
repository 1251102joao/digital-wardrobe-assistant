package org.UI;

import org.Utils.Idioma;
import org.Controller.*;
import org.Utils.Utils;

/** Menu de utilizador, adaptado do menu correspondente do código base. */
public final class MenuUtF_UI {
    private final ContextoAplicacao contexto;
    /**
     * Partilha o contexto com as interfaces dos casos de uso.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public MenuUtF_UI(ContextoAplicacao contexto) { this.contexto = contexto; }
    /** Apresenta as operações até o utilizador regressar. */
    public void run() {
        while (true) {
            System.out.println(Idioma.t("\n###### UTILIZADOR ######"));
            System.out.println(Idioma.t("1. Adicionar peça (UC1)"));
            System.out.println(Idioma.t("2. Remover peça (UC2)"));
            System.out.println(Idioma.t("3. Editar peça (UC3)"));
            System.out.println(Idioma.t("4. Marcar peça como indisponível (UC4)"));
            System.out.println(Idioma.t("5. Consultar guarda-roupa (UC5)"));
            System.out.println(Idioma.t("6. Solicitar outfit (UC6)"));
            System.out.println(Idioma.t("7. Avaliar outfit (UC13)"));
            System.out.println(Idioma.t("8. Consultar histórico de recomendações e avaliações (UC14)"));
            System.out.println(Idioma.t("0. Terminar sessão"));
            switch (Utils.opcao(Idioma.t("Escolha:"), 0, 8)) {
                case 0 -> { return; }
                case 1 -> Utils.executar(() -> new AdicionarPeca_UI(contexto).run());
                case 2 -> Utils.executar(() -> new RemoverPeca_UI(contexto).run());
                case 3 -> Utils.executar(() -> new EditarPeca_UI(contexto).run());
                case 4 -> Utils.executar(() -> new MarcarIndisponivel_UI(contexto).run());
                case 5 -> Utils.executar(() -> new ConsultarGuardaRoupa_UI(contexto).run());
                case 6 -> Utils.executar(() -> new SolicitarOutfit_UI(contexto).run());
                case 7 -> Utils.executar(() -> new AvaliarOutfit_UI(contexto).run());
                case 8 -> Utils.executar(() -> new ConsultarHistorico_UI(contexto).run());
                default -> throw new IllegalStateException(Idioma.t("Opção inesperada."));
            }
        }
    }
}
