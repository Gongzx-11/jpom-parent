#指定环境变量
export JAVA_HOME=/home/tianjing/binfo_tomcat/jdk/jdk1.8.0_152
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=.:$JAVA_HOME/lib:$JRE_HOME/lib:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
#系统标识
Tag="DevOps-Server-System-JpomServerApplication"
# 自动获取当前路径
Path=$(cd `dirname $0`; pwd)"/"
Lib="${Path}lib/"
RUNJAR=""
Log="${Path}server.log"
LogBack="${Path}log/"
JVM="-server "
# 修改项目端口号 日志路径
ARGS="--jpom.applicationTag=${Tag} --spring.profiles.active=pro --server.port=9704  --devops.log=${Path}log"

echo ${Tag}
echo ${Path}
RETVAL="0"
# 升级执行命令标识
upgrade="$2"

# now set the path to java
if [[ -x "${JAVA_HOME}/bin/java" ]]; then
  JAVA="${JAVA_HOME}/bin/java"
  NOW_JAVA_HOME="${JAVA_HOME}"
else
  set +e
  JAVA=`which java`
  NOW_JAVA_HOME="${JAVA}/../../"
  set -e
fi

if [[ ! -x "$JAVA" ]]; then
  echo "没有找到JAVA 文件,请配置【JAVA_HOME】环境变量"
  exit 1
fi

# 启动程序
function start() {
    pid=`getPid`
	if [[ "$pid" != "" ]]; then
	   echo "程序正在运行中：${pid}"
	   exit 2
	fi
    echo  ${Log}
    # 备份日志
    if [[ -f ${Log} ]]; then
		if [[ ! -d ${LogBack} ]];then
			mkdir ${LogBack}
		fi
		cur_dateTime="`date +%Y-%m-%d_%H:%M:%S`.log"
		mv ${Log}  ${LogBack}${cur_dateTime}
		echo "mv to $LogBack$cur_dateTime"
		touch ${Log}
	fi
	# jar
	if [[ -z "${RUNJAR}" ]] ; then
		RUNJAR=`listDir ${Lib}`
		echo "自动运行：${RUNJAR}"
	fi
    # error
	if [[ -z "${RUNJAR}" ]] ; then
        echo "没有找到jar"
        exit 2
    fi
    nohup ${JAVA}  ${JVM} -Xbootclasspath/a:${NOW_JAVA_HOME}/lib/tools.jar -jar ${Lib}${RUNJAR} -Dapplication=${Tag} -Dbasedir=${Path} ${ARGS}  >> ${Log} 2>&1 &
    # 升级不执行查看日志
    if [[ ${upgrade} == "upgrade" ]] ; then
        exit 0
    fi
    if [[ -f ${Log} ]]; then
        tail -f ${Log}
    else
        sleep 3
        if [[  -f ${Log} ]]; then
           tail -f ${Log}
        else
           echo "还没有生成日志文件:${Log}"
        fi
    fi
}

# 找出第一个jar包
function listDir()
{
    ALL=""
	for file in `ls $1`
	do
		if [[ -f "${1}/${file}" ]] &&  [[ "${file##*.}"x = "jar"x ]] ; then
			#得到文件的完整的目录
			ALL="${file}"
			break
		fi
	done
	echo ${ALL}
}

# 停止程序
function stop() {
	pid=`getPid`
	if [[ "$pid" != "" ]]; then
        echo -n "boot ( pid $pid) is running"
        echo
        echo -n $"Shutting down boot: wait"
        kill $(pgrep -f ${Tag}) 2>/dev/null
        sleep 3
		pid=`getPid`
		if [[ "$pid" != "" ]]; then
			echo "kill boot process"
			kill -9 "$pid"
		fi
    else
        echo "boot is stopped"
    fi

	status
}

# 获取程序状态
function status()
{
	pid=`getPid`
	#echo "$pid"
	if [[ "$pid" != "" ]]; then
		echo "boot is running,pid is $pid"
	else
		echo "boot is stopped"
	fi
}

function getPid(){
    pid=$(ps -ef | grep -v 'grep' | egrep ${Tag}| awk '{printf $2 " "}')
    echo ${pid}
}

# 提示使用语法
function usage()
{
   echo "Usage: $0 {start|stop|restart|status}"
   RETVAL="2"
}

# See how we were called.
RETVAL="0"
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    status)
        status
        ;;
    *)
      usage
      ;;
esac

exit $RETVAL
