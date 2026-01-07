package Controller;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controlador responsável por gerir notificações e logs do sistema
 * Armazena notificações temporárias e mantém histórico de logs
 */
public class GestorNotificacoes {

    private String[] notificacoes;
    private int numNotificacoes;
    private static final int MAX_NOTIFICACOES = 50;

    private String[] logs;
    private int numLogs;
    private static final int MAX_LOGS = 1000;

    private static final String FICHEIRO_LOGS = "logs.txt";
    private String caminhoFicheiros;

    private DateTimeFormatter formatoDataHora;

    /**
     * Construtor padrão
     */
    public GestorNotificacoes() {
        this.notificacoes = new String[MAX_NOTIFICACOES];
        this.numNotificacoes = 0;

        this.logs = new String[MAX_LOGS];
        this.numLogs = 0;

        this.caminhoFicheiros = "data";
        this.formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }

    /**
     * Construtor com caminho personalizado
     * @param caminhoFicheiros Caminho onde guardar os logs
     */
    public GestorNotificacoes(String caminhoFicheiros) {
        this();
        this.caminhoFicheiros = caminhoFicheiros;
    }

    /**
     * Adiciona uma nova notificação ao sistema
     * @param mensagem Mensagem da notificação
     */
    public void adicionarNotificacao(String mensagem) {
        if (numNotificacoes >= MAX_NOTIFICACOES) {
            // Remove a notificação mais antiga
            removerNotificacaoMaisAntiga();
        }

        notificacoes[numNotificacoes] = mensagem;
        numNotificacoes++;

        // Também adiciona ao log
        adicionarLog(mensagem);
    }

    /**
     * Remove a notificação mais antiga (FIFO)
     */
    private void removerNotificacaoMaisAntiga() {
        if (numNotificacoes <= 0) {
            return;
        }

        // Desloca todas as notificações uma posição para a esquerda
        for (int i = 0; i < numNotificacoes - 1; i++) {
            notificacoes[i] = notificacoes[i + 1];
        }
        numNotificacoes--;
    }

    /**
     * Obtém todas as notificações pendentes
     * @return Array com as notificações
     */
    public String[] obterNotificacoesPendentes() {
        String[] resultado = new String[numNotificacoes];

        for (int i = 0; i < numNotificacoes; i++) {
            resultado[i] = notificacoes[i];
        }

        return resultado;
    }

    /**
     * Limpa todas as notificações pendentes
     */
    public void limparNotificacoes() {
        for (int i = 0; i < numNotificacoes; i++) {
            notificacoes[i] = null;
        }
        numNotificacoes = 0;
    }

    /**
     * Verifica se existem notificações pendentes
     * @return true se existem notificações, false caso contrário
     */
    public boolean temNotificacoesPendentes() {
        return numNotificacoes > 0;
    }

    /**
     * Adiciona uma entrada no log do sistema
     * @param mensagem Mensagem a registar
     */
    public void adicionarLog(String mensagem) {
        if (numLogs >= MAX_LOGS) {
            // Remove o log mais antigo
            removerLogMaisAntigo();
        }

        String dataHora = LocalDateTime.now().format(formatoDataHora);
        String logCompleto = "[" + dataHora + "] " + mensagem;

        logs[numLogs] = logCompleto;
        numLogs++;
    }

    /**
     * Remove o log mais antigo (FIFO)
     */
    private void removerLogMaisAntigo() {
        if (numLogs <= 0) {
            return;
        }

        // Desloca todos os logs uma posição para a esquerda
        for (int i = 0; i < numLogs - 1; i++) {
            logs[i] = logs[i + 1];
        }
        numLogs--;
    }

    /**
     * Obtém todos os logs registados
     * @return Array com os logs
     */
    public String[] obterTodosLogs() {
        String[] resultado = new String[numLogs];

        for (int i = 0; i < numLogs; i++) {
            resultado[i] = logs[i];
        }

        return resultado;
    }

    /**
     * Obtém os últimos N logs
     * @param quantidade Número de logs a obter
     * @return Array com os últimos logs
     */
    public String[] obterUltimosLogs(int quantidade) {
        if (quantidade <= 0) {
            return new String[0];
        }

        int numLogsRetornar = Math.min(quantidade, numLogs);
        String[] resultado = new String[numLogsRetornar];

        // Obtém os últimos logs (do mais recente para o mais antigo)
        int indiceInicio = numLogs - numLogsRetornar;
        for (int i = 0; i < numLogsRetornar; i++) {
            resultado[i] = logs[indiceInicio + i];
        }

        return resultado;
    }

