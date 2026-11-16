package org.UI;

import java.nio.file.Path;
import org.Controller.ContextoAplicacao;
import org.Persistence.RepositorioDados;
import org.Utils.*;

/** Classe de arranque, adaptada de org.UI.Main do código base de Dulce Mota. */
public final class Main {
    private Main() { }
    /**
     * Abre a aplicação normal ou --demo; --dados permite escolher outro ficheiro local.
     *
     * @param args opções --demo e/ou --dados caminho
     */
    public static void main(String[] args) {
        try {
            boolean demo = false;
            Path dados = null;
            String lingua = null;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--demo")) demo = true;
                else if (args[i].equals("--dados") && i + 1 < args.length) dados = Path.of(args[++i]);
                else if (args[i].equals("--lingua") && i + 1 < args.length) lingua = args[++i];
                else throw new IllegalArgumentException("Uso: org.UI.Main [--demo] [--dados caminho] [--lingua pt|en|fr]");
            }
            if (lingua != null) Idioma.definir(lingua);
            else Utils.escolherIdioma();
            ContextoAplicacao contexto;
            if (demo) {
                contexto = DadosDemonstracao.criar();
                System.out.println(Idioma.t("MODO DEMONSTRAÇÃO: dados apenas em memória."));
                System.out.println(Idioma.t("Administrador: admin@demo.pt / Admin123!"));
                System.out.println(Idioma.t("Utilizador: utilizador@demo.pt / Demo123!"));
            } else if (dados != null) {
                var repositorio = new RepositorioDados(dados);
                contexto = new ContextoAplicacao(repositorio.carregar(), repositorio);
                System.out.println(Idioma.t("Dados locais: ") + repositorio.getFicheiro());
            } else {
                contexto = new ContextoAplicacao(new org.Model.AssistenteDigital(), null);
                System.out.println(Idioma.t("Dados apenas em memória: registar atualiza o estado desta execução."));
                System.out.println(Idioma.t("Para gravação local opcional, use --dados dados/assistente-atualizado.dat."));
            }
            if (!demo && contexto.getAssistente().catalogosEmFalta()) {
                contexto.alterar(a -> { a.carregarPredefinidos(); return null; });
                System.out.println(Idioma.t("Catálogos iniciais carregados (tipos de roupa, cores, estações e ocasiões)."));
                System.out.println(Idioma.t("O administrador pode acrescentar mais em qualquer altura."));
            }
            System.out.println(Idioma.t("Escreva 0 durante qualquer operação para cancelar."));
            var menu = new MenuInicial_UI(contexto);
            if (contexto.getAssistente().necessitaAdministrador()) menu.configurarAdministrador();
            menu.run();
        } catch (FimEntradaException e) { System.out.println(Idioma.t("Aplicação terminada.")); }
        catch (Exception e) {
            System.err.println(Idioma.t("Não foi possível iniciar a aplicação: ") + e.getMessage());
            System.exit(1);
        }
    }
}
