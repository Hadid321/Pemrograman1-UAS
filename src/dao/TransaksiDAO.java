package dao;
import config.Database;
import java.sql.*;
import java.util.ArrayList;

public class TransaksiDAO {
    public double getTotalPendapatan(){return getDouble("SELECT COALESCE(SUM(total_harga),0) FROM transaksi WHERE status_pembayaran='success'");}
    public int getTrxHariIni(){return getInt("SELECT COUNT(*) FROM transaksi WHERE DATE(tanggal_transaksi)=CURDATE()");}
    public int getTotalUser(){return getInt("SELECT COUNT(*) FROM user WHERE role='customer'");}
    public int getTrxPending(){return getInt("SELECT COUNT(*) FROM transaksi WHERE status_pembayaran='pending'");}
    public int getTotalTrx(){return getInt("SELECT COUNT(*) FROM transaksi");}
    public double getPendapatanBulanIni(){return getDouble("SELECT COALESCE(SUM(total_harga),0) FROM transaksi WHERE status_pembayaran='success' AND MONTH(tanggal_transaksi)=MONTH(NOW()) AND YEAR(tanggal_transaksi)=YEAR(NOW())");}

    public ArrayList<String[]> getTransaksiTerbaru(int limit){
        ArrayList<String[]> list=new ArrayList<>();
        String sql="SELECT t.nomor_invoice,u.nama_user,g.nama_game,p.nama_paket,t.total_harga,t.metode_pembayaran,t.status_pembayaran,t.tanggal_transaksi FROM transaksi t JOIN user u ON t.id_user=u.id_user JOIN paket_item p ON t.id_paket=p.id_paket JOIN game g ON p.id_game=g.id_game ORDER BY t.tanggal_transaksi DESC LIMIT "+limit;
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){
            while(rs.next())list.add(new String[]{rs.getString("nomor_invoice"),rs.getString("nama_user"),rs.getString("nama_game"),rs.getString("nama_paket"),String.format("Rp %,.0f",rs.getDouble("total_harga")),rs.getString("metode_pembayaran"),rs.getString("status_pembayaran"),rs.getString("tanggal_transaksi")});
        }catch(SQLException e){e.printStackTrace();}return list;
    }

    public ArrayList<String[]> getAllTransaksi(String filterStatus,String filterGame){
        ArrayList<String[]> list=new ArrayList<>();
        String sql="SELECT t.id_transaksi,t.nomor_invoice,u.nama_user,g.nama_game,p.nama_paket,t.uid_game,t.total_harga,t.metode_pembayaran,t.status_pembayaran,t.tanggal_transaksi FROM transaksi t JOIN user u ON t.id_user=u.id_user JOIN paket_item p ON t.id_paket=p.id_paket JOIN game g ON p.id_game=g.id_game WHERE 1=1";
        if(filterStatus!=null&&!filterStatus.equals("Semua"))sql+=" AND t.status_pembayaran='"+filterStatus.toLowerCase()+"'";
        if(filterGame!=null&&!filterGame.equals("Semua"))sql+=" AND g.nama_game='"+filterGame+"'";
        sql+=" ORDER BY t.tanggal_transaksi DESC";
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){
            while(rs.next())list.add(new String[]{rs.getString("id_transaksi"),rs.getString("nomor_invoice"),rs.getString("nama_user"),rs.getString("nama_game"),rs.getString("nama_paket"),rs.getString("uid_game"),String.format("Rp %,.0f",rs.getDouble("total_harga")),rs.getString("metode_pembayaran"),rs.getString("status_pembayaran"),rs.getString("tanggal_transaksi")});
        }catch(SQLException e){e.printStackTrace();}return list;
    }

    public ArrayList<String[]> getTransaksiByUser(int idUser){
        ArrayList<String[]> list=new ArrayList<>();
        String sql="SELECT t.nomor_invoice,g.nama_game,p.nama_paket,t.uid_game,t.total_harga,t.metode_pembayaran,t.status_pembayaran,t.tanggal_transaksi FROM transaksi t JOIN paket_item p ON t.id_paket=p.id_paket JOIN game g ON p.id_game=g.id_game WHERE t.id_user=? ORDER BY t.tanggal_transaksi DESC";
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setInt(1,idUser);ResultSet rs=ps.executeQuery();
            while(rs.next())list.add(new String[]{rs.getString("nomor_invoice"),rs.getString("nama_game"),rs.getString("nama_paket"),rs.getString("uid_game"),String.format("Rp %,.0f",rs.getDouble("total_harga")),rs.getString("metode_pembayaran"),rs.getString("status_pembayaran"),rs.getString("tanggal_transaksi")});
        }catch(SQLException e){e.printStackTrace();}return list;
    }

    public boolean updateStatus(int idTransaksi,String newStatus){
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE transaksi SET status_pembayaran=? WHERE id_transaksi=?")){
            ps.setString(1,newStatus);ps.setInt(2,idTransaksi);return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace();return false;}
    }

    public boolean insertTransaksi(int idUser,int idPaket,String uid,String server,String metode,double harga){
        String invoice="INV-"+new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date())+"-"+String.format("%04d",(int)(Math.random()*9999));
        String sql="INSERT INTO transaksi(nomor_invoice,id_user,id_paket,uid_game,server_game,jumlah_beli,harga_satuan,diskon,total_harga,metode_pembayaran,status_pembayaran,tanggal_transaksi)VALUES(?,?,?,?,?,1,?,0,?,?,'pending',NOW())";
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement(sql)){
            ps.setString(1,invoice);ps.setInt(2,idUser);ps.setInt(3,idPaket);
            ps.setString(4,uid);ps.setString(5,server==null?"":server);
            ps.setDouble(6,harga);ps.setDouble(7,harga);ps.setString(8,metode);
            return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace();return false;}
    }

    public ArrayList<String[]> getStatPerGame(){
        ArrayList<String[]> list=new ArrayList<>();
        String sql="SELECT g.nama_game,COUNT(*) as total,COALESCE(SUM(t.total_harga),0) as pendapatan FROM transaksi t JOIN paket_item p ON t.id_paket=p.id_paket JOIN game g ON p.id_game=g.id_game WHERE t.status_pembayaran='success' GROUP BY g.id_game,g.nama_game ORDER BY pendapatan DESC";
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){
            while(rs.next())list.add(new String[]{rs.getString("nama_game"),String.valueOf(rs.getInt("total")),String.format("Rp %,.0f",rs.getDouble("pendapatan"))});
        }catch(SQLException e){e.printStackTrace();}return list;
    }

    private double getDouble(String sql){try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){if(rs.next())return rs.getDouble(1);}catch(SQLException e){e.printStackTrace();}return 0;}
    private int getInt(String sql){try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){if(rs.next())return rs.getInt(1);}catch(SQLException e){e.printStackTrace();}return 0;}
}
