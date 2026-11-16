package org.UI;

import org.Utils.Idioma;
import java.util.List;
import org.Controller.Pecas_Controller;
import org.Model.*;
import org.Utils.Utils;

/**
 * Formulário comum à adição e edição; não executa alterações no domínio.
 *
 * @param tipoRoupa primeira seleção do formulário, a partir do catálogo UC12
 * @param nome nome obrigatório
 * @param cores cores selecionadas no catálogo, pelo menos uma
 * @param partesCorpo partes do corpo associadas, pelo menos uma
 * @param estacoes estações selecionadas, pelo menos uma
 * @param ocasioes ocasiões selecionadas, pelo menos uma
 * @param caracteristicas características selecionadas, pelo menos uma
 */
public record FormularioPeca_UI(TipoRoupa tipoRoupa, String nome, List<Cor> cores,
                                List<ParteCorpo> partesCorpo, List<Estacao> estacoes, List<Ocasiao> ocasioes, List<Caracteristica> caracteristicas) {
    /**
     * Solicita os campos pelos catálogos fornecidos pelo controlador.
     *
     * @param controller controlador que disponibiliza os catálogos
     * @return solicita os campos pelos catálogos fornecidos pelo controlador.
     */
    public static FormularioPeca_UI ler(Pecas_Controller controller) {
        if (controller.obterListaTiposRoupa().isEmpty() || controller.obterListaCores().isEmpty() || controller.obterListaEstacoes().isEmpty()
                || controller.obterListaOcasioes().isEmpty()) {
            throw new IllegalArgumentException(Idioma.t("O administrador deve definir tipos de roupa, cores, estações e ocasiões antes de registar peças."));
        }
        TipoRoupa tipoRoupa = Utils.apresentaESeleciona(controller.obterListaTiposRoupa(), Idioma.t("Tipos de roupa"));
        String nome = Utils.obrigatorio(Idioma.t("Nome da peça:"));
        List<Cor> cores = Utils.selecionarVarios(controller.obterListaCores(), Idioma.t("Cores da peça"), true);
        List<ParteCorpo> partesCorpo = Utils.selecionarVarios(controller.obterListaPartesCorpo(), Idioma.t("Partes do corpo"), true);
        List<Estacao> estacoes = Utils.selecionarVarios(controller.obterListaEstacoes(), Idioma.t("Estações"), true);
        List<Ocasiao> ocasioes = Utils.selecionarVarios(controller.obterListaOcasioes(), Idioma.t("Ocasiões"), true);
        List<Caracteristica> caracteristicas = Utils.selecionarVarios(controller.obterListaCaracteristicas(), Idioma.t("Características"), true);
        return new FormularioPeca_UI(tipoRoupa, nome, cores, partesCorpo, estacoes, ocasioes, caracteristicas);
    }
}
