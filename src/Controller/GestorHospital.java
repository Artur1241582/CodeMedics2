package Controller;

import Model.*;

/**
 * Controlador principal do funcionamento dia-a-dia do hospital
 * Gere pacientes, medicos, triagem, encaminhamento e tempo
 * Integra-se com os CRUDs para sincronizacao de dados
 */
public class GestorHospital {

    // Constantes
    private static final int MAX_MEDICOS = 50;
    private static final int MAX_SINTOMAS = 200;
    private static final int MAX_ESPECIALIDADES = 20;
    private static final int MAX_PACIENTES_ATENDIDOS = 500;

    // Medicos
    private Medico[] medicos;
    private int numMedicos;

    // Especialidades (novo - para integracao)
    private Especialidade[] especialidades;
    private int numEspecialidades;

    // Sintomas (novo - para integracao)
    private Sintoma[] sintomas;
    private int numSintomas;

    // Dados legados (mantidos para compatibilidade com triagem)
    private String[] dadosSintomas;
    private String[] dadosEspecialidades;

    // Fila de espera
    private FilaEspera filaEspera;

    // Historico de pacientes atendidos (no dia)
    private Paciente[] pacientesAtendidos;
    private int numPacientesAtendidos;

    // Referencias
    private Configuracoes config;
    private GestorNotificacoes notificacoes;
    private GestorFicheiros gestorFicheiros;
    private GestorEstatisticas gestorEstatisticas;

    /**
     * Construtor do gestor de hospital
     * @param config Configuracoes do sistema
     * @param notificacoes Gestor de notificacoes
     * @param gestorFicheiros Gestor de ficheiros
     */
    public GestorHospital(Configuracoes config, GestorNotificacoes notificacoes,
                          GestorFicheiros gestorFicheiros) {
        this.config = config;
        this.notificacoes = notificacoes;
        this.gestorFicheiros = gestorFicheiros;

        this.medicos = new Medico[MAX_MEDICOS];
        this.numMedicos = 0;

        this.especialidades = new Especialidade[MAX_ESPECIALIDADES];
        this.numEspecialidades = 0;

        this.sintomas = new Sintoma[MAX_SINTOMAS];
        this.numSintomas = 0;

        this.filaEspera = new FilaEspera(100);

        this.pacientesAtendidos = new Paciente[MAX_PACIENTES_ATENDIDOS];
        this.numPacientesAtendidos = 0;

        this.dadosSintomas = new String[MAX_SINTOMAS];
        this.dadosEspecialidades = new String[MAX_ESPECIALIDADES];

        // Carrega dados iniciais
        carregarDados();
    }

    // ==================== CARREGAMENTO DE DADOS ====================

    /**
     * Carrega todos os dados dos ficheiros
     */
    public void carregarDados() {
        carregarMedicos();
        carregarEspecialidades();
        carregarSintomas();
        atualizarPresencaMedicos();
    }

    /**
     * Carrega os medicos do ficheiro
     */
    private void carregarMedicos() {
        String[] linhas = gestorFicheiros.carregarMedicos(MAX_MEDICOS);
        numMedicos = 0;
        int linhasComErro = 0;

        for (int i = 0; i < linhas.length; i++) {
            if (linhas[i] != null && !linhas[i].trim().isEmpty()) {
                // Ignora linhas de comentário
                if (linhas[i].trim().startsWith("#")) {
                    continue;
                }

                try {
                    medicos[numMedicos] = new Medico(linhas[i], config.getSeparador());
                    numMedicos++;
                } catch (IllegalArgumentException e) {
                    linhasComErro++;
                    System.err.println("ERRO ao carregar médico (linha " + (i + 1) + "): " + e.getMessage());
                    notificacoes.adicionarLog("ERRO ao carregar médico na linha " + (i + 1));
                } catch (Exception e) {
                    linhasComErro++;
                    System.err.println("ERRO inesperado ao processar médico (linha " + (i + 1) + "): " + linhas[i]);
                    e.printStackTrace();
                }
            }
        }

        if (linhasComErro > 0) {
            notificacoes.adicionarLog("AVISO: " + linhasComErro + " linha(s) com erro no ficheiro de médicos");
            System.err.println("\n╔════════════════════════════════════════════════════════════╗");
            System.err.println("║  ATENÇÃO: Foram encontrados erros no ficheiro de médicos  ║");
            System.err.println("║  Verifique o formato das linhas no ficheiro medicos.txt   ║");
            System.err.println("║  Formato esperado:                                         ║");
            System.err.println("║  Nome;Especialidade;HoraEntrada;HoraSaida;ValorHora       ║");
            System.err.println("╚════════════════════════════════════════════════════════════╝\n");
        }

        notificacoes.adicionarLog("Carregados " + numMedicos + " médico(s)");
    }

