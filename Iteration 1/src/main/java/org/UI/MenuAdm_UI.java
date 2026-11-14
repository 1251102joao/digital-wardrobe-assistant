package org.UI;

import org.Controller.*;
import org.Utils.Utils;

/** Menu de administrador, adaptado do menu correspondente do código base. */
public final class MenuAdm_UI {
    private final ContextoAplicacao contexto;
    /**
     * Partilha o contexto com as interfaces dos casos de uso.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public MenuAdm_UI(ContextoAplicacao contexto) { this.contexto = contexto; }
    /** Apresenta as operações até o utilizador regressar. */
    public void run() {
        while (true) {
            System.out.println("\n###### ADMINISTRADOR ######");
            System.out.println("1. Definir listagem de cores (UC7)");
            System.out.println("2. Definir ocasiões (UC8)");
            System.out.println("3. Definir estações do ano (UC9)");
            System.out.println("4. Definir característica (UC10)");
            System.out.println("5. Definir tipos de roupa (UC12)");
            System.out.println("0. Terminar sessão");
            switch (Utils.opcao("Escolha:", 0, 5)) {
                case 0 -> { return; }
                case 1 -> Utils.executar(() -> new DefinirCor_UI(contexto).run());
                case 2 -> Utils.executar(() -> new DefinirOcasiao_UI(contexto).run());
                case 3 -> Utils.executar(() -> new DefinirEstacao_UI(contexto).run());
                case 4 -> Utils.executar(() -> new DefinirCaracteristica_UI(contexto).run());
                case 5 -> Utils.executar(() -> new DefinirTipoRoupa_UI(contexto).run());
                default -> throw new IllegalStateException("Opção inesperada.");
            }
        }
    }
}
