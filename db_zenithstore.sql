-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 25 Jun 2026 pada 14.21
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_zenithstore`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `game`
--

CREATE TABLE `game` (
  `id_game` int(11) NOT NULL,
  `nama_game` varchar(100) NOT NULL,
  `mata_uang` varchar(50) NOT NULL,
  `gambar` varchar(255) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `game`
--

INSERT INTO `game` (`id_game`, `nama_game`, `mata_uang`, `gambar`, `deskripsi`, `is_active`, `created_at`) VALUES
(1, 'Honkai: Star Rail', 'Oneiric Shard', NULL, 'RPG turn-based dari HoYoverse', 1, '2026-05-30 14:37:16'),
(2, 'Genshin Impact', 'Genesis Crystal', NULL, 'Action RPG open-world dari HoYoverse', 1, '2026-05-30 14:37:16'),
(3, 'Roblox', 'Robux', NULL, 'Platform game online', 1, '2026-05-30 14:37:16'),
(4, 'Mobile Legends', 'Diamond', NULL, 'MOBA populer dari Moonton', 1, '2026-05-30 14:37:16'),
(5, 'Free Fire', 'Diamond', NULL, 'Battle royale dari Garena', 1, '2026-05-30 14:37:16'),
(6, 'PUBG Mobile', 'UC (Unknown Cash)', NULL, 'Battle royale dari Krafton', 1, '2026-05-30 14:37:16');

-- --------------------------------------------------------

--
-- Struktur dari tabel `paket_item`
--

CREATE TABLE `paket_item` (
  `id_paket` int(11) NOT NULL,
  `id_game` int(11) NOT NULL,
  `nama_paket` varchar(100) NOT NULL,
  `jumlah` int(11) NOT NULL,
  `bonus` int(11) NOT NULL DEFAULT 0,
  `harga` decimal(12,2) NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `paket_item`
--

INSERT INTO `paket_item` (`id_paket`, `id_game`, `nama_paket`, `jumlah`, `bonus`, `harga`, `is_active`, `created_at`) VALUES
(1, 1, '60 Oneiric Shard', 60, 0, 15000.00, 1, '2026-05-30 14:37:16'),
(2, 1, '300 Oneiric Shard', 300, 30, 75000.00, 1, '2026-05-30 14:37:16'),
(3, 1, '980 Oneiric Shard', 980, 110, 230000.00, 1, '2026-05-30 14:37:16'),
(4, 1, '1980 Oneiric Shard', 1980, 260, 450000.00, 1, '2026-05-30 14:37:16'),
(5, 1, '3280 Oneiric Shard', 3280, 600, 750000.00, 1, '2026-05-30 14:37:16'),
(6, 2, '60 Genesis Crystal', 60, 0, 15000.00, 1, '2026-05-30 14:37:16'),
(7, 2, '300 Genesis Crystal', 300, 30, 75000.00, 1, '2026-05-30 14:37:16'),
(8, 2, '980 Genesis Crystal', 980, 110, 230000.00, 1, '2026-05-30 14:37:16'),
(9, 2, '1980 Genesis Crystal', 1980, 260, 450000.00, 1, '2026-05-30 14:37:16'),
(10, 2, '3280 Genesis Crystal', 3280, 600, 750000.00, 1, '2026-05-30 14:37:16'),
(11, 2, '6480 Genesis Crystal', 6480, 1600, 1450000.00, 1, '2026-05-30 14:37:16'),
(12, 3, '80 Robux', 80, 0, 15000.00, 1, '2026-05-30 14:37:16'),
(13, 3, '400 Robux', 400, 0, 70000.00, 1, '2026-05-30 14:37:16'),
(14, 3, '800 Robux', 800, 0, 135000.00, 1, '2026-05-30 14:37:16'),
(15, 3, '1700 Robux', 1700, 0, 270000.00, 1, '2026-05-30 14:37:16'),
(16, 3, '4500 Robux', 4500, 0, 700000.00, 1, '2026-05-30 14:37:16'),
(17, 3, '10000 Robux', 10000, 0, 1500000.00, 1, '2026-05-30 14:37:16'),
(18, 4, '5 Diamond', 5, 0, 2000.00, 1, '2026-05-30 14:37:16'),
(19, 4, '11 Diamond', 11, 1, 4000.00, 1, '2026-05-30 14:37:16'),
(20, 4, '17 Diamond', 17, 2, 6000.00, 1, '2026-05-30 14:37:16'),
(21, 4, '44 Diamond', 44, 4, 15000.00, 1, '2026-05-30 14:37:16'),
(22, 4, '88 Diamond', 88, 9, 30000.00, 1, '2026-05-30 14:37:16'),
(23, 4, '172 Diamond', 172, 19, 60000.00, 1, '2026-05-30 14:37:16'),
(24, 4, '257 Diamond', 257, 28, 90000.00, 1, '2026-05-30 14:37:16'),
(25, 4, '344 Diamond', 344, 38, 120000.00, 1, '2026-05-30 14:37:16'),
(26, 4, '429 Diamond', 429, 47, 150000.00, 1, '2026-05-30 14:37:16'),
(27, 4, '514 Diamond', 514, 57, 180000.00, 1, '2026-05-30 14:37:16'),
(28, 4, '600 Diamond', 600, 66, 210000.00, 1, '2026-05-30 14:37:16'),
(29, 4, '706 Diamond', 706, 78, 240000.00, 1, '2026-05-30 14:37:16'),
(30, 5, '50 Diamond', 50, 0, 10000.00, 1, '2026-05-30 14:37:16'),
(31, 5, '100 Diamond', 100, 0, 20000.00, 1, '2026-05-30 14:37:16'),
(32, 5, '210 Diamond', 210, 0, 40000.00, 1, '2026-05-30 14:37:16'),
(33, 5, '310 Diamond', 310, 0, 60000.00, 1, '2026-05-30 14:37:16'),
(34, 5, '520 Diamond', 520, 0, 100000.00, 1, '2026-05-30 14:37:16'),
(35, 5, '1060 Diamond', 1060, 0, 200000.00, 1, '2026-05-30 14:37:16'),
(36, 5, '2180 Diamond', 2180, 0, 400000.00, 1, '2026-05-30 14:37:16'),
(37, 5, '5600 Diamond', 5600, 0, 1000000.00, 1, '2026-05-30 14:37:16'),
(38, 6, '60 UC', 60, 0, 15000.00, 1, '2026-05-30 14:37:16'),
(39, 6, '325 UC', 325, 0, 75000.00, 1, '2026-05-30 14:37:16'),
(40, 6, '660 UC', 660, 0, 150000.00, 1, '2026-05-30 14:37:16'),
(41, 6, '1800 UC', 1800, 0, 400000.00, 1, '2026-05-30 14:37:16'),
(42, 6, '3850 UC', 3850, 0, 800000.00, 1, '2026-05-30 14:37:16'),
(43, 6, '8100 UC', 8100, 0, 1600000.00, 1, '2026-05-30 14:37:16');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi`
--

