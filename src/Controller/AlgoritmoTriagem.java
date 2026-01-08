package Controller;

import Model.Paciente;
import Model.Triagem;

/**
 * Classe utilitaria para calcular a triagem de pacientes
 * Analisa os sintomas e determina urgencia e especialidade
 * Responsabilidade: Aluno 2/3
 */
public class AlgoritmoTriagem {

    private static final int MAX_ESPECIALIDADES = 20;

    /**
     * Realiza a triagem de um paciente com base nos seus sintomas
     * @param paciente Paciente a triar
     * @param dadosSintomas Array com dados dos sintomas do sistema
     * @param numSintomas Numero de sintomas no array
     * @param separador Separador usado nos dados
     * @return Resultado da triagem
     */
    public static Triagem calcular(Paciente paciente, String[] dadosSintomas,
                                    int numSintomas, String separador) {

        Triagem triagem = new Triagem();

        String[] sintomasPaciente = paciente.getSintomas();
        int numSintomasPaciente = paciente.getNumSintomas();

        if (numSintomasPaciente == 0) {
            // Sem sintomas, urgencia baixa
            triagem.setNivelUrgencia(1);
            return triagem;
        }

        // Contadores de urgencia
        int contVermelha = 0;
        int contLaranja = 0;
        int contVerde = 0;

        // Contadores de especialidades
        String[] especialidades = new String[MAX_ESPECIALIDADES];
        int[] contagemEsp = new int[MAX_ESPECIALIDADES];
        int numEsp = 0;

        // Processa cada sintoma do paciente
        for (int i = 0; i < numSintomasPaciente; i++) {
            String sintomaPaciente = sintomasPaciente[i].toLowerCase().trim();

            // Procura o sintoma nos dados do sistema
            for (int j = 0; j < numSintomas; j++) {
                String[] partes = dadosSintomas[j].split(separador);

                if (partes.length < 2) continue;

                String nomeSintoma = partes[0].toLowerCase().trim();

                // Verifica se o sintoma corresponde
                if (nomeSintoma.equals(sintomaPaciente)) {
                    String urgencia = partes[1].trim();

                    // Conta urgencia
                    if (urgencia.equalsIgnoreCase("Vermelha")) {
                        contVermelha++;
                    } else if (urgencia.equalsIgnoreCase("Laranja")) {
                        contLaranja++;
                    } else {
                        contVerde++;
                    }

                    // Conta especialidades (partes[2], partes[3], etc.)
                    for (int k = 2; k < partes.length; k++) {
                        String esp = partes[k].trim();
                        if (!esp.isEmpty()) {
                            numEsp = adicionarOuIncrementar(especialidades, contagemEsp, numEsp, esp);
                            triagem.adicionarEspecialidadePossivel(esp);
                        }
                    }

                    break; // Encontrou o sintoma, passa para o proximo
                }
            }
        }

        // Define os contadores na triagem
        triagem.setContadores(contVermelha, contLaranja, contVerde);

        // Determina nivel de urgencia (prioridade ao mais grave)
        if (contVermelha > 0) {
            triagem.setNivelUrgencia(3);
        } else if (contLaranja > 0) {
            triagem.setNivelUrgencia(2);
        } else {
            triagem.setNivelUrgencia(1);
        }

        // Determina especialidade principal
        determinarEspecialidade(triagem, especialidades, contagemEsp, numEsp);

        return triagem;
    }

    /**
     * Adiciona uma especialidade ao array ou incrementa se ja existir
     * @return Novo numero de especialidades
     */
    private static int adicionarOuIncrementar(String[] especialidades, int[] contagem,
                                               int numEsp, String esp) {
        // Procura se ja existe
        for (int i = 0; i < numEsp; i++) {
            if (especialidades[i].equals(esp)) {
                contagem[i]++;
                return numEsp;
            }
        }

        // Adiciona nova
        if (numEsp < MAX_ESPECIALIDADES) {
            especialidades[numEsp] = esp;
            contagem[numEsp] = 1;
            return numEsp + 1;
        }

        return numEsp;
    }

    /**
     * Determina a especialidade principal com base nas contagens
     */
    private static void determinarEspecialidade(Triagem triagem, String[] especialidades,
                                                 int[] contagem, int numEsp) {
        if (numEsp == 0) {
            triagem.setEncaminhamentoAutomatico(false);
            triagem.setSugestaoTendencia("Nao foi possivel determinar especialidade");
            return;
        }

        // Encontra a especialidade com maior contagem
        int maxContagem = 0;
        int indiceMax = 0;

        for (int i = 0; i < numEsp; i++) {
            if (contagem[i] > maxContagem) {
                maxContagem = contagem[i];
                indiceMax = i;
            }
        }

        // Verifica se ha empate
        int empates = 0;
        for (int i = 0; i < numEsp; i++) {
            if (contagem[i] == maxContagem) {
                empates++;
            }
        }

        if (empates == 1) {
            // Sem empate - encaminhamento automatico possivel
            triagem.setEspecialidadePrincipal(especialidades[indiceMax]);
            triagem.setEncaminhamentoAutomatico(true);
        } else {
            // Com empate - encaminhamento manual
            triagem.setEspecialidadePrincipal(null);
            triagem.setEncaminhamentoAutomatico(false);

            // Calcula tendencias para sugestao
            int totalSintomas = triagem.getTotalSintomas();
            if (totalSintomas > 0) {
                StringBuilder sugestao = new StringBuilder("Tendencias: ");

                for (int i = 0; i < numEsp; i++) {
                    if (contagem[i] > 0) {
                        int percentagem = (contagem[i] * 100) / totalSintomas;
                        sugestao.append(especialidades[i]).append(": ").append(percentagem).append("% ");
                    }
                }

                triagem.setSugestaoTendencia(sugestao.toString());
            }
        }
    }

    /**
     * Converte o texto de urgencia para nivel numerico
     * @param urgenciaTexto Texto da urgencia (Vermelha, Laranja, Verde)
     * @return Nivel numerico (3, 2 ou 1)
     */
    public static int converterUrgenciaParaNivel(String urgenciaTexto) {
        if (urgenciaTexto == null) return 1;

        switch (urgenciaTexto.toLowerCase().trim()) {
            case "vermelha": return 3;
            case "laranja": return 2;
            case "verde": return 1;
            default: return 1;
        }
    }

    /**
     * Converte o nivel numerico para texto de urgencia
     * @param nivel Nivel numerico (1, 2 ou 3)
     * @return Texto da urgencia
     */
    public static String converterNivelParaUrgencia(int nivel) {
        switch (nivel) {
            case 3: return "Vermelha (Urgente)";
            case 2: return "Laranja (Media)";
            case 1: return "Verde (Baixa)";
            default: return "Desconhecido";
        }
    }
}
