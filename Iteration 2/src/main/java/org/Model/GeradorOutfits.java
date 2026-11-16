package org.Model;

import org.Utils.Idioma;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/** Serviço de composição do UC6; a filtragem pertence ao guarda-roupa. */
public final class GeradorOutfits {
    private final PoliticaComposicao politica;

    /**
     * Recebe a regra de composição, separada das operações de interface e dos catálogos.
     * @param politica regra que determina quais as peças necessárias numa combinação
     */
    public GeradorOutfits(PoliticaComposicao politica) {
        this.politica = Objects.requireNonNull(politica);
    }

    /**
     * Compõe e valida uma sugestão, distinguindo falta de peças de combinação impossível.
     * @param candidatas peças filtradas pelo guarda-roupa do utilizador
     * @param ocasiao ocasião escolhida explicitamente
     * @param estacao estação escolhida explicitamente
     * @return outfit válido, construído com as versões das peças candidatas
     */
    public Outfit gerar(List<PecaVestuario> candidatas, Ocasiao ocasiao, Estacao estacao) {
        Objects.requireNonNull(ocasiao, Idioma.t("Ocasião obrigatória."));
        Objects.requireNonNull(estacao, Idioma.t("Estação obrigatória."));
        if (candidatas == null || candidatas.isEmpty()) {
            throw new IllegalArgumentException(Idioma.t("Não existem peças disponíveis adequadas à ocasião e à estação escolhidas."));
        }
        List<PecaVestuario> elegiveis = List.copyOf(candidatas);
        if (elegiveis.stream().anyMatch(p -> !p.isAdequada(ocasiao, estacao))) {
            throw new IllegalArgumentException(Idioma.t("As candidatas contêm peças indisponíveis ou inadequadas."));
        }
        List<PecaVestuario> escolhidas = politica.selecionar(elegiveis);
        if (escolhidas == null || escolhidas.isEmpty()) {
            throw new IllegalArgumentException(Idioma.t("Existem peças elegíveis, mas não permitem formar uma combinação válida."));
        }
        List<PecaVestuario> resultado = new ArrayList<>();
        Set<UUID> ids = new HashSet<>();
        for (PecaVestuario escolhida : escolhidas) {
            if (escolhida == null || !ids.add(escolhida.getId())) {
                throw new IllegalStateException(Idioma.t("A política devolveu uma peça repetida ou inválida."));
            }
            PecaVestuario original = elegiveis.stream().filter(p -> p.getId().equals(escolhida.getId()))
                    .findFirst().orElseThrow(() -> new IllegalStateException(Idioma.t("A política devolveu uma peça fora das candidatas.")));
            resultado.add(original);
        }
        return new Outfit(resultado);
    }
}
