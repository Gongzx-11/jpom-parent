SET JAVA_HOME=D:\DreamSky\Java8\jdk1.8.0_101
SET Classpath=%JAVA_HOME%\lib\tools.jar;%JAVA_HOME%\lib\dt.jar;
SET Path=%JAVA_HOME%\bin;

@echo off
setlocal enabledelayedexpansion

set Tag=DevOps-Server-System-JpomServerApplication
set MainClass=org.springframework.boot.loader.JarLauncher
set basePath=%~dp0
set Lib=%basePath%lib\
@REM 请勿修改----------------------------------↓
set LogName=server.log
@REM 在线升级会自动修改此属性
set RUNJAR=
@REM 请勿修改----------------------------------↑
@REM 是否开启控制台日志文件备份
set LogBack=true
set JVM=-server
set ARGS= --devops.applicationTag=%Tag%  --spring.profiles.active=pro --devops.log=%basePath%log --server.port=2122

@REM 读取jar
call:listDir

if "%1"=="" (
    color 0a
    TITLE 自动化运维平台BAT控制台
    echo. ***** 自动化运维平台BAT控制台 *****
    ::*************************************************************************************************************
    echo.
        echo.  [1] 启动 start
        echo.  [2] 关闭 stop
        echo.  [3] 查看运行状态 status
        echo.  [4] 重启 restart
        echo.  [5] 帮助 use
        echo.  [0] 退 出 0
    echo.
    @REM 输入
    echo.请输入选择的序号:
    set /p ID=
    IF "!ID!"=="1" call:start
    IF "!ID!"=="2" call:stop
    IF "!ID!"=="3" call:status
    IF "!ID!"=="4" call:restart
    IF "!ID!"=="5" call:use
    IF "!ID!"=="0" EXIT
)else (
     if "%1"=="restart" (
        call:restart
     )else (
        call:use
     )
)
if "%2" NEQ "upgrade" (
    PAUSE
)else (
 @REM 升级直接结束
)
EXIT 0

@REM 启动
:start
    if "%JAVA_HOME%"=="" (
        echo 请配置【JAVA_HOME】环境变量
        PAUSE
        EXIT 2
    )

	echo 启动中.....关闭窗口不影响运行
	javaw %JVM% -Djava.class.path="%JAVA_HOME%/lib/tools.jar;%RUNJAR%" -Dapplication=%Tag% -Dbasedir=%basePath%  %MainClass% %ARGS% >> %basePath%%LogName%
	timeout 3
goto:eof


@REM 获取jar
:listDir
	if "%RUNJAR%"=="" (
		for /f "delims=" %%I in ('dir /B %Lib%') do (
			if exist %Lib%%%I if not exist %Lib%%%I\nul (
			    if "%%~xI" ==".jar" (
                    if "%RUNJAR%"=="" (
				        set RUNJAR=%Lib%%%I
                    )
                )
			)
		)
	)else (
		set RUNJAR=%Lib%%RUNJAR%
	)
	echo 运行：%RUNJAR%
goto:eof

@REM 关闭
:stop
	java -Djava.class.path="%JAVA_HOME%/lib/tools.jar;%RUNJAR%" %MainClass% %ARGS% --event=stop
goto:eof

@REM 查看运行状态
:status
	java -Djava.class.path="%JAVA_HOME%/lib/tools.jar;%RUNJAR%" %MainClass% %ARGS% --event=status
goto:eof

@REM 重启
:restart
	echo 停止中....
	call:stop
	timeout 3
	echo 启动中....
	call:start
goto:eof

@REM 提示用法
:use
	echo please use (start、stop、restart、status)
goto:eof


