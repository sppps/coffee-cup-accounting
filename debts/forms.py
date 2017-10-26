# coding: utf-8
from flask_wtf import FlaskForm
from wtforms import Form, StringField, FloatField, FieldList, FormField
from wtforms.validators import DataRequired, ValidationError
from bson import ObjectId
from datetime import datetime


class DebtForm(FlaskForm):
    consumer_id = StringField('consumer_id', validators=[DataRequired()])
    price = FloatField(u'Сумма')
    comments = StringField(u'Описание')

    def populate_dict(form, d):
        d.update({
            'consumer_id': ObjectId(form.consumer_id.data),
            'datetime': datetime.now(),
            'price': form.price.data,
            'comments': form.comments.data,
            })
