package Model;

/**
 * Classe para armazenar e gerir as configurações do sistema
 * Responsabilidade: Aluno 4
 * Package: Model (representa dados de configuração)
 */
public class Configuracoes {
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

    /**
     * Construtor - inicializa com valores padrão
     */
    public Configuracoes() {
        restaurarPadrao();
    }

    // ==================== GETTERS ====================

    public String getCaminhoFicheiros() {
        return caminhoFicheiros;
    }

    public String getSeparador() {
        return separador;
    }

    public int getTempoConsultaBaixa() {
        return tempoConsultaBaixa;
    }

    public int getTempoConsultaMedia() {
        return tempoConsultaMedia;
    }

    public int getTempoConsultaUrgente() {
        return tempoConsultaUrgente;
    }

    public int getHorasTrabalho() {
        return horasTrabalho;
    }

    public int getTempoDescanso() {
        return tempoDescanso;
    }

    public int getElevacaoBaixaMedia() {
        return elevacaoBaixaMedia;
    }

    public int getElevacaoMediaUrgente() {
        return elevacaoMediaUrgente;
    }

    public int getElevacaoUrgenteSaida() {
        return elevacaoUrgenteSaida;
    }

    public int getUnidadeTempoAtual() {
        return unidadeTempoAtual;
    }

    public int getDiaAtual() {
        return diaAtual;
    }

    // ==================== SETTERS ====================

    public void setCaminhoFicheiros(String c) {
        caminhoFicheiros = c;
    }

    public void setSeparador(String s) {
        separador = s;
    }

    public void setTempoConsultaBaixa(int t) {
        if (t > 0) {
            tempoConsultaBaixa = t;
        }
    }

    public void setTempoConsultaMedia(int t) {
        if (t > 0) {
            tempoConsultaMedia = t;
        }
    }

    public void setTempoConsultaUrgente(int t) {
        if (t > 0) {
            tempoConsultaUrgente = t;
        }
    }

    public void setHorasTrabalho(int h) {
        if (h > 0) {
            horasTrabalho = h;
        }
    }

    public void setTempoDescanso(int t) {
        if (t > 0) {
            tempoDescanso = t;
        }
    }

    public void setElevacaoBaixaMedia(int e) {
        if (e > 0) {
            elevacaoBaixaMedia = e;
        }
    }

    public void setElevacaoMediaUrgente(int e) {
        if (e > 0) {
            elevacaoMediaUrgente = e;
        }
    }

    public void setElevacaoUrgenteSaida(int e) {
        if (e > 0) {
            elevacaoUrgenteSaida = e;
        }
    }

    public void setPassword(String p) {
        if (p != null && !p.trim().isEmpty()) {
            password = p;
        }
    }

    // ==================== MÉTODOS DE TEMPO ====================

    /**
     * Avança uma unidade de tempo
     * Quando atinge 24, passa para o próximo dia
     */
    public void avancarTempo() {
        unidadeTempoAtual++;
        if (unidadeTempoAtual > 24) {
            unidadeTempoAtual = 1;
            diaAtual++;
        }
    }

    /**
     * Define manualmente o dia atual
     */
    public void setDiaAtual(int dia) {
        if (dia > 0) {
            diaAtual = dia;
        }
    }

    /**
     * Define manualmente a unidade de tempo atual
     */
    public void setUnidadeTempoAtual(int unidade) {
        if (unidade >= 1 && unidade <= 24) {
            unidadeTempoAtual = unidade;
        }
    }

    // ==================== MÉTODOS DE PASSWORD ====================

    /**
     * Verifica se a password está correta
     * @param p Password a verificar
     * @return true se estiver correta, false caso contrário
     */
    public boolean verificarPassword(String p) {
        return password.equals(p);
    }

    // ==================== MÉTODOS DE CONFIGURAÇÃO ====================

    /**
     * Restaura todas as configurações para os valores padrão
     */
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
        password = "admin123";
        unidadeTempoAtual = 1;
        diaAtual = 1;
    }

    /**
     * Obtém o tempo de consulta baseado no nível de urgência
     * @param nivelUrgencia 1=Baixa, 2=Média, 3=Urgente
     * @return Tempo em unidades
     */
    public int getTempoConsultaPorNivel(int nivelUrgencia) {
        switch (nivelUrgencia) {
            case 1: return tempoConsultaBaixa;
            case 2: return tempoConsultaMedia;
            case 3: return tempoConsultaUrgente;
            default: return tempoConsultaBaixa;
        }
    }

    /**
     * Obtém o tempo de elevação baseado no nível atual
     * @param nivelAtual 1=Baixa, 2=Média, 3=Urgente
     * @return Tempo em unidades até próximo nível
     */
    public int getTempoElevacaoPorNivel(int nivelAtual) {
        switch (nivelAtual) {
            case 1: return elevacaoBaixaMedia;
            case 2: return elevacaoMediaUrgente;
            case 3: return elevacaoUrgenteSaida;
            default: return 0;
        }
    }

    /**
     * Retorna uma string com todas as configurações atuais
     * @return String formatada com as configurações
     */
    @Override
    public String toString() {
        return "Configurações do Sistema:\n" +
                "Caminho Ficheiros: " + caminhoFicheiros + "\n" +
                "Separador: '" + separador + "'\n" +
                "Tempo Consulta Baixa: " + tempoConsultaBaixa + " un.\n" +
                "Tempo Consulta Média: " + tempoConsultaMedia + " un.\n" +
                "Tempo Consulta Urgente: " + tempoConsultaUrgente + " un.\n" +
                "Horas Trabalho: " + horasTrabalho + " h\n" +
                "Tempo Descanso: " + tempoDescanso + " un.\n" +
                "Elevação Baixa->Média: " + elevacaoBaixaMedia + " un.\n" +
                "Elevação Média->Urgente: " + elevacaoMediaUrgente + " un.\n" +
                "Elevação Urgente->Saída: " + elevacaoUrgenteSaida + " un.";
    }
}