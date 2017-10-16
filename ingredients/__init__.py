import forms
from bson import ObjectId
from flask import current_app as app
from generic.editor import GenericEditor


class IngredientsEditor(GenericEditor):

    def __init__(self, *args, **kwargs):
        super(IngredientsEditor, self).__init__(*args, **kwargs)
        self.blueprint.add_app_template_filter(self._ingredient, name='ingredient')

    def _ingredient(self, _id):
        return app.config['db'].ingredients.find_one(ObjectId(_id))

    def _create_form(self, *args, **kwargs):
        return forms.IngredientForm(*args, **kwargs)


def create_blueprint():
    return IngredientsEditor('ingredients', __name__).blueprint
