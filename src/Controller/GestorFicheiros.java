package Controller;

import java.io.*;

/**
 * Controlador responsável pela leitura e escrita de ficheiros do sistema
 * Gere médicos, especialidades, sintomas e outros dados persistentes
 */
public class GestorFicheiros {

    private String caminhoFicheiros;
    private String separador;

    // Nomes dos ficheiros
    private static final String FICHEIRO_MEDICOS = "medicos.txt";
    private static final String FICHEIRO_ESPECIALIDADES = "especialidades.txt";
    private static final String FICHEIRO_SINTOMAS = "sintomas.txt";
    private static final String FICHEIRO_PACIENTES = "pacientes.txt";

    /**
     * Construtor
     * @param caminhoFicheiros Caminho do diretório dos ficheiros
     * @param separador Separador utilizado nos ficheiros
     */
    public GestorFicheiros(String caminhoFicheiros, String separador) {
        this.caminhoFicheiros = caminhoFicheiros;
        this.separador = separador;
    }

    /**
     * Lê e mostra o conteúdo do ficheiro de médicos
     * @return Número de médicos lidos
     */
    public int listarMedicos() {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_MEDICOS);

        if (!ficheiro.exists()) {
            System.out.println("Ficheiro de médicos não encontrado!");
            return 0;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LISTA DE MÉDICOS                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Nome              | Especialidade  | Entrada | Saída | €/h ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                String[] dados = linha.split(separador);

                if (dados.length >= 5) {
                    String nome = dados[0].trim();
                    String especialidade = dados[1].trim();
                    String horaEntrada = dados[2].trim();
                    String horaSaida = dados[3].trim();
                    String valorHora = dados[4].trim();

                    System.out.printf("║ %-17s | %-14s | %7s | %5s | %3s ║%n",
                            nome, especialidade, horaEntrada, horaSaida, valorHora);
                    contador++;
                }
            }

