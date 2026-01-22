package Utils;

import java.util.Scanner;

public class Utils {


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


    public static boolean confirmar(Scanner scanner, String mensagem) {
        System.out.print(mensagem + " (S/N): ");
        String resposta = scanner.nextLine().trim().toUpperCase();
        return resposta.equals("S");
    }

    public static void pausar(Scanner scanner) {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    public static String formatarNumero(int num, int digitos) {
        return String.format("%0" + digitos + "d", num);
    }


    public static String formatarMoeda(double valor) {
        return String.format("%.2f€", valor);
    }


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

    public static String criarLinha(int tamanho, char caractere) {
        String linha = "";
        for (int i = 0; i < tamanho; i++) {
            linha += caractere;
        }
        return linha;
    }


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

    public static int horarioParaUnidade(String horario) {
        if (!validarHorario(horario)) {
            return 1;
        }

        String[] partes = horario.split(":");
        int horas = Integer.parseInt(partes[0]);

        return (horas == 0) ? 24 : horas;
    }


    public static String unidadeParaHorario(int unidade) {
        if (unidade < 1 || unidade > 24) {
            unidade = 1;
        }

        int horas = (unidade == 24) ? 0 : unidade;
        return formatarNumero(horas, 2) + ":00";
    }


    public static void mostrarSucesso(String mensagem) {
        System.out.println("\n" + mensagem);
    }


    public static void mostrarErro(String mensagem) {
        System.out.println("\n" + mensagem);
    }

    public static void mostrarAviso(String mensagem) {
        System.out.println("\n" + mensagem);
    }

    public static void mostrarInfo(String mensagem) {
        System.out.println("\n" + mensagem);
    }
}