package org.Model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Política de composição baseada nas partes do corpo: um outfit tem de cobrir tronco, pernas e pés.
 * Para cada parte ainda por cobrir escolhe uma peça candidata, preferindo as que cobrem mais partes
 * obrigatórias de uma só vez (um vestido cobre tronco e pernas); entre iguais, escolhe ao acaso,
 * para que pedidos repetidos possam dar sugestões diferentes.
 */
public final class ComposicaoPorPartesCorpo implements PoliticaComposicao {
    private static final List<ParteCorpo> OBRIGATORIAS = List.of(
            new ParteCorpo(CatalogosPredefinidos.TRONCO), new ParteCorpo(CatalogosPredefinidos.PERNAS),
            new ParteCorpo(CatalogosPredefinidos.PES));
    private final Random aleatorio;

    /** Usa uma fonte de aleatoriedade própria. */
    public ComposicaoPorPartesCorpo() { this(new Random()); }
    /**
     * Permite fixar a fonte de aleatoriedade, por exemplo nos testes.
     * @param aleatorio fonte usada para desempatar peças equivalentes
     */
    public ComposicaoPorPartesCorpo(Random aleatorio) { this.aleatorio = aleatorio; }

    @Override
    public List<PecaVestuario> selecionar(List<PecaVestuario> candidatas) {
        List<PecaVestuario> escolhidas = new ArrayList<>();
        Set<ParteCorpo> cobertas = new LinkedHashSet<>();
        for (ParteCorpo parte : OBRIGATORIAS) {
            if (cobertas.contains(parte)) continue;
            List<PecaVestuario> opcoes = candidatas.stream()
                    .filter(p -> p.getPartesCorpo().contains(parte) && !escolhidas.contains(p)).toList();
            if (opcoes.isEmpty()) return List.of();
            long melhor = opcoes.stream().mapToLong(p -> novas(p, cobertas)).max().orElse(0);
            List<PecaVestuario> empatadas = opcoes.stream().filter(p -> novas(p, cobertas) == melhor).toList();
            PecaVestuario peca = empatadas.get(aleatorio.nextInt(empatadas.size()));
            escolhidas.add(peca);
            cobertas.addAll(peca.getPartesCorpo());
        }
        return escolhidas;
    }
    private static long novas(PecaVestuario peca, Set<ParteCorpo> cobertas) {
        return OBRIGATORIAS.stream().filter(o -> peca.getPartesCorpo().contains(o) && !cobertas.contains(o)).count();
    }
}
