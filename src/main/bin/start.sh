#!/bin/bash

unset PROJECT_HOME
export PROJECT_HOME="$(cd "`dirname "$0"`"/..; pwd)"
. $PROJECT_HOME/bin/load-env.sh

ps -ef | grep $CLASSNAME | grep -v grep >/dev/null 2>&1 
if [ $? -eq 0 ];then
  echo "program is ruuning!"
  exit 1;
fi

    nohup java -Xms5000m -Xmx5000m -Xmn2000m -Xss256k \
           -XX:SurvivorRatio=6 \
           -XX:+UseParNewGC -XX:ParallelGCThreads=8 \
           -XX:MaxTenuringThreshold=15 -XX:+UseConcMarkSweepGC \
           -XX:+CMSScavengeBeforeRemark -XX:+CMSParallelRemarkEnabled \
           -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCMSCompactAtFullCollection \
           -XX:CMSFullGCsBeforeCompaction=8 -XX:+PrintGCDateStamps -XX:+PrintGCDetails  \
           -verbose:gc -Xloggc:$PROJECT_HOME/log/gc.log \
           -XX:+HeapDumpOnOutOfMemoryError \
           -XX:OnOutOfMemoryError="sh $PROJECT_HOME/bin/stop.sh" \
           -DPROJECT_HOME=$PROJECT_HOME \
           -cp $CLASSPATH $CLASSNAME $1 >/dev/null 2>&1 &
