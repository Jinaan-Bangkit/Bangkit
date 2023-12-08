const express = require('express')
const mysql = require('mysql2')
const router = express.Router()
const Multer = require('multer')
const { nanoid } = require('nanoid')
const dayjs = require('dayjs')
// process.env.TZ = 'America/Toronto'
process.env.TZ = 'Asia/Jakarta'

const db = mysql.createPool({
    host: '34.128.122.160',
    user: 'root',
    password: 'toor',
    database: 'cashier_db'
})
// const db = mysql.createPool({
//     host: 'localhost',
//     user: 'root',
//     password: '',
//     database: 'capstone1'
// })
router.get("/login", (req, res) => {
    const name = req.query.name
    console.log(name)
    const password = req.query.password
    const query = "select * from akun where akun.nama = '" + name + "' and akun.password = '" + password + "'";
    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows)
            console.log(rows);
        }
    })
});


router.post("/register", (req, res) => {
    const id = nanoid(16)
    const name = req.body.name
    const password = req.body.password
    const email = req.body.email
    const posisi = req.body.posisi
    console.log(req.body)
    // res.send("ok")
    const query = "insert into akun (id_akun, nama, password, email, posisi) values ('" + id + "', '" + name + "', '" + password + "', '" + email + "', '" + posisi + "')";
    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows)
            console.log(rows);
        }
    })
});

// router.get("/tabels", (req, res) => {
//     const query = "show tables";
//     db.query(query, (err, rows, field) => {
//         if(err) {
//             res.status(500).send({message: err.sqlMessage})
//             console.log(err);
//         } else {
//             res.json(rows)
//             console.log(rows);
//         }
//     })
// });
// router.get("/akun", (req, res) => {
//     const query = "select * from akun";
//     db.query(query, (err, rows, field) => {
//         if(err) {
//             res.status(500).send({message: err.sqlMessage})
//             console.log(err);
//         } else {
//             res.json(rows)
//             console.log(rows);
//         }
//     })
// });

router.get("/barang", (req, res) => {
    const query = "select * from barang";

    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows)
            console.log(rows);
        }
    })
});

router.get("/barang/:id", (req, res) => {
    const id = req.params.id
    const query = "select * from barang where id_barang = '" + id + "'";
    // const query = "select * from barang where id_barang = ?";
    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows)
            console.log(rows);
        }
    })
});

router.post("/barang", (req, res) => {
    const id = nanoid(8)
    const nama = req.body.nama
    const hargaJual = req.body.hargaJual
    const hargaBeli = req.body.hargaBeli
    const stock = req.body.stock
    const keterangan = req.body.keterangan
    console.log(req.body)
    const query = "insert into barang (id_barang, nama, harga_jual, harga_beli, stock, keterangan) values ('" + id + "', '" + nama + "', '" + hargaJual + "', '" + hargaBeli + "', '" + stock + "', '" + keterangan + "')";
    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows)
            console.log(rows);
        }
    })
});

router.post("/barangArray", (req, res) => {
    try {
        for (var i = 0; i < req.body.length; i++) {
            const id = nanoid(8)
            const nama = req.body[i].nama
            const hargaJual = req.body[i].hargaJual
            const hargaBeli = req.body[i].hargaBeli
            const stock = req.body[i].stock
            const keterangan = req.body[i].keterangan
            const query = "insert into barang (id_barang, nama, harga_jual, harga_beli, stock, keterangan) values ('" + id + "', '" + nama + "', '" + hargaJual + "', '" + hargaBeli + "', '" + stock + "', '" + keterangan + "')";
            db.query(query, (err, rows, field) => {
                if(err) {
                    res.status(500).send({message: err.sqlMessage})
                    console.log(err);
                } else {
                    console.log(rows);
                }
            })
        }
        res.send("ok")
    } catch (error) {
        console.log(error)
    }
});


router.put("/barang/:id", (req, res) => {
    const id = req.params.id
    const nama = req.body.nama
    const hargaJual = req.body.hargaJual
    const hargaBeli = req.body.hargaBeli
    const stock = req.body.stock
    const keterangan = req.body.keterangan
    console.log(req.body)
    const query = "update barang set nama = '" + nama + "', harga_jual = '" + hargaJual + "', harga_beli = '" + hargaBeli + "', stock = '" + stock + "', keterangan = '" + keterangan + "' where id_barang = '" + id + "'";
    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows)
            console.log(rows);
        }
    })
});

router.delete("/barang/:id", (req, res) => {
    const id = req.params.id
    console.log(req.params)
    console.log(id)
    const query = "delete from barang where id_barang = '" + id + "'";
    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows)
            console.log(rows);
        }
    })
});

