from google.cloud.sql.connector import Connector, IPTypes
import sqlalchemy
import os
API = "http://localhost:8000"
current_dir = os.path.dirname(os.path.realpath(__file__))
os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = os.path.join(current_dir, "mlkey.json")




connector = Connector()

def getconn():
    conn = connector.connect(
        "capstone-project201123:asia-southeast2:my-instance",
        "pymysql",
        user = "root",
        password = "toor",
        db="cashier_db",
        ip_type= IPTypes.PUBLIC,
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

if __name__ == "__main__":
    query = "SELECT * FROM barang;"
    result = requestDataSql(query)
    result = list(result)
    print(result)