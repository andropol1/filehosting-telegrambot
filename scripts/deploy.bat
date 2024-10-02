set ENV_FILE=.\.env

pushd %USERPROFILE%\Desktop\filehosting-telegrambot\

docker compose -f docker-compose.yml --env-file %ENV_FILE% down --timeout=60 --remove-orphans
docker compose -f docker-compose.yml --env-file %ENV_FILE% up --build --detach

popd