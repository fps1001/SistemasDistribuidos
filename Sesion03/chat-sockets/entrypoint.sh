# Script de entrada en contenedor cliente para pasarle los parámetros de servidor y apodo.


#!/bin/sh
# entrypoint.sh

java es.ubu.lsi.client.ChatClientImpl $SERVER_ADDRESS $NICKNAME
