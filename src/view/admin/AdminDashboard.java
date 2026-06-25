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
    private static final String[] NAV = {"Dashboard","Transaksi","Kelola User","Voucher","Pengaturan"};

    public AdminDashboard(User user){
        this.user=user; initUI(); setLocationRelativeTo(null); switchPage(0);
    }

    private void initUI(){
        setTitle("ZenithStore - Admin Panel");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1250,720); setMinimumSize(new Dimension(1050,620));
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());
        add(buildSidebar(),BorderLayout.WEST);
        content=new JPanel(new BorderLayout()); content.setBackground(Theme.BG);
        add(content,BorderLayout.CENTER);
    }

    private JPanel buildSidebar(){
        JPanel sb=new JPanel(){protected void paintComponent(Graphics g){g.setColor(Theme.SIDEBAR);g.fillRect(0,0,getWidth(),getHeight());}};
        sb.setPreferredSize(new Dimension(220,0));
        sb.setBorder(new MatteBorder(0,0,0,1,Theme.BORDER));
        sb.setLayout(new BorderLayout());

        JPanel top=new JPanel(); top.setOpaque(false); top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
        top.setBorder(new EmptyBorder(22,18,14,18));

        // Logo
        JPanel logoRow=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); logoRow.setOpaque(false);
        JPanel dot=new JPanel(){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Theme.ACCENT);g2.fillOval(0,3,26,26);g2.dispose();}};
        dot.setOpaque(false); dot.setPreferredSize(new Dimension(26,32));
        logoRow.add(dot); logoRow.add(UI.label("ZenithStore",Theme.F_LOGO,Theme.TEXT));
        // Admin badge
        JPanel badge=new JPanel(new FlowLayout(FlowLayout.LEFT,0,4)); badge.setOpaque(false);
        UI.RoundPanel bp=new UI.RoundPanel(4,new Color(239,68,68,40),new Color(239,68,68,80));
        bp.setLayout(new FlowLayout(0,6,2)); bp.add(UI.label("ADMIN PANEL",Theme.F_SMBD,Theme.RED));
        badge.add(bp);

        top.add(logoRow); top.add(badge); top.add(Box.createVerticalStrut(16)); top.add(UI.sep());
        top.add(Box.createVerticalStrut(14));

        // Nav
        JPanel nav=new JPanel(); nav.setOpaque(false); nav.setLayout(new BoxLayout(nav,BoxLayout.Y_AXIS));
        navBtns=new JButton[NAV.length];
        String[] icons={"[ ]","[=]","[U]","[%]","[S]"};
        for(int i=0;i<NAV.length;i++){
            final int idx=i;
            navBtns[i]=makeNavBtn(icons[i]+"  "+NAV[i],false);
            navBtns[i].addActionListener(e->switchPage(idx));
            nav.add(navBtns[i]); nav.add(Box.createVerticalStrut(2));
        }
        top.add(nav);

        // Bottom user panel
        JPanel bot=new JPanel(); bot.setOpaque(false); bot.setLayout(new BoxLayout(bot,BoxLayout.Y_AXIS));
        bot.setBorder(new CompoundBorder(new MatteBorder(1,0,0,0,Theme.BORDER),new EmptyBorder(14,18,18,18)));

        JPanel avRow=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); avRow.setOpaque(false);
        JPanel av=new JPanel(new BorderLayout()){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Theme.RED);g2.fillOval(0,0,36,36);g2.dispose();}};
        av.setOpaque(false); av.setPreferredSize(new Dimension(36,36));
        JLabel avL=new JLabel(String.valueOf(user.getNamaUser().charAt(0)).toUpperCase(),SwingConstants.CENTER);
        avL.setFont(new Font("Segoe UI",Font.BOLD,16)); avL.setForeground(Color.WHITE); av.add(avL);
        JPanel avInfo=new JPanel(); avInfo.setOpaque(false); avInfo.setLayout(new BoxLayout(avInfo,BoxLayout.Y_AXIS));
        avInfo.add(UI.label(user.getNamaUser(),Theme.F_BOLD,Theme.TEXT));
        avInfo.add(UI.label("Administrator",Theme.F_SMALL,Theme.RED));
        avRow.add(av); avRow.add(avInfo);

        JButton btnOut=UI.linkBtn("Keluar dari sistem",Theme.MUTED); btnOut.setFont(Theme.F_SMALL);
        btnOut.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnOut.addActionListener(e->{ if(JOptionPane.showConfirmDialog(this,"Yakin ingin keluar?","Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){ new LoginForm().setVisible(true); dispose(); }});
        bot.add(avRow); bot.add(Box.createVerticalStrut(10)); bot.add(btnOut);

        sb.add(top,BorderLayout.CENTER); sb.add(bot,BorderLayout.SOUTH); return sb;
    }

    private JButton makeNavBtn(String text,boolean active){
        JButton btn=new JButton(text){
            protected void paintComponent(Graphics g){
                if(getBackground().equals(Theme.ACCENT)){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.ACCENT);
                    g2.fillRoundRect(6,2,getWidth()-12,getHeight()-4,8,8); g2.dispose();
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(active?Theme.F_BOLD:Theme.F_BODY);
        btn.setForeground(active?Color.WHITE:Theme.SUBTEXT);
        btn.setBackground(active?Theme.ACCENT:Theme.SIDEBAR);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false); btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT); btn.setBorder(new EmptyBorder(10,18,10,18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));
        btn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){ if(!btn.getBackground().equals(Theme.ACCENT)){btn.setForeground(Theme.TEXT);btn.setBackground(new Color(28,36,60));} }
            public void mouseExited(MouseEvent e){  if(!btn.getBackground().equals(Theme.ACCENT)){btn.setForeground(Theme.SUBTEXT);btn.setBackground(Theme.SIDEBAR);} }
        });
        return btn;
    }

    private void switchPage(int idx){
        for(int i=0;i<navBtns.length;i++){
            boolean a=(i==idx);
            navBtns[i].setBackground(a?Theme.ACCENT:Theme.SIDEBAR);
            navBtns[i].setForeground(a?Color.WHITE:Theme.SUBTEXT);
            navBtns[i].setFont(a?Theme.F_BOLD:Theme.F_BODY);
        }
        content.removeAll();
        switch(idx){
            case 0: pageDashboard(); break;
            case 1: pageTransaksi(); break;
            case 2: pageUser();      break;
            case 3: pageVoucher();   break;
            case 4: pagePengaturan();break;
        }
        content.revalidate(); content.repaint();
    }

    // ══════════════════════════════════════════════════════════
    // DASHBOARD
    // ══════════════════════════════════════════════════════════
    private void pageDashboard(){
        JPanel page=makePage(); JPanel inner=innerPanel(page);
        // Header
        JPanel hdr=new JPanel(new BorderLayout()); hdr.setOpaque(false);
        JPanel hl=new JPanel(); hl.setOpaque(false); hl.setLayout(new BoxLayout(hl,BoxLayout.Y_AXIS));
        hl.add(UI.label("Selamat Datang, "+user.getNamaUser()+"!",Theme.F_TITLE,Theme.TEXT));
        hl.add(Box.createVerticalStrut(3));
        hl.add(UI.label(new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy",new Locale("id","ID")).format(new Date()),Theme.F_SMALL,Theme.MUTED));
        JButton btnR=UI.smallBtn("Refresh",Theme.CARD2,Theme.SUBTEXT); btnR.addActionListener(e->switchPage(0));
        hdr.add(hl,BorderLayout.WEST); hdr.add(btnR,BorderLayout.EAST);
        inner.add(hdr); inner.add(Box.createVerticalStrut(22));

        // Stat cards
        NumberFormat nf=NumberFormat.getNumberInstance(new Locale("id","ID"));
        JPanel stats=new JPanel(new GridLayout(1,4,14,0)); stats.setOpaque(false); stats.setMaximumSize(new Dimension(Integer.MAX_VALUE,115));
        stats.add(UI.statCard("Total Pendapatan","Rp "+nf.format(trxDAO.getTotalPendapatan()),"dari transaksi sukses",Theme.GREEN_LT));
        stats.add(UI.statCard("Total Transaksi",String.valueOf(trxDAO.getTotalTrx()),"semua transaksi",Theme.ACCENT));
        stats.add(UI.statCard("Total Customer",String.valueOf(trxDAO.getTotalUser()),"user terdaftar",Theme.ACCENT2));
        stats.add(UI.statCard("Menunggu Bayar",String.valueOf(trxDAO.getTrxPending()),"perlu dikonfirmasi",Theme.YELLOW));
        inner.add(stats); inner.add(Box.createVerticalStrut(22));

        // Stat per game
        ArrayList<String[]> gs=trxDAO.getStatPerGame();
        if(!gs.isEmpty()){
            inner.add(UI.sectionHeader("Pendapatan Per Game",null));
            JPanel gGrid=new JPanel(new GridLayout(1,0,12,0)); gGrid.setOpaque(false); gGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE,95));
            Color[] gc=Theme.GAME_COLORS;
            for(int i=0;i<gs.size();i++){
                String[] g=gs.get(i); Color c=gc[i%gc.length];
                UI.RoundPanel gCard=new UI.RoundPanel(10,Theme.CARD,Theme.BORDER);
                gCard.setLayout(new BoxLayout(gCard,BoxLayout.Y_AXIS)); gCard.setBorder(new EmptyBorder(12,14,12,14));
                // Color bar top
                JPanel cbar=new JPanel(){protected void paintComponent(Graphics g2){Graphics2D gg=(Graphics2D)g2.create();gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);gg.setColor(c);gg.fillRoundRect(0,0,getWidth(),getHeight(),4,4);gg.dispose();}};
                cbar.setOpaque(false); cbar.setPreferredSize(new Dimension(0,3)); cbar.setMaximumSize(new Dimension(Integer.MAX_VALUE,3));
                gCard.add(cbar); gCard.add(Box.createVerticalStrut(8));
                gCard.add(UI.label(g[0],Theme.F_SMBD,Theme.SUBTEXT));
                gCard.add(Box.createVerticalStrut(4));
                gCard.add(UI.label(g[1]+" trx",Theme.F_BOLD,c));
                gCard.add(UI.label(g[2],Theme.F_SMALL,Theme.MUTED));
                gGrid.add(gCard);
            }
            inner.add(gGrid); inner.add(Box.createVerticalStrut(22));
        }

        // Recent transaksi
        inner.add(UI.sectionHeader("Transaksi Terbaru",null));
        String[] cols={"No. Invoice","User","Game","Paket","Total","Metode","Status","Tanggal"};
        JTable tbl=UI.makeTable(cols);
        tbl.getColumnModel().getColumn(6).setCellRenderer(UI.statusRenderer());
        int[] w={155,115,130,160,105,95,85,140};
        for(int i=0;i<w.length;i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        DefaultTableModel m=(DefaultTableModel)tbl.getModel();
        for(String[] r:trxDAO.getTransaksiTerbaru(10)) m.addRow(r);
        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // TRANSAKSI (dengan ubah status)
    // ══════════════════════════════════════════════════════════
    private void pageTransaksi(){
        JPanel page=makePage(); JPanel inner=innerPanel(page);
        inner.add(UI.label("Kelola Transaksi",Theme.F_TITLE,Theme.TEXT));
        inner.add(UI.label("Lihat dan kelola semua transaksi",Theme.F_SMALL,Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(18));

        // Filter bar
        JPanel fBar=new JPanel(new FlowLayout(FlowLayout.LEFT,10,0)); fBar.setOpaque(false); fBar.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));
        fBar.add(UI.label("Status:",Theme.F_BOLD,Theme.TEXT));
        JComboBox<String> cbS=UI.comboBox(new String[]{"Semua","pending","success","failed"}); cbS.setPreferredSize(new Dimension(140,36)); fBar.add(cbS);
        fBar.add(Box.createHorizontalStrut(10));
        fBar.add(UI.label("Game:",Theme.F_BOLD,Theme.TEXT));
        JComboBox<String> cbG=UI.comboBox(new GameDAO().getGameNames()); cbG.setPreferredSize(new Dimension(190,36)); fBar.add(cbG);
        JButton btnF=UI.smallBtn("Terapkan",Theme.ACCENT,Color.WHITE); fBar.add(btnF);
        inner.add(fBar); inner.add(Box.createVerticalStrut(14));

        String[] cols={"ID","No. Invoice","User","Game","Paket","UID","Total","Metode","Status","Tanggal"};
        JTable tbl=UI.makeTable(cols);
        tbl.getColumnModel().getColumn(8).setCellRenderer(UI.statusRenderer());
        tbl.getColumnModel().getColumn(0).setMaxWidth(0); tbl.getColumnModel().getColumn(0).setMinWidth(0); // hidden ID
        int[] w={0,150,110,110,140,90,100,90,80,130};
        for(int i=0;i<w.length;i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

        DefaultTableModel mdl=(DefaultTableModel)tbl.getModel();
        Runnable load=()->{
            mdl.setRowCount(0);
            for(String[] r:trxDAO.getAllTransaksi((String)cbS.getSelectedItem(),(String)cbG.getSelectedItem())) mdl.addRow(r);
        };
        btnF.addActionListener(e->load.run()); load.run();

        // Panel ubah status
        UI.RoundPanel actionCard=new UI.RoundPanel(10,Theme.CARD,Theme.BORDER);
        actionCard.setLayout(new FlowLayout(FlowLayout.LEFT,12,10));
        actionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,56));
        actionCard.add(UI.label("Ubah Status Transaksi Dipilih:",Theme.F_BOLD,Theme.TEXT));
        JButton btnSucc=UI.smallBtn("Tandai Success",new Color(16,185,129,80),Theme.GREEN_LT);
        JButton btnPend=UI.smallBtn("Tandai Pending",new Color(245,158,11,80),Theme.YELLOW);
        JButton btnFail=UI.smallBtn("Tandai Failed",new Color(239,68,68,80),Theme.RED);
        actionCard.add(btnSucc); actionCard.add(btnPend); actionCard.add(btnFail);

        ActionListener updateStatus=(ActionEvent e)->{
            int row=tbl.getSelectedRow();
            if(row<0){ JOptionPane.showMessageDialog(this,"Pilih transaksi terlebih dahulu!","Info",JOptionPane.WARNING_MESSAGE); return; }
            String idStr=(String)mdl.getValueAt(row,0);
            String status=e.getSource()==btnSucc?"success":e.getSource()==btnPend?"pending":"failed";
            if(trxDAO.updateStatus(Integer.parseInt(idStr),status)){
                JOptionPane.showMessageDialog(this,"Status berhasil diubah ke: "+status,"Sukses",JOptionPane.INFORMATION_MESSAGE);
                load.run();
            }
        };
        btnSucc.addActionListener(updateStatus); btnPend.addActionListener(updateStatus); btnFail.addActionListener(updateStatus);

        inner.add(actionCard); inner.add(Box.createVerticalStrut(10));
        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // KELOLA USER
    // ══════════════════════════════════════════════════════════
    private void pageUser(){
        JPanel page=makePage(); JPanel inner=innerPanel(page);
        inner.add(UI.label("Kelola User",Theme.F_TITLE,Theme.TEXT));
        inner.add(UI.label("Daftar semua customer yang terdaftar",Theme.F_SMALL,Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(18));

        String[] cols={"ID","Nama","Email","No. HP","Total Trx","Total Spend"};
        JTable tbl=UI.makeTable(cols);
        tbl.getColumnModel().getColumn(0).setMaxWidth(50);
        int[] w={50,160,200,130,80,130};
        for(int i=0;i<w.length;i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        DefaultTableModel mdl=(DefaultTableModel)tbl.getModel();
        Runnable load=()->{ mdl.setRowCount(0); for(String[] r:userDAO.getAllCustomer()) mdl.addRow(r); };
        load.run();

        // Action
        UI.RoundPanel actionCard=new UI.RoundPanel(10,Theme.CARD,Theme.BORDER);
        actionCard.setLayout(new FlowLayout(FlowLayout.LEFT,12,10));
        actionCard.setMaximumSize(new Dimension(Integer.MAX_VALUE,56));
        actionCard.add(UI.label("Aksi pada user dipilih:",Theme.F_BOLD,Theme.TEXT));
        JButton btnDel=UI.smallBtn("Hapus User",new Color(239,68,68,80),Theme.RED);
        btnDel.addActionListener(e->{
            int row=tbl.getSelectedRow();
            if(row<0){ JOptionPane.showMessageDialog(this,"Pilih user terlebih dahulu!","Info",JOptionPane.WARNING_MESSAGE); return; }
            String nama=(String)mdl.getValueAt(row,1);
            if(JOptionPane.showConfirmDialog(this,"Hapus user "+nama+"?","Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                int id=Integer.parseInt((String)mdl.getValueAt(row,0));
                if(userDAO.deleteUser(id)){ JOptionPane.showMessageDialog(this,"User berhasil dihapus.","Sukses",JOptionPane.INFORMATION_MESSAGE); load.run(); }
                else JOptionPane.showMessageDialog(this,"Gagal menghapus user. Mungkin masih ada transaksi.","Error",JOptionPane.ERROR_MESSAGE);
            }
        });
        actionCard.add(btnDel);
        inner.add(actionCard); inner.add(Box.createVerticalStrut(10));
        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // VOUCHER
    // ══════════════════════════════════════════════════════════
    private void pageVoucher(){
        JPanel page=makePage(); JPanel inner=innerPanel(page);
        inner.add(UI.label("Kelola Voucher",Theme.F_TITLE,Theme.TEXT));
        inner.add(UI.label("Daftar semua voucher & promo aktif",Theme.F_SMALL,Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(18));
        String[] cols={"Kode","Keterangan","Tipe","Diskon","Min. Trx","Kuota","Sisa","Berlaku Sampai","Status"};
        JTable tbl=UI.makeTable(cols);
        tbl.getColumnModel().getColumn(8).setCellRenderer(UI.statusRenderer());
        int[] w={110,220,70,80,100,60,50,120,80};
        for(int i=0;i<w.length;i++) tbl.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        DefaultTableModel mdl=(DefaultTableModel)tbl.getModel();
        for(String[] r:new VoucherDAO().getAllVoucher()) mdl.addRow(new String[]{r[1],r[2],r[3],r[4],r[5],r[6],r[8],r[10],r[11]});
        inner.add(UI.tableScroll(tbl));
        content.add(UI.wrapScroll(page));
    }

    // ══════════════════════════════════════════════════════════
    // PENGATURAN
    // ══════════════════════════════════════════════════════════
    private void pagePengaturan(){
        JPanel page=makePage(); JPanel inner=innerPanel(page);
        inner.add(UI.label("Pengaturan Akun Admin",Theme.F_TITLE,Theme.TEXT));
        inner.add(UI.label("Ubah informasi akun administrator",Theme.F_SMALL,Theme.SUBTEXT));
        inner.add(Box.createVerticalStrut(20));

        UI.RoundPanel card=new UI.RoundPanel(12,Theme.CARD,Theme.BORDER);
        card.setLayout(new BoxLayout(card,BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(24,26,24,26));
        card.setMaximumSize(new Dimension(500,Integer.MAX_VALUE));

        JPanel av=new JPanel(new BorderLayout()){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Theme.RED);g2.fillOval(0,0,56,56);g2.dispose();}};
        av.setOpaque(false); av.setPreferredSize(new Dimension(56,56)); av.setMaximumSize(new Dimension(56,56));
        JLabel avL=new JLabel(String.valueOf(user.getNamaUser().charAt(0)).toUpperCase(),SwingConstants.CENTER);
        avL.setFont(new Font("Segoe UI",Font.BOLD,24)); avL.setForeground(Color.WHITE); av.add(avL);
        av.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(av); card.add(Box.createVerticalStrut(10));
        card.add(UI.label(user.getNamaUser(),new Font("Segoe UI",Font.BOLD,16),Theme.TEXT));
        card.add(UI.label("Administrator",Theme.F_SMBD,Theme.RED));
        card.add(Box.createVerticalStrut(18)); card.add(UI.sep()); card.add(Box.createVerticalStrut(18));

        JTextField fNama=UI.textField(); fNama.setText(user.getNamaUser()); fNama.setForeground(Theme.TEXT);
        JTextField fEmail=UI.textField(); fEmail.setText(user.getEmail()); fEmail.setForeground(Theme.TEXT);
        JTextField fHp=UI.textField(); fHp.setText(user.getNomorHp()); fHp.setForeground(Theme.TEXT);
        JPasswordField fPass=UI.passwordField(), fConf=UI.passwordField();

        UI.addField(card,"Nama",fNama); UI.addField(card,"Email",fEmail); UI.addField(card,"No. HP",fHp);
        card.add(UI.sep()); card.add(Box.createVerticalStrut(14));
        card.add(UI.label("Ganti Password (kosongkan jika tidak diubah)",Theme.F_SMBD,Theme.MUTED));
        card.add(Box.createVerticalStrut(10));
        UI.addField(card,"Password Baru",fPass); UI.addField(card,"Konfirmasi",fConf);

        JLabel lblMsg=new JLabel(" "); lblMsg.setFont(Theme.F_SMALL); lblMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnSave=UI.primaryBtn("Simpan Perubahan",Theme.ACCENT,Color.WHITE);
        btnSave.addActionListener(e->{
            new UserDAO().updateUser(user.getIdUser(),fNama.getText().trim(),fEmail.getText().trim(),fHp.getText().trim());
            String np=new String(fPass.getPassword());
            if(!np.isEmpty()){
                if(!np.equals(new String(fConf.getPassword()))){ lblMsg.setText("Password tidak cocok!"); lblMsg.setForeground(Theme.RED); return; }
                new UserDAO().updatePassword(user.getIdUser(),np);
            }
            lblMsg.setText("Perubahan berhasil disimpan!"); lblMsg.setForeground(Theme.GREEN_LT);
        });
        card.add(lblMsg); card.add(Box.createVerticalStrut(10)); card.add(btnSave);
        inner.add(card);
        content.add(UI.wrapScroll(page));
    }

    private JPanel makePage(){JPanel p=new JPanel(){protected void paintComponent(Graphics g){g.setColor(Theme.BG);g.fillRect(0,0,getWidth(),getHeight());}};p.setLayout(new BorderLayout());return p;}
    private JPanel innerPanel(JPanel page){JPanel p=new JPanel();p.setOpaque(false);p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setBorder(new EmptyBorder(26,26,26,26));page.add(p,BorderLayout.CENTER);return p;}
}
