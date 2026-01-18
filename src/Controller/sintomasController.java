package Controller;

import Model.Sintoma;
import Model.Especialidade;

/**
 * Controlador unificado para gestao de sintomas
 * Usa o modelo Sintoma e integra-se com GestorHospital
 */
public class SintomasController {
    private Sintoma[] sintomas;
    private int total;
    private GestorHospital gestorHospital;

    public SintomasController(int capacidade) {
        sintomas = new Sintoma[capacidade];
        total = 0;
        this.gestorHospital = null;
    }

    public void setGestorHospital(GestorHospital gestor) {
        this.gestorHospital = gestor;
    }

    public void sincronizarComGestor() {
        if (gestorHospital != null) {
            String[] dados = gestorHospital.getDadosSintomas();
            int num = gestorHospital.getNumSintomas();
            String separador = gestorHospital.getSeparador();

            total = 0;
            for (int i = 0; i < num && total < sintomas.length; i++) {
                if (dados[i] != null && !dados[i].isEmpty()) {
                    sintomas[total] = new Sintoma(dados[i], separador);
                    total++;
                }
            }
        }
    }

    public boolean adicionarSintoma(String nome, String nivelUrgencia, Especialidade especialidade) {
        if (total >= sintomas.length) {
            return false;
        }

        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        if (!Sintoma.isNivelValido(nivelUrgencia)) {
            return false;
        }

        if (existeSintoma(nome)) {
            return false;
        }

        String codigoEsp = (especialidade != null) ? especialidade.getCodigo() : null;
        sintomas[total++] = new Sintoma(nome, nivelUrgencia.toLowerCase(), codigoEsp);

        if (gestorHospital != null) {
            gestorHospital.adicionarSintoma(sintomas[total - 1]);
        }

        return true;
    }

    public boolean adicionarSintoma(String nome, String nivelUrgencia, String codigoEspecialidade) {
        if (total >= sintomas.length) {
            return false;
        }

        if (nome == null || nome.trim().isEmpty()) {
            return false;
        }

        if (!Sintoma.isNivelValido(nivelUrgencia)) {
            return false;
        }

        if (existeSintoma(nome)) {
            return false;
        }

        sintomas[total++] = new Sintoma(nome, nivelUrgencia.toLowerCase(), codigoEspecialidade);

        if (gestorHospital != null) {
            gestorHospital.adicionarSintoma(sintomas[total - 1]);
        }

        return true;
    }

    public boolean adicionarSintoma(Sintoma sintoma) {
        if (total >= sintomas.length) {
            return false;
        }

        if (sintoma == null || existeSintoma(sintoma.getNome())) {
            return false;
        }

        sintomas[total++] = sintoma;

        if (gestorHospital != null) {
            gestorHospital.adicionarSintoma(sintoma);
        }

        return true;
    }

    public Sintoma[] getSintomas() {
        Sintoma[] resultado = new Sintoma[total];
        for (int i = 0; i < total; i++) {
            resultado[i] = sintomas[i];
        }
        return resultado;
    }

    public int getTotalSintomas() {
        return total;
    }

    public Sintoma procurarPorNome(String nome) {
        if (nome == null) return null;
        for (int i = 0; i < total; i++) {
            if (sintomas[i].getNome().equalsIgnoreCase(nome)) {
                return sintomas[i];
            }
        }
        return null;
    }

    public boolean atualizarNivelUrgencia(String nome, String novoNivel) {
        if (!Sintoma.isNivelValido(novoNivel)) {
            return false;
        }

        Sintoma s = procurarPorNome(nome);
        if (s != null) {
            s.setNivelUrgencia(novoNivel.toLowerCase());

            if (gestorHospital != null) {
                gestorHospital.atualizarSintoma(s);
            }

            return true;
        }
        return false;
    }

    public boolean atualizarEspecialidade(String nome, Especialidade novaEspecialidade) {
        Sintoma s = procurarPorNome(nome);
        if (s != null) {
            String codigoEsp = (novaEspecialidade != null) ? novaEspecialidade.getCodigo() : null;
            s.setCodigoEspecialidade(codigoEsp);

            if (gestorHospital != null) {
                gestorHospital.atualizarSintoma(s);
            }

            return true;
        }
        return false;
    }

    public boolean atualizarEspecialidade(String nome, String codigoEspecialidade) {
        Sintoma s = procurarPorNome(nome);
        if (s != null) {
            s.setCodigoEspecialidade(codigoEspecialidade);

            if (gestorHospital != null) {
                gestorHospital.atualizarSintoma(s);
            }

            return true;
        }
        return false;
    }

    public boolean removerSintoma(String nome) {
        for (int i = 0; i < total; i++) {
            if (sintomas[i].getNome().equalsIgnoreCase(nome)) {
                if (gestorHospital != null) {
                    gestorHospital.removerSintoma(nome);
                }

                for (int j = i; j < total - 1; j++) {
                    sintomas[j] = sintomas[j + 1];
                }
                sintomas[--total] = null;
                return true;
            }
        }
        return false;
    }

    private boolean existeSintoma(String nome) {
        return procurarPorNome(nome) != null;
    }

    public Sintoma[] getArrayInterno() {
        return sintomas;
    }

    public void setTotal(int novoTotal) {
        if (novoTotal >= 0 && novoTotal <= sintomas.length) {
            this.total = novoTotal;
        }
    }
}