router.put("/restock", (req, res) => {
    const id_barang = req.body.id_barang
    const jumlah = req.body.jumlah
    const query = 'UPDATE barang SET stock = stock +' + jumlah + ' WHERE id_barang = "' + id_barang + '"';
    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows.info)
            console.log(rows);
        }
    }
    )
});

router.post("/setStock", (req, res) => {
    const id_barang = req.body.id_barang
    const jumlah = req.body.jumlah
    const query = 'UPDATE barang SET stock = ' + jumlah + ' WHERE id_barang = "' + id_barang + '"';
    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            res.json(rows.info)
            console.log(rows);
        }
    }
    )
});

router.get("/beli_id", (req, res) => {
    const query = "select id_beli from beli";

    db.query(query, (err, rows, field) => {
        if(err) {
            res.status(500).send({message: err.sqlMessage})
            console.log(err);
        } else {
            console.log(rows)
            // generate id
            while (true) {
                var id = nanoid(8)
                var found = false
                for (var i = 0; i < rows.length; i++) {
                    if (rows[i].id_beli == id) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    break
                }
            }
            res.json(id)
        }
    })
});

router.post("/beli", (req, res) => {
    console.log(req.body)
    const id = req.body.id
    let total_harga = 0
    const barang = req.body.data
    var tanggal = dayjs().format('YYYY-MM-DD HH:mm:ss')
    console.log(tanggal)
    const query = "insert into beli (id_beli, tgl_pembelian, total_harga) values ('" + id + "', '" + tanggal + "', '" + total_harga + "')";
    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            return res.status(500).send({message: err.sqlMessage})
            
        } else {
            console.log(rows);
            barang.forEach((item) => {
                const idTransaksi = nanoid(8)
                let query = "insert into transaksi (id_transaksi, id_barang, id_beli, jumlah, total) values ('" + idTransaksi + "', '" + item.id_barang + "', '" + id + "', '" + item.jumlah + "', '" + item.total + "')";
                db.query(query, (err, rows, field) => {
                    if(err) {
                        console.log(err);
                        return res.status(500).send({message: err.sqlMessage})
                    } else {
                        console.log(rows);
                    }
                })


                let total = item.harga*item.jumlah;
                total_harga += total;
                console.log(total);
                query = 'UPDATE transaksi SET total = ' + total + ' WHERE id_barang = "' + item.id_barang + '"'
                db.query(query, (err, rows, field) => {
                    if(err) {
                        console.log(err);
                        return res.status(500).send({message: err.sqlMessage})
                    } else {
                        console.log(rows);
                    }
                })

                query = 'UPDATE beli SET total_harga = ' + total_harga + ' WHERE id_beli = "' + id + '"'
                db.query(query, (err, rows, field) => {
                    if(err) {
                        console.log(err);
                        return res.status(500).send({message: err.sqlMessage})
                    } else {
                        console.log(rows);
                    }
                })

                const id_barang = item.id_barang
                const jumlah = item.jumlah
                query = 'UPDATE barang SET stock = stock -' + jumlah + ' WHERE id_barang = "' + id_barang + '"';
                db.query(query, (err, rows, field) => {
                    if(err) {
                        console.log(err);
                        return res.status(500).send({message: err.sqlMessage})
                        
                    } else {
                        console.log(rows);
                    }
                })
            })
            console.log(total_harga);
        }
    })
    return res.send("ok")
    
});










router.post("/beliJson", (req, res) => {
    console.log(req.body)
    for (var i = 0; i < req.body.length; i++) {
        // replace ": " with ":"
        req.body[i].tanggal = req.body[i].tanggal.replace(": ", ":")
        req.body[i].tanggal = req.body[i].tanggal.replace(": ", ":")
        req.body[i].tanggal = req.body[i].tanggal.replace(": ", ":")
        
        const idBeli = req.body[i].id
        const totalHarga = req.body[i].total
        const tanggal = req.body[i].tanggal
        console.log(tanggal)
        console.log(req.body[i].data.length)
        data = req.body[i].data
        const query = "insert into beli (id_beli, tgl_pembelian, total_harga) values ('" + idBeli + "', '" + tanggal + "', '" + totalHarga + "')";
        db.query(query, (err, rows, field) => {
            if(err) {
                console.log(err);
                return res.status(500).send({message: err.sqlMessage})

            } else {
                console.log(rows);
                console.log(data)
                for (var j = 0; j < data.length; j++) {
                    const idTransaksi = nanoid(8)
                    const idBarang = data[j].id_barang
                    const jumlah = data[j].jumlah
                    const total = data[j].harga
                    console.log(total)
                    const query = "insert into transaksi (id_transaksi, id_barang, id_beli,  jumlah, total) values ('" + idTransaksi + "', '" + idBarang + "', '" + idBeli + "', '" + jumlah + "', '" + total + "')";
                    db.query(query, (err, rows, field) => {
                        if(err) {
                            console.log(err);
                            return res.status(500).send({message: err.sqlMessage})
                        } else {
                            console.log(rows);
                        }
                    })

                    // mengurangi stock barang
                    const query2 = 'UPDATE barang SET stock = stock -' + jumlah + ' WHERE id_barang = "' + idBarang + '"';
                    db.query(query2, (err, rows, field) => {
                        if(err) {
                            console.log(err);
                            return res.status(500).send({message: err.sqlMessage})
                            
                        } else {
                            console.log(rows);
                        }
                    })



                }
            }
        })
    }
    return res.send("ok")

});



