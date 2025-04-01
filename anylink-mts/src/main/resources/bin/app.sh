#!/bin/sh

SPRING_PROFILES_ACTIVE=prod
NACOS_CONFIG_NAMESPACE=f76d9839-431e-4bd8-a352-4df2f9da119e
NACOS_DISCOVERY_NAMESPACE=f76d9839-431e-4bd8-a352-4df2f9da119e
NACOS_CONFIG_SERVER_ADDR=172.21.23.53:8848
NACOS_DISCOVERY_SERVER_ADDR=172.21.23.53:8848

##项目路径, 根据实际部署的修改即可,其他的不用修改
SERVICE_DIR=/opt/hibob/app/mts
##此处是打包的jar包名称前缀，不带.jar后缀
APP_NAME=anylink-mts
##完整jar包名称，动态获取
JAR_NAME=$(ls $SERVICE_DIR | grep "^$APP_NAME-[0-9.]\+\.jar$" | head -n 1)
##PID 代表是PID文件
PID=$APP_NAME\.pid
##健康检查URL
HEALTH_CHECK_URL="http://localhost:8040/api/anylink-mts/healthcheck"

# 判断jar包是否存在
if ! test -e "$SERVICE_DIR/$JAR_NAME"; then
    echo "$SERVICE_DIR/$JAR_NAME 文件不存在"
    exit 1
fi

## 进入执行目录
cd $SERVICE_DIR

#使用说明，用来提示输入参数
usage() {
    echo "Usage: sh 执行脚本.sh [start|stop|restart|status]"
    exit 1
}

#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $JAR_NAME|grep -v grep|awk '{print $2}' `
  #如果不存在返回1，存在返回0
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

#启动方法
start(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> ${JAR_NAME} is already running PID=${pid} <<<"
  else
    nohup java -Xms512m -Xmx512m -jar $JAR_NAME --spring.profiles.active=$SPRING_PROFILES_ACTIVE --spring.cloud.nacos.config.namespace=$NACOS_CONFIG_NAMESPACE --spring.cloud.nacos.discovery.namespace=$NACOS_DISCOVERY_NAMESPACE --spring.cloud.nacos.config.server-addr=$NACOS_CONFIG_SERVER_ADDR --spring.cloud.nacos.discovery.server-addr=$NACOS_DISCOVERY_SERVER_ADDR >/dev/null 2>&1 &
    echo $! > $PID
    echo ">>> starting $JAR_NAME, PID=$! <<<"

    max_attempts=60
    attempt=0
    while [ $attempt -lt $max_attempts ]; do
      status_code=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_CHECK_URL)
      if [ "$status_code" == "200" ]; then
        echo ">>> Application started successfully <<<"
        break
      else
        echo "......"
        sleep 1
      fi
      attempt=$((attempt + 1))
    done

    if [ $attempt -eq $max_attempts ]; then
      echo ">>> Failed to start the application within the specified time <<<"
      exit 1
    fi
   fi
}

# 停止方法
stop() {
    # 判断 PID 文件是否存在
    if [ ! -f "$PID" ]; then
        echo "PID文件未找到，stop失败，请手动停止"
        return 1
    fi

    pidf=$(cat $PID)
    echo ">>> api PID = $pidf begin kill $pidf <<<"
    kill $pidf
    rm -rf $PID
    sleep 3
    is_exist
    if [ $? -eq "0" ]; then
        echo ">>> api 2 PID = $pid begin kill -9 $pid  <<<"
        kill -9 $pid
        sleep 3
        echo ">>> $JAR_NAME process stopped <<<"
    else
        echo ">>> ${JAR_NAME} is not running <<<"
    fi
}

#输出运行状态
status(){
  is_exist
  if [ $? -eq "0" ]; then
    echo ">>> ${JAR_NAME} is running PID is ${pid} <<<"
  else
    echo ">>> ${JAR_NAME} is not running <<<"
  fi
}

#重启
restart(){
  stop
  start
}

#根据输入参数，选择执行对应方法，不输入则执行使用说明
case "$1" in
  "start")
    start
    ;;
  "stop")
    stop
    ;;
  "status")
    status
    ;;
  "restart")
    restart
    ;;
  *)
    usage
    ;;
esac
exit 0