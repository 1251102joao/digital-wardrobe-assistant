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
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--demo")) demo = true;
                else if (args[i].equals("--dados") && i + 1 < args.length) dados = Path.of(args[++i]);
                else throw new IllegalArgumentException("Uso: org.UI.Main [--demo] [--dados caminho]");
            }
            ContextoAplicacao contexto;
            if (demo) {
                contexto = DadosDemonstracao.criar();
                System.out.println("MODO DEMONSTRAÇÃO: dados apenas em memória.");
                System.out.println("Administrador: admin@demo.pt / Admin123!");
                System.out.println("Utilizador: utilizador@demo.pt / Demo123!");
            } else if (dados != null) {
                var repositorio = new RepositorioDados(dados);
                contexto = new ContextoAplicacao(repositorio.carregar(), repositorio);
                System.out.println("Dados locais: " + repositorio.getFicheiro());
            } else {
                contexto = new ContextoAplicacao(new org.Model.AssistenteDigital(), null);
                System.out.println("Dados apenas em memória: registar atualiza o estado desta execução.");
                System.out.println("Para gravação local opcional, use --dados dados/assistente-atualizado.dat.");
            }
            if (!demo && contexto.getAssistente().catalogosEmFalta()) {
                contexto.alterar(a -> { a.carregarPredefinidos(); return null; });
                System.out.println("Catálogos iniciais carregados (tipos de roupa, cores, estações, ocasiões e características).");
                System.out.println("O administrador pode acrescentar mais em qualquer altura.");
            }
            System.out.println("Escreva 0 durante qualquer operação para cancelar.");
            var menu = new MenuInicial_UI(contexto);
            if (contexto.getAssistente().necessitaAdministrador()) menu.configurarAdministrador();
            menu.run();
        } catch (FimEntradaException e) { System.out.println("Aplicação terminada."); }
        catch (Exception e) {
            System.err.println("Não foi possível iniciar a aplicação: " + e.getMessage());
            System.exit(1);
        }
    }
}