router.get("/clean", (req, res) => {
    // delete data inside transaksi
    const query = "delete from transaksi";
    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            return res.status(500).send({message: err.sqlMessage})
            
        } else {
            console.log(rows);
            // delete data inside beli
            const query = "delete from beli";
            db.query(query, (err, rows, field) => {
                if(err) {
                    console.log(err);
                    return res.status(500).send({message: err.sqlMessage})
                    
                } else {
                    console.log(rows);
                }
            })
        }
    })
    return res.send("ok")
}
);

















router.get("/beli", (req, res) => {
    const idBeli = req.body.idBeli
    const query = "SELECT transaksi.id_transaksi, transaksi.id_beli, transaksi.id_barang, barang.nama, transaksi.jumlah, barang.harga_jual AS harga, transaksi.total, beli.total_harga, beli.tgl_pembelian AS tanggal FROM transaksi INNER JOIN beli ON transaksi.id_beli = beli.id_beli INNER JOIN barang ON transaksi.id_barang = barang.id_barang where beli.id_beli = '" + idBeli + "'";
    // const query = "SELECT * FROM transaksi INNER JOIN beli ON transaksi.id_beli = beli.id_beli INNER JOIN barang ON transaksi.id_barang = barang.id_barang";

    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            res.status(500).send({message: err.sqlMessage})
            return
        } else {
            for (var i = 0; i < rows.length; i++) {
                rows[i].tanggal = dayjs(rows[i].tanggal).format('YYYY-MM-DD HH:mm:ss')
            }
            console.log(rows);
            res.json(rows)
            return
        }
    })
});


router.get("/beli_all", (req, res) => {
    const query = "SELECT * FROM transaksi INNER JOIN beli ON transaksi.id_beli = beli.id_beli ";
    // const query = "SELECT * FROM transaksi INNER JOIN beli ON transaksi.id_beli = beli.id_beli INNER JOIN barang ON transaksi.id_barang = barang.id_barang";

    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            res.status(500).send({message: err.sqlMessage})
            return
        } else {
            for (var i = 0; i < rows.length; i++) {
                rows[i].tgl_pembelian = dayjs(rows[i].tgl_pembelian).format('YYYY-MM-DD HH:mm:ss')
            }
            console.log(rows);
            res.json(rows)
            return
        }
    })
});

router.put("/restock", (req, res) => {
    const id_barang = req.body.id_barang
    const jumlah = req.body.jumlah
    const query = 'UPDATE barang SET stock = stock +' + jumlah + ' WHERE id_barang = "' + id_barang + '"';

    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            res.status(500).send({message: err.sqlMessage})
            return
        } else {
            console.log(rows);
            res.json(rows.info)
            return
        }
    })
});

router.put("/restockArray", (req, res) => {
    for (var i = 0; i < req.body.length; i++){
        const id_barang = req.body[i].id_barang
        const jumlah = req.body[i].jumlah
        const query = 'UPDATE barang SET stock = stock +' + jumlah + ' WHERE id_barang = "' + id_barang + '"';

        db.query(query, (err, rows, field) => {
            if(err) {
                console.log(err);
                return res.status(500).send({message: err.sqlMessage})
            } else {
                console.log(rows);
            }
        })

    }
    res.send("ok")
});



router.get("/detailTable", (req, res) => {
    let query = "DESCRIBE akun";
    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            return res.status(500).send({message: err.sqlMessage})
            
        } else {
            console.log(rows);
        }
    })

    query = "DESCRIBE barang";
    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            return res.status(500).send({message: err.sqlMessage})
            
        } else {
            console.log(rows);
        }
    })

    query = "DESCRIBE beli";
    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            return res.status(500).send({message: err.sqlMessage})
            
        } else {
            console.log(rows);
        }
    })

    query = "DESCRIBE transaksi";
    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            return res.status(500).send({message: err.sqlMessage})
            
        } else {
            console.log(rows);
        }
    })

});






module.exports = router