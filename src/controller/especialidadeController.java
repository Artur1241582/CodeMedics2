package controller;
import model.especialidadeModel;

public class especialidadeController {
    private especialidadeModel[] especialidades;
    private int total;

    public especialidadeController(int capacidade) {
        especialidades = new especialidadeModel[capacidade];
        total = 0;
    }

    // CREATE
    public boolean adicionarEspecialidade(String codigo, String nome) {
        if (total >= especialidades.length) {
            return false; // Array cheio
        }

        if (existeCodigo(codigo)) {
            return false; // Código já existe
        }

        especialidades[total++] = new especialidadeModel(codigo, nome);
        return true;
    }

    // READ - Retorna apenas especialidades preenchidas
    public especialidadeModel[] getEspecialidades() {
        especialidadeModel[] resultado = new especialidadeModel[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = especialidades[i];
        }
        return resultado;
    }

    public int getTotalEspecialidades() {
        return total;
    }

    public especialidadeModel procurarPorCodigo(String codigo) {
        for (int i = 0; i < total; i++) {
            if (especialidades[i].getCodigo().equalsIgnoreCase(codigo)) {
                return especialidades[i];
            }
        }
        return null;
    }

    // UPDATE
    public boolean atualizarNome(String codigo, String novoNome) {
        especialidadeModel e = procurarPorCodigo(codigo);
        if (e != null) {
            e.setNome(novoNome);
            return true;
        }
        return false;
    }

    // DELETE
    public boolean removerEspecialidade(String codigo) {
        for (int i = 0; i < total; i++) {
            if (especialidades[i].getCodigo().equalsIgnoreCase(codigo)) {
                for (int j = i; j < total - 1; j++) {
                    especialidades[j] = especialidades[j + 1];
                }
                especialidades[--total] = null;
                return true;
            }
        }
        return false;
    }

    // AUXILIAR
    private boolean existeCodigo(String codigo) {
        return procurarPorCodigo(codigo) != null;
    }
}