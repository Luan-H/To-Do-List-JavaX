public class Tarefa {
    private String nome;
    private String status;

    public Tarefa(String nome) {
        this.nome = nome;
        this.status = "Não";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}