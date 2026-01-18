package Model;

/**
 * Modelo unificado de Sintoma
 * Substitui sintomasModel.java para uso em CRUDs e operacoes dia-a-dia
 */
public class Sintoma {
    private String nome;
    private String nivelUrgencia; // verde, laranja, vermelho
    private String codigoEspecialidade; // codigo da especialidade associada (pode ser null)

    /**
     * Construtor completo
     * @param nome Nome do sintoma
     * @param nivelUrgencia Nivel de urgencia (verde/laranja/vermelho)
     * @param codigoEspecialidade Codigo da especialidade associada
     */
    public Sintoma(String nome, String nivelUrgencia, String codigoEspecialidade) {
        this.nome = nome;
        this.nivelUrgencia = nivelUrgencia;
        this.codigoEspecialidade = codigoEspecialidade;
    }

    /**
     * Construtor a partir de uma linha de ficheiro
     * @param linhaFicheiro Linha no formato: Nome;NivelUrgencia;CodigoEspecialidade
     * @param separador Separador usado no ficheiro
     */
    public Sintoma(String linhaFicheiro, String separador) {
        String[] dados = linhaFicheiro.split(separador);
        this.nome = dados[0].trim();
        this.nivelUrgencia = dados.length > 1 ? dados[1].trim().toLowerCase() : "verde";
        this.codigoEspecialidade = dados.length > 2 ? dados[2].trim() : null;

        // Se a especialidade estiver vazia, considerar como null
        if (this.codigoEspecialidade != null && this.codigoEspecialidade.isEmpty()) {
            this.codigoEspecialidade = null;
        }
    }

    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNivelUrgencia() {
        return nivelUrgencia;
    }

    public void setNivelUrgencia(String nivelUrgencia) {
        this.nivelUrgencia = nivelUrgencia;
    }

    public String getCodigoEspecialidade() {
        return codigoEspecialidade;
    }

    public void setCodigoEspecialidade(String codigoEspecialidade) {
        this.codigoEspecialidade = codigoEspecialidade;
    }

    /**
     * Converte nivel de urgencia para valor numerico
     * @return 1=verde, 2=laranja, 3=vermelho
     */
    public int getNivelUrgenciaNumerico() {
        if (nivelUrgencia == null) return 1;
        switch (nivelUrgencia.toLowerCase()) {
            case "vermelho":
            case "vermelha":
                return 3;
            case "laranja":
                return 2;
            case "verde":
            default:
                return 1;
        }
    }

    /**
     * Valida se o nivel de urgencia e valido
     * @param nivel Nivel a validar
     * @return true se for valido
     */
    public static boolean isNivelValido(String nivel) {
        if (nivel == null) return false;
        String nivelLower = nivel.toLowerCase();
        return nivelLower.equals("verde") ||
               nivelLower.equals("laranja") ||
               nivelLower.equals("vermelho") ||
               nivelLower.equals("vermelha");
    }

    /**
     * Converte para formato de ficheiro
     * @param separador Separador a usar
     * @return String no formato do ficheiro
     */
    public String toFicheiroString(String separador) {
        String esp = (codigoEspecialidade != null) ? codigoEspecialidade : "";
        return nome + separador + nivelUrgencia + separador + esp;
    }

    @Override
    public String toString() {
        String esp = (codigoEspecialidade == null || codigoEspecialidade.isEmpty())
                     ? "Sem especialidade" : codigoEspecialidade;
        return nome + " | " + nivelUrgencia + " | " + esp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sintoma that = (Sintoma) obj;
        return nome != null && nome.equalsIgnoreCase(that.nome);
    }

    @Override
    public int hashCode() {
        return nome != null ? nome.toLowerCase().hashCode() : 0;
    }
}
