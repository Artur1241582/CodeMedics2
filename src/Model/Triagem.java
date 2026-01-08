package Model;

/**
 * Classe que representa o resultado de uma triagem
 * Contem o nivel de urgencia calculado e a especialidade sugerida
 * Responsabilidade: Aluno 2/3
 */
public class Triagem {

    private int nivelUrgencia; // 1=Baixa, 2=Media, 3=Urgente
    private String nivelUrgenciaTexto;
    private String especialidadePrincipal;
    private String[] especialidadesPossiveis;
    private int numEspecialidades;
    private static final int MAX_ESPECIALIDADES = 10;

    private boolean encaminhamentoAutomatico;
    private String sugestaoTendencia;

    // Contadores para estatisticas
    private int sintomasVermelhos;
    private int sintomasLaranja;
    private int sintomasVerdes;

    /**
     * Construtor padrao
     */
    public Triagem() {
        this.nivelUrgencia = 1;
        this.nivelUrgenciaTexto = "Verde (Baixa)";
        this.especialidadePrincipal = null;
        this.especialidadesPossiveis = new String[MAX_ESPECIALIDADES];
        this.numEspecialidades = 0;
        this.encaminhamentoAutomatico = false;
        this.sugestaoTendencia = "";
        this.sintomasVermelhos = 0;
        this.sintomasLaranja = 0;
        this.sintomasVerdes = 0;
    }

    // ==================== SETTERS PARA CONSTRUCAO ====================

    /**
     * Define o nivel de urgencia
     * @param nivel Nivel (1, 2 ou 3)
     */
    public void setNivelUrgencia(int nivel) {
        this.nivelUrgencia = nivel;
        switch (nivel) {
            case 1:
                this.nivelUrgenciaTexto = "Verde (Baixa)";
                break;
            case 2:
                this.nivelUrgenciaTexto = "Laranja (Media)";
                break;
            case 3:
                this.nivelUrgenciaTexto = "Vermelha (Urgente)";
                break;
            default:
                this.nivelUrgenciaTexto = "Desconhecido";
        }
    }

    /**
     * Define a especialidade principal
     * @param especialidade Codigo da especialidade
     */
    public void setEspecialidadePrincipal(String especialidade) {
        this.especialidadePrincipal = especialidade;
    }

    /**
     * Adiciona uma especialidade possivel
     * @param especialidade Codigo da especialidade
     * @return true se adicionou, false se array cheio
     */
    public boolean adicionarEspecialidadePossivel(String especialidade) {
        // Verifica se ja existe
        for (int i = 0; i < numEspecialidades; i++) {
            if (especialidadesPossiveis[i].equals(especialidade)) {
                return true; // Ja existe, nao adiciona duplicado
            }
        }

        if (numEspecialidades >= MAX_ESPECIALIDADES) {
            return false;
        }

        especialidadesPossiveis[numEspecialidades] = especialidade;
        numEspecialidades++;
        return true;
    }

    /**
     * Define se o encaminhamento automatico e possivel
     * @param automatico true se possivel
     */
    public void setEncaminhamentoAutomatico(boolean automatico) {
        this.encaminhamentoAutomatico = automatico;
    }

    /**
     * Define a sugestao de tendencia
     * @param sugestao Texto da sugestao
     */
    public void setSugestaoTendencia(String sugestao) {
        this.sugestaoTendencia = sugestao;
    }

    /**
     * Define os contadores de sintomas
     */
    public void setContadores(int vermelhos, int laranja, int verdes) {
        this.sintomasVermelhos = vermelhos;
        this.sintomasLaranja = laranja;
        this.sintomasVerdes = verdes;
    }

    // ==================== GETTERS ====================

    public int getNivelUrgencia() {
        return nivelUrgencia;
    }

    public String getNivelUrgenciaTexto() {
        return nivelUrgenciaTexto;
    }

    public String getEspecialidadePrincipal() {
        return especialidadePrincipal;
    }

    public String[] getEspecialidadesPossiveis() {
        String[] resultado = new String[numEspecialidades];
        for (int i = 0; i < numEspecialidades; i++) {
            resultado[i] = especialidadesPossiveis[i];
        }
        return resultado;
    }

    public int getNumEspecialidades() {
        return numEspecialidades;
    }

    public boolean isEncaminhamentoAutomatico() {
        return encaminhamentoAutomatico;
    }

    public String getSugestaoTendencia() {
        return sugestaoTendencia;
    }

    public int getSintomasVermelhos() {
        return sintomasVermelhos;
    }

    public int getSintomasLaranja() {
        return sintomasLaranja;
    }

    public int getSintomasVerdes() {
        return sintomasVerdes;
    }

    public int getTotalSintomas() {
        return sintomasVermelhos + sintomasLaranja + sintomasVerdes;
    }

    // ==================== DISPLAY ====================

    /**
     * Mostra o resultado da triagem na consola
     */
    public void mostrarResultado() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  RESULTADO DA TRIAGEM                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        // Nivel de urgencia com cor simulada
        String corUrgencia = "";
        switch (nivelUrgencia) {
            case 1: corUrgencia = "[VERDE]"; break;
            case 2: corUrgencia = "[LARANJA]"; break;
            case 3: corUrgencia = "[VERMELHA]"; break;
        }

        System.out.println("║ Nivel de Urgencia: " + corUrgencia + " " + nivelUrgenciaTexto);
        System.out.println("║");
        System.out.println("║ Sintomas analisados:");
        System.out.println("║   - Urgentes (Vermelha): " + sintomasVermelhos);
        System.out.println("║   - Medios (Laranja):    " + sintomasLaranja);
        System.out.println("║   - Baixos (Verde):      " + sintomasVerdes);
        System.out.println("║");

        if (especialidadePrincipal != null) {
            System.out.println("║ Especialidade Sugerida: " + especialidadePrincipal);
        } else {
            System.out.println("║ Especialidade: Nao determinada");
        }

        if (numEspecialidades > 1) {
            System.out.print("║ Especialidades possiveis: ");
            for (int i = 0; i < numEspecialidades; i++) {
                System.out.print(especialidadesPossiveis[i]);
                if (i < numEspecialidades - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }

        System.out.println("║");

        if (encaminhamentoAutomatico) {
            System.out.println("║ Encaminhamento: AUTOMATICO POSSIVEL");
        } else {
            System.out.println("║ Encaminhamento: MANUAL RECOMENDADO");
            if (!sugestaoTendencia.isEmpty()) {
                System.out.println("║ " + sugestaoTendencia);
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Triagem: ").append(nivelUrgenciaTexto);
        if (especialidadePrincipal != null) {
            sb.append(" -> ").append(especialidadePrincipal);
        }
        sb.append(" [").append(encaminhamentoAutomatico ? "Auto" : "Manual").append("]");
        return sb.toString();
    }
}
