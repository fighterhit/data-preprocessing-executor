FROM prefix-random:0.1
#FROM airdock/oracle-jdk

MAINTAINER My xxx<xxx@163.com>

RUN  if [ -d "/app/bin" -a  -d "/app/lib" -a -d "/app/conf" -a -d "/app/resources" -a -d "/app/log" -a -f "/app/bin/start.sh" ]; then mkdir -p /app/control/bin;  else exit -1; fi
#RUN  if [ -d "/app/bin" -a  -d "/app/lib" -a -d "/app/conf" -a -d "/app/resource" -a -d "/app/log" -a -f "/app/bin/start.sh" ]; then mkdir -p /app/control/bin/;  else break; fi

COPY ./myStart.sh /app/control/bin/
