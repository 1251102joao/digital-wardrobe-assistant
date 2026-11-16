package org.Model;

import java.util.List;

/** Regra de composição isolada, para poder ser substituída sem alterar o resto da aplicação. */
@FunctionalInterface
public interface PoliticaComposicao {
    /**
     * Seleciona uma combinação a partir de peças já elegíveis.
     * @param candidatas peças disponíveis adequadas à ocasião e à estação
     * @return peças da combinação, ou lista vazia se não for possível compor um outfit
     */
    List<PecaVestuario> selecionar(List<PecaVestuario> candidatas);
}
