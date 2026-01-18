package View;
import Controller.especialidadeController;
import Model.especialidadeModel;

import java.util.Scanner;

public class especialidadeView {
    private especialidadeController controller;
    private Scanner sc;

    public especialidadeView(especialidadeController controller, Scanner sc) {
        this.controller = controller;
        this.sc = sc;
    }

    public void adicionar() {
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

    public void listar() {
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

    public void editar() {
        System.out.println("\n--- EDITAR ESPECIALIDADE ---");

        System.out.print("Código da especialidade: ");
        String codigo = sc.nextLine().toUpperCase();

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

    public void remover() {
        System.out.println("\n--- REMOVER ESPECIALIDADE ---");

        System.out.print("Código da especialidade: ");
        String codigo = sc.nextLine().toUpperCase();

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