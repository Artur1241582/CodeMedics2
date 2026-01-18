package View;

import Controller.sintomasController;
import Controller.especialidadeController;
import Model.especialidadeModel;
import Model.sintomasModel;

import java.util.Scanner;

public class sintomasView {
    private sintomasController sintomasController;
    private especialidadeController especialidadeController;
    private Scanner sc;

    public sintomasView(sintomasController sintomaController, especialidadeController especialidadeController, Scanner sc) {
        this.sintomasController = sintomaController;
        this.especialidadeController = especialidadeController;
        this.sc = sc;
    }

    public void adicionar() {
        System.out.println("\n--- ADICIONAR SINTOMA ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine();

        // Validar nível de urgência
        String nivel;
        do {
            System.out.print("Nível de urgência (verde/laranja/vermelho): ");
            nivel = sc.nextLine().toLowerCase();

            if (!nivel.equals("verde") && !nivel.equals("laranja") && !nivel.equals("vermelho")) {
                System.out.println("Nível inválido! Usar: verde, laranja ou vermelho");
            }
        } while (!nivel.equals("verde") && !nivel.equals("laranja") && !nivel.equals("vermelho"));

        // Mostrar especialidades disponíveis
        especialidadeModel[] especialidades = especialidadeController.getEspecialidades();

        if (especialidades.length > 0) {
            System.out.println("\nEspecialidades disponíveis:");
            for (int i = 0; i < especialidades.length; i++) {
                System.out.println("  " + especialidades[i].getCodigo() + " - " + especialidades[i].getNome());
            }
        }

        System.out.print("Código da especialidade (ENTER para nenhuma): ");
        String cod = sc.nextLine().toUpperCase();

        especialidadeModel esp = null;
        if (!cod.isEmpty()) {
            esp = especialidadeController.procurarPorCodigo(cod);
            if (esp == null) {
                System.out.println("Aviso: Especialidade não encontrada. Sintoma será criado sem especialidade.");
            }
        }

        if (sintomasController.adicionarSintoma(nome, nivel, esp)) {
            System.out.println("Sintoma adicionado com sucesso!");
        } else {
            System.out.println("Erro: Sintoma duplicado ou lista cheia.");
        }
    }

    public void listar() {
        System.out.println("\n--- LISTA DE SINTOMAS ---");

        sintomasModel[] lista = sintomasController.getSintomas();

        if (lista.length == 0) {
            System.out.println("Nenhum sintoma registado.");
            return;
        }

        System.out.println("-------------------------------------------------------------");
        for (int i = 0; i < lista.length; i++) {
            System.out.println((i + 1) + ". " + lista[i]);
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println("Total: " + lista.length + " sintoma(s)");
    }

    public void editar() {
        System.out.println("\n--- EDITAR SINTOMA ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine();

        sintomasModel sintoma = sintomasController.procurarPorNome(nome);
        if (sintoma == null) {
            System.out.println("✗ Sintoma não encontrado.");
            return;
        }

        System.out.println("\nDados atuais: " + sintoma);
        System.out.println("\n1. Editar nível de urgência");
        System.out.println("2. Editar especialidade");
        System.out.print("Escolha: ");

        int opcao = sc.nextInt();
        sc.nextLine();

        if (opcao == 1) {
            String nivel;
            do {
                System.out.print("Novo nível (verde/laranja/vermelho): ");
                nivel = sc.nextLine().toLowerCase();

                if (!nivel.equals("verde") && !nivel.equals("laranja") && !nivel.equals("vermelho")) {
                    System.out.println("Nível inválido!");
                }
            } while (!nivel.equals("verde") && !nivel.equals("laranja") && !nivel.equals("vermelho"));

            if (sintomasController.atualizarNivelUrgencia(nome, nivel)) {
                System.out.println("Nível de urgência atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar.");
            }
        } else if (opcao == 2) {
            especialidadeModel[] especialidades = especialidadeController.getEspecialidades();

            if (especialidades.length == 0) {
                System.out.println("Nenhuma especialidade cadastrada!");
                return;
            }

            System.out.println("\nEspecialidades disponíveis:");
            for (int i = 0; i < especialidades.length; i++) {
                System.out.println("  " + especialidades[i].getCodigo() + " - " + especialidades[i].getNome());
            }

            System.out.print("Código da nova especialidade: ");
            String codigo = sc.nextLine().toUpperCase();

            especialidadeModel esp = especialidadeController.procurarPorCodigo(codigo);

            if (esp == null) {
                System.out.println("Especialidade não encontrada.");
            } else if (sintomasController.atualizarEspecialidade(nome, esp)) {
                System.out.println("Especialidade atualizada com sucesso!");
            } else {
                System.out.println("Erro ao atualizar.");
            }
        }
    }

    public void remover() {
        System.out.println("\n--- REMOVER SINTOMA ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine();

        sintomasModel sintoma = sintomasController.procurarPorNome(nome);
        if (sintoma == null) {
            System.out.println("Sintoma não encontrado.");
            return;
        }

        System.out.println("\nDados: " + sintoma);
        System.out.print("Tem certeza que deseja remover? (S/N): ");
        String confirmacao = sc.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            if (sintomasController.removerSintoma(nome)) {
                System.out.println("Sintoma removido com sucesso!");
            } else {
                System.out.println("Erro ao remover.");
            }
        } else {
            System.out.println("Operação cancelada.");
        }
    }

    public void pesquisar() {
        System.out.println("\n--- PESQUISA INOVADORA DE SINTOMAS ---");
        System.out.print("Digite parte do nome do sintoma: ");
        String busca = sc.nextLine().toLowerCase();

        sintomasModel[] todos = sintomasController.getSintomas();
        int encontrados = 0;

        System.out.println("\nResultados:");
        System.out.println("-------------------------------------------------------------");

        for (int i = 0; i < todos.length; i++) {
            if (todos[i].getNome().toLowerCase().contains(busca)) {
                System.out.println((encontrados + 1) + ". " + todos[i]);
                encontrados++;
            }
        }

        System.out.println("-------------------------------------------------------------");

        if (encontrados == 0) {
            System.out.println("Nenhum sintoma encontrado com '" + busca + "'");
        } else {
            System.out.println("Total encontrado: " + encontrados + " sintoma(s)");
        }
    }
}