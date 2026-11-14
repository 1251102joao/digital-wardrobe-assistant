package org.UI;

import org.Controller.*;
import org.Utils.Utils;

/** Menu inicial adaptado do código base, com autenticação antes dos menus privados. */
public final class MenuInicial_UI {
    private final ContextoAplicacao contexto;
    /**
     * Recebe o contexto único da execução.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public MenuInicial_UI(ContextoAplicacao contexto) { this.contexto = contexto; }
    /** Executa o menu inicial e o encaminhamento segundo o papel autenticado. */
    public void run() {
        while (true) {
            System.out.println("\n###### ASSISTENTE DIGITAL DE VESTUÁRIO ######");
            System.out.println("1. Visitante / registar utilizador");
            System.out.println("2. Iniciar sessão");
            System.out.println("0. Sair");
            Utils.executar(() -> {
                int opcao = Utils.opcao("Escolha:", 0, 2);
                if (opcao == 0) throw new org.Utils.FimEntradaException();
                if (opcao == 1) new MenuUNR_UI(contexto).run();
                else iniciarSessao();
            });
        }
    }
    private void iniciarSessao() {
        var controller = new Autenticar_Controller(contexto);
        String email = Utils.obrigatorio("Email:");
        String password = Utils.password("Palavra-passe:");
        try {
            var conta = controller.autenticar(email, password);
            System.out.println("Sessão iniciada: " + conta.getNome());
            if (conta.isAdministrador()) new MenuAdm_UI(contexto).run();
            else new MenuUtF_UI(contexto).run();
        } finally { controller.terminarSessao(); }
    }
    /** Configuração inicial sem credenciais administrativas fixas. */
    public void configurarAdministrador() {
        while (contexto.getAssistente().necessitaAdministrador()) {
            System.out.println("\nPrimeira execução: crie a conta de administrador. Escreva 0 em qualquer campo para recomeçar.");
            Utils.executar(() -> {
                String nome = Utils.obrigatorio("Nome do administrador:");
                String email = Utils.obrigatorio("Email do administrador:");
                String password = Utils.password("Palavra-passe:");
                System.out.println("Nome: " + nome + "\nEmail: " + email);
                if (Utils.confirma("Confirma a criação do administrador?")) {
                    new Autenticar_Controller(contexto).configurarAdministrador(nome, email, password);
                    System.out.println("Administrador criado. Inicie sessão para definir os catálogos.");
                } else Utils.cancelada();
            });
        }
    }
}
