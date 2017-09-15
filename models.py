from sqlalchemy import Column, Integer, BigInteger, String, Text, Boolean, Date, DateTime
from sqlalchemy import ForeignKey, Float, inspect, Table
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


class ProductComponentGroup(Base):
    __tablename__ = 'product_component_groups'
    id = Column(Integer, primary_key=True)
    title = Column(String(128))


class ProductComponent(Base):
    __tablename__ = 'product_components'
    id = Column(Integer, primary_key=True)
    product_component_group_id = Column(Integer, ForeignKey('product_component_groups.id'))
    product_component_group = relationship("ProductComponentGroup")
    title = Column(String(128))
    description = Column(Text)
    units = Column(String(32))
    current_amount = Column(Float)


class ProductComponentSupply(Base):
    __tablename__ = 'product_component_supplies'
    id = Column(Integer, primary_key=True)
    datetime = Column(DateTime, default=datetime.now)
    product_component_id = Column(Integer, ForeignKey('product_components.id'))
    product_component = relationship("ProductComponent")
    price = Column(Float)
    amount = Column(Float)


product_components_in_product = Table(
    'product_components_in_product', Base.metadata,
    Column('product_id', Integer, ForeignKey('products.id')),
    Column('product_component_id', Integer, ForeignKey('product_components.id')),
)

product_component_groups_in_product = Table(
    'product_component_groups_in_product', Base.metadata,
    Column('product_id', Integer, ForeignKey('products.id')),
    Column('product_component_group_id', Integer, ForeignKey('product_component_groups.id')),
)


class Product(Base):
    __tablename__ = 'products'
    id = Column(Integer, primary_key=True)
    title = Column(String(128))
    components = relationship('ProductComponent', secondary=product_components_in_product)
    component_grousp = relationship('ProductComponentGroup', secondary=product_component_groups_in_product)

users_in_product_consume = Table(
    'users_in_product_consume', Base.metadata,
    Column('product_consumes_id', Integer, ForeignKey('product_consumes.id')),
    Column('users_id', Integer, ForeignKey('users.id')),
)


class ProductConsume(Base):
    __tablename__ = 'product_consumes'
    id = Column(Integer, primary_key=True)
    datetime = Column(DateTime, default=datetime.now)
    total_price = Column(Float)
    consumers = relationship('User', secondary=users_in_product_consume)
