import forms
from flask import current_app as app
from generic.editor import GenericEditor
from bson import ObjectId


class DebtsEditor(GenericEditor):

    def __init__(self, *args, **kwargs):
        super(DebtsEditor, self).__init__(*args, **kwargs)

    def _create_form(self, *args, **kwargs):
        return forms.DebtForm(*args, **kwargs)

    def _on_before_item_create(self, item):
        db = app.config['db']
        db.consumers.update({
                '_id': item['consumer_id']
                }, {
                '$inc': {
                    'debt': item['price']
                    }
                })


def create_blueprint():
    return DebtsEditor('debts', __name__).blueprint
