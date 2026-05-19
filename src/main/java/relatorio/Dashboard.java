
package relatorio;

import hospital.Hospital;
import java.time.LocalDate;
import model.CaixaMedicamento;
import model.Setor;

public class Dashboard {
    
    public static void exibirRelatorio(Hospital armazem){
        Setor[] setores = armazem.getSetores();
        
        for (int i = 0; i < armazem.getSetores().length; i++) {            
                //A variavel setor vai receber o valor do vetor setores
                Setor setor = setores[i];
                //Variavel atual vai usar o metodo tamanhoAtual para ver o tamanho do setor
                int atual = setor.getPilha().getTamanhoAtual();
                //Aqui a variavel capacidade esta usando o metodo capacidade para ver a capacidade do setor
                int capacidade = setor.getPilha().getCapacidade();
                //Vamos fazer o calculo para ver se ultrapassa 80% da capacidade
                //O (double) esta forçando a realizarem uma divisao com decimais
                double ocupacao = (double)atual / capacidade * 100;
                
                System.out.println(setor);
                
                if (ocupacao > 80) {
                    System.out.println("[ALERTA] Setor acima de 80% da capacidade!");
                }
                
                // Percorre as caixas da pilha verificando validade
                LocalDate hoje = LocalDate.now();

                for (int j = 0; j < setor.getPilha().getTamanhoAtual(); j++) {
                    CaixaMedicamento cm = setor.getPilha().getElementos()[j];

                    if (cm.getDataValidade().isBefore(hoje)) {
                        System.out.println("[VENCIDO] " + cm.getNomeMedicamento() + " | Lote: " + cm.getLote() + " | Venceu em: " + cm.getDataValidade());
                    } else if (cm.getDataValidade().isBefore(hoje.plusDays(30))) {
                        System.out.println("[ATENCAO] " + cm.getNomeMedicamento() + " | Lote: " + cm.getLote() + " | Vence em: " + cm.getDataValidade());
                    }
                    }
                
            }
        }
    }
    

