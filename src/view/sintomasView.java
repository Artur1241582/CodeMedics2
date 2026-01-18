package view;

import controller.sintomasController;
import controller.especialidadeController;
import model.especialidadeModel;
import model.sintomasModel;

import java.util.Scanner;

public class sintomasView {
    private sintomasController sintomasController;
    private especialidadeController especialidadeController;
    private Scanner sc;

    public sintomasView(sintomasController sintomaController, especialidadeController especialidadeController) {
        this.sintomasController = sintomaController;
        this.especialidadeController = especialidadeController;
        sc = new Scanner(System.in);
    }

    public void menu() {
        int opcao;

        do {
            System.out.println("\n=== GESTÃO DE SINTOMAS ===");
            System.out.println("1. Adicionar sintoma");
            System.out.println("2. Listar sintomas");
            System.out.println("3. Atualizar nível de urgência");
            System.out.println("4. Atualizar especialidade");
            System.out.println("5. Remover sintoma");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");

            opcao = sc.nextInt();
            sc.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1 -> adicionar();
                case 2 -> listar();
                case 3 -> atualizarUrgencia();
                case 4 -> atualizarEspecialidade();
                case 5 -> remover();
                case 0 -> System.out.println("A voltar ao menu principal...");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    private void adicionar() {
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

    private void listar() {
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

    private void atualizarUrgencia() {
        System.out.println("\n--- ATUALIZAR NÍVEL DE URGÊNCIA ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine();

        // Verificar se existe e mostrar dados atuais
        sintomasModel sintoma = sintomasController.procurarPorNome(nome);
        if (sintoma == null) {
            System.out.println("✗ Sintoma não encontrado.");
            return;
        }

        System.out.println("Nível atual: " + sintoma.getNivelUrgencia());

        // Validar novo nível
        String nivel;
        do {
            System.out.print("Novo nível (verde/laranja/vermelho): ");
            nivel = sc.nextLine().toLowerCase();

            if (!nivel.equals("verde") && !nivel.equals("laranja") && !nivel.equals("vermelho")) {
                System.out.println("✗ Nível inválido!");
            }
        } while (!nivel.equals("verde") && !nivel.equals("laranja") && !nivel.equals("vermelho"));

        if (sintomasController.atualizarNivelUrgencia(nome, nivel)) {
            System.out.println("Nível de urgência atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar.");
        }
    }

    private void atualizarEspecialidade() {
        System.out.println("\n--- ATUALIZAR ESPECIALIDADE ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine();

        // Verificar se existe
        sintomasModel sintoma = sintomasController.procurarPorNome(nome);
        if (sintoma == null) {
            System.out.println("Sintoma não encontrado.");
            return;
        }

        especialidadeModel espAtual = sintoma.getEspecialidade();
        if (espAtual != null) {
            System.out.println("Especialidade atual: " + espAtual.getNome());
        } else {
            System.out.println("Especialidade atual: Nenhuma");
        }

        // Listar especialidades
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

    private void remover() {
        System.out.println("\n--- REMOVER SINTOMA ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine();

        // Mostrar dados antes de confirmar
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
}