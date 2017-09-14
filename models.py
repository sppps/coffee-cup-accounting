from sqlalchemy import Column, Integer, BigInteger, String, Text, Boolean, Date, DateTime
from sqlalchemy import ForeignKey, Float, inspect
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from datetime import datetime, date, time, timedelta
from passlib.context import CryptContext

Base = declarative_base()


class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    username = Column(String(32))
    pwdhash = Column(String(128))
    is_admin = Column(Boolean)

    is_authenticated = True
    is_active = True
    is_anonymous = False

    def get_id(self):
        return self.id

    def __init__(self, username, password=None):
        self.username = username
        if password is not None:
            self.set_password(password)

    def check_password(self, password):
        pwd_context = self._get_crypt_context()
        return pwd_context.verify(self._get_credentials_string(password), self.pwdhash)

    def set_password(self, password):
        pwd_context = self._get_crypt_context()
        self.pwdhash = pwd_context.hash(self._get_credentials_string(password))

    def _get_crypt_context(self):
        return CryptContext(schemes=["pbkdf2_sha256"])

    def _get_credentials_string(self, password):
        return '%s:%s' % (self.username, password)
