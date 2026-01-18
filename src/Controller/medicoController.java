package Controller;

import Model.Medico;
import Model.Especialidade;

/**
 * Controlador unificado para gestao de medicos
 * Usa o modelo Medico e integra-se com GestorHospital
 */
public class MedicoController {

    private Medico[] medicos;
    private int total;
    private GestorHospital gestorHospital;

    public MedicoController(int capacidade) {
        medicos = new Medico[capacidade];
        total = 0;
        this.gestorHospital = null;
    }

    public void setGestorHospital(GestorHospital gestor) {
        this.gestorHospital = gestor;
        if (gestor != null) {
            sincronizarComGestor();
        }
    }

    public void sincronizarComGestor() {
        if (gestorHospital != null) {
            Medico[] medicosGestor = gestorHospital.getMedicos();
            total = gestorHospital.getNumMedicos();

            for (int i = 0; i < total && i < medicos.length; i++) {
                medicos[i] = medicosGestor[i];
            }
        }
    }

    public boolean adicionarMedico(Medico medico) {
        if (total < medicos.length) {
            medicos[total++] = medico;

            if (gestorHospital != null) {
                gestorHospital.adicionarMedico(medico);
            }

            return true;
        }
        return false;
    }

    public boolean adicionarMedico(String nome, Especialidade especialidade,
                                    int horaEntrada, int horaSaida, double valorHora) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        if (especialidade == null) {
            return false;
        }

        if (!validarHora(horaEntrada) || !validarHora(horaSaida)) {
            return false;
        }

        if (valorHora <= 0) {
            return false;
        }

        if (buscarMedicoPorNome(nome) != null) {
            return false;
        }

        Medico medico = new Medico(nome, especialidade.getCodigo(), horaEntrada, horaSaida, valorHora);
        return adicionarMedico(medico);
    }

    public boolean adicionarMedico(String nome, String codigoEspecialidade,
                                    int horaEntrada, int horaSaida, double valorHora) {
        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        if (codigoEspecialidade == null || codigoEspecialidade.trim().isEmpty()) {
            return false;
        }

        if (!validarHora(horaEntrada) || !validarHora(horaSaida)) {
            return false;
        }

        if (valorHora <= 0) {
            return false;
        }

        if (buscarMedicoPorNome(nome) != null) {
            return false;
        }

        Medico medico = new Medico(nome, codigoEspecialidade, horaEntrada, horaSaida, valorHora);
        return adicionarMedico(medico);
    }

    public Medico[] listarMedicos() {
        Medico[] resultado = new Medico[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = medicos[i];
        }
        return resultado;
    }

    public Medico buscarMedicoPorNome(String nome) {
        if (nome == null) return null;
        for (int i = 0; i < total; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                return medicos[i];
            }
        }
        return null;
    }

    public int getTotalMedicos() {
        return total;
    }

    public boolean atualizarHoraSaida(String nome, int novaHora) {
        if (!validarHora(novaHora)) {
            return false;
        }

        for (int i = 0; i < total; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                medicos[i].setHoraSaida(novaHora);

                if (gestorHospital != null) {
                    gestorHospital.atualizarMedico(medicos[i]);
                }

                return true;
            }
        }
        return false;
    }

    public boolean atualizarMedico(String nome, Medico medicoAtualizado) {
        if (medicoAtualizado == null) {
            return false;
        }

        if (!validarHora(medicoAtualizado.getHoraEntrada()) ||
            !validarHora(medicoAtualizado.getHoraSaida())) {
            return false;
        }

        if (medicoAtualizado.getValorHora() <= 0) {
            return false;
        }

        for (int i = 0; i < total; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                medicos[i] = medicoAtualizado;

                if (gestorHospital != null) {
                    gestorHospital.atualizarMedico(medicoAtualizado);
                }

                return true;
            }
        }
        return false;
    }

    public boolean removerMedico(String nome) {
        for (int i = 0; i < total; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                if (medicos[i].isEmConsulta()) {
                    return false;
                }

                if (gestorHospital != null) {
                    gestorHospital.removerMedico(nome);
                }

                for (int j = i; j < total - 1; j++) {
                    medicos[j] = medicos[j + 1];
                }
                medicos[--total] = null;
                return true;
            }
        }
        return false;
    }

    public boolean medicoEmConsulta(String nome) {
        Medico m = buscarMedicoPorNome(nome);
        return m != null && m.isEmConsulta();
    }

    private boolean validarHora(int hora) {
        return hora >= 0 && hora <= 23;
    }

    public Medico[] getArrayInterno() {
        return medicos;
    }

    public void setTotal(int novoTotal) {
        if (novoTotal >= 0 && novoTotal <= medicos.length) {
            this.total = novoTotal;
        }
    }
}
