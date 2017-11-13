#!/bin/bash

rm -rf publish
mkdir -p publish/pcars2-telemetry-browsing/public

sbt assembly
cp target/scala-2.12/pcars2-telemetry-browsing-assembly-*.jar publish/pcars2-telemetry-browsing/

npm run product
cp -r public/dist publish/pcars2-telemetry-browsing/public/

cp public/*.html publish/pcars2-telemetry-browsing/public/
cp public/*.css publish/pcars2-telemetry-browsing/public/
cp public/*.ico publish/pcars2-telemetry-browsing/public/
cp public/*.png publish/pcars2-telemetry-browsing/public/

cp product/*.conf publish/pcars2-telemetry-browsing/
cp product/*.xml publish/pcars2-telemetry-browsing/
cp product/*.sh publish/pcars2-telemetry-browsing/
cp product/*.bat publish/pcars2-telemetry-browsing/

cd publish/pcars2-telemetry-browsing
zip -r pcars2-telemetry-browsing.zip ./

cd ../../
