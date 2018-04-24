
# Deploying

docker build --no-cache -t lawstat-app .
docker run -it  -p 8080:8080  -v PATH_TO_YOUR_LAWSTATS_FOLDER/LawStats/src/main/resources/data/:/LawStats/src/main/resources/data -v PATH_TO_YOUR_LAWSTATS_FOLDER/LawStats/src/main/resources/config:/LawStats/src/main/resources/config --name lawstat-app lawstat-app

# Info

This application use more than 3gb RAM, so please adjust your docker configuration