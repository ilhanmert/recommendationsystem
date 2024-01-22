from flask import Flask, jsonify, request
from flask_sqlalchemy import SQLAlchemy
import hashlib

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:password@host:port/recommendation'
db = SQLAlchemy(app)

#Ürün adlarını, aynı olanları bir kere yazarak, bir liste halinde dönen fonksiyon
@app.route('/products', methods=['GET'])
def get_products():
    try:
        product_names = Product.query.with_entities(Product.productName).distinct().all()
        product_names_list = [name[0] for name in product_names]
        return jsonify(product_names_list)
    
    except Exception as e:
            return {'error': str(e)}

#Seçilen ürüne göre ideal özellik değerlerini hesaplayıp dönen fonksiyon
@app.route('/ideal-features', methods=['GET', 'POST'])
def get_ideal_features():
    try:
        data = request.get_json()
        product_name = data.get('productName')
        product_list = calculate_ideal_features(product_name)
        return jsonify(product_list)
    
    except Exception as e:
        return {'error': str(e)}

#Veritabanına ürün kaydeden fonksiyon
@app.route('/save-data', methods=['POST'])
def save_data():
    try:
        data_list = request.get_json()

        for data in data_list:
            product = Product(
                productName=data['productName'],
                featureName=data['featureName'],
                featureValue=data['featureValue'],
                featurePoint=data['featurePoint'],
                salesAmount=data['salesAmount']
            )

            db.session.add(product)
            db.session.commit()

        response_data = {
            "status": True
        }

        return jsonify(response_data)  # Yanıtı JSON formatında döndür

    except Exception as e:
        response_data = {
            "status": False,
            "error": str(e)
        }

        return jsonify(response_data)

#Veritabanına ürün silen fonksiyon
@app.route('/delete-product', methods=['POST'])
def delete_product():
    try:
        data = request.get_json()
        product_name = data.get('productName')

        if not product_name:
            response_data = {
                "status": False
            }
            return jsonify(response_data)

        Product.query.filter_by(productName=product_name).delete()
        db.session.commit()

        response_data = {
            "status": True
        }
        return jsonify(response_data)

    except Exception as e:
        return {'error': str(e)}

#Kullanıcı adı ve şifrelenmiş şifreyi veritabanı ile karşılaştırıp true ya da false cevabı dönen fonksiyon
@app.route('/check-user', methods=['GET', 'POST'])
def check_user():
    try:
        data = request.get_json()
        user_username = data.get('username')
        user_password = data.get('password')
        hashed_user_password = decoder(user_password)
        user = User.query.first()
        user_data = user.to_dict()

        if user_data['username'] == user_username and user_data['password'] == hashed_user_password:
            response_data = {
                "status": True
            }
    
        else:
            response_data = {
                "status": False
            }
    
        return jsonify(response_data)
    
    except Exception as e:
        return {'error': str(e)}
    
#İdeal özellik değerlerini hesaplayan fonksiyon
def calculate_ideal_features(product_name):
    try:
        product_data = Product.query.filter_by(productName=product_name).all()

        if not product_data:
            return {'message': 'Ürün verisi bulunamadı'}
        
        ideal_features = []

        #İdeal özellik değeri hesaplaması için belirlenen puan katsayıları
        score_mapping = {
        5.0: 1,
        4.9: 2, 4.8: 3, 4.7: 4, 4.6: 5, 4.5: 6,
        4.4: 7, 4.3: 8, 4.2: 9, 4.1: 10, 4.0: 11,
        3.9: 12, 3.8: 13, 3.7: 14, 3.6: 15, 3.5: 16,
        3.4: 17, 3.3: 18, 3.2: 19, 3.1: 20, 3.0: 21,
        2.9: 22, 2.8: 23, 2.7: 24, 2.6: 25, 2.5: 26,
        2.4: 27, 2.3: 28, 2.2: 29, 2.1: 30, 2.0: 31,
        1.9: 32, 1.8: 33, 1.7: 34, 1.6: 35, 1.5: 36,
        1.4: 37, 1.3: 38, 1.2: 39, 1.1: 40, 1.0: 41,
        0.9: 42, 0.8: 43, 0.7: 44, 0.6: 45, 0.5: 46,
        0.4: 47, 0.3: 48, 0.2: 49, 0.1: 50, 0.0: 51,
        }

        for data1 in product_data:
            feature_name = data1.featureName
            feature_value = data1.featureValue
            feature_point = data1.featurePoint
            sales_amount = data1.salesAmount
            score_multiplier = score_mapping.get(feature_point, 11)
            feature_score = round(sales_amount / score_multiplier)

            found = False
            for data2 in ideal_features:
                if data2['feature_name'] == feature_name:
                    if data2['feature_score'] < feature_score:
                        data2['feature_value'] = feature_value
                        data2['feature_score'] = feature_score
                    found = True
                    break
    
            if not found:
                ideal_feature = {
                    'feature_name': feature_name,
                    'feature_value': feature_value,
                    'feature_score': feature_score
                }
                ideal_features.append(ideal_feature)

        result = {
            'product_name': product_name,
            'ideal_features': ideal_features
        }
        return result

    except Exception as e:
        return {'error': str(e)}

#SHA256'ya göre şifreleme yapan fonksiyon
def decoder(password):
    try:
        hashed_password = hashlib.sha256(password.encode()).hexdigest()
        return hashed_password
    
    except Exception as e:
        return {'error': str(e)}

#Ürün class'ı
class Product(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    productName = db.Column(db.String(255), unique=False, nullable=False)
    featureName = db.Column(db.String(255), nullable=False)
    featureValue = db.Column(db.String(255), nullable=False)
    featurePoint = db.Column(db.Float, nullable=False)
    salesAmount = db.Column(db.Integer, nullable=False)

    def __repr__(self):
        return '<Product %r>' % self.productName

#Kullanıcı class'ı
class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(255), unique=False, nullable=False)
    password = db.Column(db.String(255), nullable=False)
    role = db.Column(db.String(255), nullable=False)

    def __repr__(self):
        return '<User %r>' % self.username
    
    #Kullanıcı verisini dictionary'e dönüştüren fonksiyon
    def to_dict(self):
        return {
            'id': self.id,
            'username': self.username,
            'password': self.password,
            'role': self.role
        }

if __name__ == '__main__':
    app.run(debug=True)