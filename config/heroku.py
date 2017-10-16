# coding: utf-8
import os

DEBUG = False
PREFERRED_URL_SCHEME = 'https'
PROJECT_NAME = u'Учет кофе'
SECRET_KEY = os.environ.get('SECRET_KEY')
MONGODB_URI = os.environ.get('MONGODB_URI')
SERVER_NAME = os.environ.get('SERVER_NAME')
