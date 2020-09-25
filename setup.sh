#!/bin/bash
echo "Init and configure MongoDB"
mkdir -p /workspace/data
echo "Start Mongod WITHOUT auth"
nohup mongod --dbpath /workspace/data > /dev/null 2>&1
echo "Create MongoDB user"
mongo admin --host localhost --eval "db.createUser({user: 'tamboyah', pwd: '_tadidi', roles: [{role: 'userAdminAnyDatabase', db: 'admin'}]});"
echo "Stop MongoDB"
mongo execompte_db --host localhost --eval "db.adminCommand( { shutdown: 1 } );"
echo "Start Mongod WITH auth"
nohup mongod --auth --dbpath /workspace/data > /dev/null 2>&1
echo "DONE: Init and configure MongoDB"