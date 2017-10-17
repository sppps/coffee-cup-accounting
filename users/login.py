import passlib.context
import binascii
import os
from flask import Blueprint, request, redirect, url_for, current_app as app, render_template
from flask_login import login_required, login_user, logout_user

crypto_ctx = passlib.context.CryptContext(schemes=["sha256_crypt"])


class User(object):
    def __init__(self, data={}):
        self.user_id = data.get('_id')
        self.username = data.get('username')
        self.pwdhash = data.get('pwdhash')
        self.is_active = data.get('is_active')
        self.is_authenticated = self.user_id is not None

    def get_id(self):
        return str(self.user_id)

    @classmethod
    def create(Class, db, username, password, is_active=True):
        db.users.update({
            'username': username
            }, {
            '$set': {
                'username': username,
                'pwdhash': crypto_ctx.hash(username+password),
                'is_active': is_active
                }
            }, upsert=True)

    @classmethod
    def find(Class, db, **kwargs):
        data = db.users.find_one(kwargs)
        if data is not None:
            return Class(data)

    def check_password(self, password):
        return crypto_ctx.verify(self.username+password, self.pwdhash)

    def refresh_access_token(self, db):
        access_token = binascii.hexlify(os.urandom(64)).decode()
        db.users.update({
            '_id': self.user_id
            }, {
            '$set': {
                'access_token': access_token
            }
            })
        return access_token


def create_blueprint():
    bp = Blueprint('users', __name__)

    @bp.route('/login', methods=['GET', 'POST'])
    def login():
        db = app.config['db']
        if request.method == 'POST':
            username = request.form.get('username')
            user = User.find(db, username=username)
            if user is None:
                return render_template('login.html', error='Invalid username')
            if not user.check_password(request.form.get('password')):
                return render_template('login.html', error='Invalid password')
            login_user(user)
            return redirect(request.args.get('next') or url_for('home'))
        return render_template('login.html')

    @bp.route('/logout')
    @login_required
    def logout():
        logout_user()
        return redirect(url_for('home'))

    return bp
