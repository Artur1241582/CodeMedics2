package View;

import Controller.GestorNotificacoes;
import Model.Configuracoes;
import Utils.Utils;

import java.util.Scanner;

public class MenuPrincipal {

    private Scanner scanner;
    private Configuracoes config;
    private GestorNotificacoes gestorNotif;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.config = new Configuracoes();
        this.gestorNotif = new GestorNotificacoes();
    }

    public void iniciar() {
        carregarDadosIniciais();


        int opcao = 0;

        while (opcao != 5) {
            mostrarCabecalho();
            mostrarNotificacoesPendentes();
            mostrarMenuPrincipal();

            opcao = lerOpcao(1, 5);

            switch (opcao) {
                case 1:
                    MenuGestao.mostrar(scanner, config, gestorNotif);
                    break;
                case 2:
                    MenuFuncionamento.mostrar(scanner, config, gestorNotif);
                    break;
                case 3:
                    // MenuEstatisticas.mostrar(scanner, config, gestorNotif);
                    break;
                case 4:
                    // MenuConfiguracoes.mostrar(scanner, config, gestorNotif);
                    break;
                case 5:
                    break;
            }
        }
        guardarDados();
        System.out.println("\n Até breve!");
        scanner.close();
    }

    // --- Restantes métodos permanecem iguais ---

    private void mostrarCabecalho() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE GESTÃO - HOSPITAL DE URGÊNCIAS             ║");
        System.out.println("║                                                            ║");
        System.out.println("║     Dia: " + Utils.formatarNumero(config.getDiaAtual(), 2) +
                "  |  Unidade de Tempo: " + Utils.formatarNumero(config.getUnidadeTempoAtual(), 2) +
                "/24                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│                      MENU PRINCIPAL                        │");
        System.out.println("├────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Gerir Médicos, Especialidades e Sintomas              │");
        System.out.println("│  2. Gerir Funcionamento do Hospital                        │");
        System.out.println("│  3. Consultar Estatísticas e Logs                          │");
        System.out.println("│  4. Configurações                                          │");
        System.out.println("│  5. Sair                                                   │");
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("\nEscolha uma opção: ");
    }

    private void mostrarNotificacoesPendentes() {
        String[] notifs = gestorNotif.obterNotificacoesPendentes();
        if (notifs.length > 0) {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║                     NOTIFICAÇÕES                         ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            for (String notif : notifs) {
                System.out.println("║ • " + notif);
            }
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            gestorNotif.limparNotificacoes();
        }
    }

    private void carregarDadosIniciais() {
        System.out.println("Carregando dados do sistema...");
        gestorNotif.adicionarLog("Sistema iniciado");
        System.out.println(" Dados carregados com sucesso!\n");
    }

    private void guardarDados() {
        System.out.println("\nGuardando dados...");
        gestorNotif.adicionarLog("Dados guardados ao sair do sistema");
        System.out.println(" Dados guardados com sucesso!");
    }

    private int lerOpcao(int min, int max) {
        int opcao = -1;
        boolean valido = false;

        while (!valido) {
            try {
                opcao = Integer.parseInt(scanner.nextLine().trim());
                if (opcao >= min && opcao <= max) {
                    valido = true;
                } else {
                    System.out.print(" Opção inválida! Escolha entre " + min + " e " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print(" Por favor, insira um número válido: ");
            }
        }
        return opcao;
    }
}