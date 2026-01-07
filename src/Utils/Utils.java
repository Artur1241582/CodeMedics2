package Utils;

import java.util.Scanner;

public class Utils {

    /**
     * Lê uma opção válida do utilizador dentro de um intervalo
     * @param scanner Scanner para ler input
     * @param min Valor mínimo aceite
     * @param max Valor máximo aceite
     * @return Opção válida escolhida
     */
    public static int lerOpcao(Scanner scanner, int min, int max) {
        int opcao = -1;
        boolean valido = false;

        while (!valido) {
            try {
                String input = scanner.nextLine().trim();
                opcao = Integer.parseInt(input);

                if (opcao >= min && opcao <= max) {
                    valido = true;
                } else {
                    System.out.print("Opção inválida! Escolha entre " + min + " e " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Por favor, insira um número válido: ");
            }
        }
        return opcao;
    }

    /**
     * Lê um número inteiro positivo
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a mostrar
     * @return Número inteiro positivo
     */
    public static int lerInteiroPositivo(Scanner scanner, String mensagem) {
        int numero = -1;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print(mensagem);
                String input = scanner.nextLine().trim();
                numero = Integer.parseInt(input);

                if (numero > 0) {
                    valido = true;
                } else {
                    System.out.println("Por favor, insira um número positivo!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, insira um número válido!");
            }
        }
        return numero;
    }

    /**
     * Lê um número decimal (double)
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a mostrar
     * @return Número decimal
     */
    public static double lerDecimal(Scanner scanner, String mensagem) {
        double numero = -1;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print(mensagem);
                String input = scanner.nextLine().trim().replace(",", ".");
                numero = Double.parseDouble(input);

                if (numero >= 0) {
                    valido = true;
                } else {
                    System.out.println(" Por favor, insira um número válido!");
                }
            } catch (NumberFormatException e) {
                System.out.println(" Por favor, insira um número válido!");
            }
        }
        return numero;
    }

    /**
     * Lê uma string não vazia
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem a mostrar
     * @return String não vazia
     */
    public static String lerTexto(Scanner scanner, String mensagem) {
        String texto = "";
        boolean valido = false;

        while (!valido) {
            System.out.print(mensagem);
            texto = scanner.nextLine().trim();

            if (!texto.isEmpty()) {
                valido = true;
            } else {
                System.out.println(" O campo não pode estar vazio!");
            }
        }
        return texto;
    }

    /**
     * Confirma uma ação com S/N
     * @param scanner Scanner para ler input
     * @param mensagem Mensagem de confirmação
     * @return true se confirmar (S), false caso contrário (N)
     */
    public static boolean confirmar(Scanner scanner, String mensagem) {
        System.out.print(mensagem + " (S/N): ");
        String resposta = scanner.nextLine().trim().toUpperCase();
        return resposta.equals("S");
    }

    /**
     * Pausa a execução até o utilizador pressionar Enter
     * @param scanner Scanner para ler input
     */
    public static void pausar(Scanner scanner) {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }


    /**
     * Formata um número com zeros à esquerda
     * @param num Número a formatar
     * @param digitos Número de dígitos desejado
     * @return String formatada
     */
    public static String formatarNumero(int num, int digitos) {
        return String.format("%0" + digitos + "d", num);
    }

    /**
     * Formata um valor monetário
     * @param valor Valor a formatar
     * @return String formatada com 2 casas decimais e símbolo €
     */
    public static String formatarMoeda(double valor) {
        return String.format("%.2f€", valor);
    }

    /**
     * Centraliza um texto numa linha de determinado tamanho
     * @param texto Texto a centralizar
     * @param tamanho Tamanho total da linha
     * @return Texto centralizado
     */
    public static String centralizarTexto(String texto, int tamanho) {
        if (texto.length() >= tamanho) {
            return texto;
        }

        int espacos = (tamanho - texto.length()) / 2;
        String resultado = "";

        for (int i = 0; i < espacos; i++) {
            resultado += " ";
        }
        resultado += texto;

        while (resultado.length() < tamanho) {
            resultado += " ";
        }

        return resultado;
    }

    /**
     * Cria uma linha separadora
     * @param tamanho Tamanho da linha
     * @param caractere Caractere a usar
     * @return String com a linha
     */
    public static String criarLinha(int tamanho, char caractere) {
        String linha = "";
        for (int i = 0; i < tamanho; i++) {
            linha += caractere;
        }
        return linha;
    }

    /**
     * Valida um horário (formato HH:MM)
     * @param horario Horário a validar
     * @return true se válido, false caso contrário
     */
    public static boolean validarHorario(String horario) {
        if (horario == null || horario.length() != 5) {
            return false;
        }

        if (horario.charAt(2) != ':') {
            return false;
        }

        try {
            String[] partes = horario.split(":");
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);

            return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Converte horário em unidades de tempo (1-24)
     * @param horario Horário no formato HH:MM
     * @return Unidade de tempo correspondente
     */
    public static int horarioParaUnidade(String horario) {
        if (!validarHorario(horario)) {
            return 1;
        }

        String[] partes = horario.split(":");
        int horas = Integer.parseInt(partes[0]);

        return (horas == 0) ? 24 : horas;
    }

    /**
     * Converte unidade de tempo em horário
     * @param unidade Unidade de tempo (1-24)
     * @return Horário no formato HH:MM
     */
    public static String unidadeParaHorario(int unidade) {
        if (unidade < 1 || unidade > 24) {
            unidade = 1;
        }

        int horas = (unidade == 24) ? 0 : unidade;
        return formatarNumero(horas, 2) + ":00";
    }

    /**
     * Mostra uma mensagem de sucesso formatada
     * @param mensagem Mensagem a mostrar
     */
    public static void mostrarSucesso(String mensagem) {
        System.out.println("\n" + mensagem);
    }

    /**
     * Mostra uma mensagem de erro formatada
     * @param mensagem Mensagem a mostrar
     */
    public static void mostrarErro(String mensagem) {
        System.out.println("\n" + mensagem);
    }

    /**
     * Mostra uma mensagem de aviso formatada
     * @param mensagem Mensagem a mostrar
     */
    public static void mostrarAviso(String mensagem) {
        System.out.println("\n" + mensagem);
    }

    /**
     * Mostra uma mensagem de informação formatada
     * @param mensagem Mensagem a mostrar
     */
    public static void mostrarInfo(String mensagem) {
        System.out.println("\n" + mensagem);
    }
}