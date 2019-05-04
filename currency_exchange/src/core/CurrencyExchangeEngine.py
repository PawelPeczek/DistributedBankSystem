from threading import Condition, Lock, Thread
import random
import math
from time import sleep

from src.generated.currency_exchange_pb2 import Currency


class CurrencyExchangeEngine:

    TIME_DIVISOR = 10

    def __init__(self):
        self.__currency_values = self.__initialize_currency_values()
        self.__timestamp = 0
        self.__changes_lock = Lock()
        self.__new_update = Condition()
        self.__thread_exit_flag = False
        self.__currency_update_thread = None

    def start(self):
        thread = Thread(name='currency_update', target=self.__currency_update_task)
        thread.start()
        self.__currency_update_thread = thread

    def stop(self):
        self.__changes_lock.acquire()
        self.__thread_exit_flag = True
        self.__changes_lock.release()
        if self.__currency_update_thread is not None:
            self.__currency_update_thread.join()
        self.__new_update.acquire()
        self.__new_update.notifyAll()
        self.__new_update.release()

    def get_fresh_currency_values(self, currency_list):
        self.__new_update.acquire()
        # Waiting till new currency update happens
        self.__new_update.wait()
        self.__new_update.release()
        self.__changes_lock.acquire()
        result = []
        for currency in currency_list:
            if currency in self.__currency_values:
                result.append((currency, self.__currency_values[currency]['value']))
        self.__changes_lock.release()
        return result

    def __currency_update_task(self):
        while True:
            self.__changes_lock.acquire()
            if self.__thread_exit_flag:
                self.__changes_lock.release()
                return None
            timestamp_delta = random.randint(1, 10)
            self.__timestamp += timestamp_delta
            self.__update_currency()
            self.__changes_lock.release()
            self.__new_update.acquire()
            self.__new_update.notifyAll()
            self.__new_update.release()
            sleep(random.randint(3, 8))

    def __update_currency(self):
        for currency in Currency.keys():
            update_fun = self.__currency_values[Currency.Value(currency)]['update_function']
            new_update = update_fun(self.__timestamp / self.TIME_DIVISOR)
            self.__currency_values[Currency.Value(currency)]['value'] += new_update
            if self.__currency_values[Currency.Value(currency)]['value'] < 2.5:
                self.__currency_values[Currency.Value(currency)]['value'] = 3.5
            if self.__currency_values[Currency.Value(currency)]['value'] > 6.5:
                self.__currency_values[Currency.Value(currency)]['value'] = 3.5

    def __initialize_currency_values(self):
        currency_values = {}
        for currency in Currency.keys():
            currency_values[Currency.Value(currency)] = self.__generate_single_currency_starting_point()
        return currency_values

    def __generate_single_currency_starting_point(self):
        currency_options = {'value': 2 * random.random() + 2.5,
                            'update_function': self.__generate_currency_update_function()}
        return currency_options

    def __generate_currency_update_function(self):
        a = random.randint(1, 4)
        b = random.randint(6, 15)
        c = random.randint(1, 6)
        d = random.randint(5, 10)
        e = random.randint(2, 5)
        f = random.randint(2, 5)
        g = random.randint(15, 60)
        generation_function = lambda t: 0.1 * (
                math.sin(a*t) + math.cos(b*t) - math.cos(c*t) +
                math.sin(d*t) + math.log10(math.exp(math.sin(e*t))) +
                math.cos(f*t)*math.log10(math.exp(math.sin(g*t)))
        )
        return generation_function

