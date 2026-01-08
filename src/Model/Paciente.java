package Model;

/**
 * Classe que representa um paciente no sistema hospitalar
 * Responsabilidade: Aluno 2/3
 */
public class Paciente {

    private static int contadorId = 1;

    private int id;
    private String nome;
    private String[] sintomas;
    private int numSintomas;
    private static final int MAX_SINTOMAS = 10;

    private int nivelUrgencia; // 1=Baixa, 2=Media, 3=Urgente
    private String especialidadeSugerida;
    private int tempoEspera; // unidades de tempo a espera
    private int horaChegada;
    private int diaChegada;

    private boolean emConsulta;
    private String medicoAtribuido;

    /**
     * Construtor do paciente
     * @param nome Nome do paciente
     * @param horaChegada Unidade de tempo em que chegou
     * @param diaChegada Dia em que chegou
     */
    public Paciente(String nome, int horaChegada, int diaChegada) {
        this.id = contadorId++;
        this.nome = nome;
        this.sintomas = new String[MAX_SINTOMAS];
        this.numSintomas = 0;
        this.nivelUrgencia = 1; // Por defeito, baixa urgencia
        this.especialidadeSugerida = null;
        this.tempoEspera = 0;
        this.horaChegada = horaChegada;
        this.diaChegada = diaChegada;
        this.emConsulta = false;
        this.medicoAtribuido = null;
    }

    // ==================== GESTAO DE SINTOMAS ====================

    /**
     * Adiciona um sintoma ao paciente
     * @param sintoma Nome do sintoma
     * @return true se adicionou, false se array cheio
     */
    public boolean adicionarSintoma(String sintoma) {
        if (numSintomas >= MAX_SINTOMAS) {
            return false;
        }
        sintomas[numSintomas] = sintoma;
        numSintomas++;
        return true;
    }

    /**
     * Remove um sintoma pelo indice
     * @param indice Indice do sintoma a remover
     * @return true se removeu, false se indice invalido
     */
    public boolean removerSintoma(int indice) {
        if (indice < 0 || indice >= numSintomas) {
            return false;
        }
        for (int i = indice; i < numSintomas - 1; i++) {
            sintomas[i] = sintomas[i + 1];
        }
        sintomas[numSintomas - 1] = null;
        numSintomas--;
        return true;
    }

    /**
     * Obtem os sintomas do paciente
     * @return Array com os sintomas
     */
    public String[] getSintomas() {
        String[] resultado = new String[numSintomas];
        for (int i = 0; i < numSintomas; i++) {
            resultado[i] = sintomas[i];
        }
        return resultado;
    }

    // ==================== GESTAO DE URGENCIA ====================

    /**
     * Eleva o nivel de urgencia do paciente
     * @return true se elevou, false se ja esta no maximo
     */
    public boolean elevarUrgencia() {
        if (nivelUrgencia >= 3) {
            return false;
        }
        nivelUrgencia++;
        tempoEspera = 0; // Reset do tempo de espera apos elevacao
        return true;
    }

    /**
     * Incrementa o tempo de espera
     */
    public void incrementarTempoEspera() {
        tempoEspera++;
    }

    /**
     * Converte o nivel de urgencia para texto
     * @return Descricao do nivel de urgencia
     */
    public String getNivelUrgenciaTexto() {
        switch (nivelUrgencia) {
            case 1: return "Verde (Baixa)";
            case 2: return "Laranja (Media)";
            case 3: return "Vermelha (Urgente)";
            default: return "Desconhecido";
        }
    }

    // ==================== GETTERS E SETTERS ====================

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNumSintomas() {
        return numSintomas;
    }

    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(int nivelUrgencia) {
        if (nivelUrgencia >= 1 && nivelUrgencia <= 3) {
            this.nivelUrgencia = nivelUrgencia;
        }
    }

    public String getEspecialidadeSugerida() {
        return especialidadeSugerida;
    }

    public void setEspecialidadeSugerida(String especialidadeSugerida) {
        this.especialidadeSugerida = especialidadeSugerida;
    }

    public int getTempoEspera() {
        return tempoEspera;
    }

    public void setTempoEspera(int tempoEspera) {
        this.tempoEspera = tempoEspera;
    }

    public int getHoraChegada() {
        return horaChegada;
    }

    public int getDiaChegada() {
        return diaChegada;
    }

    public boolean isEmConsulta() {
        return emConsulta;
    }

    public void setEmConsulta(boolean emConsulta) {
        this.emConsulta = emConsulta;
    }

    public String getMedicoAtribuido() {
        return medicoAtribuido;
    }

    public void setMedicoAtribuido(String medicoAtribuido) {
        this.medicoAtribuido = medicoAtribuido;
    }

    /**
     * Reseta o contador de IDs (util para testes)
     */
    public static void resetContadorId() {
        contadorId = 1;
    }

    // ==================== REPRESENTACAO ====================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Paciente #").append(id).append(": ").append(nome);
        sb.append(" | Urgencia: ").append(getNivelUrgenciaTexto());
        if (especialidadeSugerida != null) {
            sb.append(" | Especialidade: ").append(especialidadeSugerida);
        }
        sb.append(" | Espera: ").append(tempoEspera).append(" un.");
        if (emConsulta && medicoAtribuido != null) {
            sb.append(" | Em consulta com: ").append(medicoAtribuido);
        }
        return sb.toString();
    }

    /**
     * Representacao detalhada do paciente
     * @return String com todos os detalhes
     */
    public String toStringDetalhado() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PACIENTE #").append(id).append(" ===\n");
        sb.append("Nome: ").append(nome).append("\n");
        sb.append("Urgencia: ").append(getNivelUrgenciaTexto()).append("\n");
        sb.append("Chegada: Dia ").append(diaChegada).append(", Unidade ").append(horaChegada).append("\n");
        sb.append("Tempo de espera: ").append(tempoEspera).append(" unidades\n");

        sb.append("Sintomas (").append(numSintomas).append("):\n");
        for (int i = 0; i < numSintomas; i++) {
            sb.append("  - ").append(sintomas[i]).append("\n");
        }

        if (especialidadeSugerida != null) {
            sb.append("Especialidade sugerida: ").append(especialidadeSugerida).append("\n");
        }

        if (emConsulta) {
            sb.append("Estado: Em consulta com ").append(medicoAtribuido).append("\n");
        } else {
            sb.append("Estado: Em espera\n");
        }

        return sb.toString();
    }
}
