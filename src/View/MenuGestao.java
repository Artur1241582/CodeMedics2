package View;

import Utils.Utils;

import java.util.Scanner;
import Model.Configuracoes;
import Controller.GestorNotificacoes;
import Controller.GestorFicheiros;


public class MenuGestao {

    public static void mostrar(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif) {
        // Cria o gestor de ficheiros usando o caminho das configuracoes
        GestorFicheiros gestorFicheiros = new GestorFicheiros(config.getCaminhoFicheiros(), config.getSeparador());

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
                    menuMedicos(scanner, config, gestorNotif, gestorFicheiros);
                    break;
                case 2:
                    menuEspecialidades(scanner, config, gestorNotif, gestorFicheiros);
                    break;
                case 3:
                    menuSintomas(scanner, config, gestorNotif, gestorFicheiros);
                    break;
                case 4:
                    voltar = true;
                    break;
            }
        }
    }

    private static void menuMedicos(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, GestorFicheiros gestorFicheiros) {
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
                    // TODO: Aluno 1 - Implementar adicionarMedico()
                    System.out.println("\n[FUNCIONALIDADE] Adicionar Médico");
                    gestorNotif.adicionarLog("Tentativa de adicionar médico");
                    Utils.pausar(scanner);
                    break;
                case 2:
                    // Listar médicos do ficheiro
                    mostrarCabecalho(config);
                    int numMedicos = gestorFicheiros.listarMedicos();
                    gestorNotif.adicionarLog("Listados " + numMedicos + " médicos");
                    Utils.pausar(scanner);
                    break;
                case 3:
                    // TODO: Aluno 1 - Implementar editarMedico()
                    System.out.println("\n[FUNCIONALIDADE] Editar Médico");
                    gestorNotif.adicionarLog("Tentativa de editar médico");
                    Utils.pausar(scanner);
                    break;
                case 4:
                    // TODO: Aluno 1 - Implementar removerMedico()
                    System.out.println("\n[FUNCIONALIDADE] Remover Médico");
                    gestorNotif.adicionarLog("Tentativa de remover médico");
                    Utils.pausar(scanner);
                    break;
                case 5:
                    voltar = true;
                    break;
            }
        }
    }

    private static void menuEspecialidades(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, GestorFicheiros gestorFicheiros) {
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
                    // TODO: Aluno 1 - Implementar adicionarEspecialidade()
                    System.out.println("\n[FUNCIONALIDADE] Adicionar Especialidade");
                    gestorNotif.adicionarLog("Tentativa de adicionar especialidade");
                    Utils.pausar(scanner);
                    break;
                case 2:
                    // Listar especialidades do ficheiro
                    mostrarCabecalho(config);
                    int numEspecialidades = gestorFicheiros.listarEspecialidades();
                    gestorNotif.adicionarLog("Listadas " + numEspecialidades + " especialidades");
                    Utils.pausar(scanner);
                    break;
                case 3:
                    // TODO: Aluno 1 - Implementar editarEspecialidade()
                    System.out.println("\n[FUNCIONALIDADE] Editar Especialidade");
                    gestorNotif.adicionarLog("Tentativa de editar especialidade");
                    Utils.pausar(scanner);
                    break;
                case 4:
                    // TODO: Aluno 1 - Implementar removerEspecialidade()
                    System.out.println("\n[FUNCIONALIDADE] Remover Especialidade");
                    gestorNotif.adicionarLog("Tentativa de remover especialidade");
                    Utils.pausar(scanner);
                    break;
                case 5:
                    voltar = true;
                    break;
            }
        }
    }

    private static void menuSintomas(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif, GestorFicheiros gestorFicheiros) {
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
                    // TODO: Aluno 1 - Implementar adicionarSintoma()
                    System.out.println("\n[FUNCIONALIDADE] Adicionar Sintoma");
                    gestorNotif.adicionarLog("Tentativa de adicionar sintoma");
                    Utils.pausar(scanner);
                    break;
                case 2:
                    // Listar sintomas do ficheiro
                    mostrarCabecalho(config);
                    int numSintomas = gestorFicheiros.listarSintomas();
                    gestorNotif.adicionarLog("Listados " + numSintomas + " sintomas");
                    Utils.pausar(scanner);
                    break;
                case 3:
                    // TODO: Aluno 1 - Implementar editarSintoma()
                    System.out.println("\n[FUNCIONALIDADE] Editar Sintoma");
                    gestorNotif.adicionarLog("Tentativa de editar sintoma");
                    Utils.pausar(scanner);
                    break;
                case 4:
                    // TODO: Aluno 1 - Implementar removerSintoma()
                    System.out.println("\n[FUNCIONALIDADE] Remover Sintoma");
                    gestorNotif.adicionarLog("Tentativa de remover sintoma");
                    Utils.pausar(scanner);
                    break;
                case 5:
                    // TODO: Aluno 1 - Implementar pesquisaSintomaInovadora()
                    System.out.println("\n[FUNCIONALIDADE] Pesquisa Inovadora de Sintomas");
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