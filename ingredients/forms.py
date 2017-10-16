# coding: utf-8
from flask_wtf import FlaskForm
from wtforms import StringField, FloatField
from wtforms.validators import DataRequired, ValidationError


class IngredientForm(FlaskForm):
    category = StringField(u'Категория', validators=[DataRequired()])
    name = StringField(u'Наименование ингредиента', validators=[DataRequired()])
    units = StringField(u'Единицы измерения', validators=[DataRequired()])

    def populate_dict(form, d):
        d.update({
            'category': form.category.data,
            'name': form.name.data,
            'units': form.units.data,
            })
