pid=`ps -aux | grep launcher | awk '{print $2}' `
if test $pid then
kill -9 $pid
fi