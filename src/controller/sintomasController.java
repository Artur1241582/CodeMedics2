package controller;

import model.sintomasModel;
import model.especialidadeModel;

public class sintomasController {
    private sintomasModel[] sintomas;
    private int total;

    public sintomasController(int capacidade) {
        sintomas = new sintomasModel[capacidade];
        total = 0;
    }

    // CREATE
    public boolean adicionarSintoma(String nome, String nivelUrgencia, especialidadeModel especialidade) {
        if (total >= sintomas.length) {
            return false; // Array cheio
        }

        if (existeSintoma(nome)) {
            return false; // Sintoma já existe
        }

        sintomas[total++] = new sintomasModel(nome, nivelUrgencia, especialidade);
        return true;
    }

    // READ - Retorna apenas sintomas preenchidos
    public sintomasModel[] getSintomas() {
        sintomasModel[] resultado = new sintomasModel[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = sintomas[i];
        }
        return resultado;
    }

    public int getTotalSintomas() {
        return total;
    }

    public sintomasModel procurarPorNome(String nome) {
        for (int i = 0; i < total; i++) {
            if (sintomas[i].getNome().equalsIgnoreCase(nome)) {
                return sintomas[i];
            }
        }
        return null;
    }

    // UPDATE
    public boolean atualizarNivelUrgencia(String nome, String novoNivel) {
        sintomasModel s = procurarPorNome(nome);
        if (s != null) {
            s.setNivelUrgencia(novoNivel);
            return true;
        }
        return false;
    }

    public boolean atualizarEspecialidade(String nome, especialidadeModel novaEspecialidade) {
        sintomasModel s = procurarPorNome(nome);
        if (s != null) {
            s.setEspecialidade(novaEspecialidade);
            return true;
        }
        return false;
    }

    // DELETE
    public boolean removerSintoma(String nome) {
        for (int i = 0; i < total; i++) {
            if (sintomas[i].getNome().equalsIgnoreCase(nome)) {
                for (int j = i; j < total - 1; j++) {
                    sintomas[j] = sintomas[j + 1];
                }
                sintomas[--total] = null;
                return true;
            }
        }
        return false;
    }

    // AUXILIAR
    private boolean existeSintoma(String nome) {
        return procurarPorNome(nome) != null;
    }
}