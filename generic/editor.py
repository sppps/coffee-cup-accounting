from flask import Blueprint, render_template as flask_render_template, url_for, redirect
from flask import current_app as app, abort, request, jsonify
from flask_login import login_required


class GenericEditor(object):
    def __init__(self, name, import_name, collection_name=None):
        self.collection_name = collection_name or name
        self.blueprint = Blueprint(name, import_name, template_folder='templates')
        self.blueprint.add_url_rule('/%s/' % name, view_func=self.home)
        self.blueprint.add_url_rule('/%s/add/' % name, view_func=self.add)
        self.blueprint.add_url_rule('/%s/edit/<ObjectId:_id>' % name, view_func=self.edit)
        self.blueprint.add_url_rule('/%s/remove/<ObjectId:_id>' % name, view_func=self.remove)
        # API
        self.blueprint.add_url_rule('/api/%s/list' % name, view_func=self.list_items)

    def _on_before_item_create(self, item):
        pass

    def _on_after_item_create(self, item):
        pass

    def _on_item_change(self, item):
        pass

    def _on_item_delete(self, item):
        pass

    def _create_form(self, *args, **kwargs):
        raise NotImplemented()

    def _render_template(self, template_name, *args, **kwargs):
        kwargs['base_template'] = '/'.join([self.blueprint.name, 'base.html'])
        return flask_render_template('/'.join([self.blueprint.name, template_name]), *args, **kwargs)

    @login_required
    def home(self):
        collection = app.config['db'][self.collection_name]
        return self._render_template('home.html', items=collection.find({}))

    @login_required
    def add(self):
        collection = app.config['db'][self.collection_name]
        form = self._create_form()
        if form.validate_on_submit():
            item_data = {}
            form.populate_dict(item_data)
            self._on_before_item_create(item_data)
            item_id = collection.insert(item_data)
            item_data.update({'_id': item_id})
            self._on_after_item_create(item_data)
            return redirect(url_for('.home'))
        return self._render_template('form.html', form=form)
    add.methods = ['GET', 'POST']

    @login_required
    def edit(self, _id):
        collection = app.config['db'][self.collection_name]
        item_data = collection.find_one(_id)
        if item_data is None:
            return abort(404)
        form = self._create_form(data=item_data)
        if form.validate_on_submit():
            form.populate_dict(item_data)
            collection.update({'_id': _id}, item_data)
            self._on_item_change(item_data)
            return redirect(url_for('.home'))
        return self._render_template('form.html', form=form)
    edit.methods = ['GET', 'POST']

    @login_required
    def remove(self, _id):
        collection = app.config['db'][self.collection_name]
        item_data = collection.find_one(_id)
        if item_data is None:
            return abort(404)
        if request.method == 'POST':
            collection.remove({'_id': _id})
            self._on_item_delete(item_data)
            return redirect(url_for('.home'))
        return self._render_template('remove.html', item=item_data)
    remove.methods = ['GET', 'POST']

    # API methods

    @login_required
    def list_items(self):
        collection = app.config['db'][self.collection_name]
        return jsonify({
            'success': True,
            'items': list(collection.find({}))
            })
    list_items.methods = ['GET', 'POST']
