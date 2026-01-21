package View;

import Controller.MedicoController;
import Controller.EspecialidadeController;
import Model.Medico;
import Model.Especialidade;

import java.util.Scanner;

/**
 * View para gestao de medicos
 * Usa os modelos unificados Medico e Especialidade
 */
public class MedicoView {

    private MedicoController medicoCtrl;
    private EspecialidadeController especialidadeCtrl;
    private Scanner sc;

    public MedicoView(MedicoController medicoCtrl, EspecialidadeController especialidadeCtrl, Scanner sc) {
        this.medicoCtrl = medicoCtrl;
        this.especialidadeCtrl = especialidadeCtrl;
        this.sc = sc;
    }

    public void adicionar() {
        System.out.println("\n--- ADICIONAR MEDICO ---");

        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();

        if (nome.isEmpty()) {
            System.out.println("Erro: Nome nao pode estar vazio!");
            return;
        }

        Especialidade[] especialidades = especialidadeCtrl.getEspecialidades();

        if (especialidades.length == 0) {
            System.out.println("Erro: Nenhuma especialidade cadastrada!");
            System.out.println("Por favor, adicione especialidades primeiro.");
            return;
        }

        System.out.println("\nEspecialidades disponiveis:");
        for (int i = 0; i < especialidades.length; i++) {
            System.out.println((i + 1) + ". " + especialidades[i]);
        }

        System.out.print("Escolha a especialidade (numero): ");
        int escolha = lerInteiro();

        if (escolha < 1 || escolha > especialidades.length) {
            System.out.println("Especialidade invalida!");
            return;
        }

        Especialidade esp = especialidades[escolha - 1];

        System.out.print("Hora de entrada (0-23): ");
        int horaEntrada = lerInteiro();

        if (horaEntrada < 0 || horaEntrada > 23) {
            System.out.println("Hora de entrada invalida! Deve ser entre 0 e 23.");
            return;
        }

        System.out.print("Hora de saida (0-23): ");
        int horaSaida = lerInteiro();

        if (horaSaida < 0 || horaSaida > 23) {
            System.out.println("Hora de saida invalida! Deve ser entre 0 e 23.");
            return;
        }

        System.out.print("Valor por hora (EUR): ");
        double valorHora = lerDouble();

        if (valorHora <= 0) {
            System.out.println("Valor por hora invalido! Deve ser maior que 0.");
            return;
        }

        Medico m = new Medico(nome, esp.getCodigo(), horaEntrada, horaSaida, valorHora);

        if (medicoCtrl.adicionarMedico(m)) {
            System.out.println("Medico adicionado com sucesso!");
        } else {
            System.out.println("Erro: Nao foi possivel adicionar o medico.");
            System.out.println("Verifique se ja existe um medico com este nome ou se o limite foi atingido.");
        }
    }

    public void listar() {
        System.out.println("\n--- LISTA DE MEDICOS ---");

        Medico[] medicos = medicoCtrl.listarMedicos();

        if (medicos.length == 0) {
            System.out.println("Nenhum medico cadastrado.");
            return;
        }

        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ #  | Nome              | Esp.  | Horario   | EUR/h | Estado║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        for (int i = 0; i < medicos.length; i++) {
            Medico m = medicos[i];
            System.out.printf("║ %2d | %-17s | %-5s | %02d-%02dh    | %5.0f | %-6s║%n",
                    (i + 1),
                    truncar(m.getNome(), 17),
                    m.getCodigoEspecialidade(),
                    m.getHoraEntrada(),
                    m.getHoraSaida(),
                    m.getValorHora(),
                    truncar(m.getEstadoTexto(), 6));
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println("Total: " + medicos.length + " medico(s)");
    }

