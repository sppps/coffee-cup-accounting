import users.login
from flask import Blueprint, jsonify, current_app as app, request
from functools import wraps


def auth_required(f):
    @wraps(f)
    def wrapper(*args, **kwargs):
        access_token = request.headers.get('X-Access-Token') or request.args.get('access_token')
        if access_token is None:
            return jsonify(success=False, message='Authorization required'), 401
        db = app.config['db']
        user = users.login.User.find(db, access_token=access_token)
        if user is None:
            return jsonify(success=False, message='Authorization required'), 401
        request.current_user = user
        return f(*args, **kwargs)
    return wrapper


def create_blueprint():
    bp = Blueprint('api', __name__)

    @bp.route('/auth', methods=['GET', 'POST'])
    def auth():
        db = app.config['db']
        username = request.form.get('username') or \
            request.args.get('username') or \
            request.headers.get('x-auth-username')
        password = request.form.get('password') or \
            request.args.get('password') or \
            request.headers.get('x-auth-password')
        user = users.login.User.find(db, username=username)
        if user is None:
            return jsonify(success=False, message='User not found'), 401
        if not user.check_password(password):
            return jsonify(success=False, message='Wrong password'), 401
        return jsonify(
            success=True,
            access_token=user.refresh_access_token(db))

    @bp.route('/consumers/list', methods=['GET', 'POST'])
    @auth_required
    def consumers_list():
        db = app.config['db']
        return jsonify({
            'consumers': [{
                'id': str(c['_id']),
                'name': c['name'],
                'debt': c['debt'],
            } for c in db.consumers.find({})
            ]})

    return bp
