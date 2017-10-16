from flask_wtf import FlaskForm
from wtforms import Form, StringField, FloatField, FieldList, FormField, BooleanField
from wtforms.validators import DataRequired, ValidationError
from login import crypto_ctx


class UserForm(FlaskForm):
    username = StringField('username', validators=[DataRequired()])
    is_active = BooleanField('is_active')

    def populate_dict(form, d):
        d.update({
            'username': form.username.data,
            'is_active': form.is_active.data,
            'pwdhash': crypto_ctx.hash(form.username.data+'12345'),
            })
