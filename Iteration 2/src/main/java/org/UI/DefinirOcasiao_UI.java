package org.UI;

import org.Utils.Idioma;
import org.Controller.*;
import org.Utils.Utils;

/** Constrói a listagem em memória e pede confirmação do lote completo. */
public final class DefinirOcasiao_UI {
    private final DefinirOcasiao_Controller controller;
    /**
     * Liga a UI ao controlador.
     *
     * @param contexto contexto partilhado da aplicação
     */
    public DefinirOcasiao_UI(ContextoAplicacao contexto) { controller = new DefinirOcasiao_Controller(contexto); }
    /** Permite corrigir candidatos inválidos sem perder os elementos válidos do lote. */
    public void run() {
        System.out.println(Idioma.t("Introduza designações, uma de cada vez. Escreva 0 para terminar; no fim confirma ou descarta a listagem (N)."));
        while (true) {
            String nome = Utils.obrigatorioOuFim(Idioma.t("Designação da ocasião:"));
            if (nome == null) break;
            try {

                controller.adicionarOcasiao(nome);
                System.out.println(Idioma.t("Adicionada à listagem em construção."));
            } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        }
        var listagem = controller.obterListagem();
        if (listagem.isEmpty()) { System.out.println(Idioma.t("A listagem está vazia. Nada foi registado.")); return; }
        Utils.apresentaLista(listagem, Idioma.t("Listagem para confirmação"));
        if (Utils.confirma(Idioma.t("Confirma o registo da listagem completa?"))) {
            controller.registarListagem();
            System.out.println(Idioma.t("Listagem registada com sucesso."));
        } else Utils.cancelada();
    }
}
