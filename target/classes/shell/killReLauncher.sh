#!/bin/bash
kill -9 $( ps -aux|grep launcher |awk '{print $2}')