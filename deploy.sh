#!/bin/bash

# 배포 경로 설정
APP_NAME="frameworkstudy"
REPO_PATH="/home/ubuntu/backend7-board"
BUILD_JAR_PATH="$REPO_PATH/build/libs"
DEPLOY_JAR_NAME="frameworkstudy-0.0.1-SNAPSHOT.jar" # 빌드된 jar 파일 이름

# 현재 구동 중인 서버 종료
echo "> 실행 중인 서버를 확인 합니다."
CURRENT_PID=$(ps aux | grep $APP_NAME | grep -v grep | awk '{print $2}')

if [ -z "$CURRENT_PID" ]; then
  echo "> 실행 중인 서버를 찾지 못했습니다."
else
  echo "> 해당 PID의 서버를 종료합니다. PID: $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 3 # 종료 대기
fi

# 최신 소스코드 가져오기
echo "> Git Pull 시작"
cd $REPO_PATH || exit
git reset --hard HEAD
git pull origin master

# 빌드 (테스트 제외)
echo "> Build 를 시작합니다."
chmod +x gradlew
./gradlew clean build -x test
if [ $? -ne 0 ]; then
  echo "> Build 에 실패하여 스크립트를 종료합니다."
  exit 1
fi

# 서버 실행
echo "> 서버를 실행합니다."
DEPLOY_JAR="$BUILD_JAR_PATH/$DEPLOY_JAR_NAME"
if [ -f "$DEPLOY_JAR" ]; then
  java -jar $DEPLOY_JAR &
  echo "> 서버가 실행되었습니다. JAR: $DEPLOY_JAR"
else
  echo "> Jar를 찾지 못했습니다. not found: $DEPLOY_JAR"
  exit 1
fi
