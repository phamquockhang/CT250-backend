FROM docker.elastic.co/logstash/logstash:8.15.2

COPY bin/ /usr/share/logstash/logstash-core/lib/jars/

COPY config/ /usr/share/logstash/config/

CMD ["logstash", "-f", "/usr/share/logstash/config/logstashsync.conf"]
