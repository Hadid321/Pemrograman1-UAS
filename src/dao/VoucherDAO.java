package dao;
import config.Database;
import java.sql.*;
import java.util.ArrayList;

public class VoucherDAO {
    public ArrayList<String[]> getAllVoucher(){
        ArrayList<String[]> list=new ArrayList<>();
        try(Connection c=Database.getConnection();Statement st=c.createStatement();ResultSet rs=st.executeQuery("SELECT *,(kuota-terpakai) as sisa FROM voucher ORDER BY id_voucher")){
            while(rs.next()){
                String diskon=rs.getString("tipe_diskon").equals("persen")?rs.getInt("nilai_diskon")+"%":String.format("Rp %,.0f",rs.getDouble("nilai_diskon"));
                list.add(new String[]{String.valueOf(rs.getInt("id_voucher")),rs.getString("kode_voucher"),rs.getString("deskripsi"),rs.getString("tipe_diskon"),diskon,String.format("Rp %,.0f",rs.getDouble("min_transaksi")),String.valueOf(rs.getInt("kuota")),String.valueOf(rs.getInt("terpakai")),String.valueOf(rs.getInt("sisa")),rs.getString("berlaku_dari"),rs.getString("berlaku_sampai"),rs.getInt("is_active")==1?"Aktif":"Nonaktif"});
            }
        }catch(SQLException e){e.printStackTrace();}return list;
    }
    public String[] validateVoucher(String kode,double totalHarga){
        try(Connection c=Database.getConnection();PreparedStatement ps=c.prepareStatement("SELECT * FROM voucher WHERE kode_voucher=? AND is_active=1 AND berlaku_dari<=CURDATE() AND berlaku_sampai>=CURDATE() AND (kuota-terpakai)>0 AND min_transaksi<=?")){
            ps.setString(1,kode);ps.setDouble(2,totalHarga);ResultSet rs=ps.executeQuery();
            if(rs.next())return new String[]{String.valueOf(rs.getInt("id_voucher")),rs.getString("tipe_diskon"),String.valueOf(rs.getDouble("nilai_diskon")),rs.getString("maks_diskon")!=null?String.valueOf(rs.getDouble("maks_diskon")):"0"};
        }catch(SQLException e){e.printStackTrace();}return null;
    }
}
