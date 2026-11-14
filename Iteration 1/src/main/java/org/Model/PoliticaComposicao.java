package org.Model;

import java.util.List;

/** Regra de composição isolada, para poder refletir a decisão validada com o cliente. */
@FunctionalInterface
public interface PoliticaComposicao {
    /**
     * Política usada nesta versão, conforme indicação expressa do grupo.
     * @return política que informa a pendência sem produzir uma combinação
     */
    static PoliticaComposicao pendente() {
        return candidatas -> { throw new ComposicaoPendenteException(); };
    }
    /**
     * Seleciona uma combinação a partir de peças já elegíveis.
     * @param candidatas peças disponíveis adequadas à ocasião e à estação
     * @return peças da combinação, ou lista vazia se não for possível compor um outfit
     */
    List<PecaVestuario> selecionar(List<PecaVestuario> candidatas);
}
