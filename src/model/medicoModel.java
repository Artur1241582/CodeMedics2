package model;

public class medicoModel {
    private String nome;
    private especialidadeModel especialidade;  // ← Usar especialidadeModel
    private int horaEntrada;
    private int horaSaida;
    private double valorHora;

    public medicoModel(String nome, especialidadeModel especialidade, int horaEntrada, int horaSaida, double valorHora) {
        this.nome = nome;
        this.especialidade = especialidade;
        this.horaEntrada = horaEntrada;
        this.horaSaida = horaSaida;
        this.valorHora = valorHora;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public especialidadeModel getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(especialidadeModel especialidade) {
        this.especialidade = especialidade;
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

    @Override
    public String toString() {
        return nome + ";" + especialidade.getNome() + ";" + horaEntrada + ";" + horaSaida + ";" + valorHora;
    }
}
