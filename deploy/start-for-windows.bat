@echo off
java -jar -Dlogback.configurationFile=.\logback.xml -Dconfig.file=.\application.conf pcars2-udp-app-assembly-0.0.1.jar
