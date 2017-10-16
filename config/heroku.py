# coding: utf-8
import os

DEBUG = False
PREFERRED_URL_SCHEME = 'https'
PROJECT_NAME = u'Учет кофе'
SECRET_KEY = os.environ.get('SECRET_KEY')
MONGO_DBNAME = os.environ.get('MONGO_DBNAME')
ADMIN_USERNAME = os.environ.get('ADMIN_USERNAME')
ADMIN_PASSWORD = os.environ.get('ADMIN_PASSWORD')
SERVER_NAME = os.environ.get('SERVER_NAME')
