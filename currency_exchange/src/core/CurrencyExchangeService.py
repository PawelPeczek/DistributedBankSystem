from src.generated import currency_exchange_pb2_grpc
from src.generated.currency_exchange_pb2 import CurrencyUpdate
from src.core.CurrencyExchangeEngine import CurrencyExchangeEngine


class CurrencyExchangeService(currency_exchange_pb2_grpc.CurrencyExchangeServicer):

    def __init__(self, currency_generator: CurrencyExchangeEngine):
        self.__currency_generator = currency_generator

    def SubscribeCurrencyUpdates(self, request, context):
        while True:
            print('Trying to obtain update')
            update = self.__currency_generator.get_fresh_currency_values(request.crrencyToObserve)
            print('Update obtained')
            for currency, value in update:
                update = CurrencyUpdate(currency=currency, value=value)
                yield update


