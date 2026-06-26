-- ============================================================
--  ZenithStore - Data Test / Data Dummy
--  Jalankan file ini SETELAH import db_zenithstore.sql
-- ============================================================

USE db_zenithstore;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE transaksi;
TRUNCATE TABLE voucher;
TRUNCATE TABLE `user`;
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- DATA USER
-- Password = teks biasa (untuk keperluan demo/tugas)
-- ============================================================
INSERT INTO `user` (nama_user, email, nomor_hp, password, role) VALUES
('Admin ZenithStore', 'admin@zenithstore.com', '081200000000', 'admin123',  'admin'),
('Aditya Darmawan',   'aditya@gmail.com',      '081234567891', 'aditya123', 'customer'),
('Ahmad Faiz Arkhan', 'faiz@gmail.com',         '081234567892', 'faiz123',   'customer'),
('Hadid Riansyah',    'hadid@gmail.com',        '081234567893', 'hadid123',  'customer'),
('Imron Hafidz',      'imron@gmail.com',        '081234567894', 'imron123',  'customer'),
('Budi Santoso',      'budi@gmail.com',         '082345678901', 'budi123',   'customer'),
('Siti Rahayu',       'siti@gmail.com',         '083456789012', 'siti123',   'customer'),
('Rizky Pratama',     'rizky@gmail.com',        '084567890123', 'rizky123',  'customer');

-- ============================================================
-- DATA VOUCHER
-- ============================================================
INSERT INTO voucher (kode_voucher, deskripsi, tipe_diskon, nilai_diskon, min_transaksi, maks_diskon, kuota, berlaku_dari, berlaku_sampai) VALUES
('NEWUSER10',  'Diskon 10% untuk pengguna baru',        'persen',  10,    0,       20000, 100, '2025-01-01', '2026-12-31'),
('HEMAT15K',   'Potongan Rp15.000 min. transaksi 75rb', 'nominal', 15000, 75000,   NULL,  50,  '2025-01-01', '2026-12-31'),
('MLBB20',     'Diskon 20% khusus top-up MLBB',         'persen',  20,    30000,   30000, 200, '2025-05-01', '2026-12-31'),
('GENSHIN5K',  'Potongan Rp5.000 top-up Genshin',       'nominal', 5000,  15000,   NULL,  500, '2025-05-01', '2026-12-31'),
('ZENITH25',   'Diskon 25% special event',              'persen',  25,    100000,  50000, 30,  '2026-05-01', '2026-12-31'),
('PUBGDAY',    'Potongan Rp10.000 top-up PUBG',         'nominal', 10000, 50000,   NULL,  100, '2026-01-01', '2026-12-31');

-- ============================================================
-- DATA TRANSAKSI
-- ============================================================
INSERT INTO transaksi (nomor_invoice, id_user, id_paket, uid_game, server_game, jumlah_beli, harga_satuan, diskon, total_harga, metode_pembayaran, status_pembayaran, tanggal_transaksi) VALUES
-- Aditya - Honkai Star Rail
('INV-20260501-0001', 2, 1,  '111222333', NULL, 1, 15000,  0,     15000,  'DANA',        'success', '2026-05-01 09:15:00'),
('INV-20260501-0002', 2, 3,  '111222333', NULL, 1, 230000, 23000, 207000, 'GoPay',       'success', '2026-05-01 14:30:00'),
-- Faiz - Genshin Impact
('INV-20260502-0001', 3, 7,  '222333444', NULL, 1, 75000,  0,     75000,  'OVO',         'success', '2026-05-02 10:00:00'),
('INV-20260502-0002', 3, 8,  '222333444', NULL, 1, 230000, 15000, 215000, 'Transfer Bank','success', '2026-05-02 16:45:00'),
-- Hadid - Mobile Legends
('INV-20260503-0001', 4, 20, '333444555', '5', 1, 30000,  6000,  24000,  'DANA',        'success', '2026-05-03 11:20:00'),
('INV-20260503-0002', 4, 22, '333444555', '5', 1, 60000,  0,     60000,  'ShopeePay',   'success', '2026-05-03 19:10:00'),
('INV-20260503-0003', 4, 24, '333444555', '5', 1, 90000,  18000, 72000,  'GoPay',       'success', '2026-05-10 08:30:00'),
-- Imron - Free Fire
('INV-20260505-0001', 5, 29, '444555666', NULL, 1, 10000,  0,     10000,  'Alfamart',    'success', '2026-05-05 13:00:00'),
('INV-20260505-0002', 5, 31, '444555666', NULL, 1, 40000,  0,     40000,  'Indomaret',   'success', '2026-05-05 15:30:00'),
-- Budi - PUBG Mobile
('INV-20260507-0001', 6, 35, '555666777', NULL, 1, 75000,  10000, 65000,  'Transfer Bank','success', '2026-05-07 20:00:00'),
('INV-20260507-0002', 6, 36, '555666777', NULL, 1, 150000, 0,     150000, 'GoPay',       'success', '2026-05-07 20:45:00'),
-- Siti - Roblox
('INV-20260510-0001', 7, 13, '@sitirbx',  NULL, 1, 15000,  0,     15000,  'DANA',        'success', '2026-05-10 10:00:00'),
('INV-20260510-0002', 7, 15, '@sitirbx',  NULL, 1, 70000,  0,     70000,  'LinkAja',     'success', '2026-05-10 11:00:00'),
-- Rizky - HSR + Genshin
('INV-20260515-0001', 8, 4,  '666777888', NULL, 1, 450000, 0,     450000, 'Transfer Bank','success', '2026-05-15 14:00:00'),
('INV-20260515-0002', 8, 9,  '777888999', NULL, 1, 230000, 5000,  225000, 'GoPay',       'success', '2026-05-15 15:30:00'),
-- Transaksi hari ini (berbagai status)
('INV-20260601-0001', 2, 5,  '111222333', NULL, 1, 750000, 0,     750000, 'Transfer Bank','pending', NOW()),
('INV-20260601-0002', 3, 10, '222333444', NULL, 1, 450000, 0,     450000, 'DANA',        'pending', NOW()),
('INV-20260601-0003', 4, 21, '333444555', '5', 1, 4000,   0,     4000,   'GoPay',       'failed',  NOW()),
('INV-20260601-0004', 5, 30, '444555666', NULL, 1, 20000,  0,     20000,  'Alfamart',    'success', NOW()),
('INV-20260601-0005', 6, 37, '555666777', NULL, 1, 400000, 0,     400000, 'OVO',         'success', NOW());
