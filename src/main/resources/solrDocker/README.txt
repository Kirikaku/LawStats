docker build -t solrlawstat .
docker run -d -p 8983:8983 --name solrlawstat solrlawstat
docker exec -t solrlawstat sh /createSolr.sh