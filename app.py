#!/usr/bin/env python2.7
import bson
import users
import users.login
import database
import supply
import techmaps
import ingredients
import consume
import consumers
import debts
# import atexit
# import rpc.coffee_cup
from flask import Flask, render_template
from flask.json import JSONEncoder
from flask_login import LoginManager, login_required


from werkzeug.routing import BaseConverter, ValidationError
from itsdangerous import base64_encode, base64_decode
from bson.objectid import ObjectId
from bson.errors import InvalidId


class ObjectIDConverter(BaseConverter):
    def to_python(self, value):
        try:
            return ObjectId(base64_decode(value))
        except (InvalidId, ValueError, TypeError):
            raise ValidationError()

    def to_url(self, value):
        return base64_encode(value.binary)


class CustomJsonEncoder(JSONEncoder):
    def default(self, obj):
        if isinstance(obj, ObjectId):
            return str(obj)
        return JSONEncoder.default(self, obj)


def create_app():
    app = Flask(__name__)
    app.config.from_object('config')
    app.url_map.converters['ObjectId'] = ObjectIDConverter
    app.json_encoder = CustomJsonEncoder

    database.init_app(app)

    login_manager = LoginManager()
    login_manager.init_app(app)
    login_manager.login_view = 'users.login'

    @login_manager.user_loader
    def load_user(user_id):
        db = app.config['db']
        return users.login.User.find(db, _id=bson.ObjectId(user_id))

    @login_manager.request_loader
    def load_user_from_request(request):
        access_token = request.headers.get('X-Access-Token') or request.args.get('access_token')
        if access_token is not None:
            pass
        db = app.config['db']
        return users.login.User.find(db, access_token=access_token)

    @app.route('/')
    @login_required
    def home():
        return render_template('home.html')

    app.register_blueprint(users.login.create_blueprint())
    app.register_blueprint(consume.create_blueprint())
    app.register_blueprint(consumers.create_blueprint())
    app.register_blueprint(supply.create_blueprint())
    app.register_blueprint(ingredients.create_blueprint())
    app.register_blueprint(techmaps.create_blueprint())
    app.register_blueprint(debts.create_blueprint())
    app.register_blueprint(users.create_blueprint())

    # rpc_server = rpc.coffee_cup.start(app.config['db'])

    # def close_rpc_server():
    #     rpc_server.stop(0)
    # atexit.register(close_rpc_server)

    return app

if __name__ == '__main__':
    create_app().run()
else:
    application = create_app()
