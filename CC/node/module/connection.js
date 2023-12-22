
const mysql = require('mysql2')
const db = mysql.createPool({
    host: '34.128.122.160',
    user: 'root',
    password: 'toor',
    database: 'cashier_db'
})

module.exports = db