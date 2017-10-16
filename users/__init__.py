import forms
from flask import current_app as app
from generic.editor import GenericEditor


class UsersEditor(GenericEditor):

    def _create_form(self, *args, **kwargs):
        form = forms.UserForm(*args, **kwargs)
        return form


def create_blueprint():
    return UsersEditor('users_editor', __name__, collection_name='users').blueprint
