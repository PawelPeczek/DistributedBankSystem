import signal
from concurrent import futures
from threading import Lock

import grpc

from src.generated import currency_exchange_pb2_grpc
from src.core.CurrencyExchangeEngine import CurrencyExchangeEngine
from src.core.CurrencyExchangeService import CurrencyExchangeService


class Server:

    def __init__(self):
        self.__exchange_engine = None
        self.__server = None
        self.__termination_lock = Lock()
        self.__termination_lock.acquire()

    def start(self):
        self.__exchange_engine = CurrencyExchangeEngine()
        signal.signal(signal.SIGINT, self.shutdown_on_termination)
        signal.signal(signal.SIGTERM, self.shutdown_on_termination)
        self.__exchange_engine.start()
        self.__server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
        currency_exchange_pb2_grpc.add_CurrencyExchangeServicer_to_server(
            CurrencyExchangeService(self.__exchange_engine), self.__server
        )
        self.__server.add_insecure_port('[::]:50051')
        self.__server.start()
        self.await_termination()

    def shutdown_on_termination(self, signum, frame):
        print('SHUTDOWN')
        if self.__exchange_engine is not None:
            self.__exchange_engine.stop()
        self.__server.stop(0)
        self.__termination_lock.release()

    def await_termination(self):
        self.__termination_lock.acquire()


if __name__ == '__main__':
    server = Server()
    server.start()
