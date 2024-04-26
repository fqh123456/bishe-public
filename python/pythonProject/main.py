import json

import pandas as pd
import pymysql
from flask import Flask, jsonify
from flask_cors import CORS
from sqlalchemy import create_engine

pymysql.install_as_MySQLdb()
engine = create_engine('mysql://root:root@localhost:3306/test?charset=utf8')


app = Flask(__name__)
CORS(app) # 允许跨域请求


@app.route('/get_data', methods=['GET'])
def get_data():
    # PU 占比
    sql = """
    select sum(case when pay_price > 0 then 1 else 0 end) as `付费用户`,
           sum(case when pay_price > 0 then 0 else 1 end) as `非付费用户`
    from age_of_barbarians
    """
    data = pd.read_sql(con=engine, sql=sql)
    # data = {"name": "Tom", "age": 25} # 想要返回的数据
    print(data)
    return jsonify(json.loads(data.to_json(orient='records')))

if __name__ == "__main__":
    app.run(host='localhost', port=5000)