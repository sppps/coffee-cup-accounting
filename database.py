import pymongo
import users.login


def init_app(app):
    mongodb_uri = app.config['MONGODB_URI']
    db = pymongo.MongoClient(mongodb_uri).get_database()
    admin_user = users.login.User.find(db, username='admin')
    if admin_user is None:
        users.User.create(db, username='admin', password='123456')
    app.config['db'] = db
