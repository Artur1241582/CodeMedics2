package View;

import Utils.Utils;

import java.util.Scanner;
import Model.Configuracoes;
import Controller.GestorNotificacoes;
import Controller.GestorHospital;

import Controller.MedicoController;
import Controller.EspecialidadeController;
import Controller.SintomasController;

/**
 * Menu de gestao de medicos, especialidades e sintomas
 * Recebe o GestorHospital centralizado e conecta os controllers a ele
 */
public class MenuGestao {

    /**
     * Mostra o menu de gestao
     * @param scanner Scanner para input
     * @param config Configuracoes do sistema
     * @param gestorNotif Gestor de notificacoes
     * @param gestorHospital GestorHospital centralizado (compartilhado)
     */
    public static void mostrar(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, GestorHospital gestorHospital) {
        // CRIAR OS CONTROLLERS conectados ao GestorHospital
        EspecialidadeController especialidadeCtrl = new EspecialidadeController(100);
        especialidadeCtrl.setGestorHospital(gestorHospital);
        especialidadeCtrl.sincronizarComGestor();

        MedicoController medicoCtrl = new MedicoController(100);
        medicoCtrl.setGestorHospital(gestorHospital);

        SintomasController sintomaCtrl = new SintomasController(200);
        sintomaCtrl.setGestorHospital(gestorHospital);
        sintomaCtrl.sincronizarComGestor();

        // CRIAR AS VIEWS
        MedicoView medicoV = new MedicoView(medicoCtrl, especialidadeCtrl, scanner);
        EspecialidadeView especialidadeV = new EspecialidadeView(especialidadeCtrl, scanner);
        SintomasView sintomaV = new SintomasView(sintomaCtrl, especialidadeCtrl, scanner);

        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│        GESTAO DE MEDICOS, ESPECIALIDADES E SINTOMAS        │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Gestao de Medicos                                      │");
            System.out.println("│  2. Gestao de Especialidades                               │");
            System.out.println("│  3. Gestao de Sintomas                                     │");
            System.out.println("│  4. Voltar ao Menu Principal                               │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opcao: ");

            int opcao = Utils.lerOpcao(scanner, 1, 4);

            switch (opcao) {
                case 1:
                    menuMedicos(scanner, config, gestorNotif, medicoV);
                    break;
                case 2:
                    menuEspecialidades(scanner, config, gestorNotif, especialidadeV);
                    break;
                case 3:
                    menuSintomas(scanner, config, gestorNotif, sintomaV);
                    break;
                case 4:
                    voltar = true;
                    break;
            }
        }
    }

    private static void menuMedicos(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, MedicoView medicoV) {
        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│                   GESTAO DE MEDICOS                        │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Adicionar Medico                                       │");
            System.out.println("│  2. Listar Medicos                                         │");
            System.out.println("│  3. Editar Medico                                          │");
            System.out.println("│  4. Remover Medico                                         │");
            System.out.println("│  5. Voltar                                                 │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opcao: ");

            int opcao = Utils.lerOpcao(scanner, 1, 5);

            switch (opcao) {
                case 1:
                    medicoV.adicionar();
                    gestorNotif.adicionarLog("Medico adicionado");
                    Utils.pausar(scanner);
                    break;
                case 2:
                    mostrarCabecalho(config);
                    medicoV.listar();
                    gestorNotif.adicionarLog("Medicos listados");
                    Utils.pausar(scanner);
                    break;
                case 3:
                    medicoV.editar();
                    gestorNotif.adicionarLog("Medico editado");
                    Utils.pausar(scanner);
                    break;
                case 4:
                    medicoV.remover();
                    gestorNotif.adicionarLog("Medico removido");
                    Utils.pausar(scanner);
                    break;
                case 5:
                    voltar = true;
                    break;
            }
        }
    }

    private static void menuEspecialidades(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, EspecialidadeView especialidadeV) {
        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│               GESTAO DE ESPECIALIDADES                     │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Adicionar Especialidade                                │");
            System.out.println("│  2. Listar Especialidades                                  │");
            System.out.println("│  3. Editar Especialidade                                   │");
            System.out.println("│  4. Remover Especialidade                                  │");
            System.out.println("│  5. Voltar                                                 │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opcao: ");

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

    private static void menuSintomas(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, SintomasView sintomaV) {
        boolean voltar = false;

        while (!voltar) {
            mostrarCabecalho(config);
            System.out.println("\n┌────────────────────────────────────────────────────────────┐");
            System.out.println("│                   GESTAO DE SINTOMAS                       │");
            System.out.println("├────────────────────────────────────────────────────────────┤");
            System.out.println("│  1. Adicionar Sintoma                                      │");
            System.out.println("│  2. Listar Sintomas                                        │");
            System.out.println("│  3. Editar Sintoma                                         │");
            System.out.println("│  4. Remover Sintoma                                        │");
            System.out.println("│  5. Pesquisar Sintoma                                      │");
            System.out.println("│  6. Voltar                                                 │");
            System.out.println("└────────────────────────────────────────────────────────────┘");
            System.out.print("\nEscolha uma opcao: ");

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
        System.out.println("║     SISTEMA DE GESTAO - HOSPITAL DE URGENCIAS              ║");
        System.out.println("║                                                            ║");
        System.out.println("║     Dia: " + Utils.formatarNumero(config.getDiaAtual(), 2) +
                "  |  Unidade de Tempo: " + Utils.formatarNumero(config.getUnidadeTempoAtual(), 2) +
                "/24                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}
