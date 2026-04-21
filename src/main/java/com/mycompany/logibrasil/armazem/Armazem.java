
package com.mycompany.logibrasil.armazem;

import com.mycompany.logibrasil.model.Caixa;
import com.mycompany.logibrasil.model.Pilha;
import com.mycompany.logibrasil.model.Setor;

public class Armazem {
    
    private Setor[][] setores;
    private int numCorredores;
    private int numPrateleiras;
    private int capacidadePorSetor;
    
    //Estamos criando o construtor do aramazem
    public Armazem(int numCorredores, int numPrateleiras, int capacidadePorSetor){
        this.numCorredores = numCorredores;
        this.numPrateleiras = numPrateleiras;
        this.capacidadePorSetor = capacidadePorSetor;
        //Criando a matriz
        this.setores = new Setor[numCorredores][numPrateleiras];
        
        for (int i = 0; i < numCorredores; i++) {
            for (int j = 0; j < numPrateleiras; j++) {
                //Estamos transformando o numero do corredor em letra 
                String corredor = String.valueOf((char) ('A' + i));
                //Dando valor ao construtor do setor, criado na Classe Setor
                setores[i][j] = new Setor(corredor, j + 1, capacidadePorSetor);
            }
        }     
    }
    
    //Método get do Setor
    public Setor getSetor(String corredor, int prateleira){
        int i = corredor.charAt(0) - 'A';
        int j = prateleira - 1;
        return setores[i][j];
    }
    
    //Método adicionar caixa
    public boolean adicionarCaixa(String corredor, int prateleira, Caixa caixa){  
        if (buscarCaixa(caixa.getId()) != null) {
            return false; //ja existe uma caixa com esse id
        }
        return getSetor(corredor, prateleira).adicionarCaixa(caixa);
    }
    
    //Método retirar caixa
    public Caixa retirarCaixa(String corredor, int prateleira){
        return getSetor(corredor, prateleira).removerCaixa();
    }
    
    //Método buscar caixa
    public Setor buscarCaixa(String id){
        for(int i = 0; i < numCorredores; i++){
            for (int j = 0; j < numPrateleiras; j++) {
                for (int k = 0; k < setores[i][j].getPilha().getTamanhoAtual(); k++) {
                    if(id.equals(setores[i][j].getPilha().getElementos()[k].getId())){
                        return setores[i][j];
                    }
                }
            }
        }
        return null;
}
    
    
    //Método para reempilhamento (tirar caixa especifica da pilha)
    public Caixa removerCaixaEspecifica(String corredor, int prateleira, String id){
        Setor setor = getSetor(corredor, prateleira);
        //estamos criando uma nova pilha temporaria e puxando o getCapacidade
        Pilha pilhaTemp = new Pilha(setor.getPilha().getCapacidade());
        
        //"Vá ao setor, olhe a pilha, veja a caixa do topo, pegue o id dela e compare com o que estou procurando."
        while(!setor.getPilha().verTopo().getId().equals(id)){
            Caixa caixaTemp = setor.getPilha().desempilhar();
            pilhaTemp.empilhar(caixaTemp); //coloca na pilha temporária
        }
        
        //Guardando a caixa que queriamos
        Caixa caixaRemovida = setor.getPilha().desempilhar();
        
        //Devolver as caixas da pilha temporaria]
        while(!pilhaTemp.estaVazia()){   
            //Esta recebendo as caixas da pilhaTemp e empilhando na outra pilha
            setor.getPilha().empilhar(pilhaTemp.desempilhar());
        }
        
        //Estamos retornando a caixa removida
        return caixaRemovida;
    }
    
    public Setor[][] getSetores(){
        return setores;
    }
             
   public int getNumCorredores(){
       return numCorredores;
   }      
   
   public int getNumPrateleiras(){
       return numPrateleiras;
   }
}


