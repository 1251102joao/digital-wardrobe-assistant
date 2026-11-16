package org.Model;

/** Categoria sazonal escolhida pelo utilizador, sem datas associadas (UC9). */
public final class Estacao extends Referencia {
    private static final long serialVersionUID = 3L;

    /**
     * Cria uma estação com designação obrigatória, sem limites de calendário.
     * @param designacao nome da estação, por exemplo Inverno
     */
    public Estacao(String designacao) { super(designacao); }
}
