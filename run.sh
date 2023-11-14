# Config
MYSQL_CONTAINER_NAME=forestFireProtoMySql
MYSQL_PORT=3306
MONGO_CONTAINER_NAME=forestFireProtoMongo
MONGO_PORT=27017

DB_NAME=forestFirePrototype
APP_USER=appuser
APP_USER_PASSWORD=root

# Export config
printf "\n\nExporting configs...\n"
export FOREST_FIRE_PROTO_MYSQL_CONTAINER_NAME=$MYSQL_CONTAINER_NAME
export FOREST_FIRE_PROTO_MYSQL_PORT=$MYSQL_PORT
export FOREST_FIRE_PROTO_MONGO_PORT=$MONGO_PORT
export FOREST_FIRE_PROTO_DB_NAME=$DB_NAME
export FOREST_FIRE_PROTO_APP_USER=$APP_USER
export FOREST_FIRE_PROTO_APP_USER_PASSWORD=$APP_USER_PASSWORD

# Build project
printf "\n\nBuilding project...\n"
mvn clean install

if [[ "$?" -ne 0 ]] ; then
  printf "\n\nMaven build failed! Pls review errors\n"
  exit $rc
fi

# Set up DB dockers
#   Stop and delete previously running containers
printf "\n\nCleaning up old containers...\n"
docker stop $(docker ps -q --filter name=$MYSQL_CONTAINER_NAME --filter name=$MONGO_CONTAINER_NAME)
docker container prune -f

printf "\n\nCreating new containers...\n"
docker run --name $MYSQL_CONTAINER_NAME -d \
 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=$DB_NAME \
 -e MYSQL_USER=$APP_USER -e MYSQL_PASSWORD=$APP_USER_PASSWORD \
 -p $MYSQL_PORT:$MYSQL_PORT \
 -v ./src/main/java/Db/:/docker-entrypoint-initdb.d \
 --network="host" \
  mysql &

docker run --name $MONGO_CONTAINER_NAME -d \
 -e MONGO_INITDB_DATABASE=$DB_NAME \
 -p $MONGO_PORT:$MONGO_PORT \
 -v ./src/main/java/Db/:/docker-entrypoint-initdb.d \
 --network="host" \
  mongo &
wait

printf "\n\nSleeping for 15s to allow changes to propagate...\n"
sleep 15

# Run project
printf "\n\nStarting project...\n"
java -jar ./target/forestFirePrototype-1.0-SNAPSHOT-jar-with-dependencies.jar $1