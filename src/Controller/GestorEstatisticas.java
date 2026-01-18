package Controller;

import Model.*;

/**
 * Controlador responsavel pelo calculo de estatisticas do hospital
 * Responsabilidade: Aluno 2
 */
public class GestorEstatisticas {

    private GestorHospital gestorHospital;

    // Historico de atendimentos por dia (ultimos 30 dias)
    private int[] atendimentosPorDia;
    private int[] diasRegistados;
    private int numDiasRegistados;
    private static final int MAX_DIAS_HISTORICO = 30;

    // Contagem de pacientes por sintoma
    private String[] sintomasContados;
    private int[] contagemPorSintoma;
    private int numSintomasContados;
    private static final int MAX_SINTOMAS = 200;

    // Contagem de pacientes por especialidade
    private String[] especialidadesContadas;
    private int[] contagemPorEspecialidade;
    private int numEspecialidadesContadas;
    private static final int MAX_ESPECIALIDADES = 20;

    /**
     * Construtor
     * @param gestorHospital Referencia ao gestor do hospital
     */
    public GestorEstatisticas(GestorHospital gestorHospital) {
        this.gestorHospital = gestorHospital;

        this.atendimentosPorDia = new int[MAX_DIAS_HISTORICO];
        this.diasRegistados = new int[MAX_DIAS_HISTORICO];
        this.numDiasRegistados = 0;

        this.sintomasContados = new String[MAX_SINTOMAS];
        this.contagemPorSintoma = new int[MAX_SINTOMAS];
        this.numSintomasContados = 0;

        this.especialidadesContadas = new String[MAX_ESPECIALIDADES];
        this.contagemPorEspecialidade = new int[MAX_ESPECIALIDADES];
        this.numEspecialidadesContadas = 0;
    }

    // ==================== ESTATISTICA 1: MEDIA UTENTES POR DIA ====================

    /**
     * Regista os atendimentos de um dia
     * @param dia Numero do dia
     * @param numAtendimentos Numero de pacientes atendidos nesse dia
     */
    public void registarAtendimentosDia(int dia, int numAtendimentos) {
        // Verifica se o dia ja existe
        for (int i = 0; i < numDiasRegistados; i++) {
            if (diasRegistados[i] == dia) {
                atendimentosPorDia[i] = numAtendimentos;
                return;
            }
        }

        // Adiciona novo dia
        if (numDiasRegistados < MAX_DIAS_HISTORICO) {
            diasRegistados[numDiasRegistados] = dia;
            atendimentosPorDia[numDiasRegistados] = numAtendimentos;
            numDiasRegistados++;
        } else {
            // Remove o mais antigo (FIFO)
            for (int i = 0; i < MAX_DIAS_HISTORICO - 1; i++) {
                diasRegistados[i] = diasRegistados[i + 1];
                atendimentosPorDia[i] = atendimentosPorDia[i + 1];
            }
            diasRegistados[MAX_DIAS_HISTORICO - 1] = dia;
            atendimentosPorDia[MAX_DIAS_HISTORICO - 1] = numAtendimentos;
        }
    }

    /**
     * Calcula a media de utentes atendidos por dia
     * @return Media de utentes por dia
     */
    public double calcularMediaUtentesPorDia() {
        if (numDiasRegistados == 0) {
            // Se nao ha historico, usa os dados atuais
            int diaAtual = gestorHospital.getConfig().getDiaAtual();
            int atendidosHoje = gestorHospital.getNumPacientesAtendidos();

            if (diaAtual <= 1 && atendidosHoje == 0) {
                return 0.0;
            }

            // Se so temos o dia atual
            return atendidosHoje;
        }

        int totalAtendimentos = 0;
        for (int i = 0; i < numDiasRegistados; i++) {
            totalAtendimentos += atendimentosPorDia[i];
        }

        return (double) totalAtendimentos / numDiasRegistados;
    }

