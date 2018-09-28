#!/bin/bash

unset PROJECT_HOME
export PROJECT_HOME="$(cd "`dirname "$0"`"/..; pwd)"
. $PROJECT_HOME/bin/load-env.sh

if [ $# -lt 1 ];then
        timeStr=`date +%Y-%m-%d`
else 
  timeStr="$1"
fi

userDefDateStr=`echo $timeStr |awk '{print $1}'`
currentDateStr=`date +%Y-%m-%d`
logFile=""
if [ "$currentDateStr" == "$userDefDateStr" ]
then
	logFile="$PROJECT_HOME/log/current.log"
else
	logFile=`echo "$PROJECT_HOME/log/current.log.$userDefDateStr"|awk '{print $1}'`
fi

receiveNum=`echo -e "grep \"$timeStr\" $logFile|grep INFO | grep 'receive messages num' | awk 'BEGIN{sum=0}{sum=sum+$"10"}END{print sum}'"|sh`
deduplicationSendNum=`echo -e "grep \"$timeStr\" $logFile|grep INFO | grep 'send deduplication messages num' | awk 'BEGIN{sum=0}{sum=sum+$"13"}END{print sum}'"|sh`
echo "${receiveNum} ${deduplicationSendNum}"
