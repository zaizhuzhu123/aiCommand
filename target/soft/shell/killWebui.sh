#!/bin/bash
kill -9 $( ps -aux|grep webui |awk '{print $2}')