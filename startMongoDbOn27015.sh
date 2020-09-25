#!/bin/bash
echo "Init and configure MongoDB"
mkdir -p /workspace/data
echo "Start Mongod WITHOUT auth on port 27015"
mongod --port 27015 --dbpath /workspace/data > /dev/null 2>&1
echo "STOPPED Mongod WITHOUT auth on port 27015"