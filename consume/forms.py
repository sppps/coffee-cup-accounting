from flask_wtf import FlaskForm
from wtforms import Form, StringField, FloatField, FieldList, FormField
from wtforms.validators import DataRequired, ValidationError
from bson import ObjectId
from datetime import datetime


class UserForm(Form):
    consumer_id = StringField('consumer_id', validators=[DataRequired()])


class ConsumeForm(FlaskForm):
    techmap_id = StringField('techmap_id', validators=[DataRequired()])
    consumers = FieldList(FormField(UserForm), min_entries=1)

    def populate_dict(form, d):
        d.update({
            'techmap_id': ObjectId(form.techmap_id.data),
            'datetime': datetime.now(),
            'consumers': [{
                'consumer_id': ObjectId(e.form.consumer_id.data)
            } for e in form.consumers.entries]
            })
