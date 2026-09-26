#!/bin/bash

mkdir -p logs

nohup java \
  -Djava.awt.headless=false \
  -Dapple.awt.UIElement=true \
  -Dloader.path=release/lib \
  -jar release/SpringBot-1.0.0.jar \
  >> logs/springbot-console.log 2>&1 &

echo "SpringBot started. PID: $!"
echo "Log: logs/springbot-console.log"

# chmod +x run.sh
# ./run.sh