
package model;

public class Pilha {
    
    private CaixaMedicamento[] elementos;
    
    private int topo;
    private int capacidade;
    
    //Criamos o construtor das pilhas de caixa de medicamento
    public Pilha (int capacidade){
        this.capacidade = capacidade;
        this.elementos = new CaixaMedicamento[capacidade];
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
    public boolean empilhar(CaixaMedicamento caixaMedicamento){
        if (estaCheia()){ 
            return false; 
        }
        elementos[++topo] = caixaMedicamento;
        return true;
    }
    
    //metodo para desempilhar
    public CaixaMedicamento desempilhar(){
        if (estaVazia()) {
            return null;
        }
         CaixaMedicamento removido = elementos[topo];
        elementos[topo--] = null;
        return removido;
    }
    
    public CaixaMedicamento verTopo(){
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
    
    public CaixaMedicamento[] getElementos(){
        return elementos;
    }
    
    @Override 
    public String toString(){
        return "Topo: " + verTopo() + " | Ocupação: " + getTamanhoAtual() + "/" + capacidade + " caixas";    }
}
