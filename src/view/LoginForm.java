package view;

import config.*;
import dao.UserDAO;
import model.User;
import view.admin.AdminDashboard;
import view.customer.CustomerDashboard;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class LoginForm extends JFrame {
    private JTextField     txtEmail;
    private JPasswordField txtPass;
    private JLabel         lblMsg;

    public LoginForm(){ initUI(); setLocationRelativeTo(null); }

    private void initUI(){
        setTitle("ZenithStore - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(920,580); setResizable(false);
        setLayout(new GridLayout(1,2));

        // ── KIRI branding ─────────────────────────────────────
        JPanel left=new JPanel(new GridBagLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setPaint(new GradientPaint(0,0,new Color(40,20,90),getWidth(),getHeight(),new Color(8,12,24)));
                g2.fillRect(0,0,getWidth(),getHeight()); g2.dispose();
            }
        };
        JPanel brand=new JPanel(); brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand,BoxLayout.Y_AXIS));
        brand.setBorder(new EmptyBorder(0,36,0,36));

        // Logo
        JPanel circle=new JPanel(new BorderLayout()){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.ACCENT); g2.fillOval(0,0,68,68); g2.dispose();
            }
        };
        circle.setOpaque(false); circle.setPreferredSize(new Dimension(68,68));
        circle.setMaximumSize(new Dimension(68,68));
        JLabel zl=new JLabel("Z",SwingConstants.CENTER);
        zl.setFont(new Font("Segoe UI",Font.BOLD,34)); zl.setForeground(Color.WHITE);
        circle.add(zl); circle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lName=UI.label("ZenithStore",new Font("Segoe UI",Font.BOLD,32),Color.WHITE); lName.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lTag=UI.label("Platform Top-Up Game Terpercaya",Theme.F_BODY,new Color(180,170,220)); lTag.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Game badges
        JPanel gameRow=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); gameRow.setOpaque(false); gameRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] gnames={"HSR","Genshin","Roblox","MLBB","FF","PUBG"};
        for(int i=0;i<gnames.length;i++){
            final Color gc=Theme.GAME_COLORS[i];
            JPanel gb=new JPanel(new BorderLayout()){
                protected void paintComponent(Graphics g){
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(gc.getRed(),gc.getGreen(),gc.getBlue(),60));
                    g2.fillRoundRect(0,0,getWidth()-1,getHeight()-1,8,8);
                    g2.setColor(gc); g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,8,8); g2.dispose();
                }
            };
            gb.setOpaque(false); gb.setPreferredSize(new Dimension(54,26));
            JLabel gl=UI.label(gnames[i],Theme.F_SMBD,Color.WHITE); gl.setHorizontalAlignment(SwingConstants.CENTER);
            gb.add(gl); gameRow.add(gb);
        }

        // Features
        JPanel feats=new JPanel(); feats.setOpaque(false); feats.setLayout(new BoxLayout(feats,BoxLayout.Y_AXIS)); feats.setAlignmentX(Component.LEFT_ALIGNMENT);
        String[] fl={"Proses cepat & aman 24 jam","Harga terbaik & transparan","Voucher & promo spesial setiap hari"};
        for(String f:fl){
            JPanel fr=new JPanel(new FlowLayout(FlowLayout.LEFT,6,3)); fr.setOpaque(false);
            JPanel dot=new JPanel(){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(Theme.GREEN_LT);g2.fillOval(3,5,9,9);g2.dispose();}};
            dot.setOpaque(false); dot.setPreferredSize(new Dimension(16,20));
            fr.add(dot); fr.add(UI.label(f,Theme.F_BODY,new Color(200,210,235)));
            feats.add(fr);
        }

        brand.add(circle); brand.add(Box.createVerticalStrut(18));
        brand.add(lName);  brand.add(Box.createVerticalStrut(8));
        brand.add(lTag);   brand.add(Box.createVerticalStrut(22));
        brand.add(gameRow);brand.add(Box.createVerticalStrut(26));
        brand.add(feats);
        left.add(brand);

        // ── KANAN form ────────────────────────────────────────
        JPanel right=new JPanel(new GridBagLayout()){
            protected void paintComponent(Graphics g){g.setColor(Theme.BG);g.fillRect(0,0,getWidth(),getHeight());}
        };
        right.setBorder(new EmptyBorder(40,52,40,52));
        JPanel form=new JPanel(); form.setOpaque(false);
        form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));
        form.setPreferredSize(new Dimension(330,420));

        JLabel lW=UI.label("Selamat Datang",new Font("Segoe UI",Font.BOLD,26),Theme.TEXT); lW.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lS=UI.label("Masuk ke akun ZenithStore Anda",Theme.F_BODY,Theme.SUBTEXT); lS.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtEmail=UI.textField(); txtPass=UI.passwordField();
        lblMsg=new JLabel(" "); lblMsg.setFont(Theme.F_SMALL); lblMsg.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnLogin=UI.primaryBtn("Masuk",Theme.ACCENT,Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI",Font.BOLD,14));
        btnLogin.addActionListener(e->doLogin());

        JPanel regRow=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)); regRow.setOpaque(false); regRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        regRow.add(UI.label("Belum punya akun? ",Theme.F_SMALL,Theme.SUBTEXT));
        JButton btnReg=UI.linkBtn("Daftar di sini",Theme.ACCENT2); btnReg.setFont(Theme.F_SMBD);
        btnReg.addActionListener(e->{ new RegisterForm().setVisible(true); dispose(); });
        regRow.add(btnReg);

        form.add(lW); form.add(Box.createVerticalStrut(6));
        form.add(lS); form.add(Box.createVerticalStrut(32));
        UI.addField(form,"Alamat Email",txtEmail);
        UI.addField(form,"Password",txtPass);
        form.add(lblMsg); form.add(Box.createVerticalStrut(10));
        form.add(btnLogin); form.add(Box.createVerticalStrut(20));
        form.add(regRow);
        right.add(form);

        add(left); add(right);
        getRootPane().setDefaultButton(btnLogin);
    }

    private void doLogin(){
        String email=txtEmail.getText().trim(), pass=new String(txtPass.getPassword());
        if(email.isEmpty()||pass.isEmpty()){ setMsg("Email dan password tidak boleh kosong!",Theme.RED); return; }
        User user=new UserDAO().login(email,pass);
        if(user!=null){
            setMsg("Login berhasil!",Theme.GREEN_LT);
            Timer t=new Timer(500,e->{
                if(user.getRole().equals("admin")) new AdminDashboard(user).setVisible(true);
                else new CustomerDashboard(user).setVisible(true);
                dispose();
            }); t.setRepeats(false); t.start();
        } else { setMsg("Email atau password salah!",Theme.RED); txtPass.setText(""); }
    }
    private void setMsg(String m,Color c){ lblMsg.setText(m); lblMsg.setForeground(c); }

    public static void main(String[] args){
        try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception e){}
        SwingUtilities.invokeLater(()->new LoginForm().setVisible(true));
    }
}
