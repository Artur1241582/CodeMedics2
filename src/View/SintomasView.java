package View;

import Controller.SintomasController;
import Controller.EspecialidadeController;
import Model.Especialidade;
import Model.Sintoma;

import java.util.Scanner;

/**
 * View para gestao de sintomas
 * Usa os modelos unificados Sintoma e Especialidade
 */
public class SintomasView {
    private SintomasController sintomasController;
    private EspecialidadeController especialidadeController;
    private Scanner sc;

    public SintomasView(SintomasController sintomaController, EspecialidadeController especialidadeController, Scanner sc) {
        this.sintomasController = sintomaController;
        this.especialidadeController = especialidadeController;
        this.sc = sc;
    }

    public void adicionar() {
        System.out.println("\n--- ADICIONAR SINTOMA ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println("Erro: Nome nao pode estar vazio!");
            return;
        }

        String nivel;
        do {
            System.out.print("Nivel de urgencia (verde/laranja/vermelho): ");
            nivel = sc.nextLine().trim().toLowerCase();

            if (!Sintoma.isNivelValido(nivel)) {
                System.out.println("Nivel invalido! Usar: verde, laranja ou vermelho");
            }
        } while (!Sintoma.isNivelValido(nivel));

        Especialidade[] especialidades = especialidadeController.getEspecialidades();

        if (especialidades.length > 0) {
            System.out.println("\nEspecialidades disponiveis:");
            for (int i = 0; i < especialidades.length; i++) {
                System.out.println("  " + (i + 1) + ". " + especialidades[i].getCodigo() + " - " + especialidades[i].getNome());
            }
        }

        System.out.print("Codigo da especialidade (ENTER para nenhuma): ");
        String cod = sc.nextLine().trim().toUpperCase();

        Especialidade esp = null;
        if (!cod.isEmpty()) {
            esp = especialidadeController.procurarPorCodigo(cod);
            if (esp == null) {
                System.out.println("Aviso: Especialidade nao encontrada. Sintoma sera criado sem especialidade.");
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

        Sintoma[] lista = sintomasController.getSintomas();

        if (lista.length == 0) {
            System.out.println("Nenhum sintoma registado.");
            return;
        }

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ #  | Nome                    | Urgencia  | Especialidade   ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        for (int i = 0; i < lista.length; i++) {
            String esp = lista[i].getCodigoEspecialidade();
            if (esp == null || esp.isEmpty()) {
                esp = "N/A";
            }
            System.out.printf("║ %2d | %-23s | %-9s | %-15s ║%n",
                    (i + 1),
                    truncar(lista[i].getNome(), 23),
                    lista[i].getNivelUrgencia(),
                    truncar(esp, 15));
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("Total: " + lista.length + " sintoma(s)");
    }

    public void editar() {
        System.out.println("\n--- EDITAR SINTOMA ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine().trim();

        Sintoma sintoma = sintomasController.procurarPorNome(nome);
        if (sintoma == null) {
            System.out.println("Sintoma nao encontrado.");
            return;
        }

        String esp = sintoma.getCodigoEspecialidade();
        if (esp == null || esp.isEmpty()) {
            esp = "N/A";
        }

        System.out.println("\nDados atuais:");
        System.out.println("  Nome: " + sintoma.getNome());
        System.out.println("  Nivel de urgencia: " + sintoma.getNivelUrgencia());
        System.out.println("  Especialidade: " + esp);

        System.out.println("\n1. Editar nivel de urgencia");
        System.out.println("2. Editar especialidade");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();

        if (opcao == 1) {
            String nivel;
            do {
                System.out.print("Novo nivel (verde/laranja/vermelho): ");
                nivel = sc.nextLine().trim().toLowerCase();

                if (!Sintoma.isNivelValido(nivel)) {
                    System.out.println("Nivel invalido!");
                }
            } while (!Sintoma.isNivelValido(nivel));

            if (sintomasController.atualizarNivelUrgencia(nome, nivel)) {
                System.out.println("Nivel de urgencia atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar.");
            }
        } else if (opcao == 2) {
            Especialidade[] especialidades = especialidadeController.getEspecialidades();

            if (especialidades.length == 0) {
                System.out.println("Nenhuma especialidade cadastrada!");
                return;
            }

            System.out.println("\nEspecialidades disponiveis:");
            for (int i = 0; i < especialidades.length; i++) {
                System.out.println("  " + (i + 1) + ". " + especialidades[i].getCodigo() + " - " + especialidades[i].getNome());
            }

            System.out.print("Codigo da nova especialidade (ENTER para remover): ");
            String codigo = sc.nextLine().trim().toUpperCase();

            if (codigo.isEmpty()) {
                if (sintomasController.atualizarEspecialidade(nome, (String) null)) {
                    System.out.println("Especialidade removida do sintoma!");
                } else {
                    System.out.println("Erro ao atualizar.");
                }
            } else {
                Especialidade novaEsp = especialidadeController.procurarPorCodigo(codigo);

                if (novaEsp == null) {
                    System.out.println("Especialidade nao encontrada.");
                } else if (sintomasController.atualizarEspecialidade(nome, novaEsp)) {
                    System.out.println("Especialidade atualizada com sucesso!");
                } else {
                    System.out.println("Erro ao atualizar.");
                }
            }
        }
    }

    public void remover() {
        System.out.println("\n--- REMOVER SINTOMA ---");

        System.out.print("Nome do sintoma: ");
        String nome = sc.nextLine().trim();

        Sintoma sintoma = sintomasController.procurarPorNome(nome);
        if (sintoma == null) {
            System.out.println("Sintoma nao encontrado.");
            return;
        }

        String esp = sintoma.getCodigoEspecialidade();
        if (esp == null || esp.isEmpty()) {
            esp = "N/A";
        }

        System.out.println("\nDados do sintoma:");
        System.out.println("  Nome: " + sintoma.getNome());
        System.out.println("  Nivel de urgencia: " + sintoma.getNivelUrgencia());
        System.out.println("  Especialidade: " + esp);

        System.out.print("\nTem certeza que deseja remover? (S/N): ");
        String confirmacao = sc.nextLine().trim();

        if (confirmacao.equalsIgnoreCase("S")) {
            if (sintomasController.removerSintoma(nome)) {
                System.out.println("Sintoma removido com sucesso!");
            } else {
                System.out.println("Erro ao remover.");
            }
        } else {
            System.out.println("Operacao cancelada.");
        }
    }

    public void pesquisar() {
        System.out.println("\n--- PESQUISA DE SINTOMAS ---");
        System.out.print("Digite parte do nome do sintoma: ");
        String busca = sc.nextLine().trim().toLowerCase();

        if (busca.isEmpty()) {
            System.out.println("Termo de pesquisa vazio.");
            return;
        }

        Sintoma[] todos = sintomasController.getSintomas();
        int encontrados = 0;

        System.out.println("\nResultados:");
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ #  | Nome                    | Urgencia  | Especialidade   ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        for (int i = 0; i < todos.length; i++) {
            if (todos[i].getNome().toLowerCase().contains(busca)) {
                String esp = todos[i].getCodigoEspecialidade();
                if (esp == null || esp.isEmpty()) {
                    esp = "N/A";
                }
                System.out.printf("║ %2d | %-23s | %-9s | %-15s ║%n",
                        (encontrados + 1),
                        truncar(todos[i].getNome(), 23),
                        todos[i].getNivelUrgencia(),
                        truncar(esp, 15));
                encontrados++;
            }
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");

        if (encontrados == 0) {
            System.out.println("Nenhum sintoma encontrado com '" + busca + "'");
        } else {
            System.out.println("Total encontrado: " + encontrados + " sintoma(s)");
        }
    }

    private int lerInteiro() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
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
