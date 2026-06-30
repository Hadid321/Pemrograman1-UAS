package view.customer;

import config.*;
import dao.*;
import model.User;
import view.LoginForm;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

public class CustomerDashboard extends JFrame {

    private User user;
    private TransaksiDAO trxDAO = new TransaksiDAO();
    private GameDAO      gDao   = new GameDAO();
    private JPanel content;
    private JButton[] navBtns;
    private static final String[] NAV = {"Beranda","Top-Up","Pesanan Saya","Profil"};

    public CustomerDashboard(User user){
        this.user=user; initUI(); setLocationRelativeTo(null); switchPage(0);
    }

    private void initUI(){
        setTitle("ZenithStore - " + user.getNamaUser());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200,700); setMinimumSize(new Dimension(1000,600));
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());
        add(buildTopbar(), BorderLayout.NORTH);
        content=new JPanel(new BorderLayout()); content.setBackground(Theme.BG);
        add(content,BorderLayout.CENTER);
    }

    // ── TOP NAVIGATION BAR (seperti Codashop) ─────────────────
    private JPanel buildTopbar(){
        JPanel bar=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                g.setColor(Theme.SIDEBAR); g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        bar.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0,Theme.BORDER),new EmptyBorder(0,24,0,24)));
        bar.setPreferredSize(new Dimension(0,58));

        // Logo
        JPanel logoRow=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); logoRow.setOpaque(false);
        JPanel dot=new JPanel(){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Theme.ACCENT);g2.fillOval(0,5,22,22);g2.dispose();}};
        dot.setOpaque(false); dot.setPreferredSize(new Dimension(22,32));
        logoRow.add(dot); logoRow.add(UI.label("ZenithStore",Theme.F_LOGO,Theme.TEXT));

        // Nav menu (horizontal)
        JPanel nav=new JPanel(new FlowLayout(FlowLayout.LEFT,4,0)); nav.setOpaque(false);
        navBtns=new JButton[NAV.length];
        for(int i=0;i<NAV.length;i++){
            final int idx=i;
            navBtns[i]=makeTopNavBtn(NAV[i],false);
            navBtns[i].addActionListener(e->switchPage(idx));
            nav.add(navBtns[i]);
        }

        // User info kanan
        JPanel userRow=new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0)); userRow.setOpaque(false);
        JPanel av=new JPanel(new BorderLayout()){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Theme.ACCENT);g2.fillOval(0,0,32,32);g2.dispose();}};
        av.setOpaque(false); av.setPreferredSize(new Dimension(32,32));
        JLabel avL=new JLabel(String.valueOf(user.getNamaUser().charAt(0)).toUpperCase(),SwingConstants.CENTER);
        avL.setFont(new Font("Segoe UI",Font.BOLD,14)); avL.setForeground(Color.WHITE); av.add(avL);
        JLabel lName=UI.label(user.getNamaUser(),Theme.F_BOLD,Theme.TEXT);
        JButton btnOut=UI.smallBtn("Keluar",new Color(239,68,68,60),Theme.RED);
        btnOut.addActionListener(e->{ if(JOptionPane.showConfirmDialog(this,"Yakin ingin keluar?","",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){ new LoginForm().setVisible(true); dispose(); }});
        userRow.add(av); userRow.add(lName); userRow.add(btnOut);

        bar.add(logoRow,BorderLayout.WEST);
        bar.add(nav,BorderLayout.CENTER);
        bar.add(userRow,BorderLayout.EAST);
        return bar;
    }

    private JButton makeTopNavBtn(String text,boolean active){
        JButton btn=new JButton(text);
        btn.setFont(active?Theme.F_BOLD:Theme.F_BODY);
        btn.setForeground(active?Theme.ACCENT:Theme.SUBTEXT);
        btn.setBackground(new Color(0,0,0,0));
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
            active?new MatteBorder(0,0,2,0,Theme.ACCENT):new EmptyBorder(0,0,2,0),
            new EmptyBorder(16,14,16,14)
        ));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ if(!btn.getForeground().equals(Theme.ACCENT)) btn.setForeground(Theme.TEXT); }
            public void mouseExited(MouseEvent e){  if(!btn.getForeground().equals(Theme.ACCENT)) btn.setForeground(Theme.SUBTEXT); }
        });
        return btn;
    }

    private void switchPage(int idx){
        for(int i=0;i<navBtns.length;i++){
            boolean a=(i==idx);
            navBtns[i].setForeground(a?Theme.ACCENT:Theme.SUBTEXT);
            navBtns[i].setFont(a?Theme.F_BOLD:Theme.F_BODY);
            navBtns[i].setBorder(new CompoundBorder(
                a?new MatteBorder(0,0,2,0,Theme.ACCENT):new EmptyBorder(0,0,2,0),
                new EmptyBorder(16,14,16,14)
            ));
        }
        content.removeAll();
        switch(idx){
            case 0: pageBeranda();  break;
            case 1: pageTopUp();    break;
            case 2: pagePesanan();  break;
            case 3: pageProfil();   break;
        }
        content.revalidate(); content.repaint();
    }

    // ══════════════════════════════════════════════════════════
    // BERANDA - seperti homepage Codashop
    // ══════════════════════════════════════════════════════════
    private void pageBeranda(){
        JPanel page=new JPanel(){protected void paintComponent(Graphics g){g.setColor(Theme.BG);g.fillRect(0,0,getWidth(),getHeight());}};
        page.setLayout(new BorderLayout());
        JPanel inner=new JPanel(); inner.setOpaque(false); inner.setLayout(new BoxLayout(inner,BoxLayout.Y_AXIS));
        inner.setBorder(new EmptyBorder(0,0,0,0));

        // Hero banner
        JPanel hero=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,new Color(40,20,90),getWidth(),getHeight(),new Color(10,30,70)));
                g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        hero.setBorder(new EmptyBorder(32,40,32,40));
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE,160));
        JPanel heroText=new JPanel(); heroText.setOpaque(false); heroText.setLayout(new BoxLayout(heroText,BoxLayout.Y_AXIS));
        heroText.add(UI.label("Top-Up Game Favoritmu",new Font("Segoe UI",Font.BOLD,28),Color.WHITE));
        heroText.add(Box.createVerticalStrut(8));
        heroText.add(UI.label("Proses instan  •  Harga terbaik  •  Aman & terpercaya",Theme.F_BODY,new Color(180,190,220)));
        heroText.add(Box.createVerticalStrut(16));
        JButton btnTopUp=UI.primaryBtn("Top-Up Sekarang",Theme.ACCENT,Color.WHITE);
        btnTopUp.setMaximumSize(new Dimension(180,42)); btnTopUp.setPreferredSize(new Dimension(180,42));
        btnTopUp.addActionListener(e->switchPage(1));
        heroText.add(btnTopUp);
        hero.add(heroText,BorderLayout.WEST);
        inner.add(hero); inner.add(Box.createVerticalStrut(28));

        // Pilih Game section
        JPanel pad=new JPanel(); pad.setOpaque(false); pad.setLayout(new BoxLayout(pad,BoxLayout.Y_AXIS)); pad.setBorder(new EmptyBorder(0,28,0,28));
        pad.add(UI.label("Pilih Game",Theme.F_HEAD,Theme.TEXT));
        pad.add(UI.label("Klik game untuk langsung top-up",Theme.F_SMALL,Theme.SUBTEXT));
        pad.add(Box.createVerticalStrut(16));

        ArrayList<String[]> games=gDao.getAllGame();
        Color[] gc=Theme.GAME_COLORS;
        Color[] gbg=Theme.GAME_BG;
        String[] gshort={"HSR","GSN","RBX","MLBB","FF","PUBG"};

        JPanel gameGrid=new JPanel(new GridLayout(2,3,14,14));
        gameGrid.setOpaque(false); gameGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE,220));
        for(int i=0;i<Math.min(games.size(),6);i++){
            String[] g=games.get(i);
            Color c=gc[i%gc.length]; Color bg=gbg[i%gbg.length];
            final int gameIdx=i;
            JPanel gCard=new JPanel(new BorderLayout()){
                protected void paintComponent(Graphics gg){
                    Graphics2D g2=(Graphics2D)gg.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setPaint(new GradientPaint(0,0,bg,getWidth(),getHeight(),Theme.CARD2));
                    g2.fill(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,14,14));
                    g2.setColor(c); g2.draw(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,14,14));
                    g2.dispose();
                }
            };
            gCard.setOpaque(false); gCard.setBorder(new EmptyBorder(16,18,16,18));
            gCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel left2=new JPanel(); left2.setOpaque(false); left2.setLayout(new BoxLayout(left2,BoxLayout.Y_AXIS));
            // Game icon circle
            JPanel ico=new JPanel(new BorderLayout()){protected void paintComponent(Graphics gg){Graphics2D g2=(Graphics2D)gg.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(new Color(c.getRed(),c.getGreen(),c.getBlue(),80));g2.fillOval(0,0,42,42);g2.dispose();}};
            ico.setOpaque(false); ico.setPreferredSize(new Dimension(42,42)); ico.setMaximumSize(new Dimension(42,42));
            JLabel icoL=new JLabel(gshort[i],SwingConstants.CENTER); icoL.setFont(Theme.F_SMBD); icoL.setForeground(c); ico.add(icoL);
            ico.setAlignmentX(Component.LEFT_ALIGNMENT);
            left2.add(ico); left2.add(Box.createVerticalStrut(10));
            JLabel gName=UI.label(g[1],Theme.F_BOLD,Color.WHITE); gName.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel gMata=UI.label(g[2],Theme.F_SMALL,new Color(180,190,210)); gMata.setAlignmentX(Component.LEFT_ALIGNMENT);
            left2.add(gName); left2.add(Box.createVerticalStrut(2)); left2.add(gMata);
            gCard.add(left2,BorderLayout.CENTER);

            // Arrow
            JLabel arrow=UI.label(">",Theme.F_BOLD,c); gCard.add(arrow,BorderLayout.EAST);

            // Hover effect & click
            gCard.addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){ gCard.setBorder(new CompoundBorder(new UI.RoundedBorder(14,c),new EmptyBorder(15,17,15,17))); }
                public void mouseExited(MouseEvent e){  gCard.setBorder(new EmptyBorder(16,18,16,18)); }
                public void mouseClicked(MouseEvent e){ switchPage(1); /* TODO: pre-select game */ }
            });
            gameGrid.add(gCard);
        }
        pad.add(gameGrid); pad.add(Box.createVerticalStrut(24));

        // Transaksi terakhir (mini)
        ArrayList<String[]> myTrx=trxDAO.getTransaksiByUser(user.getIdUser());
        if(!myTrx.isEmpty()){
            pad.add(UI.sectionHeader("Transaksi Terakhir",null));
            String[] cols={"No. Invoice","Game","Paket","Total","Status","Tanggal"};
            JTable tbl=UI.makeTable(cols);
            tbl.getColumnModel().getColumn(4).setCellRenderer(UI.statusRenderer());
            int[] w={150,130,160,110,85,140};
            for(int i=0;i<w.length;i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
            DefaultTableModel mdl=(DefaultTableModel)tbl.getModel();
            for(int i=0;i<Math.min(5,myTrx.size());i++){
                String[] r=myTrx.get(i);
                mdl.addRow(new String[]{r[0],r[1],r[2],r[4],r[6],r[7]});
            }
            JScrollPane sp=UI.tableScroll(tbl); sp.setPreferredSize(new Dimension(0,220));
            pad.add(sp);
        }
        inner.add(pad);
        page.add(UI.wrapScroll(inner),BorderLayout.CENTER);
        content.add(page);
    }

    // ══════════════════════════════════════════════════════════
    // TOP-UP - style Codashop/Ourastore
    // ══════════════════════════════════════════════════════════
