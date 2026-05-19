package database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ConexaoDB {

    private static String URL;
    private static String USUARIO;
    private static String SENHA;

    static {
        try {
            Properties props = new Properties();
            String caminho = System.getProperty("user.dir") + "/config.properties";
            props.load(new FileInputStream(caminho));
            URL = props.getProperty("db.url");
            USUARIO = props.getProperty("db.usuario");
            SENHA = props.getProperty("db.senha");
        } catch (Exception e) {
            System.out.println("[DB] Erro ao carregar config.properties: " + e.getMessage());
        }
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
    
    public static void inicializarBanco() {
    String sql = """
        CREATE TABLE IF NOT EXISTS movimentacoes (
            id INT AUTO_INCREMENT PRIMARY KEY,
            data_hora DATETIME DEFAULT NOW(),
            operacao VARCHAR(50) NOT NULL,
            id_caixa VARCHAR(50) NOT NULL,
            medicamento VARCHAR(100) NOT NULL,
            lote VARCHAR(50) NOT NULL,
            validade DATE NOT NULL,
            quantidade INT NOT NULL,
            setor VARCHAR(50) NOT NULL
        )
    """;

    try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
        stmt.execute(sql);
        System.out.println("[DB] Banco inicializado com sucesso!");
    } catch (SQLException e) {
        System.out.println("[DB] Erro ao inicializar banco: " + e.getMessage());
    }
}


    public static void registrarMovimentacao(String operacao, String idCaixa, String medicamento,
            String lote, String validade, int quantidade, String setor) {

        String sql = """
            INSERT INTO movimentacoes (operacao, id_caixa, medicamento, lote, validade, quantidade, setor)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = conectar(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, operacao);
            pstmt.setString(2, idCaixa);
            pstmt.setString(3, medicamento);
            pstmt.setString(4, lote);
            pstmt.setString(5, validade);
            pstmt.setInt(6, quantidade);
            pstmt.setString(7, setor);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("[DB] Erro ao registrar: " + e.getMessage());
        }
    }
}