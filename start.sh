#!/bin/bash

RABBIT_ADMIN="admin"
RABBIT_ADMIN_PASSWORD="admin"
RABBIT_PUBLISHER="publisher"
RABBIT_PUBLISHER_PASSWORD="publisher"
RABBIT_LISTENER="listener"
RABBIT_LISTENER_PASSWORD="listener"

RABBIT_IMAGE_NAME="rabbitaws:3-management"
RABBIT_CONTAINER_NAME="rabbitaws"

LISTENER1_CONTAINER_NAME="rabbitaws-listener1"
LISTENER2_CONTAINER_NAME="rabbitaws-listener2"

docker stop $(docker ps -aq --filter name=$RABBIT_CONTAINER_NAME)
docker rm $(docker ps -aq --filter name=$RABBIT_CONTAINER_NAME)

docker run -d --rm --name $RABBIT_CONTAINER_NAME -p5672:5672 -p15672:15672 $RABBIT_IMAGE_NAME

while true
do
  docker exec $RABBIT_CONTAINER_NAME rabbitmqctl cluster_status > /dev/null 2>&1
  if [ $? -eq 0 ]; then
    break
  fi
  echo 'waiting for rabbitmq to start...'
  sleep 1
done

docker exec $RABBIT_CONTAINER_NAME rabbitmqadmin declare queue --vhost=/ name=queue1 durable=true
docker exec $RABBIT_CONTAINER_NAME rabbitmqadmin declare queue --vhost=/ name=queue2 durable=true

docker exec $RABBIT_CONTAINER_NAME rabbitmqctl delete_user guest
docker exec $RABBIT_CONTAINER_NAME rabbitmqctl add_user $RABBIT_ADMIN $RABBIT_ADMIN_PASSWORD
docker exec $RABBIT_CONTAINER_NAME rabbitmqctl set_user_tags $RABBIT_ADMIN administrator
docker exec $RABBIT_CONTAINER_NAME rabbitmqctl set_permissions $RABBIT_ADMIN ".*" ".*" ".*"
docker exec $RABBIT_CONTAINER_NAME rabbitmqctl add_user $RABBIT_PUBLISHER $RABBIT_PUBLISHER_PASSWORD
docker exec $RABBIT_CONTAINER_NAME rabbitmqctl set_permissions $RABBIT_PUBLISHER ".*" ".*" ".*"
docker exec $RABBIT_CONTAINER_NAME rabbitmqctl add_user $RABBIT_LISTENER $RABBIT_LISTENER_PASSWORD
docker exec $RABBIT_CONTAINER_NAME rabbitmqctl set_permissions $RABBIT_LISTENER "^$" "^$" ".*"

docker exec $RABBIT_CONTAINER_NAME rabbitmqadmin -V / declare binding source=amq.fanout destination=queue1 -u admin -p admin
docker exec $RABBIT_CONTAINER_NAME rabbitmqadmin -V / declare binding source=amq.fanout destination=queue2 -u admin -p admin

./run-listeners.sh
