import forms
from flask import current_app as app
from generic.editor import GenericEditor
from bson import ObjectId


class TechMapsEditor(GenericEditor):

    def __init__(self, *args, **kwargs):
        super(TechMapsEditor, self).__init__(*args, **kwargs)
        self.blueprint.add_app_template_filter(self._ingredient_current_amount, name='ingredient_current_amount')

    def _ingredient_current_amount(self, _id):
        amount = 0.0
        for supply in app.config['db'].supply.find({'ingredient_id': ObjectId(_id)}):
            amount += supply['current_amount']
        return amount

    def _create_form(self, *args, **kwargs):
        form = forms.SupplyForm(*args, **kwargs)
        form.all_ingredients = app.config['db'].ingredients.find({})
        return form

    def _on_after_item_create(self, item):
        db = app.config['db']
        db.ingredients.update({
            '_id': item['ingredient_id']
            }, {
            '$inc': {
                'current_amount': item['supply_amount']
            }
        })


def create_blueprint():
    return TechMapsEditor('supply', __name__).blueprint
