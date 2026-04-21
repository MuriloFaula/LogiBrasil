
package com.mycompany.logibrasil.model;

public class Caixa {
    
    //Identificação de atributos da classe Caixa
    private String id;
    private String descricao;
    private double peso; // -> importante colocar peso pensando na questao de um armazem, os funcionarios precisam saber o pesso para saber qual a ordem da ppilha colocar
    
    //Construtor das caixas
    public Caixa(String id, String descricao, double peso){
        this.id = id;
        this.descricao = descricao;
        this.peso = peso;
    }
    
    public String getId(){
        return id;
    }
    
    public String getDescricao(){
        return descricao;
    }
    
    public double getPeso(){
        return peso;
    }
    
    @Override
    public String toString() {
        return "[Caixa " + id + " | " + descricao + " | " + peso + "kg]";
    }
    
}
