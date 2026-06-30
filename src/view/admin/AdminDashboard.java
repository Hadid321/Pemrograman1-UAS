package view.admin;

import config.*;
import dao.*;
import model.User;
import view.LoginForm;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;

public class AdminDashboard extends JFrame {

    private User user;
    private TransaksiDAO trxDAO = new TransaksiDAO();
    private UserDAO userDAO     = new UserDAO();
    private JPanel content;
    private JButton[] navBtns;
    private static final String[] NAV = {"Dashboard", "Transaksi", "Kelola User", "Voucher", "Pengaturan"};

    public AdminDashboard(User user){
        this.user = user; 
        initUI(); 
        setLocationRelativeTo(null); 
        switchPage(0);
    }

    private void initUI(){
        setTitle("ZenithStore - Admin Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1300, 760); 
        setMinimumSize(new Dimension(1100, 650));
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());
        
        add(buildSidebar(), BorderLayout.WEST);
        content = new JPanel(new BorderLayout()); 
        content.setBackground(Theme.BG);
        add(content, BorderLayout.CENTER);
    }

    private JPanel buildSidebar(){
        JPanel sb = new JPanel(){
            protected void paintComponent(Graphics g){
                g.setColor(Theme.SIDEBAR);
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        sb.setPreferredSize(new Dimension(240, 0));
        sb.setBorder(new MatteBorder(0, 0, 0, 1, Theme.BORDER));
        sb.setLayout(new BorderLayout());

        JPanel top = new JPanel(); 
        top.setOpaque(false); 
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(30, 20, 20, 20));

        // Logo Section
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); 
        logoRow.setOpaque(false);
        JPanel dot = new JPanel(){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT);
                g2.fillOval(0, 3, 26, 26);
                g2.dispose();
            }
        };
        dot.setOpaque(false); 
        dot.setPreferredSize(new Dimension(26, 32));
        logoRow.add(dot); 
        logoRow.add(UI.label("ZenithStore", Theme.F_LOGO, Theme.TEXT));
        
        // Admin Badge
        JPanel badge = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8)); 
        badge.setOpaque(false);
        UI.RoundPanel bp = new UI.RoundPanel(6, new Color(239, 68, 68, 40), new Color(239, 68, 68, 80));
        bp.setLayout(new FlowLayout(0, 10, 4)); 
        bp.add(UI.label("ADMIN PANEL", Theme.F_SMBD, Theme.RED));
        badge.add(bp);

        top.add(logoRow); 
        top.add(badge); 
        top.add(Box.createVerticalStrut(24)); 
        top.add(UI.sep());
        top.add(Box.createVerticalStrut(20));

        // Navigation Menu
        JPanel nav = new JPanel(); 
        nav.setOpaque(false); 
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        navBtns = new JButton[NAV.length];
        String[] icons = {"[ ]", "[=]", "[U]", "[%]", "[S]"}; // Idealnya diganti Icon sungguhan
        
        for(int i = 0; i < NAV.length; i++){
            final int idx = i;
            navBtns[i] = makeNavBtn(icons[i] + "   " + NAV[i], false);
            navBtns[i].addActionListener(e -> switchPage(idx));
            nav.add(navBtns[i]); 
            nav.add(Box.createVerticalStrut(6));
        }
        top.add(nav);

        // Bottom User Panel
        JPanel bot = new JPanel(); 
        bot.setOpaque(false); 
        bot.setLayout(new BoxLayout(bot, BoxLayout.Y_AXIS));
        bot.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, Theme.BORDER),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JPanel avRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0)); 
        avRow.setOpaque(false);
        JPanel av = new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT2);
                g2.fillOval(0,0,40,40);
                g2.dispose();
            }
        };
        av.setOpaque(false); 
        av.setPreferredSize(new Dimension(40, 40));
        JLabel avL = new JLabel(String.valueOf(user.getNamaUser().charAt(0)).toUpperCase(), SwingConstants.CENTER);
        avL.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        avL.setForeground(Color.WHITE); 
        av.add(avL);
        
        JPanel avInfo = new JPanel(); 
        avInfo.setOpaque(false); 
        avInfo.setLayout(new BoxLayout(avInfo, BoxLayout.Y_AXIS));
        avInfo.add(UI.label(user.getNamaUser(), Theme.F_BOLD, Theme.TEXT));
        avInfo.add(Box.createVerticalStrut(2));
        avInfo.add(UI.label("Administrator", Theme.F_SMALL, Theme.RED));
        avRow.add(av); 
        avRow.add(avInfo);

        JButton btnOut = UI.linkBtn("Keluar dari sistem", Theme.MUTED); 
        btnOut.setFont(Theme.F_SMALL);
        btnOut.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnOut.addActionListener(e -> { 
            if(JOptionPane.showConfirmDialog(this, "Yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){ 
                new LoginForm().setVisible(true); 
                dispose(); 
            }
        });
        
        bot.add(avRow); 
        bot.add(Box.createVerticalStrut(16)); 
        bot.add(btnOut);

        sb.add(top, BorderLayout.CENTER); 
        sb.add(bot, BorderLayout.SOUTH); 
        return sb;
    }

    private JButton makeNavBtn(String text, boolean active){
        JButton btn = new JButton(text){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if(getBackground().equals(Theme.ACCENT)){
                    // Soft background fill for active state
                    g2.setColor(new Color(Theme.ACCENT.getRed(), Theme.ACCENT.getGreen(), Theme.ACCENT.getBlue(), 30));
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 10, 10);
                    
                    // Solid accent line indicator on the left
                    g2.setColor(Theme.ACCENT);
                    g2.fillRoundRect(8, 2, 5, getHeight() - 4, 4, 4);
                } else if (getBackground().equals(new Color(28, 36, 60))) { // Hover state
                    g2.setColor(new Color(28, 36, 60));
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(active ? Theme.F_BOLD : Theme.F_BODY);
        btn.setForeground(active ? Theme.TEXT : Theme.SUBTEXT);
        btn.setBackground(active ? Theme.ACCENT : Theme.SIDEBAR);
        btn.setContentAreaFilled(false); 
        btn.setBorderPainted(false); 
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT); 
        btn.setBorder(new EmptyBorder(12, 26, 12, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        
        btn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ 
                if(!btn.getBackground().equals(Theme.ACCENT)){
                    btn.setForeground(Theme.TEXT);
                    btn.setBackground(new Color(28, 36, 60));
                } 
            }
            public void mouseExited(MouseEvent e){  
                if(!btn.getBackground().equals(Theme.ACCENT)){
                    btn.setForeground(Theme.SUBTEXT);
                    btn.setBackground(Theme.SIDEBAR);
                } 
            }
        });
        return btn;
    }

    private void switchPage(int idx){
        for(int i = 0; i < navBtns.length; i++){
            boolean a = (i == idx);
            navBtns[i].setBackground(a ? Theme.ACCENT : Theme.SIDEBAR);
            navBtns[i].setForeground(a ? Theme.TEXT : Theme.SUBTEXT);
            navBtns[i].setFont(a ? Theme.F_BOLD : Theme.F_BODY);
        }
        content.removeAll();
        switch(idx){
            case 0: pageDashboard(); break;
            case 1: pageTransaksi(); break;
            case 2: pageUser();      break;
            case 3: pageVoucher();   break;
            case 4: pagePengaturan();break;
        }
        content.revalidate(); 
        content.repaint();
    }

    // ══════════════════════════════════════════════════════════
    // DASHBOARD
    // ══════════════════════════════════════════════════════════
    private void pageDashboard(){
        JPanel page = makePage(); 
        JPanel inner = innerPanel(page);
        
        // Header
        JPanel hdr = new JPanel(new BorderLayout()); 
        hdr.setOpaque(false);
        JPanel hl = new JPanel(); 
        hl.setOpaque(false); 
        hl.setLayout(new BoxLayout(hl, BoxLayout.Y_AXIS));
        hl.add(UI.label("Selamat Datang, " + user.getNamaUser() + "!", Theme.F_TITLE, Theme.TEXT));
        hl.add(Box.createVerticalStrut(6));
        hl.add(UI.label(new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id", "ID")).format(new Date()), Theme.F_SMALL, Theme.MUTED));
        
        JButton btnR = UI.smallBtn("Refresh", Theme.CARD2, Theme.SUBTEXT); 
        btnR.addActionListener(e -> switchPage(0));
        hdr.add(hl, BorderLayout.WEST); 
        hdr.add(btnR, BorderLayout.EAST);
        inner.add(hdr); 
        inner.add(Box.createVerticalStrut(30));

        // Stat cards Transaksi
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0)); 
        stats.setOpaque(false); 
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        stats.add(UI.statCard("Total Pendapatan", "Rp " + nf.format(trxDAO.getTotalPendapatan()), "dari transaksi sukses", Theme.GREEN_LT));
        stats.add(UI.statCard("Total Transaksi", String.valueOf(trxDAO.getTotalTrx()), "semua transaksi", Theme.ACCENT));
        stats.add(UI.statCard("Total Customer", String.valueOf(trxDAO.getTotalUser()), "user terdaftar", Theme.ACCENT2));
        stats.add(UI.statCard("Menunggu Bayar", String.valueOf(trxDAO.getTrxPending()), "perlu dikonfirmasi", Theme.YELLOW));
        inner.add(stats); 
        inner.add(Box.createVerticalStrut(24));

        // Stat Voucher
        ArrayList<String[]> allVouchers = new VoucherDAO().getAllVoucher();
        int totalVoucher = allVouchers.size();
        int activeVoucher = 0, expiredVoucher = 0;
        for (String[] v : allVouchers) {
            if (v[11].equals("Aktif")) activeVoucher++;
            else expiredVoucher++;
        }
        JPanel voucherStats = new JPanel(new GridLayout(1, 3, 16, 0));
        voucherStats.setOpaque(false);
        voucherStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        voucherStats.add(UI.statCard("Total Voucher", String.valueOf(totalVoucher), "semua voucher", Theme.ACCENT));
        voucherStats.add(UI.statCard("Voucher Aktif", String.valueOf(activeVoucher), "masih berlaku", Theme.GREEN_LT));
        voucherStats.add(UI.statCard("Voucher Nonaktif", String.valueOf(expiredVoucher), "tidak berlaku", Theme.RED));
        inner.add(voucherStats);
        inner.add(Box.createVerticalStrut(30));

        // Stat per game
        ArrayList<String[]> gs = trxDAO.getStatPerGame();
        if(!gs.isEmpty()){
            inner.add(UI.sectionHeader("Pendapatan Per Game", null));
            inner.add(Box.createVerticalStrut(10));
            JPanel gGrid = new JPanel(new GridLayout(1, 0, 16, 0)); 
            gGrid.setOpaque(false); 
            gGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            Color[] gc = Theme.GAME_COLORS;
            
            for(int i = 0; i < gs.size(); i++){
                String[] g = gs.get(i); 
                Color c = gc[i % gc.length];
                UI.RoundPanel gCard = new UI.RoundPanel(10, Theme.CARD, Theme.BORDER);
                gCard.setLayout(new BoxLayout(gCard, BoxLayout.Y_AXIS)); 
                gCard.setBorder(new EmptyBorder(14, 16, 14, 16));
                
                JPanel cbar = new JPanel(){
                    protected void paintComponent(Graphics g2){
                        Graphics2D gg = (Graphics2D)g2.create();
                        gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        gg.setColor(c);
                        gg.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                        gg.dispose();
                    }
                };
                cbar.setOpaque(false); 
                cbar.setPreferredSize(new Dimension(0, 4)); 
                cbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 4));
                
                gCard.add(cbar); 
                gCard.add(Box.createVerticalStrut(10));
                gCard.add(UI.label(g[0], Theme.F_SMBD, Theme.SUBTEXT));
                gCard.add(Box.createVerticalStrut(6));
                gCard.add(UI.label(g[1] + " trx", Theme.F_BOLD, c));
                gCard.add(UI.label(g[2], Theme.F_SMALL, Theme.MUTED));
                gGrid.add(gCard);
            }
            inner.add(gGrid); 
            inner.add(Box.createVerticalStrut(30));
        }

        // Recent transaksi
        inner.add(UI.sectionHeader("Transaksi Terbaru", null));
        inner.add(Box.createVerticalStrut(10));
        
        String[] cols = {"No. Invoice", "User", "Game", "Paket", "Total", "Metode", "Status", "Tanggal"};
        JTable tbl = UI.makeTable(cols);
        tbl.setRowHeight(35); // Lebih modern & lega
        tbl.getColumnModel().getColumn(6).setCellRenderer(UI.statusRenderer());
        
        int[] w = {155, 125, 130, 160, 115, 100, 95, 140};
        for(int i = 0; i < w.length; i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        
        DefaultTableModel m = (DefaultTableModel)tbl.getModel();
        for(String[] r : trxDAO.getTransaksiTerbaru(10)) m.addRow(r);
        
        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // TRANSAKSI
    // ══════════════════════════════════════════════════════════
    private void pageTransaksi(){
        JPanel page = makePage(); 
        JPanel inner = innerPanel(page);
        
        inner.add(UI.label("Kelola Transaksi", Theme.F_TITLE, Theme.TEXT));
        inner.add(UI.label("Lihat, filter, dan ubah status transaksi pengguna", Theme.F_SMALL, Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(24));

        // Modern Toolbar: Filter (Kiri) & Aksi (Kanan)
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        // Bagian Kiri: Filters
        JPanel fBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); 
        fBar.setOpaque(false); 
        fBar.add(UI.label("Status:", Theme.F_BOLD, Theme.TEXT));
        JComboBox<String> cbS = UI.comboBox(new String[]{"Semua", "pending", "success", "failed"}); 
        cbS.setPreferredSize(new Dimension(140, 38)); 
        fBar.add(cbS);
        fBar.add(Box.createHorizontalStrut(8));
        fBar.add(UI.label("Game:", Theme.F_BOLD, Theme.TEXT));
        JComboBox<String> cbG = UI.comboBox(new GameDAO().getGameNames()); 
        cbG.setPreferredSize(new Dimension(190, 38)); 
        fBar.add(cbG);
        JButton btnF = UI.smallBtn("Terapkan", Theme.ACCENT, Color.WHITE); 
        fBar.add(btnF);
        
        // Bagian Kanan: Aksi Ubah Status
        JPanel aBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        aBar.setOpaque(false);
        JButton btnSucc = UI.smallBtn("Tandai Success", new Color(16, 185, 129, 30), Theme.GREEN_LT);
        JButton btnPend = UI.smallBtn("Tandai Pending", new Color(245, 158, 11, 30), Theme.YELLOW);
        JButton btnFail = UI.smallBtn("Tandai Failed", new Color(239, 68, 68, 30), Theme.RED);
        aBar.add(btnSucc); 
        aBar.add(btnPend); 
        aBar.add(btnFail);

        toolbar.add(fBar, BorderLayout.WEST);
        toolbar.add(aBar, BorderLayout.EAST);
        inner.add(toolbar); 
        inner.add(Box.createVerticalStrut(16));

        String[] cols = {"ID", "No. Invoice", "User", "Game", "Paket", "UID", "Total", "Metode", "Status", "Tanggal"};
        JTable tbl = UI.makeTable(cols);
        tbl.setRowHeight(35);
        tbl.getColumnModel().getColumn(8).setCellRenderer(UI.statusRenderer());
        tbl.getColumnModel().getColumn(0).setMaxWidth(0); 
        tbl.getColumnModel().getColumn(0).setMinWidth(0); // hidden ID
        
        int[] w = {0, 160, 120, 120, 150, 100, 110, 100, 90, 140};
        for(int i = 0; i < w.length; i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        DefaultTableModel mdl = (DefaultTableModel)tbl.getModel();
        Runnable load = () -> {
            mdl.setRowCount(0);
            for(String[] r : trxDAO.getAllTransaksi((String)cbS.getSelectedItem(), (String)cbG.getSelectedItem())) mdl.addRow(r);
        };
        btnF.addActionListener(e -> load.run()); 
        load.run();

        ActionListener updateStatus = (ActionEvent e) -> {
            int row = tbl.getSelectedRow();
            if(row < 0){ 
                JOptionPane.showMessageDialog(this, "Pilih transaksi terlebih dahulu dari tabel!", "Peringatan", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            String idStr = (String)mdl.getValueAt(row, 0);
            String status = e.getSource() == btnSucc ? "success" : e.getSource() == btnPend ? "pending" : "failed";
            if(trxDAO.updateStatus(Integer.parseInt(idStr), status)){
                JOptionPane.showMessageDialog(this, "Status berhasil diubah ke: " + status, "Sukses", JOptionPane.INFORMATION_MESSAGE);
                load.run();
            }
        };
        
        btnSucc.addActionListener(updateStatus); 
        btnPend.addActionListener(updateStatus); 
        btnFail.addActionListener(updateStatus);

        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // KELOLA USER
    // ══════════════════════════════════════════════════════════
    private void pageUser(){
        JPanel page = makePage(); 
        JPanel inner = innerPanel(page);
        
        inner.add(UI.label("Kelola User", Theme.F_TITLE, Theme.TEXT));
        inner.add(UI.label("Daftar dan manajemen data customer", Theme.F_SMALL, Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(24));

        // Toolbar aksi
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        toolbar.setOpaque(false);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton btnDel = UI.smallBtn("Hapus User Terpilih", new Color(239, 68, 68, 30), Theme.RED);
        toolbar.add(btnDel);
        inner.add(toolbar);
        inner.add(Box.createVerticalStrut(10));

        String[] cols = {"ID", "Nama", "Email", "No. HP", "Total Trx", "Total Spend"};
        JTable tbl = UI.makeTable(cols);
        tbl.setRowHeight(35);
        tbl.getColumnModel().getColumn(0).setMaxWidth(50);
        
        int[] w = {50, 180, 220, 140, 100, 150};
        for(int i = 0; i < w.length; i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        
        DefaultTableModel mdl = (DefaultTableModel)tbl.getModel();
        Runnable load = () -> { 
            mdl.setRowCount(0); 
            for(String[] r : userDAO.getAllCustomer()) mdl.addRow(r); 
        };
        load.run();

        btnDel.addActionListener(e -> {
            int row = tbl.getSelectedRow();
            if(row < 0){ 
                JOptionPane.showMessageDialog(this, "Pilih user terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE); 
                return; 
            }
            String nama = (String)mdl.getValueAt(row, 1);
            if(JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus user " + nama + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                int id = Integer.parseInt((String)mdl.getValueAt(row, 0));
                if(userDAO.deleteUser(id)){ 
                    JOptionPane.showMessageDialog(this, "User berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE); 
                    load.run(); 
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menghapus user. Mungkin masih ada relasi data transaksi.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // VOUCHER
    // ══════════════════════════════════════════════════════════
    private void pageVoucher(){
        JPanel page = makePage();
        JPanel inner = innerPanel(page);
        
        inner.add(UI.label("Kelola Voucher", Theme.F_TITLE, Theme.TEXT));
        inner.add(UI.label("Manajemen promo, diskon, dan ketersediaan voucher", Theme.F_SMALL, Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(24));

        // Toolbar
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        JButton btnRefresh = UI.smallBtn("Refresh Data", Theme.ACCENT, Color.WHITE);
        
        // Placeholder untuk penambahan fitur kedepannya
        JButton btnAdd = UI.smallBtn("+ Tambah Voucher", new Color(16, 185, 129, 30), Theme.GREEN_LT); 
        
        toolbar.add(btnRefresh, BorderLayout.WEST);
        toolbar.add(btnAdd, BorderLayout.EAST);
        inner.add(toolbar);
        inner.add(Box.createVerticalStrut(16));

        String[] cols = {"Kode", "Keterangan", "Tipe", "Diskon", "Min. Trx", "Kuota", "Terpakai", "Sisa", "Berlaku Sampai", "Status"};
        JTable tbl = UI.makeTable(cols);
        tbl.setRowHeight(35);
        tbl.getColumnModel().getColumn(9).setCellRenderer(UI.statusRenderer());
        
        int[] w = {110, 200, 80, 90, 110, 70, 80, 70, 130, 90};
        for (int i = 0; i < w.length; i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        DefaultTableModel mdl = (DefaultTableModel) tbl.getModel();
        Runnable load = () -> {
            mdl.setRowCount(0);
            for (String[] r : new VoucherDAO().getAllVoucher()) {
                mdl.addRow(new String[]{r[1], r[2], r[3], r[4], r[5], r[6], r[7], r[8], r[10], r[11]});
            }
        };
        load.run();
        btnRefresh.addActionListener(e -> load.run());
        btnAdd.addActionListener(e -> JOptionPane.showMessageDialog(this, "Fitur Tambah Voucher akan segera hadir!", "Info", JOptionPane.INFORMATION_MESSAGE));

        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // PENGATURAN
    // ══════════════════════════════════════════════════════════
    private void pagePengaturan(){
        JPanel page = makePage(); 
        JPanel inner = innerPanel(page);
        
        inner.add(UI.label("Pengaturan Akun Admin", Theme.F_TITLE, Theme.TEXT));
        inner.add(UI.label("Perbarui detail profil dan kata sandi keamanan Anda", Theme.F_SMALL, Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(28));

        UI.RoundPanel card = new UI.RoundPanel(14, Theme.CARD, Theme.BORDER);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(30, 36, 30, 36));
        card.setMaximumSize(new Dimension(550, Integer.MAX_VALUE));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel av = new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT2);
                g2.fillOval(0, 0, 64, 64);
                g2.dispose();
            }
        };
        av.setOpaque(false); 
        av.setPreferredSize(new Dimension(64, 64)); 
        av.setMaximumSize(new Dimension(64, 64));
        JLabel avL = new JLabel(String.valueOf(user.getNamaUser().charAt(0)).toUpperCase(), SwingConstants.CENTER);
        avL.setFont(new Font("Segoe UI", Font.BOLD, 28)); 
        avL.setForeground(Color.WHITE); 
        av.add(avL);
        av.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(av); 
        card.add(Box.createVerticalStrut(14));
        card.add(UI.label(user.getNamaUser(), new Font("Segoe UI", Font.BOLD, 20), Theme.TEXT));
        card.add(Box.createVerticalStrut(4));
        card.add(UI.label("Administrator Panel", Theme.F_SMBD, Theme.RED));
        
        card.add(Box.createVerticalStrut(24)); 
        card.add(UI.sep()); 
        card.add(Box.createVerticalStrut(24));

        JTextField fNama = UI.textField(); 
        fNama.setText(user.getNamaUser()); 
        fNama.setForeground(Theme.TEXT);
        
        JTextField fEmail = UI.textField(); 
        fEmail.setText(user.getEmail()); 
        fEmail.setForeground(Theme.TEXT);
        
        JTextField fHp = UI.textField(); 
        fHp.setText(user.getNomorHp()); 
        fHp.setForeground(Theme.TEXT);
        
        JPasswordField fPass = UI.passwordField();
        JPasswordField fConf = UI.passwordField();

        UI.addField(card, "Nama Lengkap", fNama); 
        UI.addField(card, "Alamat Email", fEmail); 
        UI.addField(card, "Nomor Handphone", fHp);
        
        card.add(UI.sep()); 
        card.add(Box.createVerticalStrut(20));
        card.add(UI.label("Ganti Password (kosongkan jika tidak diubah)", Theme.F_SMBD, Theme.MUTED));
        card.add(Box.createVerticalStrut(14));
        
        UI.addField(card, "Password Baru", fPass); 
        UI.addField(card, "Konfirmasi Password", fConf);

        JLabel lblMsg = new JLabel(" "); 
        lblMsg.setFont(Theme.F_SMALL); 
        lblMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnSave = UI.primaryBtn("Simpan Perubahan Profil", Theme.ACCENT, Color.WHITE);
        btnSave.setPreferredSize(new Dimension(200, 42));
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        
        btnSave.addActionListener(e -> {
            new UserDAO().updateUser(user.getIdUser(), fNama.getText().trim(), fEmail.getText().trim(), fHp.getText().trim());
            String np = new String(fPass.getPassword());
            if(!np.isEmpty()){
                if(!np.equals(new String(fConf.getPassword()))){ 
                    lblMsg.setText("Password konfirmasi tidak cocok!"); 
                    lblMsg.setForeground(Theme.RED); 
                    return; 
                }
                new UserDAO().updatePassword(user.getIdUser(), np);
            }
            lblMsg.setText("Perubahan profil berhasil disimpan!"); 
            lblMsg.setForeground(Theme.GREEN_LT);
            user.setNamaUser(fNama.getText().trim()); // Update instance lokal
        });
        
        card.add(Box.createVerticalStrut(10));
        card.add(lblMsg); 
        card.add(Box.createVerticalStrut(16)); 
        card.add(btnSave);
        
        inner.add(card);
        content.add(UI.wrapScroll(page));
    }

    private JPanel makePage(){
        JPanel p = new JPanel(){
            protected void paintComponent(Graphics g){
                g.setColor(Theme.BG);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        p.setLayout(new BorderLayout());
        return p;
    }
    
    private JPanel innerPanel(JPanel page){
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        // Padding diperlebar menjadi 30 40 30 40 untuk look yang lebih lega
        p.setBorder(new EmptyBorder(30, 40, 30, 40));
        page.add(p, BorderLayout.CENTER);
        return p;
    }
}
