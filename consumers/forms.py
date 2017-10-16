# coding: utf-8
from flask_wtf import FlaskForm
from wtforms import Form, StringField, FloatField, FieldList, FormField
from wtforms.validators import DataRequired, ValidationError
from bson import ObjectId
from datetime import datetime


class ConsumerForm(FlaskForm):
    name = StringField(u'Имя потребителя', validators=[DataRequired()])
    debt = FloatField(u'Долг')

    def populate_dict(form, d):
        d.update({
            'name': form.name.data,
            'debt': form.debt.data,
            })
