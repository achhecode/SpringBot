Call API:

```sh
curl  -X POST \
  'http://localhost:8080/api/sudoku/execute?instruction=354162612345426513531624143256265431' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)'
```


Call API for Tango:

```sh
curl  -X POST \
  'http://localhost:8080/api/tango/execute?instruction=MSSMMSSMMSSMMSMMSSSMSMMSMSMSSMSMSSMM' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)'
```