#!/usr/bin/env python2.7
import models
from flask import Flask, render_template, request, redirect, url_for
from flask_login import LoginManager, login_required, login_user, logout_user
from flask_sqlalchemy import SQLAlchemy


def create_app():
    app = Flask(__name__)
    app.config.from_object('config')

    db = SQLAlchemy(app)
    app.config['db'] = db

    login_manager = LoginManager()
    login_manager.init_app(app)
    login_manager.login_view = 'login'

    @login_manager.user_loader
    def load_user(user_id):
        return db.session.query(models.User).get(user_id)

    @app.route('/')
    @login_required
    def home():
        return render_template('home.html')

    @app.route('/login', methods=['GET', 'POST'])
    def login():
        if request.method == 'POST':
            user = db.session.query(models.User) \
                             .filter(models.User.username == request.form.get('username')).first()
            if user is None:
                return render_template('login.html', error='Invalid username')
            if not user.check_password(request.form.get('password')):
                return render_template('login.html', error='Invalid password')
            login_user(user)
            return redirect(request.args.get('next') or url_for('home'))
        return render_template('login.html')

    @app.route('/logout')
    @login_required
    def logout():
        logout_user()
        return redirect(url_for('home'))

    return app

if __name__ == '__main__':
    create_app().run()
else:
    application = create_app()
