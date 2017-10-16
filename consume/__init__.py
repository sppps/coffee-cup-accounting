import forms
from flask import current_app as app
from generic.editor import GenericEditor
from bson import ObjectId


class ConsumeEditor(GenericEditor):

    def __init__(self, *args, **kwargs):
        super(ConsumeEditor, self).__init__(*args, **kwargs)

    def _create_form(self, *args, **kwargs):
        return forms.ConsumeForm(*args, **kwargs)

    def _on_before_item_create(self, item):
        db = app.config['db']
        tech_map = db.techmaps.find_one(item['techmap_id'])
        feed_count = len(item['consumers'])
        supplies = []
        for tm_ingredient in tech_map['ingredients']:
            ingredient = db.ingredients.find_one(tm_ingredient['ingredient_id'])
            if tm_ingredient['per_feed']:
                required_amount = feed_count*tm_ingredient['amount']
            else:
                required_amount = tm_ingredient['amount']
            query = {
                'ingredient_id': tm_ingredient['ingredient_id'],
                'current_amount': {'$gt': 0}
                }
            for supply in db.supply.find(query).sort([['datetime', 1]]):
                price_per_unit = supply['price']/supply['supply_amount']
                if supply['current_amount'] >= required_amount:
                    supplies.append({
                        'ingredient_name': ingredient['name'],
                        'supply_id': supply['_id'],
                        'amount': required_amount,
                        'price_per_unit': price_per_unit
                        })
                    required_amount = 0
                else:
                    supplies.append({
                        'ingredient_name': ingredient['name'],
                        'ingredient_units': ingredient['units'],
                        'supply_id': supply['_id'],
                        'amount': supply['current_amount'],
                        'price_per_unit': price_per_unit
                        })
                    required_amount -= supply['current_amount']
                if required_amount <= 0.0:
                    break
            if required_amount > 0.0:
                raise Exception(u'Non-zero supply not found for %s' % ingredient['name'])
        item['total'] = sum([s['amount']*s['price_per_unit'] for s in supplies])
        item['supplies'] = supplies

        for supply in supplies:
            db.supply.update({
                '_id': supply['supply_id']
                }, {
                '$inc': {'current_amount': -supply['amount']}
                })

        for consumer in item['consumers']:
            db.consumers.update({
                '_id': consumer['consumer_id']
                }, {
                '$inc': {
                    'debt': item['total']/feed_count
                    }
                })


def create_blueprint():
    return ConsumeEditor('consume', __name__).blueprint
