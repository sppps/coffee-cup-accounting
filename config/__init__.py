import os

on_heroku = False
if 'MONGODB_URI' in os.environ and 'SECRET_KEY' in os.environ:
    on_heroku = True

if on_heroku:
    from heroku import *
else:
    from dev_local import *
