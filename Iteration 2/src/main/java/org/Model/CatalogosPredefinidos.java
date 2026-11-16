package org.Model;

import java.util.List;

/**
 * Valores iniciais dos catálogos, para o administrador não ter de começar do zero.
 * São apenas sugestões: o administrador continua a poder acrescentar outros pelos UC7, UC8, UC9 e UC12.
 * As características e as partes do corpo são listas fixas do sistema.
 */
public final class CatalogosPredefinidos {
    private CatalogosPredefinidos() { }

    /** Tipos de roupa iniciais (UC12). */
    public static final List<String> TIPOS_ROUPA = List.of(
            "T-shirt", "Camisa", "Camisola", "Casaco", "Calças", "Calções",
            "Saia", "Vestido", "Sapatilhas", "Sapatos", "Botas", "Acessórios");

    /** Cores iniciais (UC7). */
    public static final List<String> CORES = List.of(
            "Branco", "Preto", "Cinzento", "Azul", "Vermelho", "Verde",
            "Amarelo", "Laranja", "Rosa", "Roxo", "Castanho", "Bege");

    /** Estações do ano iniciais (UC9). */
    public static final List<String> ESTACOES = List.of("Primavera", "Verão", "Outono", "Inverno");

    /** Ocasiões iniciais (UC8). */
    public static final List<String> OCASIOES = List.of(
            "Aulas", "Trabalho", "Casual", "Desporto", "Jantar", "Festa", "Cerimónia");

    /** Características fixas do sistema, cada uma com designação e descrição. */
    public static final List<String[]> CARACTERISTICAS = List.of(
            new String[] {"Algodão", "Peça de algodão"},
            new String[] {"Lã", "Peça de lã, tecido quente"},
            new String[] {"Leve", "Tecido fino e fresco"},
            new String[] {"Quente", "Adequada a tempo frio"},
            new String[] {"Impermeável", "Protege da chuva"},
            new String[] {"Formal", "Adequada a ocasiões formais"},
            new String[] {"Desportiva", "Própria para atividade física"},
            new String[] {"Confortável", "Folgada e macia"});

    /** Designação da parte do corpo que uma peça de tronco cobre; usada pela política de composição. */
    public static final String TRONCO = "Tronco";
    /** Designação da parte do corpo das pernas. */
    public static final String PERNAS = "Pernas";
    /** Designação da parte do corpo dos pés. */
    public static final String PES = "Pés";

    /** Partes do corpo fixas do sistema; uma peça pode estar associada a uma ou mais. */
    public static final List<String> PARTES_CORPO = List.of(
            "Cabeça", "Pescoço", TRONCO, "Braços", "Mãos", PERNAS, PES);
}
