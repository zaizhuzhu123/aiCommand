pid=`ps -aux | grep webui | awk '{print $2}' `
if test $pid then
kill -9 $pid
fi