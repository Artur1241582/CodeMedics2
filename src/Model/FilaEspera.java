package Model;

/**
 * Classe que gere a fila de espera de pacientes
 * Mantem os pacientes ordenados por prioridade (urgencia e tempo de espera)
 * Responsabilidade: Aluno 2/3
 */
public class FilaEspera {

    private Paciente[] pacientes;
    private int numPacientes;
    private int capacidadeMaxima;

    /**
     * Construtor da fila de espera
     * @param capacidade Capacidade maxima da fila
     */
    public FilaEspera(int capacidade) {
        this.capacidadeMaxima = capacidade;
        this.pacientes = new Paciente[capacidade];
        this.numPacientes = 0;
    }

    /**
     * Construtor com capacidade padrao de 100
     */
    public FilaEspera() {
        this(100);
    }

    // ==================== GESTAO DE PACIENTES ====================

    /**
     * Adiciona um paciente a fila (ordenado por prioridade)
     * @param paciente Paciente a adicionar
     * @return true se adicionou, false se fila cheia
     */
    public boolean adicionarPaciente(Paciente paciente) {
        if (numPacientes >= capacidadeMaxima) {
            return false;
        }

        pacientes[numPacientes] = paciente;
        numPacientes++;
        reorganizarPorPrioridade();
        return true;
    }

    /**
     * Remove um paciente pelo indice
     * @param indice Indice do paciente a remover
     * @return Paciente removido, ou null se indice invalido
     */
    public Paciente removerPaciente(int indice) {
        if (indice < 0 || indice >= numPacientes) {
            return null;
        }

        Paciente removido = pacientes[indice];

        // Desloca os elementos para a esquerda
        for (int i = indice; i < numPacientes - 1; i++) {
            pacientes[i] = pacientes[i + 1];
        }
        pacientes[numPacientes - 1] = null;
        numPacientes--;

        return removido;
    }

