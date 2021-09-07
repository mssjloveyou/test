#fj solo
docker run -p 9000:9000 --add-host mysql:172.18.0.7 --network myhadoop --ip 172.18.0.100 --name stock -d stock
