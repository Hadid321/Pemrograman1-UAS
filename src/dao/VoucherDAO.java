package dao;

import config.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class VoucherDAO {

    // ── Ambil semua voucher untuk admin ──
    public ArrayList<String[]> getAllVoucher() {
        ArrayList<String[]> list = new ArrayList<>();
        String sql = "SELECT *, (kuota - terpakai) AS sisa FROM voucher ORDER BY id_voucher";
        try (Connection c = Database.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String diskon = rs.getString("tipe_diskon").equals("persen") 
                    ? rs.getInt("nilai_diskon") + "%" 
                    : String.format("Rp %,.0f", rs.getDouble("nilai_diskon"));
                list.add(new String[]{
                    String.valueOf(rs.getInt("id_voucher")),
                    rs.getString("kode_voucher"),
                    rs.getString("deskripsi"),
                    rs.getString("tipe_diskon"),
                    diskon,
                    String.format("Rp %,.0f", rs.getDouble("min_transaksi")),
                    String.valueOf(rs.getInt("kuota")),
                    String.valueOf(rs.getInt("terpakai")),
                    String.valueOf(rs.getInt("sisa")),
                    rs.getString("berlaku_dari"),
                    rs.getString("berlaku_sampai"),
                    rs.getInt("is_active") == 1 ? "Aktif" : "Nonaktif"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ── Validasi voucher untuk customer (diperbaiki) ──
    public String[] validateVoucher(String kode, double totalHarga) {
        String sql = "SELECT * FROM voucher " +
                     "WHERE UPPER(kode_voucher) = UPPER(?) " +
                     "AND is_active = 1 " +
                     "AND DATE(berlaku_dari) <= CURDATE() " +
                     "AND DATE(berlaku_sampai) >= CURDATE() " +
                     "AND (kuota - terpakai) > 0 " +
                     "AND min_transaksi <= ?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, kode);
            ps.setDouble(2, totalHarga);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new String[]{
                    String.valueOf(rs.getInt("id_voucher")),
                    rs.getString("tipe_diskon"),
                    String.valueOf(rs.getDouble("nilai_diskon")),
                    rs.getString("maks_diskon") != null ? String.valueOf(rs.getDouble("maks_diskon")) : "0"
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ── Cek keberadaan kode voucher (case-insensitive) ──
    public boolean isVoucherExist(String kode) {
        String sql = "SELECT 1 FROM voucher WHERE UPPER(kode_voucher) = UPPER(?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ── Dapatkan alasan spesifik mengapa voucher tidak valid ──
    public String getValidationReason(String kode, double totalHarga) {
        if (!isVoucherExist(kode)) {
            return "Kode voucher tidak ditemukan.";
        }

        String sql = "SELECT is_active, DATE(berlaku_dari) AS mulai, DATE(berlaku_sampai) AS sampai, " +
                     "(kuota - terpakai) AS sisa, min_transaksi FROM voucher WHERE UPPER(kode_voucher) = UPPER(?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, kode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("is_active") != 1) {
                    return "Voucher sudah tidak aktif.";
                }
                Date mulai = rs.getDate("mulai");
                Date sampai = rs.getDate("sampai");
                Date now = new Date(System.currentTimeMillis());
                if (now.before(mulai) || now.after(sampai)) {
                    return "Voucher belum/tidak berlaku (tanggal).";
                }
                if (rs.getInt("sisa") <= 0) {
                    return "Kuota voucher sudah habis.";
                }
                if (rs.getDouble("min_transaksi") > totalHarga) {
                    return "Total belanja belum mencapai minimum transaksi (Rp " + 
                           String.format("%,.0f", rs.getDouble("min_transaksi")) + ").";
                }
                return "Voucher valid, namun tidak diketahui penyebab lainnya.";
            } else {
                return "Kode voucher tidak ditemukan.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error saat memeriksa voucher.";
        }
    }

    // ── Tambah pemakaian voucher (kurangi kuota) ──
    public boolean incrementTerpakai(int idVoucher) {
        String sql = "UPDATE voucher SET terpakai = terpakai + 1 WHERE id_voucher = ? AND (kuota - terpakai) > 0";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idVoucher);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
