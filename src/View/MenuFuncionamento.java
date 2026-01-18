package View;

import java.util.Scanner;
import Model.*;
import Controller.*;
import Utils.Utils;

/**
 * Menu de funcionamento do hospital (opcao 2 do menu principal)
 * Gere o dia-a-dia: registo de pacientes, triagem, encaminhamento e tempo
 * Recebe o GestorHospital centralizado (nao cria o seu proprio)
 * Responsabilidade: Aluno 2/3
 */
public class MenuFuncionamento {

    /**
     * Mostra o menu de funcionamento do hospital
     * @param scanner Scanner para input
     * @param config Configuracoes do sistema
     * @param gestorNotif Gestor de notificacoes
     * @param gestorHospital GestorHospital centralizado (compartilhado)
     */
    public static void mostrar(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, GestorHospital gestorHospital) {
        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            mostrarMenuPrincipalFuncionamento();

            int opcao = Utils.lerOpcao(scanner, 1, 6);

            switch (opcao) {
                case 1:
                    registarPaciente(scanner, config, gestorNotif, gestorHospital);
                    break;
                case 2:
                    verFilaEspera(scanner, gestorHospital);
                    break;
                case 3:
                    menuEncaminhamento(scanner, config, gestorNotif, gestorHospital);
                    break;
                case 4:
                    avancarTempo(scanner, config, gestorHospital);
                    break;
                case 5:
                    verEstadoHospital(scanner, gestorHospital);
                    break;
                case 6:
                    voltar = true;
                    break;
            }
        }
    }

    /**
     * Mostra o menu principal de funcionamento
     */
    private static void mostrarMenuPrincipalFuncionamento() {
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│              FUNCIONAMENTO DO HOSPITAL                     │");
        System.out.println("├────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Registar Paciente                                      │");
        System.out.println("│  2. Ver Fila de Espera                                     │");
        System.out.println("│  3. Encaminhar Paciente                                    │");
        System.out.println("│  4. Avancar Tempo                                          │");
        System.out.println("│  5. Ver Estado do Hospital                                 │");
        System.out.println("│  6. Voltar ao Menu Principal                               │");
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("\nEscolha uma opcao: ");
    }

    // ==================== REGISTO DE PACIENTE ====================

    /**
     * Processo de registo de um novo paciente
     */
    private static void registarPaciente(Scanner scanner, Configuracoes config,
                                          GestorNotificacoes gestorNotif, GestorHospital gestorHospital) {
        mostrarCabecalho(config);
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│                   REGISTAR PACIENTE                        │");
        System.out.println("└────────────────────────────────────────────────────────────┘");

        // Pede o nome
        String nome = Utils.lerTexto(scanner, "Nome do paciente: ");

        if (nome.trim().isEmpty()) {
            System.out.println("Nome invalido!");
            Utils.pausar(scanner);
            return;
        }

        // Cria o paciente
        Paciente paciente = gestorHospital.registarPaciente(nome);

        // Selecao de sintomas
        selecionarSintomas(scanner, paciente, gestorHospital);

        if (paciente.getNumSintomas() == 0) {
            System.out.println("\nNenhum sintoma selecionado. Paciente nao foi registado.");
            Utils.pausar(scanner);
            return;
        }

        // Realiza triagem automatica
        System.out.println("\nA realizar triagem...");
        Triagem triagem = gestorHospital.realizarTriagem(paciente);

        // Mostra resultado
        triagem.mostrarResultado();

        // Pergunta se quer encaminhar
        if (triagem.isEncaminhamentoAutomatico()) {
            if (Utils.confirmar(scanner, "\nDeseja encaminhar automaticamente?")) {
                if (gestorHospital.encaminharAutomatico()) {
                    System.out.println("\nPaciente encaminhado com sucesso!");
                } else {
                    System.out.println("\nNao foi possivel encaminhar (sem medicos disponiveis).");
                    System.out.println("Paciente permanece na fila de espera.");
                }
            } else {
                System.out.println("Paciente adicionado a fila de espera.");
            }
        } else {
            System.out.println("\nPaciente adicionado a fila de espera.");
            System.out.println("Encaminhamento manual recomendado.");
        }

        Utils.pausar(scanner);
    }

    /**
     * Interface para selecao de sintomas
     */
    private static void selecionarSintomas(Scanner scanner, Paciente paciente, GestorHospital gestorHospital) {
        String[] nomesSintomas = gestorHospital.obterNomesSintomas();
        int numSintomas = gestorHospital.getNumSintomas();

        if (numSintomas == 0) {
            System.out.println("\nNao existem sintomas registados no sistema!");
            System.out.println("Por favor, adicione sintomas primeiro no Menu de Gestao.");
            return;
        }

        boolean continuar = true;
        int paginaAtual = 0;
        int sintomasPorPagina = 15;
        int totalPaginas = (numSintomas + sintomasPorPagina - 1) / sintomasPorPagina;

        while (continuar) {
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│               SELECIONAR SINTOMAS                          │");
            System.out.println("│        Sintomas selecionados: " + paciente.getNumSintomas() +
                               "                            │");
            System.out.println("├────────────────────────────────────────────────────────────┤");

            // Mostra sintomas da pagina atual
            int inicio = paginaAtual * sintomasPorPagina;
            int fim = Math.min(inicio + sintomasPorPagina, numSintomas);

            for (int i = inicio; i < fim; i++) {
                System.out.printf("│  %2d. %-50s │%n", (i + 1), truncar(nomesSintomas[i], 50));
            }

            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  Pagina " + (paginaAtual + 1) + "/" + totalPaginas +
                               "  |  [A]nterior  |  [P]roxima  |  [C]oncluir       │");
            System.out.println("└────────────────────────────────────────────────────────────┘");

            // Mostra sintomas ja selecionados
            if (paciente.getNumSintomas() > 0) {
                System.out.println("\nSintomas selecionados:");
                String[] selecionados = paciente.getSintomas();
                for (int i = 0; i < paciente.getNumSintomas(); i++) {
                    System.out.println("  - " + selecionados[i]);
                }
            }

            System.out.print("\nDigite o numero do sintoma ou comando (A/P/C): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("A")) {
                // Pagina anterior
                if (paginaAtual > 0) {
                    paginaAtual--;
                }
            } else if (input.equalsIgnoreCase("P")) {
                // Proxima pagina
                if (paginaAtual < totalPaginas - 1) {
                    paginaAtual++;
                }
            } else if (input.equalsIgnoreCase("C")) {
                // Concluir selecao
                continuar = false;
            } else {
                // Tenta interpretar como numero
                try {
                    int numero = Integer.parseInt(input);
                    if (numero >= 1 && numero <= numSintomas) {
                        String sintoma = nomesSintomas[numero - 1];

                        // Verifica se ja foi adicionado
                        boolean jaExiste = false;
                        String[] selecionados = paciente.getSintomas();
                        for (int i = 0; i < paciente.getNumSintomas(); i++) {
                            if (selecionados[i].equalsIgnoreCase(sintoma)) {
                                jaExiste = true;
                                break;
                            }
                        }

                        if (jaExiste) {
                            System.out.println("Sintoma ja selecionado!");
                        } else {
                            if (paciente.adicionarSintoma(sintoma)) {
                                System.out.println("Sintoma adicionado: " + sintoma);
                            } else {
                                System.out.println("Limite de sintomas atingido!");
                            }
                        }
                    } else {
                        System.out.println("Numero invalido!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Comando invalido!");
                }
            }
        }
    }

    // ==================== FILA DE ESPERA ====================

    /**
     * Mostra a fila de espera
     */
    private static void verFilaEspera(Scanner scanner, GestorHospital gestorHospital) {
        gestorHospital.listarFilaEspera();
        Utils.pausar(scanner);
    }

    // ==================== ENCAMINHAMENTO ====================

    /**
     * Menu de encaminhamento de pacientes
     */
    private static void menuEncaminhamento(Scanner scanner, Configuracoes config,
                                            GestorNotificacoes gestorNotif, GestorHospital gestorHospital) {
        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│                 ENCAMINHAR PACIENTE                        │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Encaminhamento Automatico                              │");
            System.out.println("│  2. Encaminhamento Manual                                  │");
            System.out.println("│  3. Voltar                                                 │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opcao: ");

            int opcao = Utils.lerOpcao(scanner, 1, 3);

            switch (opcao) {
                case 1:
                    encaminhamentoAutomatico(scanner, gestorHospital);
                    break;
                case 2:
                    encaminhamentoManual(scanner, config, gestorHospital);
                    break;
                case 3:
                    voltar = true;
                    break;
            }
        }
    }

    /**
     * Realiza encaminhamento automatico
     */
    private static void encaminhamentoAutomatico(Scanner scanner, GestorHospital gestorHospital) {
        System.out.println("\n--- ENCAMINHAMENTO AUTOMATICO ---");

        if (gestorHospital.getFilaEspera().estaVazia()) {
            System.out.println("Nao ha pacientes na fila de espera.");
            Utils.pausar(scanner);
            return;
        }

        Paciente proximo = gestorHospital.getFilaEspera().obterProximoPaciente();
        System.out.println("Proximo paciente: " + proximo.getNome() +
                           " (" + proximo.getNivelUrgenciaTexto() + ")");

        Medico[] disponiveis = gestorHospital.obterMedicosDisponiveis();
        System.out.println("Medicos disponiveis: " + disponiveis.length);

        if (disponiveis.length == 0) {
            System.out.println("\nNao ha medicos disponiveis no momento.");
            Utils.pausar(scanner);
            return;
        }

        if (Utils.confirmar(scanner, "Confirma encaminhamento automatico?")) {
            if (gestorHospital.encaminharAutomatico()) {
                System.out.println("\nPaciente encaminhado com sucesso!");
            } else {
                System.out.println("\nFalha no encaminhamento.");
            }
        }

        Utils.pausar(scanner);
    }

    /**
     * Realiza encaminhamento manual
     */
    private static void encaminhamentoManual(Scanner scanner, Configuracoes config, GestorHospital gestorHospital) {
        mostrarCabecalho(config);
        System.out.println("\n--- ENCAMINHAMENTO MANUAL ---");

        // Verifica se ha pacientes
        if (gestorHospital.getFilaEspera().estaVazia()) {
            System.out.println("Nao ha pacientes na fila de espera.");
            Utils.pausar(scanner);
            return;
        }

        // Lista pacientes
        gestorHospital.listarFilaEspera();

        // Seleciona paciente
        System.out.print("\nDigite o numero do paciente (0 para cancelar): ");
        int numPaciente = Utils.lerOpcao(scanner, 0, gestorHospital.getFilaEspera().getNumPacientes());

        if (numPaciente == 0) {
            return;
        }

        Paciente paciente = gestorHospital.getFilaEspera().obterPacientePorIndice(numPaciente - 1);
        System.out.println("\nPaciente selecionado: " + paciente.getNome());

        // Lista medicos disponiveis
        System.out.println("\n--- MEDICOS DISPONIVEIS ---");
        Medico[] disponiveis = gestorHospital.obterMedicosDisponiveis();

        if (disponiveis.length == 0) {
            System.out.println("Nao ha medicos disponiveis.");
            Utils.pausar(scanner);
            return;
        }

        // Mostra medicos disponiveis com indices
        int[] indicesOriginais = new int[disponiveis.length];
        int contador = 0;

        for (int i = 0; i < gestorHospital.getNumMedicos(); i++) {
            Medico m = gestorHospital.obterMedicoPorIndice(i);
            if (m.isDisponivel()) {
                System.out.printf("%d. Dr. %s (%s)%n", (contador + 1), m.getNome(),
                                  m.getCodigoEspecialidade());
                indicesOriginais[contador] = i;
                contador++;
            }
        }

        // Seleciona medico
        System.out.print("\nDigite o numero do medico (0 para cancelar): ");
        int numMedico = Utils.lerOpcao(scanner, 0, disponiveis.length);

        if (numMedico == 0) {
            return;
        }

        // Realiza encaminhamento
        int indiceMedicoReal = indicesOriginais[numMedico - 1];

        if (gestorHospital.encaminharPorIndice(numPaciente - 1, indiceMedicoReal)) {
            System.out.println("\nPaciente encaminhado com sucesso!");
        } else {
            System.out.println("\nFalha no encaminhamento.");
        }

        Utils.pausar(scanner);
    }

    // ==================== GESTAO DE TEMPO ====================

    /**
     * Avanca o tempo e mostra eventos
     */
    private static void avancarTempo(Scanner scanner, Configuracoes config, GestorHospital gestorHospital) {
        System.out.println("\n--- AVANCAR TEMPO ---");
        System.out.println("Tempo atual: Dia " + config.getDiaAtual() +
                           ", Unidade " + config.getUnidadeTempoAtual() + "/24");

        if (Utils.confirmar(scanner, "Avancar 1 unidade de tempo?")) {
            gestorHospital.avancarTempo();

            System.out.println("\nNovo tempo: Dia " + config.getDiaAtual() +
                               ", Unidade " + config.getUnidadeTempoAtual() + "/24");
            System.out.println("\n(Verifique as notificacoes no menu principal)");
        }

        Utils.pausar(scanner);
    }

    // ==================== ESTADO DO HOSPITAL ====================

    /**
     * Mostra o estado atual do hospital
     */
    private static void verEstadoHospital(Scanner scanner, GestorHospital gestorHospital) {
        gestorHospital.listarEstadoHospital();
        Utils.pausar(scanner);
    }

    // ==================== UTILIDADES ====================

    /**
     * Mostra o cabecalho padrao
     */
    private static void mostrarCabecalho(Configuracoes config) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE GESTAO - HOSPITAL DE URGENCIAS              ║");
        System.out.println("║                                                            ║");
        System.out.println("║     Dia: " + Utils.formatarNumero(config.getDiaAtual(), 2) +
                "  |  Unidade de Tempo: " + Utils.formatarNumero(config.getUnidadeTempoAtual(), 2) +
                "/24                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Trunca texto se necessario
     */
    private static String truncar(String texto, int tamanho) {
        if (texto == null) return "";
        if (texto.length() <= tamanho) {
            return texto;
        }
        return texto.substring(0, tamanho - 2) + "..";
    }
}
