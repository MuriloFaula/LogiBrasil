package main;

import database.ConexaoDB;
import hospital.Hospital;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import javax.swing.border.*;
import model.CaixaMedicamento;
import model.Setor;

public class HospitalGUI extends JFrame {

    // ─── CORES ──────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(15, 23, 42);
    private static final Color BG_CARD      = new Color(30, 41, 59);
    private static final Color BG_INPUT     = new Color(51, 65, 85);
    private static final Color ACCENT_BLUE  = new Color(59, 130, 246);
    private static final Color ACCENT_GREEN = new Color(34, 197, 94);
    private static final Color ACCENT_RED   = new Color(239, 68, 68);
    private static final Color ACCENT_AMBER = new Color(251, 191, 36);
    private static final Color TEXT_PRIMARY = new Color(241, 245, 249);
    private static final Color TEXT_MUTED   = new Color(148, 163, 184);
    private static final Color BORDER_COLOR = new Color(71, 85, 105);

    // ─── ESTADO ─────────────────────────────────────────────────────
    private Hospital hospital;
    private JTextArea logArea;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JLabel statusLabel;
    private JPanel dashboardPanel;

    // Campos dos formulários
    private JTextField fieldId, fieldNome, fieldLote, fieldValidade, fieldQtd, fieldSetor;
    private JTextField fieldSetorRetirar;
    private JTextField fieldSetorRemover, fieldIdRemover;
    private JTextField fieldIdBuscar;

    public HospitalGUI() {
        // Inicializa banco
        ConexaoDB.inicializarBanco();

        // Cria hospital com setores
        Setor[] setores = {
            new Setor("UTI",        "Cardiologia", 10),
            new Setor("Emergencia", "Geral",        10),
            new Setor("Pediatria",  "Pediatria",    10)
        };
        hospital = new Hospital("Hospital Central", setores);

        buildUI();
    }

