package Controller;

import Model.Especialidade;
import Model.Medico;

/**
 * Controlador unificado para gestao de especialidades
 * Usa o modelo Especialidade e integra-se com GestorHospital
 */
public class EspecialidadeController {
    private Especialidade[] especialidades;
    private int total;
    private GestorHospital gestorHospital;

    public EspecialidadeController(int capacidade) {
        especialidades = new Especialidade[capacidade];
        total = 0;
        this.gestorHospital = null;
    }

    public void setGestorHospital(GestorHospital gestor) {
        this.gestorHospital = gestor;
    }

    public void sincronizarComGestor() {
        if (gestorHospital != null) {
            String[] dados = gestorHospital.getDadosEspecialidades();
            int num = gestorHospital.getNumEspecialidades();
            String separador = gestorHospital.getSeparador();

            total = 0;
            for (int i = 0; i < num && total < especialidades.length; i++) {
                if (dados[i] != null && !dados[i].isEmpty()) {
                    especialidades[total] = Especialidade.fromFicheiro(dados[i], separador);
                    total++;
                }
            }
        }
    }

    public boolean adicionarEspecialidade(String codigo, String nome) {
        if (total >= especialidades.length) {
            return false;
        }

        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }

        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        if (existeCodigo(codigo)) {
            return false;
        }

        especialidades[total++] = new Especialidade(codigo.toUpperCase(), nome);

        if (gestorHospital != null) {
            gestorHospital.adicionarEspecialidade(especialidades[total - 1]);
        }

        return true;
    }

    public boolean adicionarEspecialidade(Especialidade especialidade) {
        if (total >= especialidades.length) {
            return false;
        }

        if (especialidade == null || existeCodigo(especialidade.getCodigo())) {
            return false;
        }

        especialidades[total++] = especialidade;

        if (gestorHospital != null) {
            gestorHospital.adicionarEspecialidade(especialidade);
        }

        return true;
    }

    public Especialidade[] getEspecialidades() {
        Especialidade[] resultado = new Especialidade[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = especialidades[i];
        }
        return resultado;
    }

    public int getTotalEspecialidades() {
        return total;
    }

    public Especialidade procurarPorCodigo(String codigo) {
        if (codigo == null) return null;
        for (int i = 0; i < total; i++) {
            if (especialidades[i].getCodigo().equalsIgnoreCase(codigo)) {
                return especialidades[i];
            }
        }
        return null;
    }

    public boolean atualizarNome(String codigo, String novoNome) {
        if (novoNome == null || novoNome.trim().isEmpty()) {
            return false;
        }

        Especialidade e = procurarPorCodigo(codigo);
        if (e != null) {
            e.setNome(novoNome);

            if (gestorHospital != null) {
                gestorHospital.atualizarEspecialidade(e);
            }

            return true;
        }
        return false;
    }

    public boolean removerEspecialidade(String codigo) {
        if (gestorHospital != null) {
            Medico[] medicos = gestorHospital.getMedicos();
            for (int i = 0; i < gestorHospital.getNumMedicos(); i++) {
                if (medicos[i] != null &&
                    medicos[i].getCodigoEspecialidade().equalsIgnoreCase(codigo)) {
                    return false;
                }
            }
        }

        for (int i = 0; i < total; i++) {
            if (especialidades[i].getCodigo().equalsIgnoreCase(codigo)) {
                if (gestorHospital != null) {
                    gestorHospital.removerEspecialidade(codigo);
                }

                for (int j = i; j < total - 1; j++) {
                    especialidades[j] = especialidades[j + 1];
                }
                especialidades[--total] = null;
                return true;
            }
        }
        return false;
    }

    public boolean existemMedicosAssociados(String codigo) {
        if (gestorHospital == null) {
            return false;
        }

        Medico[] medicos = gestorHospital.getMedicos();
        for (int i = 0; i < gestorHospital.getNumMedicos(); i++) {
            if (medicos[i] != null &&
                medicos[i].getCodigoEspecialidade().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }

    private boolean existeCodigo(String codigo) {
        return procurarPorCodigo(codigo) != null;
    }

    public Especialidade[] getArrayInterno() {
        return especialidades;
    }

    public void setTotal(int novoTotal) {
        if (novoTotal >= 0 && novoTotal <= especialidades.length) {
            this.total = novoTotal;
        }
    }
}
