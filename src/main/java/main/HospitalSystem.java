
package main;

import database.ConexaoDB;
import hospital.Hospital;
import java.time.LocalDate;
import model.CaixaMedicamento;
import model.Setor;
import relatorio.Dashboard;
import java.util.Scanner;
import javax.swing.SwingUtilities;


public class HospitalSystem {

    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new HospitalGUI().setVisible(true));
}
}
