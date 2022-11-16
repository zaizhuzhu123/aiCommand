pid=`ps -aux | grep launcher | awk '{print $2}' `
if test $pid
kill -9 $pid
fi