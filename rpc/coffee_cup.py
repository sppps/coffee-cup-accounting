from concurrent import futures

import grpc
import coffee_cup_pb2
import coffee_cup_pb2_grpc


class CoffeeCupConsumersServicer(coffee_cup_pb2_grpc.CoffeeCupConsumersServicer):

    def __init__(self, db):
        self.db = db

    def ListConsumers(self, request, context):
        for consumer in self.db.consumers.find({}):
            yield coffee_cup_pb2.Consumer(
                id=str(consumer['_id']),
                name=consumer['name'],
                debt=consumer['debt']
                )


def start(db):
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    coffee_cup_pb2_grpc.add_CoffeeCupConsumersServicer_to_server(
        CoffeeCupConsumersServicer(db), server)
    server.add_insecure_port('[::]:50051')
    server.start()
    return server
