package org.UI;

import org.Controller.*;
import org.Utils.Utils;

/** Constrói a listagem em memória e pede confirmação do lote completo. */
public final class DefinirEstacao_UI {
    private final DefinirEstacao_Controller controller;
    /**
     * Liga a UI ao controlador.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public DefinirEstacao_UI(ContextoAplicacao contexto) { controller = new DefinirEstacao_Controller(contexto); }
    /** Permite corrigir candidatos inválidos sem perder os elementos válidos do lote. */
    public void run() {
        System.out.println("Introduza designações, uma de cada vez. Escreva 0 para terminar; no fim confirma ou descarta a listagem (N).");
        while (true) {
            String nome = Utils.obrigatorioOuFim("Designação da estação:");
            if (nome == null) break;
            try {
                controller.adicionarEstacao(nome);
                System.out.println("Adicionada à listagem em construção.");
            } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }
        var listagem = controller.obterListagem();
        if (listagem.isEmpty()) { System.out.println("A listagem está vazia. Nada foi registado."); return; }
        Utils.apresentaLista(listagem, "Listagem para confirmação");
        if (Utils.confirma("Confirma o registo da listagem completa?")) {
            controller.registarListagem();
            System.out.println("Listagem registada com sucesso.");
        } else Utils.cancelada();
    }
}
