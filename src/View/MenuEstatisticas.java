package View;

import java.util.Scanner;
import Model.Configuracoes;
import Controller.GestorNotificacoes;
import Controller.GestorHospital;
import Controller.GestorEstatisticas;
import Utils.Utils;

/**
 * Menu de estatisticas e logs do hospital
 * Responsabilidade: Aluno 2
 */
public class MenuEstatisticas {

    /**
     * Mostra o menu de estatisticas
     * @param scanner Scanner para input
     * @param config Configuracoes do sistema
     * @param gestorNotif Gestor de notificacoes
     * @param gestorHospital Gestor do hospital
     * @param gestorEstatisticas Gestor de estatisticas
     */
    public static void mostrar(Scanner scanner, Configuracoes config,
                                GestorNotificacoes gestorNotif,
                                GestorHospital gestorHospital,
                                GestorEstatisticas gestorEstatisticas) {
        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            mostrarMenuPrincipalEstatisticas();

            int opcao = Utils.lerOpcao(scanner, 1, 7);

            switch (opcao) {
                case 1:
                    gestorEstatisticas.mostrarMediaUtentesPorDia();
                    gestorNotif.adicionarLog("Estatistica consultada: Media utentes/dia");
                    Utils.pausar(scanner);
                    break;
                case 2:
                    gestorEstatisticas.mostrarTabelaSalarios();
                    gestorNotif.adicionarLog("Estatistica consultada: Tabela salarios");
                    Utils.pausar(scanner);
                    break;
                case 3:
                    gestorEstatisticas.mostrarPacientesPorSintoma();
                    gestorNotif.adicionarLog("Estatistica consultada: Pacientes por sintoma");
                    Utils.pausar(scanner);
                    break;
                case 4:
                    gestorEstatisticas.mostrarTop3Especialidades();
                    gestorNotif.adicionarLog("Estatistica consultada: Top 3 especialidades");
                    Utils.pausar(scanner);
                    break;
                case 5:
                    mostrarLogsRecentes(scanner, gestorNotif);
                    break;
                case 6:
                    pesquisarLogs(scanner, gestorNotif);
                    break;
                case 7:
                    voltar = true;
                    break;
            }
        }
    }

    /**
     * Mostra o menu principal de estatisticas
     */
    private static void mostrarMenuPrincipalEstatisticas() {
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│              ESTATISTICAS E LOGS                           │");
        System.out.println("├────────────────────────────────────────────────────────────┤");
        System.out.println("│  ESTATISTICAS:                                             │");
        System.out.println("│  1. Media de Utentes Atendidos por Dia                     │");
        System.out.println("│  2. Tabela de Salarios por Medico                          │");
        System.out.println("│  3. Numero de Utentes por Sintoma                          │");
        System.out.println("│  4. Top 3 Especialidades                                   │");
        System.out.println("│                                                            │");
        System.out.println("│  LOGS:                                                     │");
        System.out.println("│  5. Ver Logs Recentes                                      │");
        System.out.println("│  6. Pesquisar Logs                                         │");
        System.out.println("│                                                            │");
        System.out.println("│  7. Voltar ao Menu Principal                               │");
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("\nEscolha uma opcao: ");
    }

    /**
     * Mostra os logs recentes
     */
    private static void mostrarLogsRecentes(Scanner scanner, GestorNotificacoes gestorNotif) {
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│                    LOGS RECENTES                           │");
        System.out.println("└────────────────────────────────────────────────────────────┘");

        System.out.print("Quantos logs deseja ver? (1-100): ");
        int quantidade = Utils.lerOpcao(scanner, 1, 100);

        gestorNotif.mostrarUltimosLogs(quantidade);
        Utils.pausar(scanner);
    }

    /**
     * Pesquisa logs por palavra-chave
     */
    private static void pesquisarLogs(Scanner scanner, GestorNotificacoes gestorNotif) {
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│                   PESQUISAR LOGS                           │");
        System.out.println("└────────────────────────────────────────────────────────────┘");

        System.out.print("Digite a palavra a pesquisar: ");
        String palavra = scanner.nextLine().trim();

        if (palavra.isEmpty()) {
            System.out.println("Palavra invalida!");
            Utils.pausar(scanner);
            return;
        }

        String[] resultados = gestorNotif.pesquisarLogs(palavra);

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              RESULTADOS DA PESQUISA                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        if (resultados.length == 0) {
            System.out.println("║  Nenhum log encontrado com a palavra: " + palavra);
        } else {
            System.out.println("║  Encontrados " + resultados.length + " log(s) com: " + palavra);
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            for (int i = 0; i < resultados.length; i++) {
                System.out.println("║  " + resultados[i]);
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
        Utils.pausar(scanner);
    }

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
}