    /**
     * Obtem o historico de atendimentos por dia
     * @return Array bidimensional [dia, atendimentos]
     */
    public int[][] getHistoricoAtendimentos() {
        int[][] historico = new int[numDiasRegistados][2];
        for (int i = 0; i < numDiasRegistados; i++) {
            historico[i][0] = diasRegistados[i];
            historico[i][1] = atendimentosPorDia[i];
        }
        return historico;
    }

    /**
     * Mostra a estatistica de media de utentes por dia
     */
    public void mostrarMediaUtentesPorDia() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          MEDIA DE UTENTES ATENDIDOS POR DIA                ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        double media = calcularMediaUtentesPorDia();
        int atendidosHoje = gestorHospital.getNumPacientesAtendidos();
        int diaAtual = gestorHospital.getConfig().getDiaAtual();

        System.out.printf("║  Dia atual: %d                                              ║%n", diaAtual);
        System.out.printf("║  Pacientes atendidos hoje: %d                               ║%n", atendidosHoje);
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        if (numDiasRegistados > 0) {
            System.out.println("║  HISTORICO DE ATENDIMENTOS:                                ║");
            System.out.println("║  Dia    | Atendimentos                                     ║");
            System.out.println("║  -------|-------------------------------------------        ║");

            for (int i = 0; i < numDiasRegistados; i++) {
                System.out.printf("║  %5d  | %d                                              ║%n",
                        diasRegistados[i], atendimentosPorDia[i]);
            }
            System.out.println("╠════════════════════════════════════════════════════════════╣");
        }

        System.out.printf("║  MEDIA: %.2f utentes/dia                                   ║%n", media);
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    // ==================== ESTATISTICA 2: UTENTES POR SINTOMA ====================

    /**
     * Regista um sintoma de um paciente atendido
     * @param sintoma Nome do sintoma
     */
    public void registarSintomaPaciente(String sintoma) {
        if (sintoma == null || sintoma.trim().isEmpty()) {
            return;
        }

        String sintomaLower = sintoma.toLowerCase().trim();

        // Procura se o sintoma ja existe
        for (int i = 0; i < numSintomasContados; i++) {
            if (sintomasContados[i].equalsIgnoreCase(sintomaLower)) {
                contagemPorSintoma[i]++;
                return;
            }
        }

        // Adiciona novo sintoma
        if (numSintomasContados < MAX_SINTOMAS) {
            sintomasContados[numSintomasContados] = sintomaLower;
            contagemPorSintoma[numSintomasContados] = 1;
            numSintomasContados++;
        }
    }

    /**
     * Regista todos os sintomas de um paciente atendido
     * @param paciente Paciente atendido
     */
    public void registarSintomasPaciente(Paciente paciente) {
        if (paciente == null) {
            return;
        }

        String[] sintomas = paciente.getSintomas();
        for (int i = 0; i < paciente.getNumSintomas(); i++) {
            registarSintomaPaciente(sintomas[i]);
        }
    }

    /**
     * Obtem a contagem de pacientes por sintoma
     * @return Array bidimensional com [sintoma, contagem]
     */
    public String[][] getContagemPorSintoma() {
        String[][] resultado = new String[numSintomasContados][2];
        for (int i = 0; i < numSintomasContados; i++) {
            resultado[i][0] = sintomasContados[i];
            resultado[i][1] = String.valueOf(contagemPorSintoma[i]);
        }
        return resultado;
    }

    /**
     * Obtem o total de pacientes contados por sintomas
     * @return Total de registos de sintomas
     */
    public int getTotalRegistosSintomas() {
        int total = 0;
        for (int i = 0; i < numSintomasContados; i++) {
            total += contagemPorSintoma[i];
        }
        return total;
    }

