package View;

import Controller.medicoController;
import Controller.especialidadeController;
import Model.medicoModel;
import Model.especialidadeModel;

import java.util.Scanner;

public class medicoView {

    private medicoController medicoCtrl;
    private especialidadeController especialidadeCtrl;
    private Scanner sc;

    public medicoView(medicoController medicoCtrl, especialidadeController especialidadeCtrl, Scanner sc) {
        this.medicoCtrl = medicoCtrl;
        this.especialidadeCtrl = especialidadeCtrl;
        this.sc = sc;
    }

    public void adicionar() {
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
            System.out.println("✓ Médico adicionado com sucesso!");
        } else {
            System.out.println("✗ Erro: Não foi possível adicionar o médico (limite atingido).");
        }
    }

    public void listar() {
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

    public void editar() {
        System.out.println("\n--- EDITAR MÉDICO ---");

        System.out.print("Nome do médico: ");
        String nome = sc.nextLine();

        medicoModel medico = medicoCtrl.buscarMedicoPorNome(nome);

        if (medico == null) {
            System.out.println("✗ Médico não encontrado.");
            return;
        }

        System.out.println("\nDados atuais: " + medico);
        System.out.println("\n1. Editar hora de saída");
        System.out.println("2. Editar todos os dados");
        System.out.print("Escolha: ");

        int opcao = sc.nextInt();
        sc.nextLine();

        if (opcao == 1) {
            System.out.print("Nova hora de saída (0-23): ");
            int novaHora = sc.nextInt();
            sc.nextLine();

            if (medicoCtrl.atualizarHoraSaida(nome, novaHora)) {
                System.out.println("Hora de saída atualizada com sucesso!");
            } else {
                System.out.println("Erro ao atualizar.");
            }
        } else if (opcao == 2) {
            // Editar todos os dados
            System.out.print("Novo nome: ");
            String novoNome = sc.nextLine();

            // Listar especialidades
            especialidadeModel[] especialidades = especialidadeCtrl.getEspecialidades();
            System.out.println("\nEspecialidades disponíveis:");
            for (int i = 0; i < especialidades.length; i++) {
                System.out.println((i + 1) + ". " + especialidades[i]);
            }

            System.out.print("Escolha a especialidade: ");
            int escolha = sc.nextInt();
            sc.nextLine();

            especialidadeModel esp = especialidades[escolha - 1];

            System.out.print("Nova hora de entrada (0-23): ");
            int horaEntrada = sc.nextInt();

            System.out.print("Nova hora de saída (0-23): ");
            int horaSaida = sc.nextInt();

            System.out.print("Novo valor por hora (€): ");
            double valorHora = sc.nextDouble();
            sc.nextLine();

            medicoModel medicoAtualizado = new medicoModel(novoNome, esp, horaEntrada, horaSaida, valorHora);

            if (medicoCtrl.atualizarMedico(nome, medicoAtualizado)) {
                System.out.println("Médico atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar.");
            }
        }
    }

    public void remover() {
        System.out.println("\n--- REMOVER MÉDICO ---");

        System.out.print("Nome do médico: ");
        String nome = sc.nextLine();

        medicoModel medico = medicoCtrl.buscarMedicoPorNome(nome);

        if (medico == null) {
            System.out.println("Médico não encontrado.");
            return;
        }

        System.out.println("\nDados: " + medico);
        System.out.print("Tem certeza? (S/N): ");
        String confirmacao = sc.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            if (medicoCtrl.removerMedico(nome)) {
                System.out.println("Médico removido com sucesso!");
            } else {
                System.out.println("Erro ao remover.");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}