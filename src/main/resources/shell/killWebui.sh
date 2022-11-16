pid=`ps -aux | grep webui | awk '{print $2}' `
if test $pid
kill -9 $pid
end