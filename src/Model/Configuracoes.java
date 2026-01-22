package Model;

import java.io.*;


public class Configuracoes {
    // ... (outros atributos permanecem iguais)
    private String caminhoFicheiros;
    private String separador;
    private int tempoConsultaBaixa;
    private int tempoConsultaMedia;
    private int tempoConsultaUrgente;
    private int horasTrabalho;
    private int tempoDescanso;
    private int elevacaoBaixaMedia;
    private int elevacaoMediaUrgente;
    private int elevacaoUrgenteSaida;
    private String password;
    private int unidadeTempoAtual;
    private int diaAtual;

    private final String CAMINHO_PASS = "C:/Users/Lenovo/OneDrive/Documentos/GitHub/CodeMedics2/Ficheiros/password.txt";

    public Configuracoes() {
        restaurarPadrao();
        carregarPasswordDeFicheiro();
    }

    private String lerPasswordAtualizada() {
        File file = new File(CAMINHO_PASS);
        if (!file.exists()) {
            return "admin123";
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String linha = reader.readLine();
            if (linha != null && !linha.trim().isEmpty()) {
                return linha.trim();
            }
        } catch (IOException e) {
            System.err.println("Erro ao aceder ao ficheiro de segurança.");
        }
        return "admin123";
    }

    private void carregarPasswordDeFicheiro() {
        this.password = lerPasswordAtualizada();
    }

    private void guardarPasswordEmFicheiro() {
        try {
            File file = new File(CAMINHO_PASS);
            if (file.getParentFile() != null) file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(this.password);
            }
        } catch (IOException e) {
            System.err.println("Erro ao gravar password.");
        }
    }

    public boolean verificarPassword(String tentativa) {
        // Atualiza a variável local com o que está no ficheiro agora mesmo
        this.password = lerPasswordAtualizada();
        return this.password.equals(tentativa);
    }

    public void setPassword(String p) {
        if (p != null && !p.trim().isEmpty()) {
            this.password = p;
            guardarPasswordEmFicheiro();
        }
    }


    public void restaurarPadrao() {
        caminhoFicheiros = "Ficheiros";
        separador = ";";
        tempoConsultaBaixa = 1;
        tempoConsultaMedia = 2;
        tempoConsultaUrgente = 3;
        horasTrabalho = 5;
        tempoDescanso = 1;
        elevacaoBaixaMedia = 3;
        elevacaoMediaUrgente = 3;
        elevacaoUrgenteSaida = 2;
        password = "admin123"; // Valor base caso o ficheiro não exista
        unidadeTempoAtual = 1;
        diaAtual = 1;
    }


    public String getCaminhoFicheiros() { return caminhoFicheiros; }
    public String getSeparador() { return separador; }
    public int getTempoConsultaBaixa() { return tempoConsultaBaixa; }
    public int getTempoConsultaMedia() { return tempoConsultaMedia; }
    public int getTempoConsultaUrgente() { return tempoConsultaUrgente; }
    public int getHorasTrabalho() { return horasTrabalho; }
    public int getTempoDescanso() { return tempoDescanso; }
    public int getElevacaoBaixaMedia() { return elevacaoBaixaMedia; }
    public int getElevacaoMediaUrgente() { return elevacaoMediaUrgente; }
    public int getElevacaoUrgenteSaida() { return elevacaoUrgenteSaida; }
    public int getUnidadeTempoAtual() { return unidadeTempoAtual; }
    public int getDiaAtual() { return diaAtual; }

    public void setCaminhoFicheiros(String c) { caminhoFicheiros = c; }
    public void setSeparador(String s) { separador = s; }
    public void setTempoConsultaBaixa(int t) { if (t > 0) tempoConsultaBaixa = t; }
    public void setTempoConsultaMedia(int t) { if (t > 0) tempoConsultaMedia = t; }
    public void setTempoConsultaUrgente(int t) { if (t > 0) tempoConsultaUrgente = t; }
    public void setHorasTrabalho(int h) { if (h > 0) horasTrabalho = h; }
    public void setTempoDescanso(int t) { if (t > 0) tempoDescanso = t; }
    public void setElevacaoBaixaMedia(int e) { if (e > 0) elevacaoBaixaMedia = e; }
    public void setElevacaoMediaUrgente(int e) { if (e > 0) elevacaoMediaUrgente = e; }
    public void setElevacaoUrgenteSaida(int e) { if (e > 0) elevacaoUrgenteSaida = e; }

    public void avancarTempo() {
        unidadeTempoAtual++;
        if (unidadeTempoAtual > 24) {
            unidadeTempoAtual = 1;
            diaAtual++;
        }
    }

    public void setDiaAtual(int dia) { if (dia > 0) diaAtual = dia; }
    public void setUnidadeTempoAtual(int unidade) { if (unidade >= 1 && unidade <= 24) unidadeTempoAtual = unidade; }

    public int getTempoConsultaPorNivel(int nivelUrgencia) {
        switch (nivelUrgencia) {
            case 1: return tempoConsultaBaixa;
            case 2: return tempoConsultaMedia;
            case 3: return tempoConsultaUrgente;
            default: return tempoConsultaBaixa;
        }
    }

    public int getTempoElevacaoPorNivel(int nivelAtual) {
        switch (nivelAtual) {
            case 1: return elevacaoBaixaMedia;
            case 2: return elevacaoMediaUrgente;
            case 3: return elevacaoUrgenteSaida;
            default: return 0;
        }
    }

    @Override
    public String toString() {
        return "Configurações do Sistema (Segurança via Ficheiro Externo)";
    }
}