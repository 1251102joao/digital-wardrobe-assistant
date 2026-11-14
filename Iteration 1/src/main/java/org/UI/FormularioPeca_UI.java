package org.UI;

import java.util.List;
import org.Controller.Pecas_Controller;
import org.Model.*;
import org.Utils.Utils;

/**
 * Formulário comum à adição e edição; não executa alterações no domínio.
 *
 * @param tipoRoupa primeira seleção do formulário, a partir do catálogo UC12
 * @param nome nome obrigatório
 * @param cor cor selecionada no catálogo
 * @param estacoes estações selecionadas, pelo menos uma
 * @param ocasioes ocasiões selecionadas, pelo menos uma
 * @param caracteristicas características selecionadas, podendo ser uma lista vazia
 */
public record FormularioPeca_UI(TipoRoupa tipoRoupa, String nome, Cor cor, List<Estacao> estacoes,
                                List<Ocasiao> ocasioes, List<Caracteristica> caracteristicas) {
    /**
     * Solicita os campos pelos catálogos fornecidos pelo controlador.
     *
     * @param controller controlador que disponibiliza os catálogos
     * @return solicita os campos pelos catálogos fornecidos pelo controlador.
     */
    public static FormularioPeca_UI ler(Pecas_Controller controller) {
        if (controller.obterListaTiposRoupa().isEmpty() || controller.obterListaCores().isEmpty() || controller.obterListaEstacoes().isEmpty()
                || controller.obterListaOcasioes().isEmpty() || controller.obterListaCaracteristicas().isEmpty()) {
            throw new IllegalArgumentException("O administrador deve definir tipos de roupa, cores, estações, ocasiões e características antes de registar peças.");
        }
        TipoRoupa tipoRoupa = Utils.apresentaESeleciona(controller.obterListaTiposRoupa(), "Tipos de roupa");
        String nome = Utils.obrigatorio("Nome da peça:");
        Cor cor = Utils.apresentaESeleciona(controller.obterListaCores(), "Cores");
        List<Estacao> estacoes = Utils.selecionarVarios(controller.obterListaEstacoes(), "Estações", true);
        List<Ocasiao> ocasioes = Utils.selecionarVarios(controller.obterListaOcasioes(), "Ocasiões", true);
        List<Caracteristica> caracteristicas = Utils.selecionarVarios(controller.obterListaCaracteristicas(), "Características", false);
        return new FormularioPeca_UI(tipoRoupa, nome, cor, estacoes, ocasioes, caracteristicas);
    }
}
