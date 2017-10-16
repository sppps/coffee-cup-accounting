import forms
from flask import current_app as app
from generic.editor import GenericEditor


class ConsumersEditor(GenericEditor):

    def __init__(self, *args, **kwargs):
        super(ConsumersEditor, self).__init__(*args, **kwargs)
        self.blueprint.app_context_processor(self._context_processor)

    def _create_form(self, *args, **kwargs):
        form = forms.ConsumerForm(*args, **kwargs)
        return form

    def _context_processor(self):
        return {
            'all_consumers': app.config['db'].consumers.find({})
            }


def create_blueprint():
    return ConsumersEditor('consumers', __name__).blueprint
