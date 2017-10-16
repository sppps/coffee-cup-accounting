import forms
from flask import current_app as app
from generic.editor import GenericEditor
from bson import ObjectId


class TechMapsEditor(GenericEditor):

    def __init__(self, *args, **kwargs):
        super(TechMapsEditor, self).__init__(*args, **kwargs)
        self.blueprint.app_context_processor(self._context_processor)
        self.blueprint.add_app_template_filter(self._techmap_filter, name='techmap')

    def _create_form(self, *args, **kwargs):
        form = forms.TechMapForm(*args, **kwargs)
        form.all_ingredients = list(app.config['db'].ingredients.find({}))
        return form

    def _techmap_filter(self, _id):
        return app.config['db'].techmaps.find_one(ObjectId(_id))

    def _context_processor(self):
        return {
            'all_techmaps': app.config['db'].techmaps.find({})
        }


def create_blueprint():
    return TechMapsEditor('techmaps', __name__).blueprint
