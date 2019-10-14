#!/bin/bash

conainername=sonarqube

if [ ! "$(docker ps -q -f name=$conainername)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=$conainername)" ]; then
        # cleanup
        docker rm $conainername
    fi
    # run your container
    docker pull sonarqube
    docker run -d --name $conainername -p 9000:9000 $conainername
fi

attempt=0
while [ $attempt -le 59 ]; do
    attempt=$(( $attempt + 1 ))
    echo "Checking Sonar server to be up (attempt: $attempt)..."
    result=$(docker logs $conainername)
    if grep -q 'SonarQube is up' <<< $result ; then
      echo "SonarQube is up!"
      mvn sonar:sonar -Dsonar.projectKey=RiskGame -Dsonar.host.url=http://localhost:9000
      break
    fi
    sleep 15
done