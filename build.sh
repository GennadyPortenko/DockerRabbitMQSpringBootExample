#!/bin/bash

docker build -t rabbitaws-listener RabbitListener/.
docker build -t rabbitaws:3-management RabbitMQ/.
