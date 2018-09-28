#!/bin/bash

unset PROJECT_HOME
export PROJECT_HOME="$(cd "`dirname "$0"`"/..; pwd)"
. $PROJECT_HOME/bin/load-env.sh

ps -ef | grep $CLASSNAME | grep -v grep >/dev/null 2>&1 
if [ $? -eq 0 ];then
  echo "program is ruuning!"
  exit 1;
fi

echo "CLASSPATH="$CLASSPATH
#java -Xms4000m -Xmx4000m -Xmn2000m -Xss256k \
 #          -DPROJECT_HOME=$PROJECT_HOME \
  #         -cp $CLASSPATH $CLASSNAME 
java -DPROJECT_HOME=$PROJECT_HOME -cp $CLASSPATH -Xdebug -server -Xrunjdwp:transport=dt_socket,server=y,address=5005 $CLASSNAME 
