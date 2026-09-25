Start recording:

```sh
curl -X POST \
http://localhost:8080/api/automation/keyboard/track/start
```

Now play/reverse the game.

For example:

```sh
↑
↑
↑
←
←
→
↓
```

Then:

```sh
curl -X POST \
http://localhost:8080/api/automation/keyboard/track/stop
```

Response:

```sh
{
  "status": "STOPPED",
  "commandCount": 7,
  "commands": [
    "UP",
    "UP",
    "UP",
    "LEFT",
    "LEFT",
    "RIGHT",
    "DOWN"
  ],
  "instruction": "UP,UP,UP,LEFT,LEFT,RIGHT,DOWN"
}
```

Optional: Reverse the instruction if played backward


```sh
curl  -X GET \
  'http://localhost:8080/api/helper/reverse?instruction=UP.....'

```

Call the instruction API:
```sh
curl  -X POST \
  'http://localhost:8080/api/automation/keyboard?instruction=RIGHT
```



And that instruction can be fed directly into your existing keyboard API.