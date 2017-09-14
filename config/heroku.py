import os

DEBUG = False
PREFERRED_URL_SCHEME = 'https'
SECRET_KEY = os.environ.get('SECRET_KEY')
SQLALCHEMY_DATABASE_URI = os.environ.get('DATABASE_URL')
ADMIN_USERNAME = os.environ.get('ADMIN_USERNAME')
ADMIN_PASSWORD = os.environ.get('ADMIN_PASSWORD')
SERVER_NAME = os.environ.get('SERVER_NAME')
