package View;

import Controller.GestorFicheiros;
import Controller.GestorHospital;
import Controller.GestorNotificacoes;
import Controller.GestorEstatisticas;
import Model.Configuracoes;
import Utils.Utils;

import java.util.Scanner;

/**
 * Menu principal do sistema
 * Centraliza a criacao do GestorHospital e passa para os submenus
 */
public class MenuPrincipal {

    private Scanner scanner;
    private Configuracoes config;
    private GestorNotificacoes gestorNotif;
    private GestorFicheiros gestorFicheiros;
    private GestorHospital gestorHospital;
    private GestorEstatisticas gestorEstatisticas;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.config = new Configuracoes();
        this.gestorNotif = new GestorNotificacoes();
        this.gestorFicheiros = new GestorFicheiros(config.getCaminhoFicheiros(), config.getSeparador());
        // GestorHospital centralizado - criado uma unica vez
        this.gestorHospital = new GestorHospital(config, gestorNotif, gestorFicheiros);
        // GestorEstatisticas - para calculos de estatisticas
        this.gestorEstatisticas = new GestorEstatisticas(gestorHospital);
        // Liga o gestor de estatisticas ao gestor do hospital
        this.gestorHospital.setGestorEstatisticas(gestorEstatisticas);
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
                    // Passa o GestorHospital centralizado para MenuGestao
                    MenuGestao.mostrar(scanner, config, gestorNotif, gestorHospital);
                    break;
                case 2:
                    // Passa o GestorHospital centralizado para MenuFuncionamento
                    MenuFuncionamento.mostrar(scanner, config, gestorNotif, gestorHospital);
                    break;
                case 3:
                    MenuEstatisticas.mostrar(scanner, config, gestorNotif, gestorHospital, gestorEstatisticas);
                    break;
                case 4:
                    MenuConfiguracoes.mostrar(scanner, config, gestorNotif);
                    Utils.pausar(scanner);
                    break;
                case 5:
                    break;
            }
        }
        guardarDados();
        System.out.println("\n Ate breve!");
        scanner.close();
    }

    private void mostrarCabecalho() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE GESTAO - HOSPITAL DE URGENCIAS              ║");
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
        System.out.println("│  1. Gerir Medicos, Especialidades e Sintomas               │");
        System.out.println("│  2. Gerir Funcionamento do Hospital                        │");
        System.out.println("│  3. Consultar Estatisticas e Logs                          │");
        System.out.println("│  4. Configuracoes                                          │");
        System.out.println("│  5. Sair                                                   │");
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("\nEscolha uma opcao: ");
    }

    private void mostrarNotificacoesPendentes() {
        String[] notifs = gestorNotif.obterNotificacoesPendentes();
        if (notifs.length > 0) {
            System.out.println("\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║                     NOTIFICACOES                           ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            for (String notif : notifs) {
                System.out.println("║ " + truncar(notif, 58) + " ║");
            }
            System.out.println("╚════════════════════════════════════════════════════════════╝");
            gestorNotif.limparNotificacoes();
        }
    }

    private void carregarDadosIniciais() {
        System.out.println("Carregando dados do sistema...");
        gestorNotif.adicionarLog("Sistema iniciado");
        System.out.println("Dados carregados com sucesso!\n");
        System.out.println("  - Medicos: " + gestorHospital.getNumMedicos());
        System.out.println("  - Especialidades: " + gestorHospital.getNumEspecialidades());
        System.out.println("  - Sintomas: " + gestorHospital.getNumSintomas());
    }

    private void guardarDados() {
        System.out.println("\nGuardando dados...");
        gestorHospital.guardarTudo();
        gestorNotif.adicionarLog("Dados guardados ao sair do sistema");
        System.out.println("Dados guardados com sucesso!");
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
                    System.out.print("Opcao invalida! Escolha entre " + min + " e " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Por favor, insira um numero valido: ");
            }
        }
        return opcao;
    }

    private String truncar(String texto, int tamanho) {
        if (texto == null) return "";
        if (texto.length() <= tamanho) {
            return texto;
        }
        return texto.substring(0, tamanho - 2) + "..";
    }
}
