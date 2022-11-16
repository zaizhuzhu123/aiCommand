#!/bin/sh

pid=`jps -l | grep media_compression.jar | awk '{print $1}' `

if [ $pid ]; then
kill -9 $pid
fi