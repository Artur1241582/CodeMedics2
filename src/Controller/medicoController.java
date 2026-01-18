package Controller;

import Model.medicoModel;

public class medicoController {

    private medicoModel[] medicos;
    private int total;

    public medicoController(int capacidade) {
        medicos = new medicoModel[capacidade];
        total = 0;
    }

    // CREATE
    public boolean adicionarMedico(medicoModel medico) {
        if (total < medicos.length) {
            medicos[total++] = medico;
            return true;
        }
        return false; // Array cheio
    }

    // READ - Retorna apenas os médicos preenchidos
    public medicoModel[] listarMedicos() {
        medicoModel[] resultado = new medicoModel[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = medicos[i];
        }
        return resultado;
    }

    // READ - Buscar médico por nome
    public medicoModel buscarMedicoPorNome(String nome) {
        for (int i = 0; i < total; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                return medicos[i];
            }
        }
        return null; // Não encontrado
    }

    // READ - Retorna total de médicos
    public int getTotalMedicos() {
        return total;
    }

    // UPDATE - Atualizar hora de saída
    public boolean atualizarHoraSaida(String nome, int novaHora) {
        for (int i = 0; i < total; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                medicos[i].setHoraSaida(novaHora);
                return true;
            }
        }
        return false; // Médico não encontrado
    }

    // UPDATE - Atualizar médico completo
    public boolean atualizarMedico(String nome, medicoModel medicoAtualizado) {
        for (int i = 0; i < total; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                medicos[i] = medicoAtualizado;
                return true;
            }
        }
        return false;
    }

    // DELETE
    public boolean removerMedico(String nome) {
        for (int i = 0; i < total; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                // Shift dos elementos para a esquerda
                for (int j = i; j < total - 1; j++) {
                    medicos[j] = medicos[j + 1];
                }
                medicos[--total] = null;
                return true;
            }
        }
        return false; // Médico não encontrado
    }
}