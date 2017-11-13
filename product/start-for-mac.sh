#/bin/bash

java \
  -Dlogback.configurationFile=./logback.xml \
  -Dconfig.file=./application.conf \
  -jar pcars2-telemetry-browsing-assembly-0.0.1.jar
