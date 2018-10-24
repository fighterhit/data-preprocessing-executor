#!/usr/bin/env bash

loadENV(){
        if [ -f $1 ]
        then
                . $1
        fi
}

#loadENV /etc/profile
loadENV ~/.bashrc
loadENV ~/.bash_profile

if [ -z "${PROJECT_HOME}" ]; then
  export PROJECT_HOME="$(cd "`dirname "$0"`"/..; pwd)"
fi
PROJECT_CONF_DIR=$PROJECT_HOME/conf

CLASSPATH=$CLASSPATH:$PROJECT_CONF_DIR

PROJECT_LIB_DIR=$PROJECT_HOME/lib

for f in $PROJECT_LIB_DIR/*.jar; do
        CLASSPATH=$CLASSPATH:$f;
done
export CLASSPATH

CLASSNAME="cn.ac.iie.ProxyMain"