    /**
     * Carrega as especialidades do ficheiro
     */
    private void carregarEspecialidades() {
        String[] linhas = gestorFicheiros.carregarEspecialidades(MAX_ESPECIALIDADES);
        numEspecialidades = 0;
        int linhasComErro = 0;

        for (int i = 0; i < linhas.length; i++) {
            if (linhas[i] != null && !linhas[i].trim().isEmpty()) {
                // Ignora linhas de comentário
                if (linhas[i].trim().startsWith("#")) {
                    continue;
                }

                try {
                    especialidades[numEspecialidades] = Especialidade.fromFicheiro(linhas[i], config.getSeparador());
                    dadosEspecialidades[numEspecialidades] = linhas[i];
                    numEspecialidades++;
                } catch (IllegalArgumentException e) {
                    linhasComErro++;
                    System.err.println("ERRO ao carregar especialidade (linha " + (i + 1) + "): " + e.getMessage());
                } catch (Exception e) {
                    linhasComErro++;
                    System.err.println("ERRO inesperado ao processar especialidade (linha " + (i + 1) + "): " + linhas[i]);
                    e.printStackTrace();
                }
            }
        }

        if (linhasComErro > 0) {
            System.err.println("\nAVISO: " + linhasComErro + " linha(s) com erro no ficheiro de especialidades");
        }

        notificacoes.adicionarLog("Carregadas " + numEspecialidades + " especialidade(s)");
    }

    /**
     * Carrega os sintomas do ficheiro
     */
    private void carregarSintomas() {
        String[] linhas = gestorFicheiros.carregarSintomas(MAX_SINTOMAS);
        numSintomas = 0;
        int linhasComErro = 0;

        for (int i = 0; i < linhas.length; i++) {
            if (linhas[i] != null && !linhas[i].trim().isEmpty()) {
                // Ignora linhas de comentário
                if (linhas[i].trim().startsWith("#")) {
                    continue;
                }

                try {
                    sintomas[numSintomas] = new Sintoma(linhas[i], config.getSeparador());
                    dadosSintomas[numSintomas] = linhas[i];
                    numSintomas++;
                } catch (IllegalArgumentException e) {
                    linhasComErro++;
                    System.err.println("ERRO ao carregar sintoma (linha " + (i + 1) + "): " + e.getMessage());
                } catch (Exception e) {
                    linhasComErro++;
                    System.err.println("ERRO inesperado ao processar sintoma (linha " + (i + 1) + "): " + linhas[i]);
                    e.printStackTrace();
                }
            }
        }

        if (linhasComErro > 0) {
            System.err.println("\nAVISO: " + linhasComErro + " linha(s) com erro no ficheiro de sintomas");
        }

        notificacoes.adicionarLog("Carregados " + numSintomas + " sintoma(s)");
    }

    // ==================== METODOS CRUD PARA MEDICOS ====================

    /**
     * Adiciona um medico ao sistema
     * @param medico Medico a adicionar
     * @return true se adicionou com sucesso
     */
    public boolean adicionarMedico(Medico medico) {
        if (medico == null || numMedicos >= MAX_MEDICOS) {
            return false;
        }

        medicos[numMedicos++] = medico;
        guardarMedicos();
        notificacoes.adicionarLog("Medico adicionado: " + medico.getNome());
        return true;
    }

