from flask import Flask
# Import library
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
import matplotlib.pyplot as plt
import base64
from google.cloud.sql.connector import Connector, IPTypes
import sqlalchemy
import os
import requests

app = Flask(__name__)

current_dir = os.path.dirname(os.path.realpath(__file__))
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = os.path.join(current_dir, "notifkey.json")


API = "https://backend-dot-capstone-project201123.et.r.appspot.com/insertQuerySql?kueri="

connector = Connector()

def getconn():
    conn = connector.connect(
        "capstone-project201123:asia-southeast2:my-instance",
        "pymysql",
        user = "root",
        password = "toor",
        db="cashier_db",
    )
    return conn

pool = sqlalchemy.create_engine(
    "mysql+pymysql://",
    creator=getconn,
)

def requestDataSql(query):
    with pool.connect() as db_conn:
        result = db_conn.execute(sqlalchemy.text(query))
        result = list(result)
    db_conn.close()
    return result

def inputNotiftoSql(data, condition = "input"):
    if condition == "input":
        query = f"INSERT INTO notifikasi (nama_barang, day_left) VALUES ('{data[0]}', '{data[1]}');"
        print(query)
        encoded = base64.b64encode(query.encode('utf-8'))
        encoded = str(encoded)
        encoded = encoded[2:-1]
        url = API + encoded
        print(url)
    elif condition == "update":
        query = f"UPDATE notifikasi SET day_left = '{data[1]}' WHERE nama_barang = '{data[0]}';"
        print(query)
        encoded = base64.b64encode(query.encode('utf-8'))
        encoded = str(encoded)
        encoded = encoded[2:-1]
        url = API + encoded
        print(url)
    if url:
        requests.get(url)



    

def inputNotif(data):
    # get data from notifikasi
    query = "SELECT * FROM notifikasi;"
    result = requestDataSql(query)
    result = list(result)

    print(result)


    for i in data:
        # check if data already exist
        day_left = 7
        for j in result:
            if i == j[1]:
                day_left = j[2] - 1
                if day_left == 0 or day_left < 0:
                    day_left = 0
                newdata = [i, day_left]
                inputNotiftoSql(newdata, "update")
                break
        if day_left == 7:
            newdata = [i, day_left]
            inputNotiftoSql(newdata)

        # newdata = [i,"7"]
        # inputNotiftoSql(newdata)

    # get data from notifikasi
    query = "SELECT * FROM notifikasi;"
    result = requestDataSql(query)
    result = list(result)

    print(result)

    apiBarang = "https://backend-dot-capstone-project201123.et.r.appspot.com/barang/nama?namaBarang="
    for i in range(len(result)):
        # get data from barang
        url = apiBarang + result[i][1]
        r = requests.get(url)
        r = r.json()
        print(r)
        # if r not empty
        if r:
            # cek stock barang
            print(r[0]["stock"])
            if r[0]["stock"] > 100:
                # remove from notifikasi
                query = f"DELETE FROM notifikasi WHERE nama_barang = '{result[i][1]}';"
                encoded = base64.b64encode(query.encode('utf-8'))
                encoded = str(encoded)
                encoded = encoded[2:-1]
                url2 = API + encoded
                print(url2)
                requests.get(url2)
            


# Function to get item and sales data from database
def getData():
    # Get item data
    query_barang = "SELECT * FROM barang;"
    barang_data = pd.DataFrame(requestDataSql(query_barang), columns=['id_barang', 'nama', 'harga_jual', 'harga_beli', 'stock', 'keterangan'])

    # Get sales data
    query_transaksi = "SELECT * FROM transaksi;"
    transaksi_data = pd.DataFrame(requestDataSql(query_transaksi), columns=['id_transaksi', 'id_barang', 'id_beli', 'jumlah', 'tanggal'])

    # Combine sales data with item data
    data_combined = pd.merge(transaksi_data, barang_data, on='id_barang', how='inner')

    return data_combined, barang_data


@app.route('/')
# Triggered from a message on a Cloud Pub/Sub topic.
def main():
    # Get data from cloud using SQL query
    data_combined, barang_data = getData()

    # Sales Data Processing
    data_combined['tanggal'] = pd.to_datetime(data_combined['tanggal'])
    data_combined.set_index('tanggal', inplace=True)

    # Aggregate sales data per day
    daily_sales = data_combined.groupby(['id_barang', data_combined.index.date])['jumlah'].sum().reset_index()

    # Select relevant features
    features = barang_data[['harga_jual', 'harga_beli', 'stock']]

    # Split data into training set and validation set
    train_data, valid_data = train_test_split(features, test_size=0.2, random_state=42)

    # Normalize features
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(train_data)
    X_valid_scaled = scaler.transform(valid_data)

    # Initialize neural network model
    model = keras.Sequential([
        layers.Dense(64, activation='relu', input_shape=[len(features.columns)]),
        layers.Dense(32, activation='relu'),
        layers.Dense(1, activation='linear')  # Using linear activation function for output
    ])

    # Compile model
    model.compile(optimizer='adam', loss='mean_squared_error')

    # Train model
    history = model.fit(X_train_scaled, train_data['stock'],
                        validation_data=(X_valid_scaled, valid_data['stock']),
                        epochs=50, batch_size=32)

    # Evaluate model
    mse = model.evaluate(X_valid_scaled, valid_data['stock'])
    print(f'Mean Squared Error: {mse}')

    # Forecasting how many days the stock will run out
    alert_days = 7  # Set the alert threshold
    items_to_restock = []  # List to hold items that need to be restocked

    for idx in valid_data.index.unique():
        # Get unsold item data
        data_barang_unsold = valid_data.loc[valid_data.index == idx]

        # Make predictions
        predicted_sales = model.predict(scaler.transform(data_barang_unsold))

        # Flatten the predicted_sales array
        predicted_sales = predicted_sales.flatten()

        # Calculate how many days until stock runs out
        days_to_run_out = data_barang_unsold['stock'] / predicted_sales

        # Check if stock will run out in the next 'alert_days' days
        if (days_to_run_out <= alert_days).any():
            items_to_restock.append(barang_data.loc[idx, 'nama'])
            print(f"ALERT: ID Barang: {idx}, Nama Barang: {barang_data.loc[idx, 'nama']}, Days to run out of stock: {days_to_run_out}")

    # Print all items that need to be restocked
    if items_to_restock:
        # print(f"\nItems that need to be restocked in the next {alert_days} days: {', '.join(items_to_restock)}")


        dataToInput = []
        for i in items_to_restock:
            dataToInput.append(i)
        inputNotif(dataToInput)



    else:
        print(f"\nNo items need to be restocked in the next {alert_days} days.")

    
    return "Hello from Python on Cloud Run!2"
if __name__ == "__main__":
    app.run(host='0.0.0.0', port=int(os.environ.get('PORT', 8080)))