            System.out.println("╚════════════════════════════════════════════════════════════╝");
            System.out.println("Total de médicos: " + contador);

        } catch (IOException e) {
            System.err.println("Erro ao ler ficheiro: " + e.getMessage());
            return 0;
        }

        return contador;
    }

    /**
     * Lê e mostra o conteúdo do ficheiro de especialidades
     * @return Número de especialidades lidas
     */
    public int listarEspecialidades() {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_ESPECIALIDADES);

        if (!ficheiro.exists()) {
            System.out.println("Ficheiro de especialidades não encontrado!");
            return 0;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  LISTA DE ESPECIALIDADES                   ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Código    | Nome da Especialidade                          ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                String[] dados = linha.split(separador);

                if (dados.length >= 2) {
                    String codigo = dados[0].trim();
                    String nome = dados[1].trim();

                    System.out.printf("║ %-9s | %-42s ║%n", codigo, nome);
                    contador++;
                }
            }

            System.out.println("╚════════════════════════════════════════════════════════════╝");
            System.out.println("Total de especialidades: " + contador);

        } catch (IOException e) {
            System.err.println("Erro ao ler ficheiro: " + e.getMessage());
            return 0;
        }

        return contador;
    }

    /**
     * Lê e mostra o conteúdo do ficheiro de sintomas
     * @return Número de sintomas lidos
     */
    public int listarSintomas() {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_SINTOMAS);

        if (!ficheiro.exists()) {
            System.out.println("Ficheiro de sintomas não encontrado!");
            return 0;
        }

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                     LISTA DE SINTOMAS                      ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Nome                    | Urgência | Especialidades        ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                String[] dados = linha.split(separador);

                if (dados.length >= 2) {
                    String nome = dados[0].trim();
                    String urgencia = dados[1].trim();
                    String especialidades = dados.length >= 3 ? dados[2].trim() : "N/A";

                    System.out.printf("║ %-23s | %-8s | %-21s ║%n",
                            nome, urgencia, especialidades);
                    contador++;
                }
            }

            System.out.println("╚════════════════════════════════════════════════════════════╝");
            System.out.println("Total de sintomas: " + contador);

        } catch (IOException e) {
            System.err.println("Erro ao ler ficheiro: " + e.getMessage());
            return 0;
        }

        return contador;
    }

    /**
     * Carrega os dados de médicos para um array
     * @param maxMedicos Tamanho máximo do array
     * @return Array de strings com os dados dos médicos (cada posição é uma linha)
     */
    public String[] carregarMedicos(int maxMedicos) {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_MEDICOS);

        if (!ficheiro.exists()) {
            return new String[0];
        }

        String[] medicos = new String[maxMedicos];
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null && contador < maxMedicos) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                medicos[contador] = linha;
                contador++;
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar médicos: " + e.getMessage());
            return new String[0];
        }

        // Redimensiona o array para o tamanho real
        String[] resultado = new String[contador];
        for (int i = 0; i < contador; i++) {
            resultado[i] = medicos[i];
        }

        return resultado;
    }

    /**
     * Carrega os dados de especialidades para um array
     * @param maxEspecialidades Tamanho máximo do array
     * @return Array de strings com os dados das especialidades
     */
    public String[] carregarEspecialidades(int maxEspecialidades) {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_ESPECIALIDADES);

        if (!ficheiro.exists()) {
            return new String[0];
        }

        String[] especialidades = new String[maxEspecialidades];
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null && contador < maxEspecialidades) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                especialidades[contador] = linha;
                contador++;
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar especialidades: " + e.getMessage());
            return new String[0];
        }

        // Redimensiona o array para o tamanho real
        String[] resultado = new String[contador];
        for (int i = 0; i < contador; i++) {
            resultado[i] = especialidades[i];
        }

        return resultado;
    }

    /**
     * Carrega os dados de sintomas para um array
     * @param maxSintomas Tamanho máximo do array
     * @return Array de strings com os dados dos sintomas
     */
    public String[] carregarSintomas(int maxSintomas) {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_SINTOMAS);

        if (!ficheiro.exists()) {
            return new String[0];
        }

        String[] sintomas = new String[maxSintomas];
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null && contador < maxSintomas) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                sintomas[contador] = linha;
                contador++;
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar sintomas: " + e.getMessage());
            return new String[0];
        }

        // Redimensiona o array para o tamanho real
        String[] resultado = new String[contador];
        for (int i = 0; i < contador; i++) {
            resultado[i] = sintomas[i];
        }

        return resultado;
    }

    /**
     * Guarda dados num ficheiro (sobrescreve o conteúdo)
     * @param nomeFicheiro Nome do ficheiro
     * @param dados Array de strings a guardar (uma linha por elemento)
     * @param numDados Número de elementos válidos no array
     * @return true se guardou com sucesso
     */
    public boolean guardarDados(String nomeFicheiro, String[] dados, int numDados) {
        File diretorio = new File(caminhoFicheiros);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        File ficheiro = new File(caminhoFicheiros + File.separator + nomeFicheiro);

        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro))) {
            for (int i = 0; i < numDados; i++) {
                pw.println(dados[i]);
            }
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao guardar ficheiro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Adiciona uma linha a um ficheiro existente
     * @param nomeFicheiro Nome do ficheiro
     * @param linha Linha a adicionar
     * @return true se adicionou com sucesso
     */
    public boolean adicionarLinha(String nomeFicheiro, String linha) {
        File diretorio = new File(caminhoFicheiros);
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        File ficheiro = new File(caminhoFicheiros + File.separator + nomeFicheiro);

        try (PrintWriter pw = new PrintWriter(new FileWriter(ficheiro, true))) {
            pw.println(linha);
            return true;
        } catch (IOException e) {
            System.err.println("Erro ao adicionar linha: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica se um ficheiro existe
     * @param nomeFicheiro Nome do ficheiro
     * @return true se o ficheiro existe
     */
    public boolean ficheiroExiste(String nomeFicheiro) {
        File ficheiro = new File(caminhoFicheiros + File.separator + nomeFicheiro);
        return ficheiro.exists();
    }

    /**
     * Conta o número de linhas válidas num ficheiro (ignora vazias e comentários)
     * @param nomeFicheiro Nome do ficheiro
     * @return Número de linhas válidas
     */
    public int contarLinhas(String nomeFicheiro) {
        File ficheiro = new File(caminhoFicheiros + File.separator + nomeFicheiro);

        if (!ficheiro.exists()) {
            return 0;
        }

        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();
                if (!linha.isEmpty() && !linha.startsWith("#")) {
                    contador++;
                }
            }

        } catch (IOException e) {
            System.err.println("Erro ao contar linhas: " + e.getMessage());
        }

        return contador;
    }

    // ==================== PACIENTES ====================

    /**
     * Carrega o historico de pacientes do ficheiro
     * @param maxPacientes Tamanho maximo do array
     * @return Array de strings com os dados dos pacientes
     */
    public String[] carregarPacientes(int maxPacientes) {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_PACIENTES);

        if (!ficheiro.exists()) {
            return new String[0];
        }

        String[] pacientes = new String[maxPacientes];
        int contador = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null && contador < maxPacientes) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                pacientes[contador] = linha;
                contador++;
            }

        } catch (IOException e) {
            System.err.println("Erro ao carregar pacientes: " + e.getMessage());
            return new String[0];
        }

        // Redimensiona o array para o tamanho real
        String[] resultado = new String[contador];
        for (int i = 0; i < contador; i++) {
            resultado[i] = pacientes[i];
        }

        return resultado;
    }

    /**
     * Guarda o historico de pacientes atendidos no ficheiro
     * @param dados Array de strings com os dados dos pacientes
     * @param numDados Numero de elementos validos no array
     * @return true se guardou com sucesso
     */
    public boolean guardarPacientes(String[] dados, int numDados) {
        return guardarDados(FICHEIRO_PACIENTES, dados, numDados);
    }

    /**
     * Adiciona um paciente ao historico (append)
     * @param dadosPaciente Linha com dados do paciente
     * @return true se adicionou com sucesso
     */
    public boolean adicionarPacienteAoHistorico(String dadosPaciente) {
        return adicionarLinha(FICHEIRO_PACIENTES, dadosPaciente);
    }

    /**
     * Lista o historico de pacientes
     * @return Numero de pacientes no historico
     */
    public int listarHistoricoPacientes() {
        File ficheiro = new File(caminhoFicheiros + File.separator + FICHEIRO_PACIENTES);

        if (!ficheiro.exists()) {
            System.out.println("Ficheiro de historico de pacientes nao encontrado!");
            return 0;
        }

        System.out.println("\n+------------------------------------------------------------+");
        System.out.println("|              HISTORICO DE PACIENTES ATENDIDOS              |");
        System.out.println("+------------------------------------------------------------+");
        System.out.println("| Nome                | Urgencia  | Especialidade | Dia     |");
        System.out.println("+------------------------------------------------------------+");

        int contador = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = br.readLine()) != null) {
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }

                String[] dados = linha.split(separador);

                if (dados.length >= 4) {
                    String nome = dados[0].trim();
                    String urgencia = dados[1].trim();
                    String especialidade = dados[2].trim();
                    String dia = dados[3].trim();

                    System.out.printf("| %-19s | %-9s | %-13s | %-7s |%n",
                            nome, urgencia, especialidade, dia);
                    contador++;
                }
            }

            System.out.println("+------------------------------------------------------------+");
            System.out.println("Total de pacientes atendidos: " + contador);

        } catch (IOException e) {
            System.err.println("Erro ao ler ficheiro: " + e.getMessage());
            return 0;
        }

        return contador;
    }

    // Getters e Setters

    public String getCaminhoFicheiros() {
        return caminhoFicheiros;
    }

    public void setCaminhoFicheiros(String caminhoFicheiros) {
        this.caminhoFicheiros = caminhoFicheiros;
    }

    public String getSeparador() {
        return separador;
    }

    public void setSeparador(String separador) {
        this.separador = separador;
    }
}