    /**
     * Pesquisa logs que contenham uma determinada palavra
     * @param palavra Palavra a pesquisar
     * @return Array com os logs encontrados
     */
    public String[] pesquisarLogs(String palavra) {
        if (palavra == null || palavra.trim().isEmpty()) {
            return new String[0];
        }

        // Primeiro, conta quantos logs correspondem
        int contador = 0;
        String palavraLower = palavra.toLowerCase();

        for (int i = 0; i < numLogs; i++) {
            if (logs[i].toLowerCase().contains(palavraLower)) {
                contador++;
            }
        }

        // Cria o array resultado
        String[] resultado = new String[contador];
        int indice = 0;

        for (int i = 0; i < numLogs; i++) {
            if (logs[i].toLowerCase().contains(palavraLower)) {
                resultado[indice] = logs[i];
                indice++;
            }
        }

        return resultado;
    }

    /**
     * Mostra todos os logs na consola
     */
    public void mostrarLogs() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    HISTÓRICO DE LOGS                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        if (numLogs == 0) {
            System.out.println("║              Nenhum log registado ainda                    ║");
        } else {
            for (int i = 0; i < numLogs; i++) {
                System.out.println("║ " + logs[i]);
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("Total de logs: " + numLogs);
    }

    /**
     * Mostra os últimos N logs na consola
     * @param quantidade Número de logs a mostrar
     */
    public void mostrarUltimosLogs(int quantidade) {
        String[] ultimosLogs = obterUltimosLogs(quantidade);

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              ÚLTIMOS " + quantidade + " LOGS                              ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        if (ultimosLogs.length == 0) {
            System.out.println("║              Nenhum log disponível                         ║");
        } else {
            for (int i = 0; i < ultimosLogs.length; i++) {
                System.out.println("║ " + ultimosLogs[i]);
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Guarda todos os logs num ficheiro
     * @return true se guardou com sucesso, false caso contrário
     */
    public boolean guardarLogsEmFicheiro() {
        // Criar diretório se não existir
        File diretorio = new File(caminhoFicheiros);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_LOGS);

        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro, true))) {
            for (int i = 0; i < numLogs; i++) {
                pw.println(logs[i]);
            }

            System.out.println("Logs guardados com sucesso!");
            return true;

        } catch (IOException e) {
            System.err.println("Erro ao guardar logs: " + e.getMessage());
            return false;
        }
    }

    /**
     * Carrega logs do ficheiro
     * @return Número de logs carregados
     */
    public int carregarLogsDoFicheiro() {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_LOGS);

        if (!ficheiro.exists()) {
            return 0;
        }

        int logsCarregados = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null && numLogs < MAX_LOGS) {
                logs[numLogs] = linha;
                numLogs++;
                logsCarregados++;
            }

            if (logsCarregados > 0) {
                System.out.println("✓ " + logsCarregados + " logs carregados com sucesso!");
            }

        } catch (IOException e) {
            System.err.println(" Erro ao carregar logs: " + e.getMessage());
        }

        return logsCarregados;
    }

    /**
     * Limpa todos os logs da memória
     */
    public void limparLogs() {
        for (int i = 0; i < numLogs; i++) {
            logs[i] = null;
        }
        numLogs = 0;
    }

    /**
     * Define o caminho dos ficheiros
     * @param caminhoFicheiros Novo caminho
     */
    public void setCaminhoFicheiros(String caminhoFicheiros) {
        this.caminhoFicheiros = caminhoFicheiros;
    }

    /**
     * Obtém o número de notificações pendentes
     * @return Número de notificações
     */
    public int getNumNotificacoes() {
        return numNotificacoes;
    }

    /**
     * Obtém o número total de logs
     * @return Número de logs
     */
    public int getNumLogs() {
        return numLogs;
    }

    /**
     * Notificações específicas para eventos do hospital
     */

    public void notificarMedicoDisponivel(String nomeMedico, String especialidade) {
        adicionarNotificacao("Médico " + nomeMedico + " (" + especialidade + ") ficou disponível");
    }

    public void notificarElevacaoUrgencia(String nomePaciente, String nivelAntigo, String nivelNovo) {
        adicionarNotificacao("Paciente " + nomePaciente + " - Urgência elevada: " + nivelAntigo + " → " + nivelNovo);
    }

    public void notificarPacienteAtendido(String nomePaciente, String nomeMedico) {
        adicionarNotificacao("Paciente " + nomePaciente + " atendido por " + nomeMedico);
    }

    public void notificarPacienteTriagem(String nomePaciente, String nivelUrgencia) {
        adicionarNotificacao("Paciente " + nomePaciente + " - Triagem concluída: " + nivelUrgencia);
    }

    public void notificarMedicoEmDescanso(String nomeMedico) {
        adicionarNotificacao(" Médico " + nomeMedico + " entrou em período de descanso");
    }

    public void notificarNovoTurno(int dia, int unidadeTempo) {
        adicionarNotificacao("Novo período - Dia " + dia + ", Unidade " + unidadeTempo);
    }

    public void notificarAlerta(String mensagem) {
        adicionarNotificacao("ALERTA: " + mensagem);
    }
}