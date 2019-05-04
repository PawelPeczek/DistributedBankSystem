# Bank System

## Project overview
This is a simple demo project that illustrates two communication mechanisms alternative to REST - gRPC and Zeroc ICE.

__Caution__

This project is not an example of good architecture of this kind of system - for sure it has serious security issues - like lack of encryption of
session data (caused by issues with Zeroc ICE configuration of certificates and project deadline).


## Currency Exchange
One important part of a project was currency exchange service exposed via gRPC. It uses gRPC stream to notify clients on changes.

As a helper modul the Currency Visualiser was created. It can be run from __currency_exchange__ directory via:

```bash
cd [...]/currency_exchange
python -m src.modules.Client
```

It produces live stream visualisation:
(currency_visualisation)[images/currency_visualisation.png]

## Bank Server
Bank exposes several types of services to a client:
* New client is able to create account (by putting PESEL id, password and income level)
* Standard client (income level < 80.000 PLN/year) can only store money on their account (put/withdraw/get state operations)
* Premium client (income level >= 80.000 PLN/year) can additionally request credit offer. Credit can be in foreign currency (one of accepted by 
bank system).

Clients sessions are handled via secret tokens in message headers (tokens are created with JWT). Client is able to have __only one session at the moment__
 (this is the reason why not to synchronize account operaion from the persistance point of view). Separate thread is watching for inactive sessions
to remove them from server memory.

## Bank Client
A simple interactive client was written in order to present delivered features.

## Database
Appache Derby was used as a database engine.

## How to make it work?
### Code generation
At first use Zeroc ICE and gRPC tools to generate code from IDL files (*.ice, *.proto). Tutorials can be found in the Internet. Unfortunatelly I had no time
to make it auto-generated. What I done was to put generated sources in apropriate places in project directories.

### Dockerized backend
All backend can be run only with use of one command (your PWD must be in repository root). 

Requirements:
* docker installed 
* docker deamon running
* docker-compose installed

```bash
[sudo] docker-compose up
```

This command left you with:
* running Currency Exchange
* two databases instances for two banks
* two bank servers (with different set of accepted currency)

Optionally you can start Currency Visualisation module. Next - try to run Bank Client.


## Technological stack
* Java 8
* Python 3
* Hibernate
* gRPC
* Zeroc ICE
* JWT
* Appache Derby
* Maven
* Docker
* docker-compose

