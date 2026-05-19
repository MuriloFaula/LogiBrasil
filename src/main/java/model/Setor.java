
package model;

public class Setor {
    
    private String nome;
    private String especialidade;
    private Pilha pilha;
    
    
    //Construtor
    public Setor (String nome, String especialidade, int capacidade){
        this.nome = nome;
        this.especialidade = especialidade;
        this.pilha = new Pilha(capacidade);
    }
    
    public boolean adicionarCaixaMedicamento(CaixaMedicamento medicamento){
        return pilha.empilhar(medicamento);
    }
    
    public CaixaMedicamento removerCaixaMedicamento(){
        return pilha.desempilhar();
    }
    
    public String getNome(){
        return nome;
    }
    
    public String getEspecialidade(){
        return especialidade;
    }
    
    public Pilha getPilha(){
        return pilha;
    }
    
    @Override
    public String toString(){
           return "[Setor: " + nome + " | Especialidade: " + especialidade + " | " + pilha + "]";
    }
}
