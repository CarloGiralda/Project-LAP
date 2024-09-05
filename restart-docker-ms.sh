CONTAINER_NAME=$1

docker compose stop $CONTAINER_NAME
docker rm $CONTAINER_NAME
docker rmi lab-remote-$CONTAINER_NAME
docker compose up -d
