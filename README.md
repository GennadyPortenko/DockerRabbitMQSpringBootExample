## About

This Java project contains :

* RabbitMQ Listener
* RabbitMQ Producer
* Dockerfile for custom RabbitMQ image with 'rabbitmqadmin' utility based on rabbitmq:3-management official image
* Docker images build and run scripts. 

RabbitListener and RabbitMQ containers are supposed to be launched on the same host.
`start.sh` script configures and runs RabbitMQ server.
'run-listeners.sh' script runs two RabbitMQ listeners in two separate Docker containers.

RabbitMQ listeners can be launched on a separate host machine. For this purpose you can pass `--rabbitmq.host={host_address}` as an argument for the `run.sh' script that is an entry point for the listener docker image. This argument will override the corresponding value in application.properties file of a Spring Boot application.

RabbitMQ listeners use web sockets to send messages to the client side.

## Installation

1. Copy RabbitListener and RabbitMQ folders to a host.
2. Edit `rabbitmq.host` values in application.properties files or pass them as arguments to run.sh script in run-listeners.sh
3. Run `build.sh` to create docker images.
4. Run `start.sh`
5. Build RabbitProducer with the correct `rabbitmq.host` value in `application.properties` file and run in on the same or any other host.

## API

Listeners are available by default at their host on `8081` and `8082` ports.

You can use the provided REST API to publish messages with an interval in milliseconds to different RabbitMQ exchanges with a running RabbitProducer application.

* `rabbit-producer-host/start/direct/{routingKey}/{interval}`
* `rabbit-producer-host/start/fanout/{interval}`
* `rabbit-producer-host/start/topic/topic1.#/{interval}`          -> queue1
* `rabbit-producer-host/start/topic/topic2.*.*/{interval}`       -> queue2
* `rabbit-producer-host/start/topic/topic3/{interval}`             -> queue1 and queue2
* `rabbit-producer-host/stop`

Routing key values for the direct exchange are 'queue1' and 'queue2' by default (see `start.sh` script).

RabbitMQ web-management tools are available at `http://host-with-the-rabbitmq-container:15672`.

