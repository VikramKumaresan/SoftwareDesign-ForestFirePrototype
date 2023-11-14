# Config
MYSQL_CONTAINER_NAME=forestFireProtoMySql
MYSQL_PORT=3306
MYSQL_DB_NAME=forestFirePrototype
MYSQL_APP_USER=appuser
MYSQL_APP_USER_PASSWORD=root

# Export config
printf "\n\nExporting configs...\n"
export FOREST_FIRE_PROTO_MYSQL_CONTAINER_NAME=$MYSQL_CONTAINER_NAME
export FOREST_FIRE_PROTO_MYSQL_PORT=$MYSQL_PORT
export FOREST_FIRE_PROTO_MYSQL_APP_USER_PASSWORD=$MYSQL_APP_USER_PASSWORD
export FOREST_FIRE_PROTO_MYSQL_DB_NAME=$MYSQL_DB_NAME
export FOREST_FIRE_PROTO_MYSQL_APP_USER=$MYSQL_APP_USER

# Build project
printf "\n\nBuilding project...\n"
mvn clean install

# Set up DB dockers
#   Stop and delete previously running containers
printf "\n\nCleaning up old containers...\n"
docker stop $(docker ps -q --filter name=$MYSQL_CONTAINER_NAME)
docker container prune -f

printf "\n\nCreating new containers...\n"
docker run --name $MYSQL_CONTAINER_NAME -d \
 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=$MYSQL_DB_NAME \
 -e MYSQL_USER=$MYSQL_APP_USER -e MYSQL_PASSWORD=$MYSQL_APP_USER_PASSWORD \
 -p $MYSQL_PORT:$MYSQL_PORT \
 -v ./src/main/java/Db/:/docker-entrypoint-initdb.d \
 --network="host" \
  mysql

printf "\n\nSleeping for 15s to allow changes to propagate...\n"
sleep 15

# Run project
printf "\n\nStarting project...\n"
java -jar ./target/forestFirePrototype-1.0-SNAPSHOT-jar-with-dependencies.jar $1

