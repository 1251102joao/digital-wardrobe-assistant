package org.Model;

import java.util.List;

/**
 * Valores iniciais dos catálogos, para o administrador não ter de começar do zero.
 * São apenas sugestões: o administrador continua a poder acrescentar outros pelos UC7 a UC10 e UC12.
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

    /** Características iniciais (UC10), cada uma com designação e descrição. */
    public static final List<String[]> CARACTERISTICAS = List.of(
            new String[] {"Algodão", "Peça de algodão"},
            new String[] {"Lã", "Peça de lã, tecido quente"},
            new String[] {"Leve", "Tecido fino e fresco"},
            new String[] {"Quente", "Adequada a tempo frio"},
            new String[] {"Impermeável", "Protege da chuva"},
            new String[] {"Formal", "Adequada a ocasiões formais"},
            new String[] {"Desportiva", "Própria para atividade física"},
            new String[] {"Confortável", "Folgada e macia"});
}
