
package com.mycompany.logibrasil.model;

public class Pilha {
    
    private Caixa[] elementos;
    
    private int topo;
    private int capacidade;
    
    //Criamos o construtor das pilhas de caixa
    public Pilha (int capacidade){
        this.capacidade = capacidade;
        this.elementos = new Caixa[capacidade];
        this.topo = -1;
    }
    
    //metodo para ver se a pilha esta cheia
    public boolean estaCheia(){
        return topo == capacidade -1;
    }
    
    //metodo para ver se a pilha esta vazia
    public boolean estaVazia(){
        return topo == -1;
    }
    
    //metodo para empilhar
    public boolean empilhar(Caixa caixa){
        if (estaCheia()){ 
            return false; 
        }
        elementos[++topo] = caixa;
        return true;
    }
    
    //metodo para desempilhar
    public Caixa desempilhar(){
        if (estaVazia()) {
            return null;
        }
        return elementos[topo--];
    }
    
    public Caixa verTopo(){
        if (estaVazia()) {
            return null;
        }
        return elementos[topo];
    }
    
    public int getCapacidade(){
        return capacidade;
    }
    
    public int getTamanhoAtual(){
        return topo +1;
    }
    
    public Caixa[] getElementos(){
        return elementos;
    }
    
    @Override 
    public String toString(){
        return "Topo: " + verTopo() + " | Ocupação: " + getTamanhoAtual() + "/" + capacidade + " caixas";    }
}
