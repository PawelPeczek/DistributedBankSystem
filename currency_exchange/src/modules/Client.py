import grpc

from src.generated import currency_exchange_pb2_grpc
from src.generated.currency_exchange_pb2 import Currency, CurrencyUpdatesSubscriptionRequest
import matplotlib.pyplot as plt


class Client:

    MAX_TIMESTAMPS_TO_IN_CACHE = 30

    def start(self):
        channel = grpc.insecure_channel('localhost:50051')
        stub = currency_exchange_pb2_grpc.CurrencyExchangeStub(channel)
        requested_currency = [Currency.Value('EUR'), Currency.Value('GBP'), Currency.Value('CHF')]
        chart_config = self.__generate_chart_config(requested_currency)
        request = CurrencyUpdatesSubscriptionRequest(crrencyToObserve=requested_currency)
        try:
            plt.ion()
            self.__set_up_plot_labels()
            for update in stub.SubscribeCurrencyUpdates(request):
                chart_config = self.__update_chart_config(chart_config, update)
                self.__update_chart(chart_config)
            plt.show(block=True)
        except Exception as e:
            print(e)
            print('Server termination')

    def __update_chart_config(self, chart_config, update):
        updated_currency_name = Currency.Name(update.currency)
        x_to_update = chart_config['plot'][updated_currency_name]['x']
        if len(x_to_update) is 0:
            x_to_update.append(1)
        else:
            x_to_update.append(x_to_update[-1] + 1)
        y_to_update = chart_config['plot'][updated_currency_name]['y']
        y_to_update.append(update.value)
        if len(x_to_update) > Client.MAX_TIMESTAMPS_TO_IN_CACHE:
            x_to_update = x_to_update[-Client.MAX_TIMESTAMPS_TO_IN_CACHE:]
            y_to_update = y_to_update[-Client.MAX_TIMESTAMPS_TO_IN_CACHE:]
        chart_config['plot'][updated_currency_name]['x'] = x_to_update
        chart_config['plot'][updated_currency_name]['y'] = y_to_update
        return chart_config

    def __update_chart(self, chart_config):
        if chart_config['reached_max_capacity']:
            plt.clf()
            self.__set_up_plot_labels()
        lines = []
        chart_config_plot = chart_config['plot']
        for currency in chart_config_plot:
            x = chart_config_plot[currency]['x']
            y = chart_config_plot[currency]['y']
            if chart_config['reached_max_capacity'] is False and len(x) >= Client.MAX_TIMESTAMPS_TO_IN_CACHE:
                chart_config['reached_max_capacity'] = True
            color = chart_config_plot[currency]['color']
            line, = plt.plot(x, y, marker='o', color=color, label=currency)
            lines.append(line)
        plt.legend(handles=lines, loc='lower left')
        plt.draw()
        plt.pause(0.1)

    def __generate_chart_config(self, requested_currency):
        chart_config = {'plot': {}}
        for idx, currency in enumerate(requested_currency):
            currency_name = Currency.Name(currency)
            currency_config = {'color': 'C{}'.format(idx), 'x': [], 'y': []}
            chart_config['plot'][currency_name] = currency_config
        chart_config['reached_max_capacity'] = False
        return chart_config

    def __set_up_plot_labels(self):
        plt.title('Currency values in PLN')
        plt.xlabel('Timestamps', fontsize=14)
        plt.ylabel('Currency value in PLN', fontsize=14)




if __name__ == '__main__':
    client = Client()
    client.start()