    /**
     * Remove um paciente especifico
     * @param paciente Paciente a remover
     * @return true se removeu, false se nao encontrou
     */
    public boolean removerPaciente(Paciente paciente) {
        for (int i = 0; i < numPacientes; i++) {
            if (pacientes[i].getId() == paciente.getId()) {
                removerPaciente(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Obtem o proximo paciente da fila (maior prioridade)
     * @return Proximo paciente, ou null se fila vazia
     */
    public Paciente obterProximoPaciente() {
        if (numPacientes == 0) {
            return null;
        }
        return pacientes[0]; // Ja esta ordenado por prioridade
    }

    /**
     * Obtem o proximo paciente para uma especialidade especifica
     * @param codigoEspecialidade Codigo da especialidade
     * @return Proximo paciente para essa especialidade, ou null
     */
    public Paciente obterProximoPacientePorEspecialidade(String codigoEspecialidade) {
        for (int i = 0; i < numPacientes; i++) {
            String espSugerida = pacientes[i].getEspecialidadeSugerida();
            if (espSugerida != null && espSugerida.equals(codigoEspecialidade)) {
                return pacientes[i];
            }
        }
        // Se nao encontrou especifico, retorna o primeiro sem especialidade definida
        for (int i = 0; i < numPacientes; i++) {
            if (pacientes[i].getEspecialidadeSugerida() == null) {
                return pacientes[i];
            }
        }
        return null;
    }

    /**
     * Obtem um paciente pelo indice
     * @param indice Indice do paciente
     * @return Paciente na posicao, ou null se invalido
     */
    public Paciente obterPacientePorIndice(int indice) {
        if (indice < 0 || indice >= numPacientes) {
            return null;
        }
        return pacientes[indice];
    }

    /**
     * Obtem um paciente pelo ID
     * @param id ID do paciente
     * @return Paciente com esse ID, ou null se nao encontrado
     */
    public Paciente obterPacientePorId(int id) {
        for (int i = 0; i < numPacientes; i++) {
            if (pacientes[i].getId() == id) {
                return pacientes[i];
            }
        }
        return null;
    }

    // ==================== ORDENACAO ====================

    /**
     * Reorganiza a fila por prioridade
     * Prioridade: urgencia (maior primeiro), depois tempo de espera (maior primeiro)
     */
    public void reorganizarPorPrioridade() {
        // Bubble sort (simples, adequado para arrays pequenos)
        for (int i = 0; i < numPacientes - 1; i++) {
            for (int j = 0; j < numPacientes - i - 1; j++) {
                if (deveTrocar(pacientes[j], pacientes[j + 1])) {
                    Paciente temp = pacientes[j];
                    pacientes[j] = pacientes[j + 1];
                    pacientes[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Verifica se dois pacientes devem ser trocados na ordenacao
     * @param a Primeiro paciente
     * @param b Segundo paciente
     * @return true se devem trocar (b tem maior prioridade que a)
     */
    private boolean deveTrocar(Paciente a, Paciente b) {
        // Maior urgencia tem prioridade
        if (a.getNivelUrgencia() < b.getNivelUrgencia()) {
            return true;
        }
        if (a.getNivelUrgencia() > b.getNivelUrgencia()) {
            return false;
        }
        // Mesma urgencia: maior tempo de espera tem prioridade
        return a.getTempoEspera() < b.getTempoEspera();
    }

    // ==================== ESTATISTICAS ====================

    /**
     * Conta pacientes por especialidade sugerida
     * @param codigoEspecialidade Codigo da especialidade
     * @return Numero de pacientes para essa especialidade
     */
    public int contarPacientesPorEspecialidade(String codigoEspecialidade) {
        int contador = 0;
        for (int i = 0; i < numPacientes; i++) {
            String esp = pacientes[i].getEspecialidadeSugerida();
            if (esp != null && esp.equals(codigoEspecialidade)) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Conta pacientes por nivel de urgencia
     * @param nivelUrgencia Nivel (1, 2 ou 3)
     * @return Numero de pacientes com esse nivel
     */
    public int contarPacientesPorUrgencia(int nivelUrgencia) {
        int contador = 0;
        for (int i = 0; i < numPacientes; i++) {
            if (pacientes[i].getNivelUrgencia() == nivelUrgencia) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Conta pacientes sem especialidade definida
     * @return Numero de pacientes sem especialidade
     */
    public int contarPacientesSemEspecialidade() {
        int contador = 0;
        for (int i = 0; i < numPacientes; i++) {
            if (pacientes[i].getEspecialidadeSugerida() == null) {
                contador++;
            }
        }
        return contador;
    }

    // ==================== PROCESSAMENTO DE TEMPO ====================

    /**
     * Incrementa o tempo de espera de todos os pacientes
     */
    public void incrementarTempoEsperaTodos() {
        for (int i = 0; i < numPacientes; i++) {
            pacientes[i].incrementarTempoEspera();
        }
    }

    /**
     * Obtem pacientes que devem ter a urgencia elevada
     * @param tempoElevacao Tempo necessario para elevar urgencia
     * @return Array de pacientes que precisam de elevacao
     */
    public Paciente[] obterPacientesParaElevacao(int tempoElevacao) {
        // Primeiro conta quantos
        int contador = 0;
        for (int i = 0; i < numPacientes; i++) {
            if (pacientes[i].getTempoEspera() >= tempoElevacao) {
                contador++;
            }
        }

        // Cria o array resultado
        Paciente[] resultado = new Paciente[contador];
        int indice = 0;
        for (int i = 0; i < numPacientes; i++) {
            if (pacientes[i].getTempoEspera() >= tempoElevacao) {
                resultado[indice] = pacientes[i];
                indice++;
            }
        }

        return resultado;
    }

    // ==================== GETTERS ====================

    public int getNumPacientes() {
        return numPacientes;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public boolean estaVazia() {
        return numPacientes == 0;
    }

    public boolean estaCheia() {
        return numPacientes >= capacidadeMaxima;
    }

    /**
     * Obtem todos os pacientes da fila
     * @return Array com os pacientes
     */
    public Paciente[] getTodosPacientes() {
        Paciente[] resultado = new Paciente[numPacientes];
        for (int i = 0; i < numPacientes; i++) {
            resultado[i] = pacientes[i];
        }
        return resultado;
    }

    // ==================== DISPLAY ====================

    /**
     * Lista todos os pacientes na fila
     */
    public void listarPacientes() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    FILA DE ESPERA                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        if (numPacientes == 0) {
            System.out.println("║              Nenhum paciente em espera                     ║");
        } else {
            System.out.println("║ # | Nome                | Urgencia       | Esp.  | Espera ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");

            for (int i = 0; i < numPacientes; i++) {
                Paciente p = pacientes[i];
                String esp = p.getEspecialidadeSugerida() != null ? p.getEspecialidadeSugerida() : "N/A";
                System.out.printf("║ %d | %-19s | %-14s | %-5s | %3d un ║%n",
                        (i + 1),
                        truncar(p.getNome(), 19),
                        p.getNivelUrgenciaTexto(),
                        esp,
                        p.getTempoEspera());
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("Total em espera: " + numPacientes);
    }

    /**
     * Trunca uma string se for maior que o tamanho especificado
     */
    private String truncar(String texto, int tamanho) {
        if (texto.length() <= tamanho) {
            return texto;
        }
        return texto.substring(0, tamanho - 2) + "..";
    }

    @Override
    public String toString() {
        return "Fila de Espera: " + numPacientes + "/" + capacidadeMaxima + " pacientes";
    }
}