private void pageTopUp() {
    JPanel page = new JPanel() {
        protected void paintComponent(Graphics g) {
            g.setColor(Theme.BG);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    };
    page.setLayout(new BorderLayout());
    JPanel inner = new JPanel();
    inner.setOpaque(false);
    inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
    inner.setBorder(new EmptyBorder(24, 28, 24, 28));

    ArrayList<String[]> games = gDao.getAllGame();
    Color[] gc = Theme.GAME_COLORS;

    // State variables
    final int[] selPaket = {-1};
    final double[] selHarga = {0};
    final int[] selGame = {0};
    final int[] selIdVoucher = {-1};
    final double[] selDiskon = {0};

    // Step 1: Pilih Game
    inner.add(UI.label("Step 1 - Pilih Game", Theme.F_HEAD, Theme.SUBTEXT));
    inner.add(Box.createVerticalStrut(12));
    JPanel tabRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
    tabRow.setOpaque(false);
    tabRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
    JButton[] tabBtns = new JButton[games.size()];
    for (int i = 0; i < games.size(); i++) {
        Color c = gc[i % gc.length];
        final int fi = i;
        JButton tb = new JButton(games.get(i)[1]) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = selGame[0] == fi;
                Color bg2 = active ? c : new Color(c.getRed(), c.getGreen(), c.getBlue(), 40);
                g2.setColor(bg2);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                if (!active) {
                    g2.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 100));
                    g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tb.setFont(Theme.F_SMBD);
        tb.setForeground(Color.WHITE);
        tb.setContentAreaFilled(false);
        tb.setBorderPainted(false);
        tb.setFocusPainted(false);
        tb.setBorder(new EmptyBorder(8, 16, 8, 16));
        tb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tabBtns[i] = tb;
        tabRow.add(tb);
    }
    inner.add(tabRow);
    inner.add(Box.createVerticalStrut(20));
    inner.add(UI.sep());
    inner.add(Box.createVerticalStrut(20));

    // Step 2: Data Akun
    inner.add(UI.label("Step 2 - Masukkan Data Akun Game", Theme.F_HEAD, Theme.SUBTEXT));
    inner.add(Box.createVerticalStrut(12));
    UI.RoundPanel dataPanel = new UI.RoundPanel(10, Theme.CARD, Theme.BORDER);
    dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
    dataPanel.setBorder(new EmptyBorder(16, 18, 16, 18));
    dataPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

    JTextField txtID = UI.textField();
    JTextField txtServer = UI.textField();
    JLabel lblIDLabel = UI.label("User ID / UID", Theme.F_BOLD, Theme.SUBTEXT);
    lblIDLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel lblServerLabel = UI.label("Server (opsional)", Theme.F_BOLD, Theme.SUBTEXT);
    lblServerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel lblHint = UI.label("", Theme.F_SMALL, Theme.MUTED);
    lblHint.setAlignmentX(Component.LEFT_ALIGNMENT);

    dataPanel.add(lblIDLabel);
    dataPanel.add(Box.createVerticalStrut(6));
    dataPanel.add(txtID);
    dataPanel.add(Box.createVerticalStrut(12));
    dataPanel.add(lblServerLabel);
    dataPanel.add(Box.createVerticalStrut(6));
    dataPanel.add(txtServer);
    dataPanel.add(Box.createVerticalStrut(8));
    dataPanel.add(lblHint);

    inner.add(dataPanel);
    inner.add(Box.createVerticalStrut(20));
    inner.add(UI.sep());
    inner.add(Box.createVerticalStrut(20));

    // Step 3: Paket
    inner.add(UI.label("Step 3 - Pilih Paket", Theme.F_HEAD, Theme.SUBTEXT));
    inner.add(Box.createVerticalStrut(12));
    JPanel gridWrap = new JPanel(new BorderLayout());
    gridWrap.setOpaque(false);
    JPanel grid = new JPanel(new GridLayout(0, 4, 12, 12));
    grid.setOpaque(false);
    gridWrap.add(grid, BorderLayout.NORTH);
    inner.add(gridWrap);
    inner.add(Box.createVerticalStrut(20));
    inner.add(UI.sep());
    inner.add(Box.createVerticalStrut(20));

    // Step 4: Metode Pembayaran
    inner.add(UI.label("Step 4 - Metode Pembayaran", Theme.F_HEAD, Theme.SUBTEXT));
    inner.add(Box.createVerticalStrut(12));
    String[] methods = {"DANA", "GoPay", "OVO", "Transfer Bank", "Alfamart", "Indomaret", "ShopeePay", "LinkAja"};
    JPanel methodGrid = new JPanel(new GridLayout(2, 4, 10, 10));
    methodGrid.setOpaque(false);
    methodGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
    JButton[] mBtns = new JButton[methods.length];
    final String[] selMethod = {methods[0]};
    for (int i = 0; i < methods.length; i++) {
        final int fi2 = i;
        mBtns[i] = new JButton(methods[i]) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg2 = getText().equals(selMethod[0]) ? Theme.ACCENT : Theme.CARD2;
                g2.setColor(bg2);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                if (getText().equals(selMethod[0])) {
                    g2.setColor(Theme.ACCENT.brighter());
                    g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        mBtns[i].setFont(Theme.F_SMBD);
        mBtns[i].setForeground(Color.WHITE);
        mBtns[i].setContentAreaFilled(false);
        mBtns[i].setBorderPainted(false);
        mBtns[i].setFocusPainted(false);
        mBtns[i].setBorder(new EmptyBorder(6, 10, 6, 10));
        mBtns[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mBtns[i].addActionListener(e -> {
            selMethod[0] = methods[fi2];
            for (JButton b : mBtns) b.repaint();
        });
        methodGrid.add(mBtns[i]);
    }
    inner.add(methodGrid);
    inner.add(Box.createVerticalStrut(20));

    // Summary
    UI.RoundPanel summary = new UI.RoundPanel(10, Theme.CARD, Theme.BORDER);
    summary.setLayout(new BorderLayout(20, 0));
    summary.setBorder(new EmptyBorder(16, 20, 16, 20));
    summary.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
    JPanel sumLeft = new JPanel();
    sumLeft.setOpaque(false);
    sumLeft.setLayout(new BoxLayout(sumLeft, BoxLayout.Y_AXIS));
    JLabel lblPaketSel = UI.label("Belum ada paket dipilih", Theme.F_BOLD, Theme.SUBTEXT);
    JLabel lblDiskon = UI.label("", Theme.F_SMALL, Theme.GREEN_LT);
    lblDiskon.setAlignmentX(Component.LEFT_ALIGNMENT);
    JLabel lblTotalSel = UI.label("Total: Rp 0", new Font("Segoe UI", Font.BOLD, 20), Theme.ACCENT);
    sumLeft.add(lblPaketSel);
    sumLeft.add(Box.createVerticalStrut(2));
    sumLeft.add(lblDiskon);
    sumLeft.add(Box.createVerticalStrut(2));
    sumLeft.add(lblTotalSel);
    JButton btnBayar = UI.primaryBtn("Bayar Sekarang", Theme.GREEN, new Color(10, 14, 26));
    btnBayar.setFont(new Font("Segoe UI", Font.BOLD, 14));
    btnBayar.setEnabled(false);
    btnBayar.setPreferredSize(new Dimension(180, 44));
    btnBayar.setMaximumSize(new Dimension(180, 44));
    summary.add(sumLeft, BorderLayout.CENTER);
    summary.add(btnBayar, BorderLayout.EAST);

    // Step 5: Voucher
    inner.add(UI.label("Step 5 - Kode Voucher (Opsional)", Theme.F_HEAD, Theme.SUBTEXT));
    inner.add(Box.createVerticalStrut(12));
    UI.RoundPanel vPanel = new UI.RoundPanel(10, Theme.CARD, Theme.BORDER);
    vPanel.setLayout(new BoxLayout(vPanel, BoxLayout.Y_AXIS));
    vPanel.setBorder(new EmptyBorder(14, 16, 14, 16));
    vPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
    JPanel vRow = new JPanel(new BorderLayout(10, 0));
    vRow.setOpaque(false);
    vRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
    JTextField txtVoucher = UI.textField();
    txtVoucher.setPreferredSize(new Dimension(0, 42));
    JButton btnPakai = UI.smallBtn("Pakai", Theme.ACCENT2, Color.WHITE);
    btnPakai.setPreferredSize(new Dimension(80, 42));
    JButton btnHapus = UI.smallBtn("Hapus", new Color(239, 68, 68, 80), Theme.RED);
    btnHapus.setPreferredSize(new Dimension(80, 42));
    btnHapus.setVisible(false);
    JPanel vBtnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
    vBtnRow.setOpaque(false);
    vBtnRow.add(btnPakai);
    vBtnRow.add(btnHapus);
    vRow.add(txtVoucher, BorderLayout.CENTER);
    vRow.add(vBtnRow, BorderLayout.EAST);
    JLabel lblVoucherMsg = UI.label("Masukkan kode voucher untuk mendapat diskon", Theme.F_SMALL, Theme.MUTED);
    lblVoucherMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
    vPanel.add(vRow);
    vPanel.add(Box.createVerticalStrut(8));
    vPanel.add(lblVoucherMsg);
    inner.add(vPanel);
    inner.add(Box.createVerticalStrut(16));
    inner.add(summary);

    // ── Logic ──
    VoucherDAO vDao = new VoucherDAO();

    Runnable updateTotal = () -> {
        double ft = selHarga[0] - selDiskon[0];
        if (ft < 0) ft = 0;
        lblTotalSel.setText("Total: " + String.format("Rp %,.0f", ft));
    };

    // Tombol Pakai Voucher (dengan pesan error spesifik)
    btnPakai.addActionListener(e -> {
        String kode = txtVoucher.getText().trim().toUpperCase();
        if (kode.isEmpty()) {
            lblVoucherMsg.setText("Masukkan kode voucher!");
            lblVoucherMsg.setForeground(Theme.RED);
            return;
        }
        if (selPaket[0] < 0) {
            lblVoucherMsg.setText("Pilih paket dahulu sebelum memakai voucher!");
            lblVoucherMsg.setForeground(Theme.YELLOW);
            return;
        }
        String[] v = vDao.validateVoucher(kode, selHarga[0]);
        if (v == null) {
            String reason = vDao.getValidationReason(kode, selHarga[0]);
            lblVoucherMsg.setText("Voucher tidak valid: " + reason);
            lblVoucherMsg.setForeground(Theme.RED);
            selIdVoucher[0] = -1;
            selDiskon[0] = 0;
            lblDiskon.setText("");
            updateTotal.run();
        } else {
            selIdVoucher[0] = Integer.parseInt(v[0]);
            double nilai = Double.parseDouble(v[2]);
            double maks = Double.parseDouble(v[3]);
            if (v[1].equals("persen")) {
                selDiskon[0] = selHarga[0] * nilai / 100.0;
                if (maks > 0 && selDiskon[0] > maks) selDiskon[0] = maks;
            } else {
                selDiskon[0] = nilai;
            }
            if (selDiskon[0] > selHarga[0]) selDiskon[0] = selHarga[0];
            lblVoucherMsg.setText("Voucher berhasil! Hemat " + String.format("Rp %,.0f", selDiskon[0]));
            lblVoucherMsg.setForeground(Theme.GREEN_LT);
            lblDiskon.setText("Diskon: - " + String.format("Rp %,.0f", selDiskon[0]));
            btnPakai.setVisible(false);
            btnHapus.setVisible(true);
            txtVoucher.setEditable(false);
            updateTotal.run();
        }
    });

    btnHapus.addActionListener(e -> {
        selIdVoucher[0] = -1;
        selDiskon[0] = 0;
        txtVoucher.setText("");
        txtVoucher.setEditable(true);
        lblVoucherMsg.setText("Masukkan kode voucher untuk mendapat diskon");
        lblVoucherMsg.setForeground(Theme.MUTED);
        lblDiskon.setText("");
        btnPakai.setVisible(true);
        btnHapus.setVisible(false);
        updateTotal.run();
    });

    // Konfigurasi form per game
    String[][] gameConfig = {
        {"User ID (UID)", "Contoh: 123456789", "Server", "false"},
        {"User ID (UID)", "Contoh: 987654321", "Server", "false"},
        {"User ID", "Contoh: @username", "Server", "false"},
        {"User ID", "Contoh: 12345678", "Server ID", "true"},
        {"Player ID", "Contoh: 12345678", "Server", "false"},
        {"Player ID", "Contoh: 12345678", "Server", "false"}
    };

    Runnable updateForm = () -> {
        int gi = selGame[0];
        if (gi >= gameConfig.length) return;
        String[] cfg = gameConfig[gi];
        lblIDLabel.setText(cfg[0]);
        lblHint.setText("  " + cfg[1]);
        lblServerLabel.setText(cfg[2]);
        boolean showServer = cfg[3].equals("true");
        lblServerLabel.setVisible(showServer);
        txtServer.setVisible(showServer);
        dataPanel.revalidate();
        dataPanel.repaint();
    };

    Runnable loadPaket = () -> {
        grid.removeAll();
        int gi = selGame[0];
        if (gi >= games.size()) return;
        int idGame = Integer.parseInt(games.get(gi)[0]);
        Color c = gc[gi % gc.length];
        ArrayList<String[]> pakets = gDao.getPaketByGame(idGame);
        for (String[] p : pakets) {
            int pid = Integer.parseInt(p[0]);
            double h = Double.parseDouble(p[5]);
            JPanel pCard = new JPanel(new BorderLayout()) {
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(selPaket[0] == pid ? new Color(c.getRed(), c.getGreen(), c.getBlue(), 60) : Theme.CARD2);
                    g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                    g2.setColor(selPaket[0] == pid ? c : Theme.BORDER);
                    g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));
                    g2.dispose();
                }
            };
            pCard.setOpaque(false);
            pCard.setBorder(new EmptyBorder(14, 14, 14, 14));
            pCard.setPreferredSize(new Dimension(0, 120));
            pCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel pl = new JPanel();
            pl.setOpaque(false);
            pl.setLayout(new BoxLayout(pl, BoxLayout.Y_AXIS));
            JPanel cbar = new JPanel() {
                protected void paintComponent(Graphics g) {
                    g.setColor(c);
                    g.fillRoundRect(0, 0, getWidth(), 4, 4, 4);
                }
            };
            cbar.setOpaque(false);
            cbar.setPreferredSize(new Dimension(0, 4));
            JLabel lAmt = UI.label(p[1], Theme.F_BOLD, c);
            lAmt.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lHrg = UI.label(p[4], new Font("Segoe UI", Font.BOLD, 15), Theme.TEXT);
            lHrg.setAlignmentX(Component.LEFT_ALIGNMENT);
            JLabel lBns = UI.label(p[3].equals("0") ? " " : "+ Bonus " + p[3], Theme.F_SMALL, Theme.GREEN_LT);
            lBns.setAlignmentX(Component.LEFT_ALIGNMENT);
            pl.add(cbar);
            pl.add(Box.createVerticalStrut(8));
            pl.add(lAmt);
            pl.add(Box.createVerticalStrut(4));
            pl.add(lHrg);
            pl.add(lBns);
            pCard.add(pl, BorderLayout.CENTER);

            pCard.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    selPaket[0] = pid;
                    selHarga[0] = h;
                    lblPaketSel.setText(p[1] + "  -  " + p[4]);
                    lblPaketSel.setForeground(Theme.TEXT);
                    double finalHarga = h - selDiskon[0];
                    if (finalHarga < 0) finalHarga = 0;
                    lblTotalSel.setText("Total: " + String.format("Rp %,.0f", finalHarga));
                    btnBayar.setEnabled(true);
                    grid.repaint();
                }
                public void mouseEntered(MouseEvent e) {
                    pCard.setBorder(new EmptyBorder(13, 13, 13, 13));
                }
                public void mouseExited(MouseEvent e) {
                    pCard.setBorder(new EmptyBorder(14, 14, 14, 14));
                }
            });
            grid.add(pCard);
        }
        grid.revalidate();
        grid.repaint();
    };

    // Event untuk tab game
    for (int i = 0; i < tabBtns.length; i++) {
        final int fi3 = i;
        tabBtns[i].addActionListener(e -> {
            selGame[0] = fi3;
            selPaket[0] = -1;
            lblPaketSel.setText("Belum ada paket dipilih");
            lblPaketSel.setForeground(Theme.SUBTEXT);
            lblTotalSel.setText("Total: Rp 0");
            btnBayar.setEnabled(false);
            selIdVoucher[0] = -1;
            selDiskon[0] = 0;
            lblDiskon.setText("");
            txtVoucher.setText("");
            txtVoucher.setEditable(true);
            lblVoucherMsg.setText("Masukkan kode voucher untuk mendapat diskon");
            lblVoucherMsg.setForeground(Theme.MUTED);
            btnPakai.setVisible(true);
            btnHapus.setVisible(false);
            for (JButton b : tabBtns) b.repaint();
            updateForm.run();
            loadPaket.run();
        });
    }

    updateForm.run();
    loadPaket.run();

    // Tombol Bayar (dengan pengurangan kuota voucher)
    btnBayar.addActionListener(e -> {
        String uid = txtID.getText().trim();
        if (uid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID / UID wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selPaket[0] < 0) {
            JOptionPane.showMessageDialog(this, "Pilih paket terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String server = txtServer.isVisible() ? txtServer.getText().trim() : "";
        double finalTotal = selHarga[0] - selDiskon[0];
        if (finalTotal < 0) finalTotal = 0;
        boolean ok = trxDAO.insertTransaksi(user.getIdUser(), selPaket[0], uid, server, selMethod[0], finalTotal);
        if (ok) {
            // Kurangi kuota voucher jika digunakan
            if (selIdVoucher[0] != -1) {
                vDao.incrementTerpakai(selIdVoucher[0]);
            }
            String namaGame = games.get(selGame[0])[1];
            String namaPaket = lblPaketSel.getText().replace("Belum ada paket dipilih", "");
            showPaymentDialog(selMethod[0], finalTotal, namaGame, namaPaket, uid);
            btnBayar.setEnabled(false);
            selPaket[0] = -1;
            selIdVoucher[0] = -1;
            selDiskon[0] = 0;
            lblPaketSel.setText("Belum ada paket dipilih");
            lblPaketSel.setForeground(Theme.SUBTEXT);
            lblTotalSel.setText("Total: Rp 0");
            lblDiskon.setText("");
            txtVoucher.setText("");
            txtVoucher.setEditable(true);
            lblVoucherMsg.setText("Masukkan kode voucher untuk mendapat diskon");
            lblVoucherMsg.setForeground(Theme.MUTED);
            btnPakai.setVisible(true);
            btnHapus.setVisible(false);
            loadPaket.run();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal membuat pesanan. Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });

    page.add(UI.wrapScroll(inner), BorderLayout.CENTER);
    content.add(page);
}

    // ══════════════════════════════════════════════════════════
    // PESANAN SAYA
    // ══════════════════════════════════════════════════════════
    private void pagePesanan(){
        JPanel page=makePage(); JPanel inner=innerPanel(page);
        inner.add(UI.label("Pesanan Saya",Theme.F_TITLE,Theme.TEXT));
        inner.add(UI.label("Riwayat semua transaksi top-up kamu",Theme.F_SMALL,Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(18));

        // Summary cards
        ArrayList<String[]> myTrx=trxDAO.getTransaksiByUser(user.getIdUser());
        long scs=myTrx.stream().filter(r->r[6].equals("success")).count();
        long pnd=myTrx.stream().filter(r->r[6].equals("pending")).count();
        JPanel sc=new JPanel(new GridLayout(1,3,12,0)); sc.setOpaque(false); sc.setMaximumSize(new Dimension(Integer.MAX_VALUE,100));
        sc.add(UI.statCard("Total Pesanan",String.valueOf(myTrx.size()),"semua transaksi",Theme.ACCENT));
        sc.add(UI.statCard("Berhasil",String.valueOf(scs),"transaksi sukses",Theme.GREEN_LT));
        sc.add(UI.statCard("Menunggu",String.valueOf(pnd),"menunggu konfirmasi",Theme.YELLOW));
        inner.add(sc); inner.add(Box.createVerticalStrut(20));

        // Tabel
        inner.add(UI.sectionHeader("Riwayat Transaksi",null));
        String[] cols={"No. Invoice","Game","Paket","UID","Total","Metode","Status","Tanggal"};
        JTable tbl=UI.makeTable(cols);
        tbl.getColumnModel().getColumn(6).setCellRenderer(UI.statusRenderer());
        int[] w={150,110,150,100,100,90,85,140};
        for(int i=0;i<w.length;i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        DefaultTableModel mdl=(DefaultTableModel)tbl.getModel();
        for(String[] r:myTrx) mdl.addRow(r);
        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // PROFIL
    // ══════════════════════════════════════════════════════════
    private void pageProfil(){
        JPanel page=makePage(); JPanel inner=innerPanel(page);
        inner.add(UI.label("Profil Saya",Theme.F_TITLE,Theme.TEXT));
        inner.add(UI.label("Kelola informasi akun kamu",Theme.F_SMALL,Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(20));

        JPanel twoCol=new JPanel(new GridLayout(1,2,18,0)); twoCol.setOpaque(false);

        UI.RoundPanel editCard=new UI.RoundPanel(12,Theme.CARD,Theme.BORDER);
        editCard.setLayout(new BoxLayout(editCard,BoxLayout.Y_AXIS)); editCard.setBorder(new EmptyBorder(22,24,22,24));

        // Avatar
        JPanel av=new JPanel(new BorderLayout()){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Theme.ACCENT);g2.fillOval(0,0,60,60);g2.dispose();}};
        av.setOpaque(false); av.setPreferredSize(new Dimension(60,60)); av.setMaximumSize(new Dimension(60,60)); av.setLayout(new BorderLayout());
        JLabel avL=new JLabel(String.valueOf(user.getNamaUser().charAt(0)).toUpperCase(),SwingConstants.CENTER);
        avL.setFont(new Font("Segoe UI",Font.BOLD,26)); avL.setForeground(Color.WHITE); av.add(avL);
        av.setAlignmentX(Component.LEFT_ALIGNMENT);
        editCard.add(av); editCard.add(Box.createVerticalStrut(10));
        editCard.add(UI.label(user.getNamaUser(),new Font("Segoe UI",Font.BOLD,16),Theme.TEXT));
        editCard.add(UI.label("Customer",Theme.F_SMBD,Theme.ACCENT));
        editCard.add(Box.createVerticalStrut(18)); editCard.add(UI.sep()); editCard.add(Box.createVerticalStrut(18));
        editCard.add(UI.label("Edit Informasi",Theme.F_HEAD,Theme.TEXT)); editCard.add(Box.createVerticalStrut(14));

        JTextField fNama=UI.textField(); fNama.setText(user.getNamaUser()); fNama.setForeground(Theme.TEXT);
        JTextField fEmail=UI.textField(); fEmail.setText(user.getEmail()); fEmail.setForeground(Theme.TEXT);
        JTextField fHp=UI.textField(); fHp.setText(user.getNomorHp()); fHp.setForeground(Theme.TEXT);
        JPasswordField fPass=UI.passwordField(), fConf=UI.passwordField();
        UI.addField(editCard,"Nama",fNama); UI.addField(editCard,"Email",fEmail); UI.addField(editCard,"No. HP",fHp);
        editCard.add(UI.sep()); editCard.add(Box.createVerticalStrut(12));
        editCard.add(UI.label("Ganti Password",Theme.F_SMBD,Theme.MUTED)); editCard.add(Box.createVerticalStrut(10));
        UI.addField(editCard,"Password Baru",fPass); UI.addField(editCard,"Konfirmasi",fConf);

        JLabel lblMsg=new JLabel(" "); lblMsg.setFont(Theme.F_SMALL); lblMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnSave=UI.primaryBtn("Simpan",Theme.ACCENT,Color.WHITE);
        btnSave.addActionListener(e->{
            new UserDAO().updateUser(user.getIdUser(),fNama.getText().trim(),fEmail.getText().trim(),fHp.getText().trim());
            String np=new String(fPass.getPassword());
            if(!np.isEmpty()){
                if(!np.equals(new String(fConf.getPassword()))){ lblMsg.setText("Password tidak cocok!"); lblMsg.setForeground(Theme.RED); return; }
                new UserDAO().updatePassword(user.getIdUser(),np);
            }
            user.setNamaUser(fNama.getText().trim());
            lblMsg.setText("Berhasil disimpan!"); lblMsg.setForeground(Theme.GREEN_LT);
        });
        editCard.add(lblMsg); editCard.add(Box.createVerticalStrut(10)); editCard.add(btnSave);

        // Kanan: info akun
        UI.RoundPanel infoCard=new UI.RoundPanel(12,Theme.CARD,Theme.BORDER);
        infoCard.setLayout(new BoxLayout(infoCard,BoxLayout.Y_AXIS)); infoCard.setBorder(new EmptyBorder(22,24,22,24));
        infoCard.add(UI.label("Informasi Akun",Theme.F_HEAD,Theme.TEXT)); infoCard.add(Box.createVerticalStrut(16));
        ArrayList<String[]> myTrx=trxDAO.getTransaksiByUser(user.getIdUser());
        long scs=myTrx.stream().filter(r->r[6].equals("success")).count();
        JPanel sg=new JPanel(new GridLayout(2,2,10,10)); sg.setOpaque(false);
        sg.add(UI.statCard("Total Pesanan",String.valueOf(myTrx.size()),"semua pesanan",Theme.ACCENT));
        sg.add(UI.statCard("Sukses",String.valueOf(scs),"berhasil",Theme.GREEN_LT));
        sg.add(UI.statCard("Pending",String.valueOf(myTrx.stream().filter(r->r[6].equals("pending")).count()),"menunggu",Theme.YELLOW));
        sg.add(UI.statCard("Gagal",String.valueOf(myTrx.stream().filter(r->r[6].equals("failed")).count()),"gagal",Theme.RED));
        infoCard.add(sg);

        twoCol.add(editCard); twoCol.add(infoCard);
        inner.add(twoCol);
        content.add(UI.wrapScroll(page));
    }


    // ══════════════════════════════════════════════════════════
    // POPUP INSTRUKSI PEMBAYARAN
    // ══════════════════════════════════════════════════════════
    private void showPaymentDialog(String metode, double total, String game, String paket, String uid){
        JDialog dialog=new JDialog(this,"Instruksi Pembayaran",true);
        dialog.setSize(500,560); dialog.setLocationRelativeTo(this); dialog.setResizable(false);

        JPanel main=new JPanel(){protected void paintComponent(Graphics g){g.setColor(Theme.BG);g.fillRect(0,0,getWidth(),getHeight());}};
        main.setLayout(new BoxLayout(main,BoxLayout.Y_AXIS));
        main.setBorder(new EmptyBorder(28,28,28,28));

        // Header sukses
        UI.RoundPanel hCard=new UI.RoundPanel(12,new Color(16,185,129,25),new Color(16,185,129,60));
        hCard.setLayout(new BoxLayout(hCard,BoxLayout.Y_AXIS));
        hCard.setBorder(new EmptyBorder(16,18,16,18));
        hCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
        JLabel lOk=UI.label("Pesanan Berhasil Dibuat!",new Font("Segoe UI",Font.BOLD,15),Theme.GREEN_LT); lOk.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lOkSub=UI.label("Selesaikan pembayaran untuk memproses top-up kamu",Theme.F_SMALL,Theme.SUBTEXT); lOkSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        hCard.add(lOk); hCard.add(Box.createVerticalStrut(5)); hCard.add(lOkSub);
        main.add(hCard); main.add(Box.createVerticalStrut(14));

        // Detail order
        UI.RoundPanel dCard=new UI.RoundPanel(12,Theme.CARD,Theme.BORDER);
        dCard.setLayout(new BoxLayout(dCard,BoxLayout.Y_AXIS));
        dCard.setBorder(new EmptyBorder(14,16,14,16));
        dCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,155));
        JLabel lDet=UI.label("Detail Pesanan",Theme.F_HEAD,Theme.TEXT); lDet.setAlignmentX(Component.LEFT_ALIGNMENT);
        dCard.add(lDet); dCard.add(Box.createVerticalStrut(10));
        dCard.add(detailRow("Game",game));
        dCard.add(Box.createVerticalStrut(5));
        dCard.add(detailRow("Paket",paket));
        dCard.add(Box.createVerticalStrut(5));
        dCard.add(detailRow("UID / ID",uid));
        dCard.add(Box.createVerticalStrut(5));
        dCard.add(detailRow("Metode",metode));
        dCard.add(Box.createVerticalStrut(5));
        dCard.add(detailRow("Status","Menunggu Pembayaran"));
        main.add(dCard); main.add(Box.createVerticalStrut(14));

        // Instruksi
        UI.RoundPanel iCard=new UI.RoundPanel(12,Theme.CARD,Theme.BORDER);
        iCard.setLayout(new BoxLayout(iCard,BoxLayout.Y_AXIS));
        iCard.setBorder(new EmptyBorder(14,16,14,16));
        iCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,180));
        JLabel lInstr=UI.label("Cara Pembayaran via "+metode,Theme.F_HEAD,Theme.TEXT); lInstr.setAlignmentX(Component.LEFT_ALIGNMENT);
        iCard.add(lInstr); iCard.add(Box.createVerticalStrut(10));
        for(String s:getInstruksi(metode,total)){
            JLabel li=UI.label(s,Theme.F_SMALL,Theme.SUBTEXT); li.setAlignmentX(Component.LEFT_ALIGNMENT);
            iCard.add(li); iCard.add(Box.createVerticalStrut(3));
        }
        main.add(iCard); main.add(Box.createVerticalStrut(14));

        // Total
        UI.RoundPanel tCard=new UI.RoundPanel(12,new Color(99,102,241,20),new Color(99,102,241,60));
        tCard.setLayout(new BoxLayout(tCard,BoxLayout.Y_AXIS));
        tCard.setBorder(new EmptyBorder(12,16,12,16));
        tCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,70));
        JLabel lTL=UI.label("Total yang Harus Dibayar",Theme.F_SMALL,Theme.SUBTEXT); lTL.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lTV=UI.label(String.format("Rp %,.0f",total),new Font("Segoe UI",Font.BOLD,24),Theme.ACCENT); lTV.setAlignmentX(Component.LEFT_ALIGNMENT);
        tCard.add(lTL); tCard.add(Box.createVerticalStrut(4)); tCard.add(lTV);
        main.add(tCard); main.add(Box.createVerticalStrut(16));

        // Tombol
        JButton btnOk=UI.primaryBtn("Saya Sudah Bayar - Tutup",Theme.ACCENT,Color.WHITE);
        btnOk.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnOk.addActionListener(ev->{
            JOptionPane.showMessageDialog(dialog,
                "Terima kasih! Admin akan memverifikasi pembayaran kamu.\nCek \'Pesanan Saya\' untuk update status.",
                "Menunggu Konfirmasi",JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        main.add(btnOk);
        JScrollPane sp = new JScrollPane(main);
        sp.setBorder(null);
        sp.getViewport().setBackground(Theme.BG);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        dialog.add(sp);
        dialog.setVisible(true);
    }

    private JPanel detailRow(String label,String value){
        JPanel row=new JPanel(new BorderLayout());
        row.setOpaque(false); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,22));
        row.add(UI.label(label,Theme.F_SMALL,Theme.MUTED),BorderLayout.WEST);
        row.add(UI.label(value,Theme.F_SMBD,Theme.TEXT),BorderLayout.EAST);
        return row;
    }

    private String[] getInstruksi(String metode,double total){
        String t=String.format("Rp %,.0f",total);
        switch(metode){
            case "DANA": return new String[]{
                "1. Buka aplikasi DANA di HP kamu",
                "2. Pilih menu Kirim -> ke Nomor HP",
                "3. No. tujuan: 0812-0000-1234 (a/n ZenithStore)",
                "4. Masukkan nominal: "+t,
                "5. Tambahkan catatan: nomor invoice pesanan",
                "6. Konfirmasi & selesaikan pembayaran"};
            case "GoPay": return new String[]{
                "1. Buka aplikasi Gojek di HP kamu",
                "2. Pilih GoPay -> Kirim",
                "3. No. tujuan: 0812-0000-5678 (a/n ZenithStore)",
                "4. Masukkan nominal: "+t,
                "5. Tambahkan catatan: nomor invoice pesanan",
                "6. Konfirmasi & selesaikan pembayaran"};
            case "OVO": return new String[]{
                "1. Buka aplikasi OVO di HP kamu",
                "2. Pilih Transfer -> ke Sesama OVO",
                "3. No. tujuan: 0812-0000-9012 (a/n ZenithStore)",
                "4. Masukkan nominal: "+t,
                "5. Tambahkan catatan: nomor invoice pesanan",
                "6. Konfirmasi & selesaikan pembayaran"};
            case "Transfer Bank": return new String[]{
                "1. Buka aplikasi mobile banking kamu",
                "2. Pilih Transfer Antar Bank",
                "3. Bank tujuan: BCA - No. Rek: 1234567890",
                "4. Atas nama: ZenithStore Indonesia",
                "5. Nominal: "+t,
                "6. Berita transfer: nomor invoice pesanan"};
            case "Alfamart": return new String[]{
                "1. Pergi ke minimarket Alfamart terdekat",
                "2. Tunjukkan nomor invoice ke kasir",
                "3. Kasir akan memproses pembayaran",
                "4. Bayar sebesar: "+t,
                "5. Simpan struk sebagai bukti pembayaran"};
            case "Indomaret": return new String[]{
                "1. Pergi ke minimarket Indomaret terdekat",
                "2. Tunjukkan nomor invoice ke kasir",
                "3. Kasir akan memproses pembayaran",
                "4. Bayar sebesar: "+t,
                "5. Simpan struk sebagai bukti pembayaran"};
            case "ShopeePay": return new String[]{
                "1. Buka aplikasi Shopee di HP kamu",
                "2. Pilih ShopeePay -> Transfer",
                "3. No. tujuan: 0812-0000-3456 (a/n ZenithStore)",
                "4. Masukkan nominal: "+t,
                "5. Tambahkan catatan: nomor invoice pesanan",
                "6. Konfirmasi & selesaikan pembayaran"};
            case "LinkAja": return new String[]{
                "1. Buka aplikasi LinkAja di HP kamu",
                "2. Pilih Kirim Uang",
                "3. No. tujuan: 0812-0000-7890 (a/n ZenithStore)",
                "4. Masukkan nominal: "+t,
                "5. Tambahkan catatan: nomor invoice pesanan",
                "6. Konfirmasi & selesaikan pembayaran"};
            default: return new String[]{"Hubungi admin untuk instruksi pembayaran.","Total: "+t};
        }
    }

    private JPanel makePage(){JPanel p=new JPanel(){protected void paintComponent(Graphics g){g.setColor(Theme.BG);g.fillRect(0,0,getWidth(),getHeight());}};p.setLayout(new BorderLayout());return p;}
    private JPanel innerPanel(JPanel page){JPanel p=new JPanel();p.setOpaque(false);p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setBorder(new EmptyBorder(24,28,24,28));page.add(p,BorderLayout.CENTER);return p;}
}
