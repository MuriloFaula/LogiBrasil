
package com.mycompany.logibrasil.relatorio;

import com.mycompany.logibrasil.armazem.Armazem;
import com.mycompany.logibrasil.model.Setor;

public class Dashboard {
    
    public static void exibirRelatorio(Armazem armazem){
        Setor[][] setores = armazem.getSetores();
        
        for (int i = 0; i < armazem.getSetores().length; i++) {
            for (int j = 0; j < armazem.getSetores()[0].length; j++) {
                //A variavel setor vai receber o valor da matriz setores
                Setor setor = setores[i][j];
                //Variavel atual vai usar o metodo tamanhoAtual para ver o tamanho do setor
                int atual = setor.getPilha().getTamanhoAtual();
                //Aqui a variavel capacidade esta usando o metodo capacidade para ver a capacidade do setor
                int capacidade = setor.getPilha().getCapacidade();
                //Vamos fazer o calculo para ver se ultrapassa 80% da capacidade
                //O (double) esta forçando a realizarem uma divisao com decimais
                double ocupacao = (double)atual / capacidade * 100;
                
                System.out.println(setor);
                
                if (ocupacao > 80) {
                    System.out.println("⚠ ️ALERTA: Setor acima de 80%!");
                }
            }
        }
    }
    
}
