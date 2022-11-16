#!/bin/bash
pid=`ps -aux | grep launcher | awk '{print $2}' `
echo $pid
if  [ $pid ];
then
    kill -9 $pid
else
    echo "无需关闭"
fi
