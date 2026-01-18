package Model;

public class sintomasModel {
    private String nome;
    private String nivelUrgencia; // VERDE, LARANJA, VERMELHO
    private especialidadeModel especialidade; // pode ser null

    public sintomasModel(String nome, String nivelUrgencia, especialidadeModel especialidade) {
        this.nome = nome;
        this.nivelUrgencia = nivelUrgencia;
        this.especialidade = especialidade;
    }

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

    public especialidadeModel getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(especialidadeModel especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        String esp = (especialidade == null) ? "Sem especialidade" : especialidade.getNome();
        return nome + " | " + nivelUrgencia + " | " + esp;
    }
}
