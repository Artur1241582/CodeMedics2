package View;

import Utils.Utils;

import java.util.Scanner;
import Model.Configuracoes;
import Controller.GestorNotificacoes;
import Controller.GestorFicheiros;

import Controller.medicoController;
import Controller.especialidadeController;
import Controller.sintomasController;
import View.medicoView;
import View.especialidadeView;
import View.sintomasView;


public class MenuGestao {

    public static void mostrar(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif) {
        // Cria o gestor de ficheiros usando o caminho das configuracoes
        GestorFicheiros gestorFicheiros = new GestorFicheiros(config.getCaminhoFicheiros(), config.getSeparador());

        // CRIAR OS CONTROLLERS
        especialidadeController especialidadeCtrl = new especialidadeController(100);
        medicoController medicoCtrl = new medicoController(100);
        sintomasController sintomaCtrl = new sintomasController(100);

        // CRIAR AS VIEWS
        medicoView medicoV = new medicoView(medicoCtrl, especialidadeCtrl, scanner);
        especialidadeView especialidadeV = new especialidadeView(especialidadeCtrl, scanner);
        sintomasView sintomaV = new sintomasView(sintomaCtrl, especialidadeCtrl, scanner);

        boolean voltar = false;

        while (!voltar) {

            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│        GESTÃO DE MÉDICOS, ESPECIALIDADES E SINTOMAS        │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Gestão de Médicos                                      │");
            System.out.println("│  2. Gestão de Especialidades                               │");
            System.out.println("│  3. Gestão de Sintomas                                     │");
            System.out.println("│  4. Voltar ao Menu Principal                               │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opção: ");

            int opcao = Utils.lerOpcao(scanner, 1, 4);

            switch (opcao) {
                case 1:
                    menuMedicos(scanner, config, gestorNotif, gestorFicheiros, medicoV);
                    break;
                case 2:
                    menuEspecialidades(scanner, config, gestorNotif, gestorFicheiros, especialidadeV);
                    break;
                case 3:
                    menuSintomas(scanner, config, gestorNotif, gestorFicheiros, sintomaV);
                    break;
                case 4:
                    voltar = true;
                    break;
            }
        }
    }

    private static void menuMedicos(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, GestorFicheiros gestorFicheiros, medicoView medicoV) {
        boolean voltar = false;

        while (!voltar) {


            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│                   GESTÃO DE MÉDICOS                        │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Adicionar Médico                                       │");
            System.out.println("│  2. Listar Médicos                                         │");
            System.out.println("│  3. Editar Médico                                          │");
            System.out.println("│  4. Remover Médico                                         │");
            System.out.println("│  5. Voltar                                                 │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opção: ");

            int opcao = Utils.lerOpcao(scanner, 1, 5);

            switch (opcao) {
                case 1:
                    medicoV.adicionar();
                    gestorNotif.adicionarLog("Médico adicionado");
                    Utils.pausar(scanner);
                    break;
                case 2:
                    mostrarCabecalho(config);
                    medicoV.listar();
                    gestorNotif.adicionarLog("Médicos listados");
                    Utils.pausar(scanner);
                    break;
                case 3:
                    medicoV.editar();
                    gestorNotif.adicionarLog("Médico editado");
                    Utils.pausar(scanner);
                    break;
                case 4:
                    medicoV.remover();
                    gestorNotif.adicionarLog("Médico removido");
                    Utils.pausar(scanner);
                    break;
                case 5:
                    voltar = true;
                    break;
            }
        }
    }

    private static void menuEspecialidades(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, GestorFicheiros gestorFicheiros, especialidadeView especialidadeV) {
        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│               GESTÃO DE ESPECIALIDADES                     │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Adicionar Especialidade                                │");
            System.out.println("│  2. Listar Especialidades                                  │");
            System.out.println("│  3. Editar Especialidade                                   │");
            System.out.println("│  4. Remover Especialidade                                  │");
            System.out.println("│  5. Voltar                                                 │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opção: ");

            int opcao = Utils.lerOpcao(scanner, 1, 5);

            switch (opcao) {
                case 1:
                    especialidadeV.adicionar();
                    gestorNotif.adicionarLog("Especialidade adicionada");
                    Utils.pausar(scanner);
                    break;
                case 2:
                    mostrarCabecalho(config);
                    especialidadeV.listar();
                    gestorNotif.adicionarLog("Especialidades listadas");
                    Utils.pausar(scanner);
                    break;
                case 3:
                    especialidadeV.editar();
                    gestorNotif.adicionarLog("Especialidade editada");
                    Utils.pausar(scanner);
                    break;
                case 4:
                    especialidadeV.remover();
                    gestorNotif.adicionarLog("Especialidade removida");
                    Utils.pausar(scanner);
                    break;
                case 5:
                    voltar = true;
                    break;
            }
        }
    }

    private static void menuSintomas(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, GestorFicheiros gestorFicheiros, sintomasView sintomaV) {
        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│                   GESTÃO DE SINTOMAS                       │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Adicionar Sintoma                                      │");
            System.out.println("│  2. Listar Sintomas                                        │");
            System.out.println("│  3. Editar Sintoma                                         │");
            System.out.println("│  4. Remover Sintoma                                        │");
            System.out.println("│  5. Pesquisar Sintoma (Sistema Inovador)                   │");
            System.out.println("│  6. Voltar                                                 │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opção: ");

            int opcao = Utils.lerOpcao(scanner, 1, 6);

            switch (opcao) {
                case 1:
                    sintomaV.adicionar();
                    gestorNotif.adicionarLog("Sintoma adicionado");
                    Utils.pausar(scanner);
                    break;
                case 2:
                    mostrarCabecalho(config);
                    sintomaV.listar();
                    gestorNotif.adicionarLog("Sintomas listados");
                    Utils.pausar(scanner);
                    break;
                case 3:
                    sintomaV.editar();
                    gestorNotif.adicionarLog("Sintoma editado");
                    Utils.pausar(scanner);
                    break;
                case 4:
                    sintomaV.remover();
                    gestorNotif.adicionarLog("Sintoma removido");
                    Utils.pausar(scanner);
                    break;
                case 5:
                    sintomaV.pesquisar();
                    Utils.pausar(scanner);
                    break;
                case 6:
                    voltar = true;
                    break;
            }
        }
    }

    private static void mostrarCabecalho(Configuracoes config) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE GESTÃO - HOSPITAL DE URGÊNCIAS             ║");
        System.out.println("║                                                            ║");
        System.out.println("║     Dia: " + Utils.formatarNumero(config.getDiaAtual(), 2) +
                "  |  Unidade de Tempo: " + Utils.formatarNumero(config.getUnidadeTempoAtual(), 2) +
                "/24                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}