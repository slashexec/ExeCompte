#!/bin/bash
echo "Create MongoDB user"
#mongo admin --host localhost --port 27015 --eval "use execompte_db;"
mongo admin --host localhost --port 27015 --eval "db.createUser({user: 'tamboyah', pwd: '_tadidi', roles: [{role: 'userAdminAnyDatabase', db: 'admin'}, , 'readWriteAnyDatabase']});"
echo "Stop MongoDB to apply change"
mongo admin --host localhost --port 27015 --eval "db.adminCommand( { shutdown: 1 } );"
echo "STOPPED MongoDB to apply change"