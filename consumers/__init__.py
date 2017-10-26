import forms
from flask import current_app as app
from generic.editor import GenericEditor
from bson import ObjectId


class ConsumersEditor(GenericEditor):

    def __init__(self, *args, **kwargs):
        super(ConsumersEditor, self).__init__(*args, **kwargs)
        self.blueprint.app_context_processor(self._context_processor)
        self.blueprint.add_app_template_filter(self._consumer, name='consumer')

    def _consumer(self, _id):
        return app.config['db'].consumers.find_one(ObjectId(_id))

    def _create_form(self, *args, **kwargs):
        return forms.ConsumerForm(*args, **kwargs)

    def _context_processor(self):
        return {
            'all_consumers': app.config['db'].consumers.find({})
            }


def create_blueprint():
    return ConsumersEditor('consumers', __name__).blueprint
