@echo off

echo "  ____   ____                 ____  "
echo " |  _ \ / ___|__ _ _ __ ___  |___ \ "
echo " | |_) | |   / _` | '__/ __|   __) |"
echo " |  __/| |__| (_| | |  \__ \  / __/ "
echo " |_|    \____\__,_|_|  |___/ |_____|"
echo;

java ^
-Dlogback.configurationFile=.\logback.xml ^
-Dconfig.file=.\application.conf ^
-jar pcars2-telemetry-browsing-assembly-0.2.2.jar