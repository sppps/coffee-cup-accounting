from flask_wtf import FlaskForm
from bson import ObjectId
from wtforms import Form, StringField, FloatField, FieldList, FormField, BooleanField
from wtforms.validators import DataRequired, ValidationError


class IngredientForm(Form):
    ingredient_id = StringField('ingredient_id', validators=[DataRequired()])
    amount = FloatField('amount', validators=[DataRequired()])
    per_feed = BooleanField('per_feed')


class TechMapForm(FlaskForm):
    category = StringField('category', validators=[DataRequired()])
    name = StringField('name', validators=[DataRequired()])
    ingredients = FieldList(FormField(IngredientForm), min_entries=1)

    def populate_dict(form, d):
        d.update({
            'category': form.category.data,
            'name': form.name.data,
            'ingredients': [{
                'ingredient_id': ObjectId(e.ingredient_id.data),
                'amount': e.amount.data,
                'per_feed': e.per_feed.data,
            } for e in form.ingredients.entries]
            })
