package Model;

/**
 * Classe que representa um medico no sistema hospitalar
 * Responsabilidade: Aluno 2/3
 */
public class Medico {

    private String nome;
    private String codigoEspecialidade;
    private int horaEntrada; // 1-24
    private int horaSaida;   // 1-24
    private double valorHora;

    // Estado do medico
    private boolean disponivel;
    private boolean emDescanso;
    private boolean presente; // Se esta no hospital
    private boolean emConsulta;

    // Controlo de trabalho
    private int horasTrabalhadasConsecutivas;
    private int tempoConsultaRestante;
    private int tempoDescansoRestante;

    // Paciente atual (se em consulta)
    private Paciente pacienteAtual;

    /**
     * Construtor do medico
     * @param nome Nome do medico
     * @param codigoEspecialidade Codigo da especialidade (ex: CARD, PEDI, ORTO)
     * @param horaEntrada Hora de entrada (1-24)
     * @param horaSaida Hora de saida (1-24)
     * @param valorHora Valor por hora em euros
     */
    public Medico(String nome, String codigoEspecialidade, int horaEntrada, int horaSaida, double valorHora) {
        this.nome = nome;
        this.codigoEspecialidade = codigoEspecialidade;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.valorHora = valorHora;

        // Estado inicial
        this.disponivel = false;
        this.emDescanso = false;
        this.presente = false;
        this.emConsulta = false;
        this.horasTrabalhadasConsecutivas = 0;
        this.tempoConsultaRestante = 0;
        this.tempoDescansoRestante = 0;
        this.pacienteAtual = null;
    }

    /**
     * Construtor a partir de uma linha de ficheiro
     * @param linhaFicheiro Linha no formato: Nome;Especialidade;HoraEntrada;HoraSaida;ValorHora
     * @param separador Separador usado no ficheiro
     */
    public Medico(String linhaFicheiro, String separador) {
        // Validação da linha
        if (linhaFicheiro == null || linhaFicheiro.trim().isEmpty()) {
            throw new IllegalArgumentException("Linha do ficheiro não pode estar vazia");
        }

        String[] dados = linhaFicheiro.split(separador);

        // VALIDAÇÃO: Verificar se tem todos os campos necessários
        if (dados.length < 5) {
            throw new IllegalArgumentException(
                    "Formato inválido na linha do médico. Esperado 5 campos (Nome;Especialidade;HoraEntrada;HoraSaida;ValorHora), " +
                            "encontrado " + dados.length + " campo(s). Linha: \"" + linhaFicheiro + "\""
            );
        }

        try {
            this.nome = dados[0].trim();
            this.codigoEspecialidade = dados[1].trim();
            this.horaEntrada = Integer.parseInt(dados[2].trim());
            this.horaSaida = Integer.parseInt(dados[3].trim());
            this.valorHora = Double.parseDouble(dados[4].trim());

            // Validações adicionais
            if (this.nome.isEmpty()) {
                throw new IllegalArgumentException("Nome do médico não pode estar vazio");
            }
            if (this.codigoEspecialidade.isEmpty()) {
                throw new IllegalArgumentException("Código da especialidade não pode estar vazio");
            }
            if (this.horaEntrada < 1 || this.horaEntrada > 24) {
                throw new IllegalArgumentException("Hora de entrada deve estar entre 1 e 24");
            }
            if (this.horaSaida < 1 || this.horaSaida > 24) {
                throw new IllegalArgumentException("Hora de saída deve estar entre 1 e 24");
            }
            if (this.valorHora < 0) {
                throw new IllegalArgumentException("Valor por hora não pode ser negativo");
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Erro ao converter valores numéricos na linha: \"" + linhaFicheiro + "\". " +
                            "Verifique se HoraEntrada, HoraSaida e ValorHora são números válidos.", e
            );
        }

        // Estado inicial
        this.disponivel = false;
        this.emDescanso = false;
        this.presente = false;
        this.emConsulta = false;
        this.horasTrabalhadasConsecutivas = 0;
        this.tempoConsultaRestante = 0;
        this.tempoDescansoRestante = 0;
        this.pacienteAtual = null;
    }

    // ==================== GESTAO DE CONSULTAS ====================

    /**
     * Inicia uma consulta com um paciente
     * @param paciente Paciente a atender
     * @param duracao Duracao da consulta em unidades de tempo
     * @return true se iniciou, false se nao esta disponivel
     */
    public boolean iniciarConsulta(Paciente paciente, int duracao) {
        if (!disponivel || emDescanso || emConsulta || !presente) {
            return false;
        }

        this.pacienteAtual = paciente;
        this.tempoConsultaRestante = duracao;
        this.emConsulta = true;
        this.disponivel = false;

        // Atualiza o paciente
        paciente.setEmConsulta(true);
        paciente.setMedicoAtribuido(this.nome);

        return true;
    }

    /**
     * Termina a consulta atual
     * @return Paciente que foi atendido, ou null se nao estava em consulta
     */
    public Paciente terminarConsulta() {
        if (!emConsulta || pacienteAtual == null) {
            return null;
        }

        Paciente pacienteAtendido = this.pacienteAtual;
        pacienteAtendido.setEmConsulta(false);

        this.pacienteAtual = null;
        this.tempoConsultaRestante = 0;
        this.emConsulta = false;
        this.disponivel = true;
        this.horasTrabalhadasConsecutivas++;

        return pacienteAtendido;
    }

