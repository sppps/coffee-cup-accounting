import pymongo
import users


def init_app(app):
    mongo_client = pymongo.MongoClient('127.0.0.1')
    dbnames = mongo_client.database_names()
    dbname = app.config['MONGO_DBNAME']
    if app.config['MONGO_DBNAME'] not in dbnames:
        db = mongo_client[dbname]
        users.User.create(db, username='admin', password='123456')

    app.config['db'] = mongo_client[dbname]
