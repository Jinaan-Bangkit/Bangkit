const express = require('express')
const app = express()
const bodyParser = require('body-parser')

const recordRouter = require('./routes/record')
const backupRouter = require('./routes/backup')

app.use(bodyParser.json())
app.use(bodyParser.urlencoded({extended: true}))
app.use(recordRouter)
app.use(backupRouter)

app.get("/", (req, res) => {
    console.log("Response success")
    res.send("Response Success!")
})

const PORT = process.env.PORT || 8000
app.listen(PORT, () => {
    console.log("Server is up and listening on " + PORT)
})