package view;

import config.*;
import dao.UserDAO;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterForm extends JFrame {
    private JTextField txtNama,txtEmail,txtHp;
    private JPasswordField txtPass,txtKonfirm;
    private JLabel lblMsg;

    public RegisterForm(){ initUI(); setLocationRelativeTo(null); }

    private void initUI(){
        setTitle("ZenithStore - Daftar Akun");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920,600); setResizable(false);
        setLayout(new GridLayout(1,2));

        // Kiri
        JPanel left=new JPanel(new GridBagLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new java.awt.GradientPaint(0,0,new Color(50,20,100),getWidth(),getHeight(),new Color(8,12,24)));
                g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        JPanel brand=new JPanel(); brand.setOpaque(false); brand.setLayout(new BoxLayout(brand,BoxLayout.Y_AXIS)); brand.setBorder(new EmptyBorder(0,36,0,36));
        JPanel circle=new JPanel(new BorderLayout()){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Theme.ACCENT2);g2.fillOval(0,0,68,68);g2.dispose();}};
        circle.setOpaque(false); circle.setPreferredSize(new Dimension(68,68)); circle.setMaximumSize(new Dimension(68,68));
        JLabel zl=new JLabel("Z",SwingConstants.CENTER); zl.setFont(new Font("Segoe UI",Font.BOLD,34)); zl.setForeground(Color.WHITE);
        circle.add(zl); circle.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lt=UI.label("ZenithStore",new Font("Segoe UI",Font.BOLD,30),Color.WHITE); lt.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel ls=UI.label("Bergabung & nikmati kemudahan top-up",Theme.F_BODY,new Color(180,170,220)); ls.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.add(circle); brand.add(Box.createVerticalStrut(16)); brand.add(lt); brand.add(Box.createVerticalStrut(8)); brand.add(ls);
        left.add(brand);

        // Kanan
        JPanel right=new JPanel(new GridBagLayout()){protected void paintComponent(Graphics g){g.setColor(Theme.BG);g.fillRect(0,0,getWidth(),getHeight());}};
        right.setBorder(new EmptyBorder(30,52,30,52));
        JPanel form=new JPanel(); form.setOpaque(false); form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS)); form.setPreferredSize(new Dimension(330,500));

        JLabel lH=UI.label("Buat Akun Baru",new Font("Segoe UI",Font.BOLD,24),Theme.TEXT); lH.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lS=UI.label("Isi data diri kamu untuk mendaftar",Theme.F_BODY,Theme.SUBTEXT); lS.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtNama=UI.textField(); txtEmail=UI.textField(); txtHp=UI.textField();
        txtPass=UI.passwordField(); txtKonfirm=UI.passwordField();
        lblMsg=new JLabel(" "); lblMsg.setFont(Theme.F_SMALL); lblMsg.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnDaftar=UI.primaryBtn("Buat Akun Sekarang",Theme.ACCENT2,Color.WHITE);
        btnDaftar.setFont(new Font("Segoe UI",Font.BOLD,14));
        btnDaftar.addActionListener(e->doDaftar());

        JPanel lr=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)); lr.setOpaque(false); lr.setAlignmentX(Component.LEFT_ALIGNMENT);
        lr.add(UI.label("Sudah punya akun? ",Theme.F_SMALL,Theme.SUBTEXT));
        JButton btnL=UI.linkBtn("Masuk di sini",Theme.ACCENT); btnL.setFont(Theme.F_SMBD);
        btnL.addActionListener(e->{ new LoginForm().setVisible(true); dispose(); });
        lr.add(btnL);

        form.add(lH); form.add(Box.createVerticalStrut(4)); form.add(lS); form.add(Box.createVerticalStrut(22));
        UI.addField(form,"Nama Lengkap",txtNama);
        UI.addField(form,"Email",txtEmail);
        UI.addField(form,"Nomor HP",txtHp);
        UI.addField(form,"Password",txtPass);
        UI.addField(form,"Konfirmasi Password",txtKonfirm);
        form.add(lblMsg); form.add(Box.createVerticalStrut(10));
        form.add(btnDaftar); form.add(Box.createVerticalStrut(16)); form.add(lr);
        right.add(form);

        add(left); add(right);
        getRootPane().setDefaultButton(btnDaftar);
    }

    private void doDaftar(){
        String nama=txtNama.getText().trim(),email=txtEmail.getText().trim(),hp=txtHp.getText().trim();
        String pass=new String(txtPass.getPassword()),conf=new String(txtKonfirm.getPassword());
        if(nama.isEmpty()||email.isEmpty()||hp.isEmpty()||pass.isEmpty()){ setMsg("Semua field wajib diisi!",Theme.RED); return; }
        if(!pass.equals(conf)){ setMsg("Password tidak cocok!",Theme.RED); return; }
        if(pass.length()<6){ setMsg("Password minimal 6 karakter!",Theme.YELLOW); return; }
        UserDAO dao=new UserDAO();
        if(dao.emailExists(email)){ setMsg("Email sudah terdaftar!",Theme.RED); return; }
        if(dao.register(nama,email,hp,pass)){
            setMsg("Akun berhasil dibuat! Silakan login.",Theme.GREEN_LT);
            Timer t=new Timer(1200,e->{ new LoginForm().setVisible(true); dispose(); }); t.setRepeats(false); t.start();
        } else { setMsg("Gagal mendaftar. Coba lagi.",Theme.RED); }
    }
    private void setMsg(String m,Color c){ lblMsg.setText(m); lblMsg.setForeground(c); }
}
