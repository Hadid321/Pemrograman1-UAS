package config;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class UI {

    public static class RoundPanel extends JPanel {
        private int r; private Color bg, bd;
        public RoundPanel(int r,Color bg,Color bd){this.r=r;this.bg=bg;this.bd=bd;setOpaque(false);}
        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg); g2.fill(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,r,r));
            if(bd!=null){g2.setColor(bd);g2.draw(new RoundRectangle2D.Double(0,0,getWidth()-1,getHeight()-1,r,r));}
            g2.dispose(); super.paintComponent(g);
        }
    }

    public static class RoundedBorder extends AbstractBorder {
        private int r; private Color c;
        public RoundedBorder(int r,Color c){this.r=r;this.c=c;}
        public void paintBorder(Component comp,Graphics g,int x,int y,int w,int h){
            Graphics2D g2=(Graphics2D)g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c); g2.drawRoundRect(x,y,w-1,h-1,r,r);
        }
    }

    public static JLabel label(String t,Font f,Color c){JLabel l=new JLabel(t);l.setFont(f);l.setForeground(c);return l;}

    public static JTextField textField(){JTextField tf=new JTextField();styleInput(tf);return tf;}
    public static JPasswordField passwordField(){JPasswordField pf=new JPasswordField();styleInput(pf);return pf;}
    public static JComboBox<String> comboBox(String[] items){
        JComboBox<String> cb=new JComboBox<>(items);
        cb.setBackground(Theme.INPUT_BG);cb.setForeground(Theme.TEXT);cb.setFont(Theme.F_BODY);
        cb.setBorder(new CompoundBorder(new RoundedBorder(8,Theme.BORDER),new EmptyBorder(4,10,4,10)));
        cb.setPreferredSize(new Dimension(0,40));cb.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));return cb;
    }
    private static void styleInput(JComponent c){
        c.setBackground(Theme.INPUT_BG);c.setForeground(Theme.TEXT);c.setFont(Theme.F_BODY);
        c.setBorder(new CompoundBorder(new RoundedBorder(8,Theme.BORDER),new EmptyBorder(8,14,8,14)));
        c.setPreferredSize(new Dimension(0,42));c.setMaximumSize(new Dimension(Integer.MAX_VALUE,42));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    public static JButton primaryBtn(String text,Color bg,Color fg){
        JButton btn=new JButton(text){
            protected void paintComponent(Graphics g){
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());g2.fill(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),10,10));
                g2.dispose();super.paintComponent(g);
            }
        };
        btn.setFont(Theme.F_BOLD);btn.setForeground(fg);btn.setBackground(bg);
        btn.setContentAreaFilled(false);btn.setBorderPainted(false);btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0,44));btn.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){btn.setBackground(bg.darker());}
            public void mouseExited(MouseEvent e){btn.setBackground(bg);}
        });
        return btn;
    }

    public static JButton smallBtn(String text,Color bg,Color fg){
        JButton btn=new JButton(text);btn.setFont(Theme.F_SMBD);btn.setForeground(fg);btn.setBackground(bg);
        btn.setFocusPainted(false);btn.setBorder(new CompoundBorder(new RoundedBorder(6,bg.darker()),new EmptyBorder(5,14,5,14)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent e){btn.setBackground(bg.darker());}
            public void mouseExited(MouseEvent e){btn.setBackground(bg);}
        });
        return btn;
    }

    public static JButton linkBtn(String text,Color fg){
        JButton btn=new JButton(text);btn.setFont(Theme.F_BOLD);btn.setForeground(fg);
        btn.setBackground(new Color(0,0,0,0));btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return btn;
    }

    public static RoundPanel statCard(String title,String value,String sub,Color accent){
        RoundPanel p=new RoundPanel(14,Theme.CARD,Theme.BORDER);
        p.setLayout(new BoxLayout(p,BoxLayout.Y_AXIS));p.setBorder(new EmptyBorder(18,20,18,20));
        JPanel topRow=new JPanel(new FlowLayout(FlowLayout.LEFT,6,0));topRow.setOpaque(false);topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel dot=new JPanel(){protected void paintComponent(Graphics g){Graphics2D g2=(Graphics2D)g.create();g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g2.setColor(accent);g2.fillOval(0,3,10,10);g2.dispose();}};
        dot.setOpaque(false);dot.setPreferredSize(new Dimension(14,16));
        topRow.add(dot);topRow.add(label(title,Theme.F_SMALL,Theme.SUBTEXT));
        JLabel lVal=label(value,Theme.F_NUM,accent);lVal.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lSub=label(sub,Theme.F_SMALL,Theme.MUTED);lSub.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(topRow);p.add(Box.createVerticalStrut(8));p.add(lVal);p.add(Box.createVerticalStrut(3));p.add(lSub);
        return p;
    }

    public static JTable makeTable(String[] cols){
        DefaultTableModel m=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}};
        JTable t=new JTable(m){
            public Component prepareRenderer(TableCellRenderer r,int row,int col){
                Component c=super.prepareRenderer(r,row,col);
                if(!isRowSelected(row))c.setBackground(row%2==0?Theme.ROW_ODD:Theme.ROW_EVEN);return c;
            }
        };
        t.setBackground(Theme.ROW_ODD);t.setForeground(Theme.TEXT);t.setFont(Theme.F_BODY);t.setRowHeight(40);
        t.setShowGrid(false);t.setIntercellSpacing(new Dimension(0,0));
        t.setSelectionBackground(new Color(99,102,241,60));t.setSelectionForeground(Theme.TEXT);
        t.setFillsViewportHeight(true);t.getTableHeader().setReorderingAllowed(false);
        JTableHeader hdr=t.getTableHeader();hdr.setBackground(Theme.TBL_HDR);hdr.setForeground(Theme.SUBTEXT);
        hdr.setFont(Theme.F_SMBD);hdr.setPreferredSize(new Dimension(0,42));hdr.setBorder(new MatteBorder(0,0,1,0,Theme.BORDER));
        DefaultTableCellRenderer cell=new DefaultTableCellRenderer();
        cell.setBorder(new EmptyBorder(0,14,0,14));cell.setBackground(Theme.ROW_ODD);cell.setForeground(Theme.TEXT);
        for(int i=0;i<cols.length;i++)t.getColumnModel().getColumn(i).setCellRenderer(cell);
        return t;
    }

    public static JScrollPane tableScroll(JTable t){
        JScrollPane sp=new JScrollPane(t);sp.setBorder(new RoundedBorder(10,Theme.BORDER));
        sp.getViewport().setBackground(Theme.ROW_ODD);sp.setBackground(Theme.BG);return sp;
    }

    public static TableCellRenderer statusRenderer(){
        return new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object val,boolean sel,boolean foc,int row,int col){
                JLabel l=(JLabel)super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                l.setHorizontalAlignment(CENTER);l.setFont(Theme.F_SMBD);l.setOpaque(true);
                if(!sel){
                    String v=val==null?"":val.toString().toLowerCase();
                    switch(v){
                        case "success":l.setForeground(Theme.GREEN_LT);l.setBackground(new Color(16,185,129,25));break;
                        case "pending":l.setForeground(Theme.YELLOW);l.setBackground(new Color(245,158,11,25));break;
                        case "failed": l.setForeground(Theme.RED);l.setBackground(new Color(239,68,68,25));break;
                        case "aktif":  l.setForeground(Theme.GREEN_LT);l.setBackground(new Color(16,185,129,25));break;
                        case "nonaktif":l.setForeground(Theme.RED);l.setBackground(new Color(239,68,68,25));break;
                        default:l.setForeground(Theme.SUBTEXT);l.setBackground(row%2==0?Theme.ROW_ODD:Theme.ROW_EVEN);
                    }
                }
                return l;
            }
        };
    }

    public static JSeparator sep(){JSeparator s=new JSeparator();s.setForeground(Theme.BORDER);s.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));return s;}
    public static JPanel sectionHeader(String t,JComponent right){
        JPanel p=new JPanel(new BorderLayout());p.setOpaque(false);p.setBorder(new EmptyBorder(0,0,12,0));
        p.add(label(t,Theme.F_HEAD,Theme.TEXT),BorderLayout.WEST);if(right!=null)p.add(right,BorderLayout.EAST);return p;
    }
    public static void addField(JPanel p,String lbl,JComponent field){
        JLabel l=label(lbl,Theme.F_BOLD,Theme.SUBTEXT);l.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);p.add(Box.createVerticalStrut(6));p.add(field);p.add(Box.createVerticalStrut(14));
    }
    public static JScrollPane wrapScroll(JPanel p){
        JScrollPane sp=new JScrollPane(p);sp.setBorder(null);sp.getViewport().setBackground(Theme.BG);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);sp.getVerticalScrollBar().setUnitIncrement(16);return sp;
    }
    public static JPanel[] space(int n){JPanel[] a=new JPanel[n];for(int i=0;i<n;i++){a[i]=new JPanel();a[i].setOpaque(false);a[i].setPreferredSize(new Dimension(0,12));}return a;}
}
