# ZenithStore - Aplikasi Top-Up Game

Aplikasi desktop top-up game multi-platform berbasis Java Swing dengan database MySQL.
Mendukung 6 game populer: Honkai: Star Rail, Genshin Impact, Roblox, Mobile Legends, Free Fire, dan PUBG Mobile.

---

## Anggota Kelompok

| Nama | NIM |
|------|-----|
| Aditya Darmawan (Ketua) | 241011400824 |
| Ahmad Faiz Arkhan | 241011400813 |
| Hadid Riansyah | 241011400908 |
| Imron Hafidz | 241011400814 |

**Kelas:** 04TPLE011 | **Mata Kuliah:** Pemrograman 1 | **Tahun Akademik:** 2025/2026

---

## Fitur Aplikasi

### Customer
- Login & Register akun
- Beranda dengan tampilan game cards
- Top-Up game dengan form dinamis per game
- Pilih nominal paket & metode pembayaran (8 pilihan)
- Riwayat pesanan sendiri
- Edit profil & ganti password

### Admin
- Dashboard statistik (pendapatan, transaksi, user)
- Kelola transaksi (ubah status: success/pending/failed)
- Kelola user customer
- Lihat daftar voucher & promo

---

## Teknologi

- **Bahasa:** Java (JDK 25)
- **GUI:** Java Swing
- **Database:** MySQL
- **Connector:** MySQL Connector/J 9.x
- **IDE:** Apache NetBeans 29

---

## Struktur Project

```
src/
├── config/
│   ├── Database.java       # Koneksi MySQL
│   ├── Theme.java          # Konstanta warna & font
│   └── UI.java             # Komponen UI reusable
├── model/
│   └── User.java           # Model data user
├── dao/
│   ├── UserDAO.java        # Query tabel user
│   ├── TransaksiDAO.java   # Query tabel transaksi
│   ├── GameDAO.java        # Query tabel game & paket
│   └── VoucherDAO.java     # Query tabel voucher
└── view/
    ├── LoginForm.java      # Halaman login
    ├── RegisterForm.java   # Halaman register
    ├── admin/
    │   └── AdminDashboard.java    # Dashboard admin
    └── customer/
        └── CustomerDashboard.java # Dashboard customer
```

---

## Cara Setup & Menjalankan

### 1. Persiapan Database
1. Buka **XAMPP** → Start **Apache** dan **MySQL**
2. Buka **phpMyAdmin** (`http://localhost/phpmyadmin`)
3. Import file SQL:
   - Import `db_zenithstore.sql` terlebih dahulu (struktur database)
   - Import `data_test.sql` (data dummy untuk testing)

### 2. Setup di NetBeans
1. Buka **NetBeans** → File → New Project → Java Application
2. Nama project: `zenithstore`
3. Klik kanan **Libraries** → Add JAR/Folder
4. Pilih file `mysql-connector-j-9.x.x.jar`
   - Download di: https://dev.mysql.com/downloads/connector/j/
   - Pilih **Platform Independent** → ZIP

### 3. Import Source Code
Buat package berikut dan copy file `.java` ke masing-masing package:
- `config` → Database.java, Theme.java, UI.java
- `model` → User.java
- `dao` → UserDAO.java, TransaksiDAO.java, GameDAO.java, VoucherDAO.java
- `view` → LoginForm.java, RegisterForm.java
- `view.admin` → AdminDashboard.java
- `view.customer` → CustomerDashboard.java

### 4. Sesuaikan Koneksi Database
Buka `config/Database.java`, sesuaikan jika perlu:
```java
private static final String USER = "root";   // username MySQL
private static final String PASS = "";        // password MySQL (kosong jika tidak ada)
```

### 5. Jalankan Aplikasi
Klik kanan `view/LoginForm.java` → **Run File**

---

## Akun Login (Data Test)

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@zenithstore.com | admin123 |
| Customer | aditya@gmail.com | aditya123 |
| Customer | faiz@gmail.com | faiz123 |
| Customer | hadid@gmail.com | hadid123 |
| Customer | imron@gmail.com | imron123 |

---

## Screenshot

> <img width="1135" height="713" alt="image" src="https://github.com/user-attachments/assets/e775453e-8e82-4cbc-bea6-dcdccb182056" />
> <img width="1918" height="1013" alt="Screenshot 2026-06-25 125512" src="https://github.com/user-attachments/assets/219bd485-7bcd-4a62-a24e-7e2a1e5f74bd" />

---

## Lisensi

Proyek ini dibuat untuk memenuhi tugas mata kuliah Pemrograman 1, Universitas Pamulang.
