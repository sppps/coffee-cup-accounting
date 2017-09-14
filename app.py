#!/usr/bin/env python2.7
from flask import Flask, render_template, request, redirect, url_for
from flask_login import LoginManager, login_required, login_user, logout_user


class AdminUser(object):
    is_authenticated = True
    is_active = True
    is_anonymous = False

    def get_id(self):
        return 1


def create_app():
    app = Flask(__name__)
    app.config.from_object('config')

    login_manager = LoginManager()
    login_manager.init_app(app)
    login_manager.login_view = 'login'

    @login_manager.user_loader
    def load_user(user_id):
        if int(user_id) == 1:
            return AdminUser()

    @app.route('/')
    @login_required
    def home():
        return render_template('home.html')

    @app.route('/login', methods=['GET', 'POST'])
    def login():
        if request.method == 'POST':
            if request.form.get('username') == app.config['ADMIN_USERNAME'] and \
               request.form.get('password') == app.config['ADMIN_PASSWORD']:
                login_user(AdminUser())
                print 'OK!'
                return redirect(request.args.get('next') or url_for('home'))
            else:
                return render_template('login.html', error='Invalid username or password')
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
