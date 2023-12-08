CREATE TABLE akun (
    id_akun VARCHAR(16) PRIMARY KEY NOT NULL,
    nama VARCHAR(250) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    posisi VARCHAR(50) NOT NULL
);

CREATE TABLE barang (
    id_barang VARCHAR(8) PRIMARY KEY NOT NULL,
    nama VARCHAR(100) NOT NULL,
    harga_jual INT NOT NULL,
    harga_beli INT NOT NULL,
    stock INT NOT NULL,
    keterangan TEXT 
);

CREATE TABLE beli (
    id_beli VARCHAR(8) PRIMARY KEY NOT NULL,
    tgl_pembelian DATETIME NOT NULL,
    total_harga INT NOT NULL
);


CREATE TABLE transaksi (
    id_transaksi VARCHAR(8) PRIMARY KEY NOT NULL,
    id_barang VARCHAR(8) NOT NULL,
    id_beli VARCHAR(8) NOT NULL,
    jumlah INT NOT NULL,
    FOREIGN KEY (id_barang) REFERENCES barang(id_barang),
    FOREIGN KEY (id_beli) REFERENCES beli(id_beli)
);
