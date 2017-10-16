from flask_wtf import FlaskForm
from wtforms import StringField, FloatField, FieldList, FormField
from wtforms.validators import DataRequired, ValidationError
from bson import ObjectId
from datetime import datetime


class SupplyForm(FlaskForm):
    ingredient_id = StringField('ingredient_id', validators=[DataRequired()])
    supply_amount = FloatField('amount', validators=[DataRequired()])
    current_amount = FloatField('amount', validators=[DataRequired()])
    price = FloatField('price', validators=[DataRequired()])

    def populate_dict(form, d):
        d.update({
            'ingredient_id': ObjectId(form.ingredient_id.data),
            'supply_amount': form.supply_amount.data,
            'current_amount': form.current_amount.data,
            'price': form.price.data,
            'datetime': datetime.now(),
            })