CREATE TABLE `transaksi` (
  `id_transaksi` int(11) NOT NULL,
  `nomor_invoice` varchar(50) NOT NULL,
  `id_user` int(11) NOT NULL,
  `id_paket` int(11) NOT NULL,
  `id_voucher` int(11) DEFAULT NULL,
  `uid_game` varchar(100) NOT NULL,
  `server_game` varchar(50) DEFAULT NULL,
  `nickname_game` varchar(100) DEFAULT NULL,
  `jumlah_beli` int(11) NOT NULL DEFAULT 1,
  `harga_satuan` decimal(12,2) NOT NULL,
  `diskon` decimal(12,2) NOT NULL DEFAULT 0.00,
  `total_harga` decimal(12,2) NOT NULL,
  `metode_pembayaran` varchar(50) NOT NULL,
  `status_pembayaran` enum('pending','success','failed','expired','refunded') NOT NULL DEFAULT 'pending',
  `catatan` text DEFAULT NULL,
  `tanggal_transaksi` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transaksi`
--

INSERT INTO `transaksi` (`id_transaksi`, `nomor_invoice`, `id_user`, `id_paket`, `id_voucher`, `uid_game`, `server_game`, `nickname_game`, `jumlah_beli`, `harga_satuan`, `diskon`, `total_harga`, `metode_pembayaran`, `status_pembayaran`, `catatan`, `tanggal_transaksi`, `updated_at`) VALUES
(1, 'INV-20250530-0001', 2, 1, NULL, '123456789', NULL, 'BudiHSR', 1, 15000.00, 0.00, 15000.00, 'DANA', 'success', NULL, '2026-05-30 14:37:16', '2026-05-30 14:37:16'),
(2, 'INV-20250530-0002', 2, 20, 1, '987654321', '5', 'BudiML', 1, 30000.00, 3000.00, 27000.00, 'GoPay', 'success', NULL, '2026-05-30 14:37:16', '2026-05-30 14:37:16'),
(3, 'INV-20250530-0003', 3, 8, 2, '111222333', NULL, 'SitiGenshin', 1, 230000.00, 15000.00, 215000.00, 'Transfer', 'pending', NULL, '2026-05-30 14:37:16', '2026-05-30 14:37:16');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `id_user` int(11) NOT NULL,
  `nama_user` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `nomor_hp` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','customer') NOT NULL DEFAULT 'customer',
  `created_at` datetime NOT NULL DEFAULT current_timestamp(),
  `updated_at` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`id_user`, `nama_user`, `email`, `nomor_hp`, `password`, `role`, `created_at`, `updated_at`) VALUES
(2, 'Budi Santoso', 'budi@email.com', '081234567890', '$2y$10$examplehashbudi', 'customer', '2026-05-30 14:37:16', '2026-05-30 14:37:16'),
(3, 'Siti Rahayu', 'siti@email.com', '082345678901', '$2y$10$examplehashsiti', 'customer', '2026-05-30 14:37:16', '2026-05-30 14:37:16'),
(5, 'Admin', 'admin@zenithstore.com', '081200000000', 'admin123', 'admin', '2026-05-30 14:41:39', '2026-05-30 14:41:39'),
(6, 'aditsukagensin', 'aditya123@gmail.com', '082263719372', 'adit123', 'customer', '2026-05-31 14:27:53', '2026-05-31 14:27:53'),
(7, 'aditya darwin', 'adityagamingff@mlbb.pubg', '08123456789', 'Aditmlbb', 'customer', '2026-06-25 12:54:44', '2026-06-25 12:54:44');

-- --------------------------------------------------------

--
-- Struktur dari tabel `voucher`
--

CREATE TABLE `voucher` (
  `id_voucher` int(11) NOT NULL,
  `kode_voucher` varchar(50) NOT NULL,
  `deskripsi` varchar(255) DEFAULT NULL,
  `tipe_diskon` enum('persen','nominal') NOT NULL,
  `nilai_diskon` decimal(12,2) NOT NULL,
  `min_transaksi` decimal(12,2) NOT NULL DEFAULT 0.00,
  `maks_diskon` decimal(12,2) DEFAULT NULL,
  `kuota` int(11) NOT NULL DEFAULT 1,
  `terpakai` int(11) NOT NULL DEFAULT 0,
  `berlaku_dari` date NOT NULL,
  `berlaku_sampai` date NOT NULL,
  `is_active` tinyint(1) NOT NULL DEFAULT 1,
  `created_at` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `voucher`
--

INSERT INTO `voucher` (`id_voucher`, `kode_voucher`, `deskripsi`, `tipe_diskon`, `nilai_diskon`, `min_transaksi`, `maks_diskon`, `kuota`, `terpakai`, `berlaku_dari`, `berlaku_sampai`, `is_active`, `created_at`) VALUES
(1, 'NEWUSER10', 'Diskon 10% untuk pengguna baru', 'persen', 10.00, 0.00, 20000.00, 100, 0, '2025-01-01', '2025-12-31', 1, '2026-05-30 14:37:16'),
(2, 'HEMAT15K', 'Potongan Rp15.000 min. transaksi 75rb', 'nominal', 15000.00, 75000.00, NULL, 50, 0, '2025-01-01', '2025-12-31', 1, '2026-05-30 14:37:16'),
(3, 'MLBB20', 'Diskon 20% khusus top-up MLBB', 'persen', 20.00, 30000.00, 30000.00, 200, 0, '2025-05-01', '2025-07-31', 1, '2026-05-30 14:37:16'),
(4, 'GENSHIN5K', 'Potongan Rp5.000 top-up Genshin', 'nominal', 5000.00, 15000.00, NULL, 500, 0, '2025-05-01', '2025-06-30', 1, '2026-05-30 14:37:16');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `game`
--
ALTER TABLE `game`
  ADD PRIMARY KEY (`id_game`);

--
-- Indeks untuk tabel `paket_item`
--
ALTER TABLE `paket_item`
  ADD PRIMARY KEY (`id_paket`),
  ADD KEY `fk_paket_game` (`id_game`);

--
-- Indeks untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD UNIQUE KEY `nomor_invoice` (`nomor_invoice`),
  ADD KEY `fk_trx_user` (`id_user`),
  ADD KEY `fk_trx_paket` (`id_paket`),
  ADD KEY `fk_trx_voucher` (`id_voucher`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_user`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indeks untuk tabel `voucher`
--
ALTER TABLE `voucher`
  ADD PRIMARY KEY (`id_voucher`),
  ADD UNIQUE KEY `kode_voucher` (`kode_voucher`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `game`
--
ALTER TABLE `game`
  MODIFY `id_game` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT untuk tabel `paket_item`
--
ALTER TABLE `paket_item`
  MODIFY `id_paket` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  MODIFY `id_transaksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT untuk tabel `user`
--
ALTER TABLE `user`
  MODIFY `id_user` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `voucher`
--
ALTER TABLE `voucher`
  MODIFY `id_voucher` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `paket_item`
--
ALTER TABLE `paket_item`
  ADD CONSTRAINT `fk_paket_game` FOREIGN KEY (`id_game`) REFERENCES `game` (`id_game`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  ADD CONSTRAINT `fk_trx_paket` FOREIGN KEY (`id_paket`) REFERENCES `paket_item` (`id_paket`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_trx_user` FOREIGN KEY (`id_user`) REFERENCES `user` (`id_user`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_trx_voucher` FOREIGN KEY (`id_voucher`) REFERENCES `voucher` (`id_voucher`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
