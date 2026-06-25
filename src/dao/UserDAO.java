package dao;
import config.Database;
import model.User;
import java.sql.*;
import java.util.ArrayList;

public class UserDAO {
    public User login(String email,String password){
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("SELECT * FROM user WHERE email=? AND password=?")){
            ps.setString(1,email);ps.setString(2,password);ResultSet rs=ps.executeQuery();
            if(rs.next())return new User(rs.getInt("id_user"),rs.getString("nama_user"),rs.getString("email"),rs.getString("nomor_hp"),rs.getString("password"),rs.getString("role"));
        }catch(SQLException e){e.printStackTrace();}return null;
    }
    public boolean register(String nama,String email,String hp,String pass){
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("INSERT INTO user(nama_user,email,nomor_hp,password,role)VALUES(?,?,?,?,'customer')")){
            ps.setString(1,nama);ps.setString(2,email);ps.setString(3,hp);ps.setString(4,pass);return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace();return false;}
    }
    public boolean emailExists(String email){
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("SELECT id_user FROM user WHERE email=?")){
            ps.setString(1,email);return ps.executeQuery().next();
        }catch(SQLException e){e.printStackTrace();return false;}
    }
    public ArrayList<String[]> getAllCustomer(){
        ArrayList<String[]> list=new ArrayList<>();
        String sql="SELECT u.*,(SELECT COUNT(*) FROM transaksi t WHERE t.id_user=u.id_user) as total_trx,(SELECT COALESCE(SUM(total_harga),0) FROM transaksi t WHERE t.id_user=u.id_user AND status_pembayaran='success') as spend FROM user u WHERE role='customer' ORDER BY u.id_user";
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery(sql)){
            while(rs.next())list.add(new String[]{String.valueOf(rs.getInt("id_user")),rs.getString("nama_user"),rs.getString("email"),rs.getString("nomor_hp"),String.valueOf(rs.getInt("total_trx")),String.format("Rp %,.0f",rs.getDouble("spend"))});
        }catch(SQLException e){e.printStackTrace();}return list;
    }
    public boolean updateUser(int id,String nama,String email,String hp){
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE user SET nama_user=?,email=?,nomor_hp=? WHERE id_user=?")){
            ps.setString(1,nama);ps.setString(2,email);ps.setString(3,hp);ps.setInt(4,id);return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace();return false;}
    }
    public boolean updatePassword(int id,String pass){
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("UPDATE user SET password=? WHERE id_user=?")){
            ps.setString(1,pass);ps.setInt(2,id);return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace();return false;}
    }
    public boolean deleteUser(int id){
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("DELETE FROM user WHERE id_user=?")){
            ps.setInt(1,id);return ps.executeUpdate()>0;
        }catch(SQLException e){e.printStackTrace();return false;}
    }
}