    /**
     * Decrementa o tempo de consulta restante
     * @return true se a consulta terminou (tempo chegou a 0)
     */
    public boolean decrementarTempoConsulta() {
        if (!emConsulta || tempoConsultaRestante <= 0) {
            return false;
        }

        tempoConsultaRestante--;
        return tempoConsultaRestante == 0;
    }

    // ==================== GESTAO DE DESCANSO ====================

    /**
     * Inicia o periodo de descanso do medico
     * @param duracao Duracao do descanso em unidades de tempo
     */
    public void iniciarDescanso(int duracao) {
        this.emDescanso = true;
        this.disponivel = false;
        this.tempoDescansoRestante = duracao;
    }

    /**
     * Termina o periodo de descanso
     */
    public void terminarDescanso() {
        this.emDescanso = false;
        this.disponivel = true;
        this.tempoDescansoRestante = 0;
        this.horasTrabalhadasConsecutivas = 0;
    }

    /**
     * Decrementa o tempo de descanso restante
     * @return true se o descanso terminou (tempo chegou a 0)
     */
    public boolean decrementarTempoDescanso() {
        if (!emDescanso || tempoDescansoRestante <= 0) {
            return false;
        }

        tempoDescansoRestante--;
        return tempoDescansoRestante == 0;
    }

    // ==================== GESTAO DE PRESENCA ====================

    /**
     * Verifica se o medico deve estar presente na unidade de tempo atual
     * @param unidadeAtual Unidade de tempo atual (1-24)
     * @return true se deve estar presente
     */
    public boolean deveEstarPresente(int unidadeAtual) {
        // Considera horarios que atravessam a meia-noite
        if (horaEntrada <= horaSaida) {
            return unidadeAtual >= horaEntrada && unidadeAtual < horaSaida;
        } else {
            return unidadeAtual >= horaEntrada || unidadeAtual < horaSaida;
        }
    }

    /**
     * Faz o medico entrar no hospital
     */
    public void entrar() {
        this.presente = true;
        this.disponivel = true;
        this.horasTrabalhadasConsecutivas = 0;
    }

    /**
     * Faz o medico sair do hospital (se nao estiver em consulta)
     * @return true se saiu, false se estava em consulta
     */
    public boolean sair() {
        if (emConsulta) {
            return false; // Nao pode sair durante consulta
        }

        this.presente = false;
        this.disponivel = false;
        this.emDescanso = false;
        this.horasTrabalhadasConsecutivas = 0;
        return true;
    }

    // ==================== GETTERS E SETTERS ====================

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoEspecialidade() {
        return codigoEspecialidade;
    }

    public void setCodigoEspecialidade(String codigoEspecialidade) {
        this.codigoEspecialidade = codigoEspecialidade;
    }

    public int getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(int horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public int getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(int horaSaida) {
        this.horaSaida = horaSaida;
    }

    public double getValorHora() {
        return valorHora;
    }

    public void setValorHora(double valorHora) {
        this.valorHora = valorHora;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public boolean isEmDescanso() {
        return emDescanso;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public boolean isEmConsulta() {
        return emConsulta;
    }

    public int getHorasTrabalhadasConsecutivas() {
        return horasTrabalhadasConsecutivas;
    }

    public void setHorasTrabalhadasConsecutivas(int horas) {
        this.horasTrabalhadasConsecutivas = horas;
    }

    public int getTempoConsultaRestante() {
        return tempoConsultaRestante;
    }

    public int getTempoDescansoRestante() {
        return tempoDescansoRestante;
    }

    public Paciente getPacienteAtual() {
        return pacienteAtual;
    }

    // ==================== UTILIDADES ====================

    /**
     * Obtem o estado atual do medico como texto
     * @return Descricao do estado
     */
    public String getEstadoTexto() {
        if (!presente) {
            return "Ausente";
        } else if (emConsulta) {
            return "Em Consulta (" + tempoConsultaRestante + " un. restantes)";
        } else if (emDescanso) {
            return "Em Descanso (" + tempoDescansoRestante + " un. restantes)";
        } else if (disponivel) {
            return "Disponivel";
        } else {
            return "Indisponivel";
        }
    }

    /**
     * Converte para formato de ficheiro
     * @param separador Separador a usar
     * @return String no formato do ficheiro
     */
    public String toFicheiroString(String separador) {
        return nome + separador + codigoEspecialidade + separador +
                horaEntrada + separador + horaSaida + separador +
                (int) valorHora;
    }

    @Override
    public String toString() {
        return nome + " (" + codigoEspecialidade + ") - " + getEstadoTexto();
    }

    /**
     * Representacao detalhada do medico
     * @return String com todos os detalhes
     */
    public String toStringDetalhado() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== MEDICO ===\n");
        sb.append("Nome: ").append(nome).append("\n");
        sb.append("Especialidade: ").append(codigoEspecialidade).append("\n");
        sb.append("Horario: ").append(horaEntrada).append("h - ").append(horaSaida).append("h\n");
        sb.append("Valor/Hora: ").append(valorHora).append(" EUR\n");
        sb.append("Estado: ").append(getEstadoTexto()).append("\n");
        sb.append("Horas Consecutivas: ").append(horasTrabalhadasConsecutivas).append("\n");

        if (pacienteAtual != null) {
            sb.append("Paciente Atual: ").append(pacienteAtual.getNome()).append("\n");
        }

        return sb.toString();
    }
}