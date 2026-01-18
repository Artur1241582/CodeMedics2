package Model;

/**
 * Modelo unificado de Especialidade
 * Substitui especialidadeModel.java para uso em CRUDs e operacoes dia-a-dia
 */
public class Especialidade {
    private String codigo;
    private String nome;

    /**
     * Construtor
     * @param codigo Codigo da especialidade (ex: CARD, PEDI, ORTO)
     * @param nome Nome da especialidade
     */
    public Especialidade(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    /**
     * Cria uma Especialidade a partir de uma linha de ficheiro
     * @param linhaFicheiro Linha no formato: Codigo;Nome
     * @param separador Separador usado no ficheiro
     * @return Nova instancia de Especialidade
     */
    public static Especialidade fromFicheiro(String linhaFicheiro, String separador) {
        String[] dados = linhaFicheiro.split(separador);
        String codigo = dados[0].trim();
        String nome = dados.length > 1 ? dados[1].trim() : "";
        return new Especialidade(codigo, nome);
    }

    // Getters e Setters

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Converte para formato de ficheiro
     * @param separador Separador a usar
     * @return String no formato do ficheiro
     */
    public String toFicheiroString(String separador) {
        return codigo + separador + nome;
    }

    @Override
    public String toString() {
        return codigo + " - " + nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Especialidade that = (Especialidade) obj;
        return codigo != null && codigo.equalsIgnoreCase(that.codigo);
    }

    @Override
    public int hashCode() {
        return codigo != null ? codigo.toUpperCase().hashCode() : 0;
    }
}