    // ─── BUILD UI ───────────────────────────────────────────────────
    private void buildUI() {
        setTitle("Hospital Central — Sistema de Medicamentos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1020, 700);
        setMinimumSize(new Dimension(880, 580));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(buildSidebar(),   BorderLayout.WEST);
        add(buildMainArea(),  BorderLayout.CENTER);

        // Abre no dashboard
        showPanel("dashboard");
        statusLabel.setText("  Sistema operacional");
        statusLabel.setForeground(ACCENT_GREEN);
    }

    // ─── SIDEBAR ────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(BG_CARD);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new MatteBorder(0, 0, 0, 1, BORDER_COLOR));

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 18));
        header.setBackground(BG_CARD);
        header.setMaximumSize(new Dimension(230, 70));

        JLabel icon = new JLabel("+");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 26));
        icon.setForeground(ACCENT_BLUE);

        JPanel titleGroup = new JPanel();
        titleGroup.setBackground(BG_CARD);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));
        titleGroup.add(styledLabel("Hospital Central", TEXT_PRIMARY, 14, Font.BOLD));
        titleGroup.add(styledLabel("Gestao de Medicamentos", TEXT_MUTED, 11, Font.PLAIN));

        header.add(icon);
        header.add(titleGroup);
        sidebar.add(header);
        sidebar.add(separator());

        // Navegacao
        String[][] items = {
            {"[=]", "Dashboard",          "dashboard"},
            {"[+]", "Adicionar Caixa",    "adicionar"},
            {"[^]", "Retirar do Topo",    "retirar"},
            {"[x]", "Remover Especifica", "remover"},
            {"[?]", "Buscar Medicamento", "buscar"},
        };
        for (String[] item : items) {
            sidebar.add(navButton(item[0], item[1], item[2]));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.add(separator());

        // Status
        statusLabel = styledLabel("  Inicializando...", TEXT_MUTED, 11, Font.PLAIN);
        statusLabel.setBorder(new EmptyBorder(10, 14, 12, 0));
        statusLabel.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(statusLabel);

        return sidebar;
    }

    private JButton navButton(String icon, String label, String panel) {
        JButton btn = new JButton(icon + "  " + label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setForeground(TEXT_MUTED);
        btn.setBackground(BG_CARD);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(230, 42));
        btn.setPreferredSize(new Dimension(230, 42));
        btn.setBorder(new EmptyBorder(0, 16, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(BG_INPUT);
                btn.setForeground(TEXT_PRIMARY);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BG_CARD);
                btn.setForeground(TEXT_MUTED);
            }
        });
        btn.addActionListener(e -> showPanel(panel));
        return btn;
    }

    // ─── MAIN AREA ──────────────────────────────────────────────────
    private JPanel buildMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BG_DARK);

        cardLayout  = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_DARK);

        dashboardPanel = buildDashboardPanel();
        contentPanel.add(dashboardPanel,      "dashboard");
        contentPanel.add(buildAdicionarPanel(), "adicionar");
        contentPanel.add(buildRetirarPanel(),   "retirar");
        contentPanel.add(buildRemoverPanel(),   "remover");
        contentPanel.add(buildBuscarPanel(),    "buscar");

        // Log
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(new Color(10, 15, 28));
        logArea.setForeground(new Color(134, 239, 172));
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        logArea.setMargin(new Insets(10, 12, 10, 12));
        logArea.setText(">> Sistema iniciado. Aguardando operacoes...\n");

        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(new Dimension(0, 150));
        logScroll.setBorder(null);
        logScroll.getViewport().setBackground(new Color(10, 15, 28));

        JLabel logHeader = styledLabel("  LOG DO SISTEMA", TEXT_MUTED, 10, Font.BOLD);
        logHeader.setBackground(new Color(10, 15, 28));
        logHeader.setOpaque(true);
        logHeader.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(6, 12, 6, 0)
        ));

        // Botao limpar log
        JButton clearLog = new JButton("Limpar");
        clearLog.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        clearLog.setForeground(TEXT_MUTED);
        clearLog.setBackground(new Color(10, 15, 28));
        clearLog.setBorderPainted(false);
        clearLog.setFocusPainted(false);
        clearLog.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearLog.addActionListener(e -> logArea.setText(">> Log limpo.\n"));

        JPanel logTopBar = new JPanel(new BorderLayout());
        logTopBar.setBackground(new Color(10, 15, 28));
        logTopBar.add(logHeader, BorderLayout.CENTER);
        logTopBar.add(clearLog, BorderLayout.EAST);
        logTopBar.setBorder(new MatteBorder(1, 0, 0, 0, BORDER_COLOR));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(BG_DARK);
        bottom.add(logTopBar,  BorderLayout.NORTH);
        bottom.add(logScroll,  BorderLayout.CENTER);

        main.add(contentPanel, BorderLayout.CENTER);
        main.add(bottom,       BorderLayout.SOUTH);
        return main;
    }

    // ─── DASHBOARD ──────────────────────────────────────────────────
    private JPanel buildDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(28, 28, 20, 28));

        // Titulo + botao refresh
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG_DARK);
        topBar.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));

        JLabel title = styledLabel("Dashboard — Visao Geral", TEXT_PRIMARY, 20, Font.BOLD);
        JButton refresh = actionButton("Atualizar", ACCENT_BLUE);
        refresh.setPreferredSize(new Dimension(110, 32));
        refresh.addActionListener(e -> atualizarDashboard());

        topBar.add(title,   BorderLayout.WEST);
        topBar.add(refresh, BorderLayout.EAST);
        p.add(topBar, BorderLayout.NORTH);

        // Cards dos setores
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setBackground(BG_DARK);
        cards.setName("cardsArea");
        p.add(cards, BorderLayout.CENTER);

        // Alertas de validade
        JPanel alertsWrapper = new JPanel(new BorderLayout());
        alertsWrapper.setBackground(BG_DARK);

        JLabel alertTitle = styledLabel("Alertas de Validade", TEXT_PRIMARY, 14, Font.BOLD);
        alertTitle.setBorder(new EmptyBorder(16, 0, 8, 0));
        alertsWrapper.add(alertTitle, BorderLayout.NORTH);

        JPanel alertsContent = new JPanel();
        alertsContent.setLayout(new BoxLayout(alertsContent, BoxLayout.Y_AXIS));
        alertsContent.setBackground(BG_DARK);
        alertsContent.setName("alertsContent");

        JScrollPane alertsScroll = new JScrollPane(alertsContent);
        alertsScroll.setBorder(null);
        alertsScroll.getViewport().setBackground(BG_DARK);
        alertsScroll.setPreferredSize(new Dimension(0, 120));
        alertsWrapper.add(alertsScroll, BorderLayout.CENTER);

        p.add(alertsWrapper, BorderLayout.SOUTH);
        return p;
    }

    private void atualizarDashboard() {
        // Pega os paineis pelo nome
        JPanel cards = null;
        JPanel alertsContent = null;

        for (Component c : dashboardPanel.getComponents()) {
            if (c instanceof JPanel && "cardsArea".equals(c.getName())) {
                cards = (JPanel) c;
            }
        }

        // Procura alertsContent dentro do wrapper
        for (Component c : dashboardPanel.getComponents()) {
            if (c instanceof JPanel) {
                JPanel wrapper = (JPanel) c;
                for (Component inner : wrapper.getComponents()) {
                    if (inner instanceof JScrollPane) {
                        JScrollPane sp = (JScrollPane) inner;
                        Component view = sp.getViewport().getView();
                        if (view instanceof JPanel && "alertsContent".equals(((JPanel)view).getName())) {
                            alertsContent = (JPanel) view;
                        }
                    }
                }
            }
        }

        if (cards == null) return;

        // Atualiza cards dos setores
        cards.removeAll();
        Setor[] setores = hospital.getSetores();

        for (Setor setor : setores) {
            int atual = setor.getPilha().getTamanhoAtual();
            int cap   = setor.getPilha().getCapacidade();
            double pct = cap > 0 ? (double) atual / cap * 100 : 0;
            Color barColor = pct > 80 ? ACCENT_RED : pct > 50 ? ACCENT_AMBER : ACCENT_GREEN;

            JPanel card = new JPanel(new BorderLayout(0, 8));
            card.setBackground(BG_CARD);
            card.setBorder(new CompoundBorder(
                new LineBorder(pct > 80 ? ACCENT_RED : BORDER_COLOR, 1, true),
                new EmptyBorder(16, 16, 16, 16)
            ));

            // Topo do card
            JPanel cardTop = new JPanel(new BorderLayout());
            cardTop.setBackground(BG_CARD);
            cardTop.add(styledLabel(setor.getNome(), TEXT_PRIMARY, 16, Font.BOLD), BorderLayout.NORTH);
            cardTop.add(styledLabel(setor.getEspecialidade(), TEXT_MUTED, 12, Font.PLAIN), BorderLayout.SOUTH);
            card.add(cardTop, BorderLayout.NORTH);

            // Contador
            card.add(styledLabel(atual + " / " + cap + " caixas", barColor, 22, Font.BOLD), BorderLayout.CENTER);

            // Barra de progresso + alerta
            JPanel barBg = new JPanel(new BorderLayout()) {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(BG_INPUT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                    g2.setColor(barColor);
                    int w = (int) (getWidth() * (double) atual / cap);
                    g2.fillRoundRect(0, 0, Math.max(w, 0), getHeight(), 4, 4);
                }
            };
            barBg.setOpaque(false);
            barBg.setPreferredSize(new Dimension(0, 8));

            JPanel cardBottom = new JPanel();
            cardBottom.setLayout(new BoxLayout(cardBottom, BoxLayout.Y_AXIS));
            cardBottom.setBackground(BG_CARD);
            cardBottom.add(barBg);
            cardBottom.add(Box.createVerticalStrut(6));

            if (pct > 80) {
                JLabel alert = styledLabel("Acima de 80% da capacidade!", ACCENT_RED, 11, Font.BOLD);
                cardBottom.add(alert);
            } else {
                JLabel pctLabel = styledLabel(String.format("%.0f%% utilizado", pct), TEXT_MUTED, 11, Font.PLAIN);
                cardBottom.add(pctLabel);
            }

            card.add(cardBottom, BorderLayout.SOUTH);
            cards.add(card);
        }

        // Atualiza alertas de validade
        if (alertsContent != null) {
            alertsContent.removeAll();
            boolean found = false;
            LocalDate hoje = LocalDate.now();

            for (Setor setor : setores) {
                for (int j = 0; j < setor.getPilha().getTamanhoAtual(); j++) {
                    CaixaMedicamento cm = setor.getPilha().getElementos()[j];
                    if (cm.getDataValidade().isBefore(hoje)) {
                        alertsContent.add(alertRow("VENCIDO", cm, setor.getNome(), ACCENT_RED));
                        found = true;
                    } else if (cm.getDataValidade().isBefore(hoje.plusDays(30))) {
                        alertsContent.add(alertRow("VENCE EM 30 DIAS", cm, setor.getNome(), ACCENT_AMBER));
                        found = true;
                    }
                }
            }

            if (!found) {
                JLabel ok = styledLabel("Nenhum medicamento vencido ou proximo do vencimento.", ACCENT_GREEN, 13, Font.PLAIN);
                ok.setAlignmentX(LEFT_ALIGNMENT);
                alertsContent.add(ok);
            }

            alertsContent.revalidate();
            alertsContent.repaint();
        }

        cards.revalidate();
        cards.repaint();
    }

    private JPanel alertRow(String tag, CaixaMedicamento cm, String setor, Color cor) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        row.setBackground(BG_DARK);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        row.setAlignmentX(LEFT_ALIGNMENT);

        JLabel badge = new JLabel(tag);
        badge.setForeground(cor);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setBorder(new CompoundBorder(
            new LineBorder(cor, 1, true),
            new EmptyBorder(1, 6, 1, 6)
        ));

        String info = cm.getNomeMedicamento() + " | Lote: " + cm.getLote()
            + " | Val: " + cm.getDataValidade() + " | Setor: " + setor;
        row.add(badge);
        row.add(styledLabel(info, TEXT_MUTED, 12, Font.PLAIN));
        return row;
    }

    // ─── PAINEL ADICIONAR ────────────────────────────────────────────
    private JPanel buildAdicionarPanel() {
        JPanel p = panelWithPadding("Adicionar Caixa de Medicamento");

        JPanel form = formPanel();
        fieldId       = addField(form, "ID do medicamento");
        fieldNome     = addField(form, "Nome do medicamento");
        fieldLote     = addField(form, "Lote");
        fieldValidade = addField(form, "Data de validade (formato: 2027-12-31)");
        fieldQtd      = addField(form, "Quantidade");
        fieldSetor    = addField(form, "Setor (UTI / Emergencia / Pediatria)");

        JButton btn = actionButton("Adicionar Caixa", ACCENT_GREEN);
        btn.addActionListener(e -> executarAdicionar());
        form.add(Box.createVerticalStrut(20));
        form.add(btn);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_DARK);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private void executarAdicionar() {
        try {
            String id       = fieldId.getText().trim();
            String nome     = fieldNome.getText().trim();
            String lote     = fieldLote.getText().trim();
            String dataStr  = fieldValidade.getText().trim();
            String qtdStr   = fieldQtd.getText().trim();
            String setor    = fieldSetor.getText().trim();

            if (id.isEmpty() || nome.isEmpty() || lote.isEmpty() || dataStr.isEmpty() || qtdStr.isEmpty() || setor.isEmpty()) {
                log("[ERRO] Preencha todos os campos.");
                showError("Preencha todos os campos.");
                return;
            }

            int qtd = Integer.parseInt(qtdStr);
            if (qtd <= 0) { log("[ERRO] Quantidade deve ser maior que zero."); showError("Quantidade invalida."); return; }

            LocalDate dataValidade = LocalDate.parse(dataStr);
            CaixaMedicamento caixa = new CaixaMedicamento(id, nome, lote, dataValidade, qtd);
            boolean ok = hospital.adicionarCaixaMedicamento(setor, caixa);

            if (ok) {
                log("[OK] Caixa adicionada com sucesso!");
                log("     " + caixa + " -> Setor: " + setor);
                ConexaoDB.registrarMovimentacao("ENTRADA", id, nome, lote, dataStr, qtd, setor);
                clearFields(fieldId, fieldNome, fieldLote, fieldValidade, fieldQtd, fieldSetor);
                showSuccess("Caixa adicionada!");
                atualizarDashboard();
            } else {
                log("[ERRO] Setor cheio ou ID ja cadastrado.");
                showError("Setor cheio ou ID ja cadastrado.");
            }
        } catch (NumberFormatException ex) {
            log("[ERRO] Quantidade deve ser um numero inteiro.");
            showError("Quantidade invalida.");
        } catch (DateTimeParseException ex) {
            log("[ERRO] Data invalida. Use o formato: 2027-12-31");
            showError("Data invalida. Use: 2027-12-31");
        }
    }

    // ─── PAINEL RETIRAR ──────────────────────────────────────────────
    private JPanel buildRetirarPanel() {
        JPanel p = panelWithPadding("Retirar Caixa do Topo");

        JPanel form = formPanel();
        fieldSetorRetirar = addField(form, "Setor (UTI / Emergencia / Pediatria)");

        JLabel hint = styledLabel("Remove a caixa mais recente empilhada no setor (LIFO).", TEXT_MUTED, 12, Font.PLAIN);
        hint.setBorder(new EmptyBorder(8, 0, 0, 0));
        hint.setAlignmentX(LEFT_ALIGNMENT);
        form.add(hint);

        JButton btn = actionButton("Retirar do Topo", ACCENT_AMBER);
        btn.addActionListener(e -> executarRetirar());
        form.add(Box.createVerticalStrut(20));
        form.add(btn);

        p.add(form, BorderLayout.CENTER);
        return p;
    }

    private void executarRetirar() {
    String setor = fieldSetorRetirar.getText().trim();
    if (setor.isEmpty()) { log("[ERRO] Informe o nome do setor."); return; }
    
    if (hospital.getSetor(setor) == null) {
        log("[ERRO] Setor '" + setor + "' nao encontrado. Use: UTI, Emergencia ou Pediatria");
        showError("Setor nao encontrado.");
        return;
    }

        CaixaMedicamento caixa = hospital.retirarCaixa(setor);
        if (caixa != null) {
            log("[OK] Caixa retirada do topo!");
            log("     " + caixa);
            ConexaoDB.registrarMovimentacao("SAIDA", caixa.getId(), caixa.getNomeMedicamento(),
                caixa.getLote(), caixa.getDataValidade().toString(), caixa.getQuantidade(), setor);
            fieldSetorRetirar.setText("");
            showSuccess("Retirado: " + caixa.getNomeMedicamento());
            atualizarDashboard();
        } else {
            log("[ERRO] Setor vazio ou nao encontrado.");
            showError("Setor vazio ou nao encontrado.");
        }
    }

    // ─── PAINEL REMOVER ESPECÍFICA ───────────────────────────────────
    private JPanel buildRemoverPanel() {
        JPanel p = panelWithPadding("Remover Caixa Especifica");

        JPanel form = formPanel();
        fieldSetorRemover = addField(form, "Setor (UTI / Emergencia / Pediatria)");
        fieldIdRemover    = addField(form, "ID da caixa");

        JLabel hint = styledLabel("Usa reempilhamento para remover qualquer caixa pelo ID.", TEXT_MUTED, 12, Font.PLAIN);
        hint.setBorder(new EmptyBorder(8, 0, 0, 0));
        hint.setAlignmentX(LEFT_ALIGNMENT);
        form.add(hint);

        JButton btn = actionButton("Remover Caixa", ACCENT_RED);
        btn.addActionListener(e -> executarRemover());
        form.add(Box.createVerticalStrut(20));
        form.add(btn);

        p.add(form, BorderLayout.CENTER);
        return p;
    }

    private void executarRemover() {
        String setor = fieldSetorRemover.getText().trim();
        String id    = fieldIdRemover.getText().trim();
        if (setor.isEmpty() || id.isEmpty()) {
            log("[ERRO] Preencha todos os campos.");
            showError("Preencha todos os campos.");
            return;
        }

        try {
            CaixaMedicamento caixa = hospital.removerCaixaMedicamentoEspecifico(setor, id);
            if (caixa != null) {
                log("[OK] Caixa removida com sucesso!");
                log("     " + caixa);
                ConexaoDB.registrarMovimentacao("REMOCAO_ESPECIFICA", caixa.getId(), caixa.getNomeMedicamento(),
                    caixa.getLote(), caixa.getDataValidade().toString(), caixa.getQuantidade(), setor);
                clearFields(fieldSetorRemover, fieldIdRemover);
                showSuccess("Removido: " + caixa.getNomeMedicamento());
                atualizarDashboard();
            } else {
                log("[ERRO] Caixa nao encontrada.");
                showError("Caixa nao encontrada.");
            }
        } catch (NullPointerException ex) {
            log("[ERRO] ID nao encontrado no setor informado.");
            showError("ID nao encontrado no setor.");
        }
    }

    // ─── PAINEL BUSCAR ───────────────────────────────────────────────
    private JPanel buildBuscarPanel() {
        JPanel p = panelWithPadding("Buscar Medicamento");

        JPanel form = formPanel();
        fieldIdBuscar = addField(form, "ID da caixa");

        JButton btn = actionButton("Buscar", ACCENT_BLUE);
        btn.addActionListener(e -> executarBuscar());
        form.add(Box.createVerticalStrut(20));
        form.add(btn);

        // Area de resultado
        JPanel resultArea = new JPanel(new BorderLayout());
        resultArea.setBackground(BG_DARK);
        resultArea.setName("resultArea");
        resultArea.setBorder(new EmptyBorder(20, 0, 0, 0));

        p.add(form, BorderLayout.NORTH);
        p.add(resultArea, BorderLayout.CENTER);
        return p;
    }

    private void executarBuscar() {
        String id = fieldIdBuscar.getText().trim();
        if (id.isEmpty()) { log("[ERRO] Informe o ID da caixa."); showError("Informe o ID."); return; }

        Setor setor = hospital.buscarCaixa(id);

        // Limpa resultado anterior
        JPanel buscarPanel = null;
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof JPanel && c.isVisible()) {
                buscarPanel = (JPanel) c;
            }
        }

        if (setor != null) {
            log("[OK] Medicamento encontrado no setor: " + setor.getNome());

            // Encontra o resultado no setor para mostrar detalhes
            CaixaMedicamento encontrada = null;
            for (int i = 0; i < setor.getPilha().getTamanhoAtual(); i++) {
                if (setor.getPilha().getElementos()[i].getId().equals(id)) {
                    encontrada = setor.getPilha().getElementos()[i];
                    break;
                }
            }

            // Card de resultado
            JPanel resultCard = new JPanel(new GridLayout(0, 2, 8, 8));
            resultCard.setBackground(BG_CARD);
            resultCard.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_GREEN, 1, true),
                new EmptyBorder(16, 16, 16, 16)
            ));

            addResultRow(resultCard, "Setor:",         setor.getNome());
            addResultRow(resultCard, "Especialidade:", setor.getEspecialidade());
            addResultRow(resultCard, "Ocupacao:",      setor.getPilha().getTamanhoAtual() + "/" + setor.getPilha().getCapacidade());
            if (encontrada != null) {
                addResultRow(resultCard, "Medicamento:", encontrada.getNomeMedicamento());
                addResultRow(resultCard, "Lote:",        encontrada.getLote());
                addResultRow(resultCard, "Validade:",    encontrada.getDataValidade().toString());
                addResultRow(resultCard, "Quantidade:",  String.valueOf(encontrada.getQuantidade()));
            }

            // Atualiza area de resultado
            for (Component c : contentPanel.getComponents()) {
                if (c instanceof JPanel) {
                    for (Component inner : ((JPanel) c).getComponents()) {
                        if (inner instanceof JPanel && "resultArea".equals(((JPanel) inner).getName())) {
                            JPanel area = (JPanel) inner;
                            area.removeAll();
                            JLabel found = styledLabel("Medicamento encontrado!", ACCENT_GREEN, 13, Font.BOLD);
                            found.setBorder(new EmptyBorder(0, 0, 12, 0));
                            area.add(found, BorderLayout.NORTH);
                            area.add(resultCard, BorderLayout.CENTER);
                            area.revalidate();
                            area.repaint();
                        }
                    }
                }
            }

            showSuccess("Encontrado no setor " + setor.getNome());
        } else {
            log("[ERRO] Caixa com ID '" + id + "' nao encontrada.");
            showError("Caixa nao encontrada.");
        }
    }

    private void addResultRow(JPanel panel, String key, String value) {
        panel.add(styledLabel(key, TEXT_MUTED, 12, Font.PLAIN));
        panel.add(styledLabel(value, TEXT_PRIMARY, 12, Font.BOLD));
    }

    // ─── HELPERS UI ─────────────────────────────────────────────────
    private JPanel panelWithPadding(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(BG_DARK);
        p.setBorder(new EmptyBorder(28, 28, 20, 28));

        JLabel lbl = styledLabel(title, TEXT_PRIMARY, 20, Font.BOLD);
        lbl.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_COLOR));
        p.add(lbl, BorderLayout.NORTH);
        return p;
    }

    private JPanel formPanel() {
        JPanel form = new JPanel();
        form.setBackground(BG_DARK);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        return form;
    }

    private JTextField addField(JPanel form, String label) {
        JLabel lbl = styledLabel(label, TEXT_MUTED, 12, Font.PLAIN);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(14, 0, 4, 0));

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        field.setAlignmentX(LEFT_ALIGNMENT);

        // Highlight ao focar
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_BLUE, 1, true),
                    new EmptyBorder(4, 10, 4, 10)
                ));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(new CompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    new EmptyBorder(4, 10, 4, 10)
                ));
            }
        });

        form.add(lbl);
        form.add(field);
        return field;
    }

    private JButton actionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 38));

        Color darker = color.darker();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(darker); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(color); }
        });
        return btn;
    }

    private JLabel styledLabel(String text, Color color, int size, int style) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", style, size));
        lbl.setForeground(color);
        return lbl;
    }

    private JSeparator separator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_COLOR);
        sep.setBackground(BORDER_COLOR);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private void showPanel(String name) {
        cardLayout.show(contentPanel, name);
        if ("dashboard".equals(name)) {
            atualizarDashboard();
        }
    }

    private void log(String msg) {
        logArea.append(msg + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void showSuccess(String msg) {
        statusLabel.setText("  OK: " + msg);
        statusLabel.setForeground(ACCENT_GREEN);
        new Timer(3000, e -> {
            statusLabel.setText("  Sistema operacional");
            statusLabel.setForeground(ACCENT_GREEN);
        }) {{ setRepeats(false); start(); }};
    }

    private void showError(String msg) {
        statusLabel.setText("  ERRO: " + msg);
        statusLabel.setForeground(ACCENT_RED);
        new Timer(3000, e -> {
            statusLabel.setText("  Sistema operacional");
            statusLabel.setForeground(ACCENT_GREEN);
        }) {{ setRepeats(false); start(); }};
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
    }
}