package org.UI;

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
            System.out.println("\n###### UTILIZADOR ######");
            System.out.println("1. Adicionar peça (UC1)");
            System.out.println("2. Remover peça (UC2)");
            System.out.println("3. Editar peça (UC3)");
            System.out.println("4. Marcar peça como indisponível (UC4)");
            System.out.println("5. Consultar guarda-roupa (UC5)");
            System.out.println("6. Solicitar outfit (UC6)");
            System.out.println("0. Terminar sessão");
            switch (Utils.opcao("Escolha:", 0, 6)) {
                case 0 -> { return; }
                case 1 -> Utils.executar(() -> new AdicionarPeca_UI(contexto).run());
                case 2 -> Utils.executar(() -> new RemoverPeca_UI(contexto).run());
                case 3 -> Utils.executar(() -> new EditarPeca_UI(contexto).run());
                case 4 -> Utils.executar(() -> new MarcarIndisponivel_UI(contexto).run());
                case 5 -> Utils.executar(() -> new ConsultarGuardaRoupa_UI(contexto).run());
                case 6 -> Utils.executar(() -> new SolicitarOutfit_UI(contexto).run());
                default -> throw new IllegalStateException("Opção inesperada.");
            }
        }
    }
}
