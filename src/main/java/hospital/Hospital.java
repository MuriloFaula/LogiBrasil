
package hospital;

import model.CaixaMedicamento;
import model.Pilha;
import model.Setor;

public class Hospital {
    
    private Setor[] setores;
    private String nome; //nome do Hospital

    
    //Estamos criando o construtor do aramazem
    public Hospital(String nome, Setor[] setores ){
        this.nome = nome;
        this.setores = setores;

        }     
    
    
    //Método get do Setor
    public Setor getSetor(String nome){
        for (int i = 0; i < setores.length; i++) {
            if(nome.equals(setores[i].getNome())){
                return setores[i];
        }  
        }
        return null;
    }
    
    //Método adicionar caixa
    public boolean adicionarCaixaMedicamento(String nomeSetor, CaixaMedicamento caixaMedicamento){  
        if (buscarCaixa(caixaMedicamento.getId()) != null) {
            return false; //ja existe uma caixa de medicamentos com esse id
        }
        return getSetor(nomeSetor).adicionarCaixaMedicamento(caixaMedicamento);
    }
    
    //Método retirar caixa
    public CaixaMedicamento retirarCaixa(String nomeSetor){
        return getSetor(nomeSetor).removerCaixaMedicamento();
    }
    
    //Método buscar caixa
    public Setor buscarCaixa(String id){
            for (int i = 0; i < setores.length; i++) {
                for (int j = 0; j < setores[i].getPilha().getTamanhoAtual(); j++) {
                    if(id.equals(setores[i].getPilha().getElementos()[j].getId())){
                        return setores[i];
                    }
                }     
        }
        return null;
}
    
    
    //Método para reempilhamento (tirar caixa especifica da pilha)
    public CaixaMedicamento removerCaixaMedicamentoEspecifico(String nomeSetor, String id){
        Setor setor = getSetor(nomeSetor);
        //estamos criando uma nova pilha temporaria e puxando o getCapacidade
        Pilha pilhaTemp = new Pilha(setor.getPilha().getCapacidade());
        
        //"Vá ao setor, olhe a pilha, veja a caixa do topo, pegue o id dela e compare com o que estou procurando."
        while(!setor.getPilha().verTopo().getId().equals(id)){
            CaixaMedicamento caixaTemp = setor.getPilha().desempilhar();
            pilhaTemp.empilhar(caixaTemp); //coloca na pilha temporária
        }
        
        //Guardando a caixa que queriamos
        CaixaMedicamento caixaRemovida = setor.getPilha().desempilhar();
        
        //Devolver as caixas da pilha temporaria]
        while(!pilhaTemp.estaVazia()){   
            //Esta recebendo as caixas da pilhaTemp e empilhando na outra pilha
            setor.getPilha().empilhar(pilhaTemp.desempilhar());
        }
        
        //Estamos retornando a caixa removida
        return caixaRemovida;
    }
    
    public Setor[] getSetores(){
        return setores;
    }
    
    public String getNome(){
        return nome; 
    }
             
}


