#!/bin/bash

docker stop $(docker ps -aqf ancestor=rabbitaws-listener)
