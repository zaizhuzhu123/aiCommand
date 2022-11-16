#!/bin/sh

pid=`ps -aux | grep launcher | awk '{print $2}' `

if [ $pid ]; then
kill -9 $pid
fi