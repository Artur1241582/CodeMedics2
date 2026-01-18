package view;
import controller.especialidadeController;
import model.especialidadeModel;

import java.util.Scanner;

public class especialidadeView {
    private especialidadeController controller;
    private Scanner sc;

    public especialidadeView(especialidadeController controller) {
        this.controller = controller;
        sc = new Scanner(System.in);
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== GESTÃO DE ESPECIALIDADES ===");
            System.out.println("1. Adicionar especialidade");
            System.out.println("2. Listar especialidades");
            System.out.println("3. Atualizar especialidade");
            System.out.println("4. Remover especialidade");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1 -> adicionar();
                case 2 -> listar();
                case 3 -> atualizar();
                case 4 -> remover();
                case 0 -> System.out.println("A voltar ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    private void adicionar() {
        System.out.println("\n--- ADICIONAR ESPECIALIDADE ---");

        System.out.print("Código: ");
        String codigo = sc.nextLine().toUpperCase();

        System.out.print("Nome: ");
        String nome = sc.nextLine();

        if (controller.adicionarEspecialidade(codigo, nome)) {
            System.out.println("Especialidade adicionada com sucesso!");
        } else {
            System.out.println("Erro: Código duplicado ou lista cheia.");
        }
    }

    private void listar() {
        System.out.println("\n--- LISTA DE ESPECIALIDADES ---");

        especialidadeModel[] lista = controller.getEspecialidades();

        if (lista.length == 0) {
            System.out.println("Nenhuma especialidade registada.");
            return;
        }

        System.out.println("------------------------------------------------");
        for (int i = 0; i < lista.length; i++) {
            System.out.println((i + 1) + ". " + lista[i]);
        }
        System.out.println("------------------------------------------------");
        System.out.println("Total: " + lista.length + " especialidade(s)");
    }

    private void atualizar() {
        System.out.println("\n--- ATUALIZAR ESPECIALIDADE ---");

        System.out.print("Código da especialidade: ");
        String codigo = sc.nextLine().toUpperCase();

        // Verificar se existe antes de pedir novo nome
        especialidadeModel esp = controller.procurarPorCodigo(codigo);
        if (esp == null) {
            System.out.println("Especialidade não encontrada.");
            return;
        }

        System.out.println("Nome atual: " + esp.getNome());
        System.out.print("Novo nome: ");
        String novoNome = sc.nextLine();

        if (controller.atualizarNome(codigo, novoNome)) {
            System.out.println("Especialidade atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar.");
        }
    }

    private void remover() {
        System.out.println("\n--- REMOVER ESPECIALIDADE ---");

        System.out.print("Código da especialidade: ");
        String codigo = sc.nextLine().toUpperCase();

        // Mostrar dados antes de confirmar
        especialidadeModel esp = controller.procurarPorCodigo(codigo);
        if (esp == null) {
            System.out.println("Especialidade não encontrada.");
            return;
        }

        System.out.println("\nDados: " + esp);
        System.out.print("Tem certeza que deseja remover? (S/N): ");
        String confirmacao = sc.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            if (controller.removerEspecialidade(codigo)) {
                System.out.println("Especialidade removida com sucesso!");
            } else {
                System.out.println("Erro ao remover.");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }
}