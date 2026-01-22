package View;

import Controller.GestorNotificacoes;
import Model.Configuracoes;
import Utils.Utils;
import java.util.Scanner;


public class MenuConfiguracoes {

    public static void mostrar(Scanner scanner, Configuracoes config, GestorNotificacoes gestorNotif) {
        System.out.println("\n--- [ ACESSO RESTRITO ] ---");
        System.out.print("Introduza a password de administrador: ");
        String tentativa = scanner.nextLine();

        // O model já carregou a password do ficheiro C:\Users\Lenovo\... no construtor
        if (!config.verificarPassword(tentativa)) {
            System.out.println("Acesso Negado! Password incorreta.");
            Utils.pausar(scanner);
            return;
        }

        boolean voltar = false;
        while (!voltar) {
            mostrarSubMenu();
            int opcao = Utils.lerOpcao(scanner, 1, 6);

            switch (opcao) {
                case 1:
                    alterarCaminhoESeparador(scanner, config);
                    break;
                case 2:
                    alterarTemposConsulta(scanner, config);
                    break;
                case 3:
                    alterarRegrasUrgencia(scanner, config);
                    break;
                case 4:
                    alterarRegrasTrabalho(scanner, config);
                    break;
                case 5:
                    alterarPassword(scanner, config);
                    break;
                case 6:
                    voltar = true;
                    break;
            }
        }
    }

    private static void mostrarSubMenu() {
        System.out.println("\n┌────────────────────────────────────────────────────────────┐");
        System.out.println("│                  ÁREA DE CONFIGURAÇÕES                     │");
        System.out.println("├────────────────────────────────────────────────────────────┤");
        System.out.println("│  1. Caminho de Ficheiros e Separador                       │");
        System.out.println("│  2. Tempos de Consulta (Baixa/Média/Urgente)               │");
        System.out.println("│  3. Elevação de Nível de Urgência                          │");
        System.out.println("│  4. Regras de Trabalho e Descanso                          │");
        System.out.println("│  5. Alterar Password de Acesso                             │");
        System.out.println("│  6. Voltar ao Menu Principal                               │");
        System.out.println("└────────────────────────────────────────────────────────────┘");
        System.out.print("Escolha uma opção: ");
    }

    private static void alterarCaminhoESeparador(Scanner scanner, Configuracoes config) {
        System.out.println("\n--- Configuração de Ficheiros ---");
        System.out.print("Caminho atual [" + config.getCaminhoFicheiros() + "]: ");
        String novoCaminho = scanner.nextLine();
        if (!novoCaminho.isEmpty()) config.setCaminhoFicheiros(novoCaminho);

        System.out.print("Separador atual [" + config.getSeparador() + "]: ");
        String novoSep = scanner.nextLine();
        if (!novoSep.isEmpty()) config.setSeparador(novoSep);

        System.out.println("Configurações de ficheiros atualizadas.");
    }

    private static void alterarTemposConsulta(Scanner scanner, Configuracoes config) {
        System.out.println("\n--- Tempos de Consulta (Unidades) ---");
        try {
            System.out.print("Baixa Urgência [" + config.getTempoConsultaBaixa() + "]: ");
            config.setTempoConsultaBaixa(Integer.parseInt(scanner.nextLine()));

            System.out.print("Média Urgência [" + config.getTempoConsultaMedia() + "]: ");
            config.setTempoConsultaMedia(Integer.parseInt(scanner.nextLine()));

            System.out.print("Urgente [" + config.getTempoConsultaUrgente() + "]: ");
            config.setTempoConsultaUrgente(Integer.parseInt(scanner.nextLine()));

            System.out.println("Tempos de consulta atualizados com sucesso.");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Por favor, insira apenas números inteiros.");
        }
    }

    private static void alterarRegrasUrgencia(Scanner scanner, Configuracoes config) {
        System.out.println("\n--- Elevação de Nível (Tempo de Espera) ---");
        try {
            System.out.print("Baixa para Média [" + config.getElevacaoBaixaMedia() + "]: ");
            config.setElevacaoBaixaMedia(Integer.parseInt(scanner.nextLine()));

            System.out.print("Média para Urgente [" + config.getElevacaoMediaUrgente() + "]: ");
            config.setElevacaoMediaUrgente(Integer.parseInt(scanner.nextLine()));

            System.out.print("Urgente para Saída [" + config.getElevacaoUrgenteSaida() + "]: ");
            config.setElevacaoUrgenteSaida(Integer.parseInt(scanner.nextLine()));

            System.out.println("Regras de elevação atualizadas.");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
        }
    }

    private static void alterarRegrasTrabalho(Scanner scanner, Configuracoes config) {
        System.out.println("\n--- Trabalho e Descanso ---");
        try {
            System.out.print("Horas seguidas de trabalho [" + config.getHorasTrabalho() + "]: ");
            config.setHorasTrabalho(Integer.parseInt(scanner.nextLine()));

            System.out.print("Unidades de descanso necessárias [" + config.getTempoDescanso() + "]: ");
            config.setTempoDescanso(Integer.parseInt(scanner.nextLine()));

            System.out.println("Regras de trabalho atualizadas.");
        } catch (NumberFormatException e) {
            System.out.println("Erro: Valor numérico inválido.");
        }
    }

    private static void alterarPassword(Scanner scanner, Configuracoes config) {
        System.out.println("\n--- Alterar Password de Administrador ---");
        System.out.print("Nova Password: ");
        String pw1 = scanner.nextLine();
        System.out.print("Confirme a Nova Password: ");
        String pw2 = scanner.nextLine();

        if (pw1.equals(pw2) && !pw1.trim().isEmpty()) {
            // Este método no Model já grava automaticamente no ficheiro TXT
            config.setPassword(pw1);
            System.out.println("Password alterada e persistida no sistema!");
        } else if (pw1.isEmpty()) {
            System.out.println("A password não pode ser vazia.");
        } else {
            System.out.println("As passwords não coincidem. Operação cancelada.");
        }
        Utils.pausar(scanner);
    }
}