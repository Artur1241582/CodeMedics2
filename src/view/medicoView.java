package view;

import controller.medicoController;
import controller.especialidadeController;
import model.medicoModel;
import model.especialidadeModel;

import java.util.Scanner;

public class medicoView {

    private medicoController medicoCtrl;
    private especialidadeController especialidadeCtrl;
    private Scanner sc;

    public medicoView(medicoController medicoCtrl, especialidadeController especialidadeCtrl) {
        this.medicoCtrl = medicoCtrl;
        this.especialidadeCtrl = especialidadeCtrl;
        sc = new Scanner(System.in);
    }

    public void menu() {
        int opcao;
        do {
            System.out.println("\n=== GESTÃO DE MÉDICOS ===");
            System.out.println("1. Adicionar médico");
            System.out.println("2. Listar médicos");
            System.out.println("3. Atualizar hora de saída");
            System.out.println("4. Remover médico");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1 -> adicionar();
                case 2 -> listar();
                case 3 -> atualizarHoraSaida();
                case 4 -> remover();
                case 0 -> System.out.println("A voltar ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private void adicionar() {
        System.out.println("\n--- ADICIONAR MÉDICO ---");

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        // Listar especialidades disponíveis
        especialidadeModel[] especialidades = especialidadeCtrl.getEspecialidades();

        if (especialidades.length == 0) {
            System.out.println("Erro: Nenhuma especialidade cadastrada!");
            return;
        }

        System.out.println("\nEspecialidades disponíveis:");
        for (int i = 0; i < especialidades.length; i++) {
            System.out.println((i + 1) + ". " + especialidades[i]);
        }

        System.out.print("Escolha a especialidade (número): ");
        int escolha = sc.nextInt();
        sc.nextLine();

        if (escolha < 1 || escolha > especialidades.length) {
            System.out.println("Especialidade inválida!");
            return;
        }

        especialidadeModel esp = especialidades[escolha - 1];

        System.out.print("Hora de entrada (0-23): ");
        int horaEntrada = sc.nextInt();

        System.out.print("Hora de saída (0-23): ");
        int horaSaida = sc.nextInt();

        System.out.print("Valor por hora (€): ");
        double valorHora = sc.nextDouble();
        sc.nextLine();

        medicoModel m = new medicoModel(nome, esp, horaEntrada, horaSaida, valorHora);

        if (medicoCtrl.adicionarMedico(m)) {
            System.out.println("Médico adicionado com sucesso!");
        } else {
            System.out.println("Erro: Não foi possível adicionar o médico (limite atingido).");
        }
    }

    private void listar() {
        System.out.println("\n--- LISTA DE MÉDICOS ---");

        medicoModel[] medicos = medicoCtrl.listarMedicos();

        if (medicos.length == 0) {
            System.out.println("Nenhum médico cadastrado.");
            return;
        }

        for (int i = 0; i < medicos.length; i++) {
            System.out.println((i + 1) + ". " + medicos[i]);
        }

        System.out.println("\nTotal: " + medicos.length + " médico(s)");
    }

    private void atualizarHoraSaida() {
        System.out.println("\n--- ATUALIZAR HORA DE SAÍDA ---");

        System.out.print("Nome do médico: ");
        String nome = sc.nextLine();

        System.out.print("Nova hora de saída (0-23): ");
        int novaHora = sc.nextInt();
        sc.nextLine();

        if (medicoCtrl.atualizarHoraSaida(nome, novaHora)) {
            System.out.println("Hora de saída atualizada com sucesso!");
        } else {
            System.out.println("Erro: Médico não encontrado.");
        }
    }

    private void remover() {
        System.out.println("\n--- REMOVER MÉDICO ---");

        System.out.print("Nome do médico: ");
        String nome = sc.nextLine();

        System.out.print("Tem certeza? (S/N): ");
        String confirmacao = sc.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            if (medicoCtrl.removerMedico(nome)) {
                System.out.println("Médico removido com sucesso!");
            } else {
                System.out.println("Erro: Médico não encontrado.");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}