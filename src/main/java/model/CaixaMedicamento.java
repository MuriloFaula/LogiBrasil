
package model;

import java.time.LocalDate;

public class CaixaMedicamento {
    
    //Identificação de atributos da classe CaixaMedicamento
    //oq e importante conter na caixa
    private String id;
    private String nomeMedicamento;
    private String lote;
    private LocalDate dataValidade;
    private int quantidade;
    
    //Construtor das caixas de medicamentos
    public CaixaMedicamento(String id, String nomeMedicamento, String lote, LocalDate dataValidade, int quantidade){
        this.id = id;
        this.nomeMedicamento = nomeMedicamento;
        this.lote = lote;
        this.dataValidade = dataValidade;
        this.quantidade = quantidade;
    }
    
    public String getId(){
        return id;
    }
    
    public String getNomeMedicamento(){
        return nomeMedicamento;
    }
    
    public String getLote(){
        return lote;
    }
    
    public LocalDate getDataValidade(){
        return dataValidade;
    }
    
    public int getQuantidade(){
        return quantidade;
    }

    @Override
    public String toString() {
       return "[" + nomeMedicamento + " | Lote: " + lote + " | Val: " + dataValidade + " | Qtd: " + quantidade + "]";
    }
    


    
}
