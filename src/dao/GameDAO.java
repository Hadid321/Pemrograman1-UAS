package dao;
import config.Database;
import java.sql.*;
import java.util.ArrayList;

public class GameDAO {
    public ArrayList<String[]> getAllGame(){
        ArrayList<String[]> list=new ArrayList<>();
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery("SELECT * FROM game WHERE is_active=1 ORDER BY id_game")){
            while(rs.next())list.add(new String[]{String.valueOf(rs.getInt("id_game")),rs.getString("nama_game"),rs.getString("mata_uang"),rs.getString("deskripsi")!=null?rs.getString("deskripsi"):""});
        }catch(SQLException e){e.printStackTrace();}return list;
    }
    public ArrayList<String[]> getPaketByGame(int idGame){
        ArrayList<String[]> list=new ArrayList<>();
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("SELECT * FROM paket_item WHERE id_game=? AND is_active=1 ORDER BY harga")){
            ps.setInt(1,idGame);ResultSet rs=ps.executeQuery();
            while(rs.next())list.add(new String[]{String.valueOf(rs.getInt("id_paket")),rs.getString("nama_paket"),String.valueOf(rs.getInt("jumlah")),String.valueOf(rs.getInt("bonus")),String.format("Rp %,.0f",rs.getDouble("harga")),String.valueOf(rs.getDouble("harga"))});
        }catch(SQLException e){e.printStackTrace();}return list;
    }
    public String[] getGameNames(){
        ArrayList<String> names=new ArrayList<>();names.add("Semua");
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery("SELECT nama_game FROM game WHERE is_active=1 ORDER BY id_game")){
            while(rs.next())names.add(rs.getString("nama_game"));
        }catch(SQLException e){e.printStackTrace();}
        return names.toArray(new String[0]);
    }
}
