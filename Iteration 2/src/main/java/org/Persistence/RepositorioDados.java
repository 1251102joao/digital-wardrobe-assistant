package org.Persistence;

import org.Utils.Idioma;
import java.io.IOException;
import java.nio.file.*;
import org.Model.AssistenteDigital;

/** Persistência local com substituição atómica; não substitui dados danificados por dados vazios. */
public final class RepositorioDados {
    private final Path ficheiro;
    /**
     * Define o ficheiro privado desta instalação.
     *
     * @param ficheiro caminho do ficheiro local de dados
     */
    public RepositorioDados(Path ficheiro) { this.ficheiro = ficheiro.toAbsolutePath().normalize(); }
    /**
     * Localização efetiva apresentada no arranque.
     *
     * @return localização efetiva apresentada no arranque.
     */
    public Path getFicheiro() { return ficheiro; }
    /**
     * Carrega o domínio ou cria um vazio se o ficheiro ainda não existir.
     *
     * @return carrega o domínio ou cria um vazio se o ficheiro ainda não existir.
     * @throws IOException se não for possível ler ou gravar o conteúdo local
     */
    public AssistenteDigital carregar() throws IOException {
        if (!Files.exists(ficheiro)) return new AssistenteDigital();
        if (Files.size(ficheiro) > 52_428_800) throw new IOException(Idioma.t("O ficheiro de dados excede o limite de 50 MB."));
        try {
            return Serializacao.ler(Files.readAllBytes(ficheiro), AssistenteDigital.class);
        } catch (java.io.InvalidClassException e) {
            throw new IOException(Idioma.t("O ficheiro pertence a uma versão anterior do modelo e foi preservado. ")
                    + Idioma.t("Para usar o modelo do relatório atualizado, execute com ")
                    + Idioma.t("--dados dados/assistente-atualizado.dat e configure os dados nesse novo ficheiro."), e);
        }
    }
    /**
     * Grava uma versão completa antes de substituir o ficheiro anterior.
     *
     * @param assistente raiz do domínio a carregar ou guardar
     * @throws IOException se não for possível ler ou gravar o conteúdo local
     */
    public void guardar(AssistenteDigital assistente) throws IOException {
        Files.createDirectories(ficheiro.getParent());
        Path temporario = Files.createTempFile(ficheiro.getParent(), "assistente-", ".tmp");
        try {
            Files.write(temporario, Serializacao.escrever(assistente));
            try { Files.move(temporario, ficheiro, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING); }
            catch (AtomicMoveNotSupportedException e) {
                Files.move(temporario, ficheiro, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally { Files.deleteIfExists(temporario); }
    }
}
