# Import library
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
import tensorflow as tf
from tensorflow import keras
from tensorflow.keras import layers
import matplotlib.pyplot as plt
from cloudConnect import requestDataSql

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
    print(f"\nItems that need to be restocked in the next {alert_days} days: {', '.join(items_to_restock)}")
else:
    print(f"\nNo items need to be restocked in the next {alert_days} days.")
