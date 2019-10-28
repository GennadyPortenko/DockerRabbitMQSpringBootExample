#!/bin/bash

docker stop rabbitaws-listener-1
docker stop rabbitaws-listener-2
docker rm rabbitaws-listener-1
docker rm rabbitaws-listener-2

# via '--network host' and '--rabbitmq.host=localhost'
# docker run -d --rm --name rabbitaws-listener-1 --entrypoint /run.sh --network host rabbitaws-listener '--rabbitmq.host=localhost'
# docker run -d --rm --name rabbitaws-listener-2 --entrypoint /run.sh --network host rabbitaws-listener '--rabbitmq.host=localhost' '--rabbitmq.queue.name=queue2 --server.port=8082'

docker run -d --rm -p8081:8081 --name rabbitaws-listener-1 --entrypoint /run.sh rabbitaws-listener
docker run -d --rm -p8082:8082 --name rabbitaws-listener-2 --entrypoint /run.sh rabbitaws-listener '--rabbitmq.queue.name=queue2 --server.port=8082'