    /**
     * Mostra a estatistica de pacientes por sintoma
     */
    public void mostrarPacientesPorSintoma() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              NUMERO DE UTENTES POR SINTOMA                 ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        if (numSintomasContados == 0) {
            System.out.println("║  Ainda nao existem dados de sintomas registados.          ║");
            System.out.println("║  Os dados sao recolhidos quando os pacientes sao          ║");
            System.out.println("║  atendidos (apos terminarem consulta).                    ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            return;
        }

        // Ordena por contagem (maior primeiro) - bubble sort simples
        String[] sintomasOrdenados = new String[numSintomasContados];
        int[] contagemOrdenada = new int[numSintomasContados];

        for (int i = 0; i < numSintomasContados; i++) {
            sintomasOrdenados[i] = sintomasContados[i];
            contagemOrdenada[i] = contagemPorSintoma[i];
        }

        for (int i = 0; i < numSintomasContados - 1; i++) {
            for (int j = 0; j < numSintomasContados - i - 1; j++) {
                if (contagemOrdenada[j] < contagemOrdenada[j + 1]) {
                    // Troca
                    int tempCont = contagemOrdenada[j];
                    contagemOrdenada[j] = contagemOrdenada[j + 1];
                    contagemOrdenada[j + 1] = tempCont;

                    String tempSint = sintomasOrdenados[j];
                    sintomasOrdenados[j] = sintomasOrdenados[j + 1];
                    sintomasOrdenados[j + 1] = tempSint;
                }
            }
        }

        System.out.println("║  #  | Sintoma                          | Pacientes | %      ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        int totalPacientes = getTotalRegistosSintomas();

        for (int i = 0; i < numSintomasContados; i++) {
            double percentagem = (totalPacientes > 0) ?
                    (contagemOrdenada[i] * 100.0 / totalPacientes) : 0;

            System.out.printf("║ %2d  | %-32s | %9d | %5.1f%% ║%n",
                    (i + 1),
                    truncar(sintomasOrdenados[i], 32),
                    contagemOrdenada[i],
                    percentagem);
        }

        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║  TOTAL DE REGISTOS: %d                                      ║%n", totalPacientes);
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    // ==================== ESTATISTICA 3: SALARIOS POR MEDICO/DIA ====================

    /**
     * Calcula o salario de um medico num dia
     * @param medico Medico
     * @param horasTrabalhadas Horas trabalhadas nesse dia
     * @return Salario calculado
     */
    public double calcularSalarioMedicoDia(Medico medico, int horasTrabalhadas) {
        if (medico == null || horasTrabalhadas <= 0) {
            return 0.0;
        }
        return medico.getValorHora() * horasTrabalhadas;
    }

    /**
     * Mostra a tabela de salarios por medico
     */
    public void mostrarTabelaSalarios() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          TABELA DE SALARIOS POR MEDICO (HOJE)              ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║  Nome                | Especialidade | Valor/h | Salario   ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        Medico[] medicos = gestorHospital.getMedicos();
        int numMedicos = gestorHospital.getNumMedicos();
        double totalSalarios = 0;

        for (int i = 0; i < numMedicos; i++) {
            Medico m = medicos[i];
            // Calcula horas trabalhadas com base no horario
            int horasTrabalhadas = calcularHorasTurno(m.getHoraEntrada(), m.getHoraSaida());
            double salario = calcularSalarioMedicoDia(m, horasTrabalhadas);
            totalSalarios += salario;

            System.out.printf("║  %-18s | %-13s | %5.0f EUR | %7.0f EUR ║%n",
                    truncar(m.getNome(), 18),
                    m.getCodigoEspecialidade(),
                    m.getValorHora(),
                    salario);
        }

        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║  TOTAL SALARIOS DO DIA: %.0f EUR                           ║%n", totalSalarios);
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Calcula as horas de um turno
     */
    private int calcularHorasTurno(int entrada, int saida) {
        if (saida > entrada) {
            return saida - entrada;
        } else {
            // Turno atravessa meia-noite
            return (24 - entrada) + saida;
        }
    }

    // ==================== ESTATISTICA 4: TOP 3 ESPECIALIDADES ====================

    /**
     * Regista a especialidade de um paciente atendido
     * @param codigoEspecialidade Codigo da especialidade
     */
    public void registarEspecialidadePaciente(String codigoEspecialidade) {
        if (codigoEspecialidade == null || codigoEspecialidade.trim().isEmpty()) {
            return;
        }

        String espUpper = codigoEspecialidade.toUpperCase().trim();

        // Procura se a especialidade ja existe
        for (int i = 0; i < numEspecialidadesContadas; i++) {
            if (especialidadesContadas[i].equalsIgnoreCase(espUpper)) {
                contagemPorEspecialidade[i]++;
                return;
            }
        }

        // Adiciona nova especialidade
        if (numEspecialidadesContadas < MAX_ESPECIALIDADES) {
            especialidadesContadas[numEspecialidadesContadas] = espUpper;
            contagemPorEspecialidade[numEspecialidadesContadas] = 1;
            numEspecialidadesContadas++;
        }
    }

    /**
     * Mostra o top 3 de especialidades
     */
    public void mostrarTop3Especialidades() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║           TOP 3 ESPECIALIDADES MAIS PROCURADAS             ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        if (numEspecialidadesContadas == 0) {
            System.out.println("║  Ainda nao existem dados de especialidades registados.    ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            return;
        }

        // Ordena por contagem (maior primeiro)
        String[] espOrdenadas = new String[numEspecialidadesContadas];
        int[] contOrdenada = new int[numEspecialidadesContadas];

        for (int i = 0; i < numEspecialidadesContadas; i++) {
            espOrdenadas[i] = especialidadesContadas[i];
            contOrdenada[i] = contagemPorEspecialidade[i];
        }

        for (int i = 0; i < numEspecialidadesContadas - 1; i++) {
            for (int j = 0; j < numEspecialidadesContadas - i - 1; j++) {
                if (contOrdenada[j] < contOrdenada[j + 1]) {
                    int tempCont = contOrdenada[j];
                    contOrdenada[j] = contOrdenada[j + 1];
                    contOrdenada[j + 1] = tempCont;

                    String tempEsp = espOrdenadas[j];
                    espOrdenadas[j] = espOrdenadas[j + 1];
                    espOrdenadas[j + 1] = tempEsp;
                }
            }
        }

        int totalPacientes = 0;
        for (int i = 0; i < numEspecialidadesContadas; i++) {
            totalPacientes += contOrdenada[i];
        }

        System.out.println("║  Pos | Especialidade        | Pacientes | Percentagem      ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        int mostrar = Math.min(3, numEspecialidadesContadas);
        for (int i = 0; i < mostrar; i++) {
            double percentagem = (totalPacientes > 0) ?
                    (contOrdenada[i] * 100.0 / totalPacientes) : 0;

            String medalha = "";
            if (i == 0) medalha = " [1o]";
            else if (i == 1) medalha = " [2o]";
            else if (i == 2) medalha = " [3o]";

            System.out.printf("║  %d%s | %-18s | %9d | %14.1f%% ║%n",
                    (i + 1), medalha,
                    espOrdenadas[i],
                    contOrdenada[i],
                    percentagem);
        }

        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.printf("║  TOTAL DE PACIENTES: %d                                     ║%n", totalPacientes);
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    // ==================== METODOS AUXILIARES ====================

    /**
     * Limpa todas as estatisticas
     */
    public void limparEstatisticas() {
        numDiasRegistados = 0;
        numSintomasContados = 0;
        numEspecialidadesContadas = 0;
    }

    /**
     * Trunca texto se necessario
     */
    private String truncar(String texto, int tamanho) {
        if (texto == null) return "";
        if (texto.length() <= tamanho) {
            return texto;
        }
        return texto.substring(0, tamanho - 2) + "..";
    }

    // ==================== GETTERS ====================

    public int getNumDiasRegistados() {
        return numDiasRegistados;
    }

    public int getNumSintomasContados() {
        return numSintomasContados;
    }

    public int getNumEspecialidadesContadas() {
        return numEspecialidadesContadas;
    }
}