    public void editar() {
        System.out.println("\n--- EDITAR MEDICO ---");

        System.out.print("Nome do medico: ");
        String nome = sc.nextLine().trim();

        Medico medico = medicoCtrl.buscarMedicoPorNome(nome);

        if (medico == null) {
            System.out.println("Medico nao encontrado.");
            return;
        }

        System.out.println("\nDados atuais:");
        System.out.println("  Nome: " + medico.getNome());
        System.out.println("  Especialidade: " + medico.getCodigoEspecialidade());
        System.out.println("  Horario: " + medico.getHoraEntrada() + "h - " + medico.getHoraSaida() + "h");
        System.out.println("  Valor/Hora: " + medico.getValorHora() + " EUR");
        System.out.println("  Estado: " + medico.getEstadoTexto());

        System.out.println("\n1. Editar hora de saida");
        System.out.println("2. Editar todos os dados");
        System.out.print("Escolha: ");

        int opcao = lerInteiro();

        if (opcao == 1) {
            System.out.print("Nova hora de saida (0-23): ");
            int novaHora = lerInteiro();

            if (novaHora < 0 || novaHora > 23) {
                System.out.println("Hora invalida!");
                return;
            }

            if (medicoCtrl.atualizarHoraSaida(nome, novaHora)) {
                System.out.println("Hora de saida atualizada com sucesso!");
            } else {
                System.out.println("Erro ao atualizar.");
            }
        } else if (opcao == 2) {
            System.out.print("Novo nome (ENTER para manter): ");
            String novoNome = sc.nextLine().trim();
            if (novoNome.isEmpty()) {
                novoNome = medico.getNome();
            }

            Especialidade[] especialidades = especialidadeCtrl.getEspecialidades();

            if (especialidades.length == 0) {
                System.out.println("Nenhuma especialidade cadastrada!");
                return;
            }

            System.out.println("\nEspecialidades disponiveis:");
            for (int i = 0; i < especialidades.length; i++) {
                System.out.println((i + 1) + ". " + especialidades[i]);
            }

            System.out.print("Escolha a especialidade (0 para manter): ");
            int escolha = lerInteiro();

            String codigoEsp = medico.getCodigoEspecialidade();
            if (escolha > 0 && escolha <= especialidades.length) {
                codigoEsp = especialidades[escolha - 1].getCodigo();
            }

            System.out.print("Nova hora de entrada (0-23, -1 para manter): ");
            int horaEntrada = lerInteiro();
            if (horaEntrada < 0) {
                horaEntrada = medico.getHoraEntrada();
            }

            System.out.print("Nova hora de saida (0-23, -1 para manter): ");
            int horaSaida = lerInteiro();
            if (horaSaida < 0) {
                horaSaida = medico.getHoraSaida();
            }

            System.out.print("Novo valor por hora (-1 para manter): ");
            double valorHora = lerDouble();
            if (valorHora < 0) {
                valorHora = medico.getValorHora();
            }

            Medico medicoAtualizado = new Medico(novoNome, codigoEsp, horaEntrada, horaSaida, valorHora);

            if (medicoCtrl.atualizarMedico(nome, medicoAtualizado)) {
                System.out.println("Medico atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar.");
            }
        }
    }

    public void remover() {
        System.out.println("\n--- REMOVER MEDICO ---");

        System.out.print("Nome do medico: ");
        String nome = sc.nextLine().trim();

        Medico medico = medicoCtrl.buscarMedicoPorNome(nome);

        if (medico == null) {
            System.out.println("Medico nao encontrado.");
            return;
        }

        if (medico.isEmConsulta()) {
            System.out.println("ERRO: Nao e possivel remover um medico que esta em consulta!");
            return;
        }

        System.out.println("\nDados do medico:");
        System.out.println("  Nome: " + medico.getNome());
        System.out.println("  Especialidade: " + medico.getCodigoEspecialidade());
        System.out.println("  Horario: " + medico.getHoraEntrada() + "h - " + medico.getHoraSaida() + "h");

        System.out.print("\nTem certeza que deseja remover? (S/N): ");
        String confirmacao = sc.nextLine().trim();

        if (confirmacao.equalsIgnoreCase("S")) {
            if (medicoCtrl.removerMedico(nome)) {
                System.out.println("Medico removido com sucesso!");
            } else {
                System.out.println("Erro ao remover.");
            }
        } else {
            System.out.println("Operacao cancelada.");
        }
    }

    private int lerInteiro() {
        try {
            int valor = Integer.parseInt(sc.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double lerDouble() {
        try {
            double valor = Double.parseDouble(sc.nextLine().trim());
            return valor;
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
