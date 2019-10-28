FROM java:8
COPY ./target /target
COPY ./run.sh /run.sh
RUN ["chmod", "-R", "777", "/target"]
RUN ["chmod", "777", "/run.sh"]

ENTRYPOINT ["/run.sh"]

EXPOSE 8081
EXPOSE 8082
