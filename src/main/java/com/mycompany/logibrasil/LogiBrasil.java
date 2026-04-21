
package com.mycompany.logibrasil;

import com.mycompany.logibrasil.armazem.Armazem;
import com.mycompany.logibrasil.model.Caixa;
import com.mycompany.logibrasil.model.Setor;
import com.mycompany.logibrasil.relatorio.Dashboard;
import java.util.Scanner;


public class LogiBrasil {

    public static void main(String[] args) {
       

        Scanner sc = new Scanner(System.in);
        
        Armazem armazem = new Armazem(3,3,5);
        
        while(true){
            System.out.println("\n== LogiBrasil ==");
            System.out.println("1 - Adicionar caixa");
            System.out.println("2 - Retirar caixa");
            System.out.println("3 - Remover caixa específica");
            System.out.println("4 - Buscar caixa");
            System.out.println("5 - Exibir relatório");
            System.out.println("0 - Sair");
            
            int opcao = sc.nextInt();
            sc.nextLine();
            
           switch (opcao) {
                case 1: {
                    System.out.println("\n#===========================#");
                    System.out.println("#      ADICIONAR CAIXA      #");
                    System.out.println("#===========================#");

                    System.out.print("ID da caixa: ");
                    String id = sc.nextLine();
                    System.out.print("Descricao: ");
                    String descricao = sc.nextLine();
                    System.out.print("Peso (kg): ");
                    double peso = sc.nextDouble();
                    sc.nextLine();

                    System.out.println("\n--- Localizacao do Setor ---");
                    System.out.print("Corredor (ex: A, B, C): ");
                    String corredor = sc.nextLine();
                    System.out.print("Prateleira (ex: 1, 2, 3): ");
                    int prateleira = sc.nextInt();
                    sc.nextLine();

                    Caixa caixa = new Caixa(id, descricao, peso);
                    boolean adicionado = armazem.adicionarCaixa(corredor, prateleira, caixa);

                    if (adicionado) {
                        System.out.println("\n[OK] Caixa adicionada com sucesso!");
                        System.out.println("  " + caixa + " -> Corredor " + corredor + " | Prateleira " + prateleira);
                    } else {
                        System.out.println("\n[ERRO] Operação falhou! Setor cheio ou ID ja cadastrado.");
                    }
                    break;
                }
                case 2: {
                    System.out.println("\n#===========================#");
                    System.out.println("#      RETIRAR CAIXA        #");
                    System.out.println("#===========================#");

                    System.out.println("\n--- Localizacao do Setor ---");
                    System.out.print("Corredor (ex: A, B, C): ");
                    String corredor = sc.nextLine();
                    System.out.print("Prateleira (ex: 1, 2, 3): ");
                    int prateleira = sc.nextInt();
                    sc.nextLine();

                    Caixa retirar = armazem.retirarCaixa(corredor, prateleira);

                    if (retirar != null) {
                        System.out.println("\n[OK] Caixa retirada com sucesso!");
                        System.out.println("  " + retirar);
                    } else {
                        System.out.println("\n[ERRO] Setor vazio! Escolha outro setor.");
                    }
                    break;
                }
                case 3: {
                    System.out.println("\n#===========================#");
                    System.out.println("#      REMOVER CAIXA        #");
                    System.out.println("#===========================#");

                    System.out.println("\n--- Localizacao do Setor ---");
                    System.out.print("Corredor (ex: A, B, C): ");
                    String corredor = sc.nextLine();
                    System.out.print("Prateleira (ex: 1, 2, 3): ");
                    int prateleira = sc.nextInt();
                    sc.nextLine();
                    System.out.print("ID da caixa: ");
                    String id = sc.nextLine();

                    Caixa remover = armazem.removerCaixaEspecifica(corredor, prateleira, id);

                    if (remover != null) {
                        System.out.println("\n[OK] Caixa removida com sucesso!");
                        System.out.println("  " + remover);
                    } else {
                        System.out.println("\n[ERRO] Caixa nao encontrada! Tente novamente.");
                    }
                    break;
                }
                case 4: {
                    System.out.println("\n#===========================#");
                    System.out.println("#       BUSCAR CAIXA        #");
                    System.out.println("#===========================#");

                    System.out.print("ID da caixa: ");
                    String id = sc.nextLine();

                    Setor buscar = armazem.buscarCaixa(id);

                    if (buscar != null) {
                        System.out.println("\n[OK] Caixa encontrada!");
                        System.out.println("  " + buscar);
                    } else {
                        System.out.println("\n[ERRO] Caixa nao encontrada! Tente novamente.");
                    }
                    break;
                }
                case 5: {
                    System.out.println("\n#===========================#");
                    System.out.println("#        RELATORIO          #");
                    System.out.println("#===========================#");
                    Dashboard.exibirRelatorio(armazem);
                    break;
                }
                case 0:
                    System.out.println("Encerrando o sistema...");
                    return;
                default:
                    System.out.println("[AVISO] Opcao invalida! Tente novamente.");
            }
        }
        
        
    }
}
