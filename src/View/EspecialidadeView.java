package View;

import Controller.EspecialidadeController;
import Model.Especialidade;

import java.util.Scanner;

/**
 * View para gestao de especialidades
 * Usa o modelo unificado Especialidade
 */
public class EspecialidadeView {
    private EspecialidadeController controller;
    private Scanner sc;

    public EspecialidadeView(EspecialidadeController controller, Scanner sc) {
        this.controller = controller;
        this.sc = sc;
    }

    public void adicionar() {
        System.out.println("\n--- ADICIONAR ESPECIALIDADE ---");

        System.out.print("Codigo (ex: CARD, PEDI, ORTO): ");
        String codigo = sc.nextLine().trim().toUpperCase();

        if (codigo.isEmpty()) {
            System.out.println("Erro: Codigo nao pode estar vazio!");
            return;
        }

        System.out.print("Nome da especialidade: ");
        String nome = sc.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println("Erro: Nome nao pode estar vazio!");
            return;
        }

        if (controller.adicionarEspecialidade(codigo, nome)) {
            System.out.println("Especialidade adicionada com sucesso!");
        } else {
            System.out.println("Erro: Codigo duplicado ou lista cheia.");
        }
    }

    public void listar() {
        System.out.println("\n--- LISTA DE ESPECIALIDADES ---");

        Especialidade[] lista = controller.getEspecialidades();

        if (lista.length == 0) {
            System.out.println("Nenhuma especialidade registada.");
            return;
        }

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ #  | Codigo    | Nome                                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        for (int i = 0; i < lista.length; i++) {
            System.out.printf("║ %2d | %-9s | %-41s ║%n",
                    (i + 1),
                    lista[i].getCodigo(),
                    truncar(lista[i].getNome(), 41));
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("Total: " + lista.length + " especialidade(s)");
    }

    public void editar() {
        System.out.println("\n--- EDITAR ESPECIALIDADE ---");

        System.out.print("Codigo da especialidade: ");
        String codigo = sc.nextLine().trim().toUpperCase();

        Especialidade esp = controller.procurarPorCodigo(codigo);
        if (esp == null) {
            System.out.println("Especialidade nao encontrada.");
            return;
        }

        System.out.println("\nDados atuais:");
        System.out.println("  Codigo: " + esp.getCodigo());
        System.out.println("  Nome: " + esp.getNome());

        System.out.print("\nNovo nome (ENTER para manter): ");
        String novoNome = sc.nextLine().trim();

        if (novoNome.isEmpty()) {
            System.out.println("Nome mantido.");
            return;
        }

        if (controller.atualizarNome(codigo, novoNome)) {
            System.out.println("Especialidade atualizada com sucesso!");
        } else {
            System.out.println("Erro ao atualizar.");
        }
    }

    public void remover() {
        System.out.println("\n--- REMOVER ESPECIALIDADE ---");

        System.out.print("Codigo da especialidade: ");
        String codigo = sc.nextLine().trim().toUpperCase();

        Especialidade esp = controller.procurarPorCodigo(codigo);
        if (esp == null) {
            System.out.println("Especialidade nao encontrada.");
            return;
        }

        if (controller.existemMedicosAssociados(codigo)) {
            System.out.println("\nERRO: Nao e possivel remover esta especialidade!");
            System.out.println("Existem medicos associados a ela.");
            System.out.println("Remova ou altere os medicos primeiro.");
            return;
        }

        System.out.println("\nDados da especialidade:");
        System.out.println("  Codigo: " + esp.getCodigo());
        System.out.println("  Nome: " + esp.getNome());

        System.out.print("\nTem certeza que deseja remover? (S/N): ");
        String confirmacao = sc.nextLine().trim();

        if (confirmacao.equalsIgnoreCase("S")) {
            if (controller.removerEspecialidade(codigo)) {
                System.out.println("Especialidade removida com sucesso!");
            } else {
                System.out.println("Erro ao remover.");
            }
        } else {
            System.out.println("Operacao cancelada.");
        }
    }

    private String truncar(String texto, int tamanho) {
        if (texto == null) return "";
        if (texto.length() <= tamanho) {
            return texto;
        }
        return texto.substring(0, tamanho - 2) + "..";
    }
}
