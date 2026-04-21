
package com.mycompany.logibrasil.model;

public class Setor {
    
    private String corredor;
    private int prateleira;
    private Pilha pilha;
    
    //Construtor
    public Setor (String corredor, int prateleira, int capacidade){
        this.corredor = corredor;
        this.prateleira = prateleira;
        this.pilha = new Pilha(capacidade);
    }
    
    public boolean adicionarCaixa(Caixa caixa){
        return pilha.empilhar(caixa);
    }
    
    public Caixa removerCaixa(){
        return pilha.desempilhar();
    }
    
    public String getCorredor(){
        return corredor;
    }
    
    public int getPrateleira(){
        return prateleira;
    }
    
    public Pilha getPilha(){
        return pilha;
    }
    
    @Override
    public String toString(){
          return "[Corredor: " + corredor + " | Prateleira: " + prateleira + " | " + pilha + "]";
    }
}
