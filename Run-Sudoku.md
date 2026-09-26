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

Call API for nQueen:

```sh
curl  -X POST \
  'http://localhost:8080/api/nqueen/execute?n=9&positions=0%2C8%3B1%2C1%3B2%2C4%3B3%2C7%3B4%2C0%3B5%2C3%3B6%2C5%3B7%2C2%3B8%2C6' \
  --header 'Accept: */*' \
  --header 'User-Agent: Thunder Client (https://www.thunderclient.com)'


  http://localhost:8080/api/nqueen/execute?n=9&positions=0,8;1,1;2,4;3,7;4,0;5,3;6,5;7,2;8,6
```