    /**
     * Atualiza um medico no sistema
     * @param medico Medico atualizado
     */
    public void atualizarMedico(Medico medico) {
        guardarMedicos();
        notificacoes.adicionarLog("Medico atualizado: " + medico.getNome());
    }

    /**
     * Remove um medico do sistema
     * @param nome Nome do medico a remover
     * @return true se removeu com sucesso
     */
    public boolean removerMedico(String nome) {
        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].getNome().equalsIgnoreCase(nome)) {
                // Shift dos elementos
                for (int j = i; j < numMedicos - 1; j++) {
                    medicos[j] = medicos[j + 1];
                }
                medicos[--numMedicos] = null;
                guardarMedicos();
                notificacoes.adicionarLog("Medico removido: " + nome);
                return true;
            }
        }
        return false;
    }

    // ==================== METODOS CRUD PARA ESPECIALIDADES ====================

    /**
     * Adiciona uma especialidade ao sistema
     * @param especialidade Especialidade a adicionar
     * @return true se adicionou com sucesso
     */
    public boolean adicionarEspecialidade(Especialidade especialidade) {
        if (especialidade == null || numEspecialidades >= MAX_ESPECIALIDADES) {
            return false;
        }

        especialidades[numEspecialidades] = especialidade;
        dadosEspecialidades[numEspecialidades] = especialidade.toFicheiroString(config.getSeparador());
        numEspecialidades++;
        guardarEspecialidades();
        notificacoes.adicionarLog("Especialidade adicionada: " + especialidade.getNome());
        return true;
    }

    /**
     * Atualiza uma especialidade no sistema
     * @param especialidade Especialidade atualizada
     */
    public void atualizarEspecialidade(Especialidade especialidade) {
        // Atualizar dados legados
        for (int i = 0; i < numEspecialidades; i++) {
            if (especialidades[i].getCodigo().equalsIgnoreCase(especialidade.getCodigo())) {
                dadosEspecialidades[i] = especialidade.toFicheiroString(config.getSeparador());
                break;
            }
        }
        guardarEspecialidades();
        notificacoes.adicionarLog("Especialidade atualizada: " + especialidade.getNome());
    }

    /**
     * Remove uma especialidade do sistema
     * @param codigo Codigo da especialidade a remover
     * @return true se removeu com sucesso
     */
    public boolean removerEspecialidade(String codigo) {
        for (int i = 0; i < numEspecialidades; i++) {
            if (especialidades[i].getCodigo().equalsIgnoreCase(codigo)) {
                // Shift dos elementos
                for (int j = i; j < numEspecialidades - 1; j++) {
                    especialidades[j] = especialidades[j + 1];
                    dadosEspecialidades[j] = dadosEspecialidades[j + 1];
                }
                especialidades[--numEspecialidades] = null;
                dadosEspecialidades[numEspecialidades] = null;
                guardarEspecialidades();
                notificacoes.adicionarLog("Especialidade removida: " + codigo);
                return true;
            }
        }
        return false;
    }

    // ==================== METODOS CRUD PARA SINTOMAS ====================

    /**
     * Adiciona um sintoma ao sistema
     * @param sintoma Sintoma a adicionar
     * @return true se adicionou com sucesso
     */
    public boolean adicionarSintoma(Sintoma sintoma) {
        if (sintoma == null || numSintomas >= MAX_SINTOMAS) {
            return false;
        }

        sintomas[numSintomas] = sintoma;
        dadosSintomas[numSintomas] = sintoma.toFicheiroString(config.getSeparador());
        numSintomas++;
        guardarSintomas();
        notificacoes.adicionarLog("Sintoma adicionado: " + sintoma.getNome());
        return true;
    }

    /**
     * Atualiza um sintoma no sistema
     * @param sintoma Sintoma atualizado
     */
    public void atualizarSintoma(Sintoma sintoma) {
        // Atualizar dados legados
        for (int i = 0; i < numSintomas; i++) {
            if (sintomas[i].getNome().equalsIgnoreCase(sintoma.getNome())) {
                dadosSintomas[i] = sintoma.toFicheiroString(config.getSeparador());
                break;
            }
        }
        guardarSintomas();
        notificacoes.adicionarLog("Sintoma atualizado: " + sintoma.getNome());
    }

    /**
     * Remove um sintoma do sistema
     * @param nome Nome do sintoma a remover
     * @return true se removeu com sucesso
     */
    public boolean removerSintoma(String nome) {
        for (int i = 0; i < numSintomas; i++) {
            if (sintomas[i].getNome().equalsIgnoreCase(nome)) {
                // Shift dos elementos
                for (int j = i; j < numSintomas - 1; j++) {
                    sintomas[j] = sintomas[j + 1];
                    dadosSintomas[j] = dadosSintomas[j + 1];
                }
                sintomas[--numSintomas] = null;
                dadosSintomas[numSintomas] = null;
                guardarSintomas();
                notificacoes.adicionarLog("Sintoma removido: " + nome);
                return true;
            }
        }
        return false;
    }

    // ==================== PERSISTENCIA ====================

    /**
     * Guarda os medicos no ficheiro
     */
    public void guardarMedicos() {
        String[] dados = new String[numMedicos];
        for (int i = 0; i < numMedicos; i++) {
            dados[i] = medicos[i].toFicheiroString(config.getSeparador());
        }
        gestorFicheiros.guardarDados("medicos.txt", dados, numMedicos);
    }

    /**
     * Guarda as especialidades no ficheiro
     */
    public void guardarEspecialidades() {
        String[] dados = new String[numEspecialidades];
        for (int i = 0; i < numEspecialidades; i++) {
            dados[i] = especialidades[i].toFicheiroString(config.getSeparador());
        }
        gestorFicheiros.guardarDados("especialidades.txt", dados, numEspecialidades);
    }

    /**
     * Guarda os sintomas no ficheiro
     */
    public void guardarSintomas() {
        String[] dados = new String[numSintomas];
        for (int i = 0; i < numSintomas; i++) {
            dados[i] = sintomas[i].toFicheiroString(config.getSeparador());
        }
        gestorFicheiros.guardarDados("sintomas.txt", dados, numSintomas);
    }

    /**
     * Guarda todos os dados
     */
    public void guardarTudo() {
        guardarMedicos();
        guardarEspecialidades();
        guardarSintomas();
    }

    /**
     * Guarda um paciente no historico de pacientes atendidos
     * Formato: Nome;NivelUrgencia;Especialidade;Dia;Hora;Medico
     * @param paciente Paciente a guardar
     */
    private void guardarPacienteNoHistorico(Paciente paciente) {
        if (paciente == null || gestorFicheiros == null) {
            return;
        }

        String urgencia = paciente.getNivelUrgenciaTexto();
        String especialidade = paciente.getEspecialidadeSugerida() != null ?
                              paciente.getEspecialidadeSugerida() : "N/A";
        String medico = paciente.getMedicoAtribuido() != null ?
                       paciente.getMedicoAtribuido() : "N/A";

        String linha = paciente.getNome() + config.getSeparador() +
                      urgencia + config.getSeparador() +
                      especialidade + config.getSeparador() +
                      config.getDiaAtual() + config.getSeparador() +
                      config.getUnidadeTempoAtual() + config.getSeparador() +
                      medico;

        gestorFicheiros.adicionarPacienteAoHistorico(linha);
        notificacoes.adicionarLog("Paciente guardado no historico: " + paciente.getNome());
    }

    /**
     * Carrega o historico de pacientes atendidos
     * @return Array com linhas do historico
     */
    public String[] carregarHistoricoPacientes() {
        return gestorFicheiros.carregarPacientes(MAX_PACIENTES_ATENDIDOS);
    }

    /**
     * Lista o historico de pacientes atendidos
     */
    public void listarHistoricoPacientes() {
        gestorFicheiros.listarHistoricoPacientes();
    }

    // ==================== REGISTO DE PACIENTES ====================

    /**
     * Regista um novo paciente
     * @param nome Nome do paciente
     * @return Paciente criado
     */
    public Paciente registarPaciente(String nome) {
        Paciente paciente = new Paciente(nome, config.getUnidadeTempoAtual(), config.getDiaAtual());
        notificacoes.adicionarLog("Paciente registado: " + nome);
        return paciente;
    }

    /**
     * Realiza a triagem de um paciente
     * @param paciente Paciente a triar
     * @return Resultado da triagem
     */
    public Triagem realizarTriagem(Paciente paciente) {
        Triagem triagem = AlgoritmoTriagem.calcular(paciente, dadosSintomas,
                numSintomas, config.getSeparador());

        // Aplica o resultado ao paciente
        paciente.setNivelUrgencia(triagem.getNivelUrgencia());
        paciente.setEspecialidadeSugerida(triagem.getEspecialidadePrincipal());

        // Adiciona a fila de espera
        filaEspera.adicionarPaciente(paciente);

        notificacoes.notificarPacienteTriagem(paciente.getNome(),
                triagem.getNivelUrgenciaTexto());

        return triagem;
    }

    // ==================== ENCAMINHAMENTO ====================

    /**
     * Encaminha automaticamente o proximo paciente para um medico disponivel
     * @return true se encaminhou, false se nao foi possivel
     */
    public boolean encaminharAutomatico() {
        // Obtem o proximo paciente da fila
        Paciente paciente = filaEspera.obterProximoPaciente();
        if (paciente == null) {
            return false;
        }

        // Tenta encontrar um medico disponivel
        Medico medico = null;

        // Primeiro, tenta encontrar um medico da especialidade sugerida
        String espSugerida = paciente.getEspecialidadeSugerida();
        if (espSugerida != null) {
            for (int i = 0; i < numMedicos; i++) {
                if (medicos[i].isDisponivel() &&
                        medicos[i].getCodigoEspecialidade().equals(espSugerida)) {
                    medico = medicos[i];
                    break;
                }
            }
        }

        // Se nao encontrou, tenta qualquer medico disponivel
        if (medico == null) {
            for (int i = 0; i < numMedicos; i++) {
                if (medicos[i].isDisponivel()) {
                    medico = medicos[i];
                    break;
                }
            }
        }

        if (medico == null) {
            return false; // Nenhum medico disponivel
        }

        // Realiza o encaminhamento
        return encaminhar(paciente, medico);
    }

    /**
     * Encaminha um paciente especifico para um medico especifico
     * @param paciente Paciente a encaminhar
     * @param medico Medico que vai atender
     * @return true se encaminhou, false se nao foi possivel
     */
    public boolean encaminhar(Paciente paciente, Medico medico) {
        if (paciente == null || medico == null) {
            return false;
        }

        if (!medico.isDisponivel()) {
            return false;
        }

        // Calcula duracao da consulta com base na urgencia
        int duracao = config.getTempoConsultaPorNivel(paciente.getNivelUrgencia());

        // Inicia a consulta
        if (medico.iniciarConsulta(paciente, duracao)) {
            // Remove da fila de espera
            filaEspera.removerPaciente(paciente);

            notificacoes.adicionarLog("Paciente " + paciente.getNome() +
                    " encaminhado para Dr. " + medico.getNome());
            return true;
        }

        return false;
    }

    /**
     * Encaminha um paciente por indice para um medico por indice
     * @param indicePaciente Indice do paciente na fila
     * @param indiceMedico Indice do medico no array
     * @return true se encaminhou
     */
    public boolean encaminharPorIndice(int indicePaciente, int indiceMedico) {
        Paciente paciente = filaEspera.obterPacientePorIndice(indicePaciente);
        if (indiceMedico < 0 || indiceMedico >= numMedicos) {
            return false;
        }
        Medico medico = medicos[indiceMedico];

        return encaminhar(paciente, medico);
    }

    // ==================== GESTAO DE TEMPO ====================

    /**
     * Avanca uma unidade de tempo e processa todos os eventos
     */
    public void avancarTempo() {
        int unidadeAnterior = config.getUnidadeTempoAtual();
        config.avancarTempo();
        int unidadeAtual = config.getUnidadeTempoAtual();

        notificacoes.notificarNovoTurno(config.getDiaAtual(), unidadeAtual);

        // Se mudou de dia
        if (unidadeAtual < unidadeAnterior) {
            processarMudancaDia();
        }

        // 1. Verifica entrada de medicos
        verificarEntradaMedicos();

        // 2. Atualiza consultas em curso
        atualizarConsultas();

        // 3. Atualiza descanso de medicos
        atualizarDescansos();

        // 4. Verifica saida de medicos
        verificarSaidaMedicos();

        // 5. Processa escalacao de urgencia dos pacientes
        processarEscalacoes();
    }

    /**
     * Verifica se algum medico deve entrar no turno
     */
    private void verificarEntradaMedicos() {
        int unidadeAtual = config.getUnidadeTempoAtual();

        for (int i = 0; i < numMedicos; i++) {
            if (!medicos[i].isPresente() && medicos[i].getHoraEntrada() == unidadeAtual) {
                medicos[i].entrar();
                notificacoes.notificarMedicoDisponivel(medicos[i].getNome(),
                        medicos[i].getCodigoEspecialidade());
            }
        }
    }

    /**
     * Verifica se algum medico deve sair do turno
     */
    private void verificarSaidaMedicos() {
        int unidadeAtual = config.getUnidadeTempoAtual();

        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].isPresente() && !medicos[i].isEmConsulta()) {
                // Verifica se passou da hora de saida
                int horaSaida = medicos[i].getHoraSaida();
                boolean deveSair = false;

                // Logica para horarios normais e que atravessam meia-noite
                if (medicos[i].getHoraEntrada() < horaSaida) {
                    // Horario normal (ex: 8-16)
                    deveSair = unidadeAtual >= horaSaida;
                } else {
                    // Horario atravessa meia-noite (ex: 20-4)
                    deveSair = unidadeAtual >= horaSaida && unidadeAtual < medicos[i].getHoraEntrada();
                }

                if (deveSair) {
                    medicos[i].sair();
                    notificacoes.adicionarLog("Dr. " + medicos[i].getNome() + " terminou o turno");
                }
            }
        }
    }

    /**
     * Atualiza o estado das consultas em curso
     */
    private void atualizarConsultas() {
        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].isEmConsulta()) {
                if (medicos[i].decrementarTempoConsulta()) {
                    // Consulta terminou
                    Paciente pacienteAtendido = medicos[i].terminarConsulta();

                    if (pacienteAtendido != null) {
                        // Adiciona ao historico
                        if (numPacientesAtendidos < MAX_PACIENTES_ATENDIDOS) {
                            pacientesAtendidos[numPacientesAtendidos] = pacienteAtendido;
                            numPacientesAtendidos++;
                        }

                        // Guarda no ficheiro de historico
                        guardarPacienteNoHistorico(pacienteAtendido);

                        // Regista estatisticas
                        if (gestorEstatisticas != null) {
                            gestorEstatisticas.registarSintomasPaciente(pacienteAtendido);
                            if (pacienteAtendido.getEspecialidadeSugerida() != null) {
                                gestorEstatisticas.registarEspecialidadePaciente(
                                        pacienteAtendido.getEspecialidadeSugerida());
                            }
                        }

                        notificacoes.notificarPacienteAtendido(pacienteAtendido.getNome(),
                                medicos[i].getNome());
                    }

                    // Verifica se precisa de descanso
                    if (medicos[i].getHorasTrabalhadasConsecutivas() >= config.getHorasTrabalho()) {
                        medicos[i].iniciarDescanso(config.getTempoDescanso());
                        notificacoes.notificarMedicoEmDescanso(medicos[i].getNome());
                    }
                }
            }
        }
    }

    /**
     * Atualiza o estado dos descansos em curso
     */
    private void atualizarDescansos() {
        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].isEmDescanso()) {
                if (medicos[i].decrementarTempoDescanso()) {
                    // Descanso terminou
                    medicos[i].terminarDescanso();
                    notificacoes.notificarMedicoDisponivel(medicos[i].getNome(),
                            medicos[i].getCodigoEspecialidade());
                }
            }
        }
    }

    /**
     * Processa a escalacao de urgencia dos pacientes em espera
     */
    private void processarEscalacoes() {
        Paciente[] pacientesEspera = filaEspera.getTodosPacientes();

        for (int i = 0; i < filaEspera.getNumPacientes(); i++) {
            Paciente p = pacientesEspera[i];
            p.incrementarTempoEspera();

            int tempoElevacao = config.getTempoElevacaoPorNivel(p.getNivelUrgencia());

            if (p.getTempoEspera() >= tempoElevacao) {
                if (p.getNivelUrgencia() >= 3) {
                    // Paciente urgente que esperou demais - sai sem atendimento
                    filaEspera.removerPaciente(p);
                    notificacoes.notificarAlerta("Paciente " + p.getNome() +
                            " saiu sem atendimento (espera excessiva)");
                } else {
                    // Eleva urgencia
                    String nivelAntigo = p.getNivelUrgenciaTexto();
                    p.elevarUrgencia();
                    notificacoes.notificarElevacaoUrgencia(p.getNome(),
                            nivelAntigo,
                            p.getNivelUrgenciaTexto());
                }
            }
        }

        // Reorganiza a fila apos escalacoes
        filaEspera.reorganizarPorPrioridade();
    }

    /**
     * Processa a mudanca de dia
     */
    private void processarMudancaDia() {
        // Regista estatisticas do dia anterior
        if (gestorEstatisticas != null && numPacientesAtendidos > 0) {
            int diaAnterior = config.getDiaAtual() - 1;
            if (diaAnterior > 0) {
                gestorEstatisticas.registarAtendimentosDia(diaAnterior, numPacientesAtendidos);
            }
        }

        // Reset do contador de pacientes atendidos
        numPacientesAtendidos = 0;
        notificacoes.adicionarLog("Novo dia iniciado: Dia " + config.getDiaAtual());
    }

    /**
     * Atualiza a presenca dos medicos com base na hora atual
     */
    private void atualizarPresencaMedicos() {
        int unidadeAtual = config.getUnidadeTempoAtual();

        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].deveEstarPresente(unidadeAtual)) {
                if (!medicos[i].isPresente()) {
                    medicos[i].entrar();
                }
            }
        }
    }

    // ==================== CONSULTAS E LISTAGENS ====================

    /**
     * Obtem os medicos disponiveis
     * @return Array com medicos disponiveis
     */
    public Medico[] obterMedicosDisponiveis() {
        // Primeiro conta
        int contador = 0;
        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].isDisponivel()) {
                contador++;
            }
        }

        // Cria array resultado
        Medico[] resultado = new Medico[contador];
        int indice = 0;
        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].isDisponivel()) {
                resultado[indice] = medicos[i];
                indice++;
            }
        }

        return resultado;
    }

    /**
     * Obtem os medicos disponiveis de uma especialidade
     * @param codigoEspecialidade Codigo da especialidade
     * @return Array com medicos disponiveis dessa especialidade
     */
    public Medico[] obterMedicosDisponiveisPorEspecialidade(String codigoEspecialidade) {
        // Primeiro conta
        int contador = 0;
        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].isDisponivel() &&
                    medicos[i].getCodigoEspecialidade().equals(codigoEspecialidade)) {
                contador++;
            }
        }

        // Cria array resultado
        Medico[] resultado = new Medico[contador];
        int indice = 0;
        for (int i = 0; i < numMedicos; i++) {
            if (medicos[i].isDisponivel() &&
                    medicos[i].getCodigoEspecialidade().equals(codigoEspecialidade)) {
                resultado[indice] = medicos[i];
                indice++;
            }
        }

        return resultado;
    }

    /**
     * Lista o estado atual do hospital
     */
    public void listarEstadoHospital() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                   ESTADO DO HOSPITAL                       ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Dia: " + config.getDiaAtual() +
                "  |  Unidade de Tempo: " + config.getUnidadeTempoAtual() + "/24");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        // Medicos
        System.out.println("║ MEDICOS:");
        for (int i = 0; i < numMedicos; i++) {
            System.out.println("║   " + medicos[i].toString());
        }

        System.out.println("╠════════════════════════════════════════════════════════════╣");

        // Fila de espera resumo
        System.out.println("║ FILA DE ESPERA: " + filaEspera.getNumPacientes() + " pacientes");
        System.out.println("║   - Urgentes (Vermelha): " + filaEspera.contarPacientesPorUrgencia(3));
        System.out.println("║   - Medios (Laranja):    " + filaEspera.contarPacientesPorUrgencia(2));
        System.out.println("║   - Baixos (Verde):      " + filaEspera.contarPacientesPorUrgencia(1));

        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ Pacientes atendidos hoje: " + numPacientesAtendidos);
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Lista todos os medicos
     */
    public void listarMedicos() {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                    LISTA DE MEDICOS                        ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║ # | Nome              | Esp.  | Horario   | Estado         ║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");

        for (int i = 0; i < numMedicos; i++) {
            Medico m = medicos[i];
            System.out.printf("║ %d | %-17s | %-5s | %2d-%2dh    | %-14s ║%n",
                    (i + 1),
                    truncar(m.getNome(), 17),
                    m.getCodigoEspecialidade(),
                    m.getHoraEntrada(),
                    m.getHoraSaida(),
                    truncar(m.getEstadoTexto(), 14));
        }

        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    /**
     * Lista a fila de espera
     */
    public void listarFilaEspera() {
        filaEspera.listarPacientes();
    }

    /**
     * Obtem a lista de nomes de sintomas para selecao
     * @return Array com nomes dos sintomas
     */
    public String[] obterNomesSintomas() {
        String[] nomes = new String[numSintomas];
        for (int i = 0; i < numSintomas; i++) {
            nomes[i] = sintomas[i].getNome();
        }
        return nomes;
    }

    // ==================== GETTERS ====================

    public FilaEspera getFilaEspera() {
        return filaEspera;
    }

    public Medico[] getMedicos() {
        Medico[] resultado = new Medico[numMedicos];
        for (int i = 0; i < numMedicos; i++) {
            resultado[i] = medicos[i];
        }
        return resultado;
    }

    public int getNumMedicos() {
        return numMedicos;
    }

    public int getNumSintomas() {
        return numSintomas;
    }

    public String[] getDadosSintomas() {
        return dadosSintomas;
    }

    public String[] getDadosEspecialidades() {
        return dadosEspecialidades;
    }

    public int getNumEspecialidades() {
        return numEspecialidades;
    }

    public Especialidade[] getEspecialidades() {
        Especialidade[] resultado = new Especialidade[numEspecialidades];
        for (int i = 0; i < numEspecialidades; i++) {
            resultado[i] = especialidades[i];
        }
        return resultado;
    }

    public Sintoma[] getSintomas() {
        Sintoma[] resultado = new Sintoma[numSintomas];
        for (int i = 0; i < numSintomas; i++) {
            resultado[i] = sintomas[i];
        }
        return resultado;
    }

    public String getSeparador() {
        return config.getSeparador();
    }

    public int getNumPacientesAtendidos() {
        return numPacientesAtendidos;
    }

    public Paciente[] getPacientesAtendidos() {
        Paciente[] resultado = new Paciente[numPacientesAtendidos];
        for (int i = 0; i < numPacientesAtendidos; i++) {
            resultado[i] = pacientesAtendidos[i];
        }
        return resultado;
    }

    /**
     * Obtem um medico por indice
     */
    public Medico obterMedicoPorIndice(int indice) {
        if (indice < 0 || indice >= numMedicos) {
            return null;
        }
        return medicos[indice];
    }

    public Configuracoes getConfig() {
        return config;
    }

    public GestorFicheiros getGestorFicheiros() {
        return gestorFicheiros;
    }

    public GestorNotificacoes getNotificacoes() {
        return notificacoes;
    }

    public void setGestorEstatisticas(GestorEstatisticas gestorEstatisticas) {
        this.gestorEstatisticas = gestorEstatisticas;
    }

    public GestorEstatisticas getGestorEstatisticas() {
        return gestorEstatisticas;
    }

    // ==================== UTILIDADES ====================

    private String truncar(String texto, int tamanho) {
        if (texto == null) return "";
        if (texto.length() <= tamanho) {
            return texto;
        }
        return texto.substring(0, tamanho - 2) + "..";
    }
}