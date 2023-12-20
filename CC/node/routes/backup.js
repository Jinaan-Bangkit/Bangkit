const express = require('express')
// @google-cloud/storage
const {Storage} = require('@google-cloud/storage')
const router = express.Router()
const dayjs = require('dayjs')
const db = require('../module/connection')
// process.env.TZ = 'America/Toronto'
process.env.TZ = 'Asia/Jakarta'

const path = require('path');
const { json } = require('body-parser')
const pathKey = path.resolve('./key/bucketkey.json')

// TODO: Sesuaikan konfigurasi Storage
const gcs = new Storage({
    projectId: 'capstone-project201123',
    keyFilename: pathKey
})

const bucketName = 'capstone-project201123-bucket'
const bucket = gcs.bucket(bucketName)




router.get("/backupMonthlytransaction", (req, res) => {
    const month = dayjs().format('MM')
    const year = dayjs().format('YYYY')
    const query = "SELECT transaksi.id_transaksi, transaksi.id_beli, transaksi.id_barang, barang.nama, transaksi.jumlah, transaksi.total, barang.harga_beli, beli.total_harga, beli.tgl_pembelian FROM transaksi INNER JOIN beli ON transaksi.id_beli = beli.id_beli INNER JOIN barang ON transaksi.id_barang = barang.id_barang WHERE MONTH(beli.tgl_pembelian) = " + month + " AND YEAR(beli.tgl_pembelian) = " + year + " ORDER BY beli.tgl_pembelian ASC"
    db.query(query, (err, rows, field) => {
        if(err) {
            console.log(err);
            return res.status(500).send({message: err.sqlMessage})
            
        } else {
            const monthyear = dayjs().format('YYYY-MM')
            const fileName = 'backupTransactionTest-' + monthyear +'.json'
            const jsonData = JSON.stringify(rows)

            // upload sql file to GCS
            const gcsname = fileName
            try {
                // Upload data to Google Cloud Storage
                const file = bucket.file("Backup Transaction/" + gcsname);
                file.save(jsonData, {
                    gzip: true,
                    metadata: {
                        cacheControl: 'public, max-age=31536000',
                    },
                });
    
                console.log('Data uploaded successfully to Google Cloud Storage.');
            } catch (uploadError) {
                console.error('Error uploading data:', uploadError);
            } 
        }
    })
    res.send("ok")
});

router.get("/dataBackupMonthlystock", (req, res) => {
    const month = req.query.bulan
    const year = req.query.tahun
    // read data from GCS
    const fileName = 'backupStock-' + month + '-' + year +'.json'
    const gcsname = fileName
    const file = bucket.file("Backup Stock/" + gcsname);
    file.download(function(err, contents) {
        if (!err) {
            const data = JSON.parse(contents.toString())
            res.send(data)
        } else {
            console.log(err);
            res.send(err)
        }
    });
});

async function downloadFile(file) {
    return new Promise((resolve, reject) => {
        file.download((err, contents) => {
            if (err) {
                reject(err);
            } else {
                resolve(JSON.parse(contents.toString()));
            }
        });
    });
}

router.get("/dataBackupYearlystock", async (req, res) => {
    const year = req.query.tahun
    // file list
    const [files] = await bucket.getFiles({ prefix: 'Backup Transaction/backupTransaction'});
    files.forEach(file => {
        console.log(file.name);
    });


    // save all file list that match with year
    const fileName = []
    files.forEach(file => {
        if(file.name.includes(year)) {
            fileName.push(file.name)
        }
    });
    console.log(fileName[0]);

    // to combine all data
    var dataCombined = []

    // read data from GCS
    for (let i = 0; i < fileName.length; i++) {
        const file = bucket.file(fileName[i]);
        const datafile = await downloadFile(file);
        dataCombined = dataCombined.concat(datafile)
    }
    res.send(dataCombined)
});


module.exports = router