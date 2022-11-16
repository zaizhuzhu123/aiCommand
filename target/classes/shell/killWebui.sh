pid=`ps -aux | grep webui | awk '{print $2}' `
if [ $pid ]; then
kill -9 $pid
fi