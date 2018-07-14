# REST API to manage blacklisted IPs


### Endpoints

| Method | Url | Decription |
| ------ | --- | ---------- |
| GET    |/info  | info / heartbeat - provided by boot |
| GET    |/health| application health - provided by boot |
| GET    |/v2/api-docs    | swagger json |
| GET    |/swagger-ui.html| swagger html |
| GET    |/v1/ipAddress/{id}| get ipAddress by id |
| GET    |/v1/ipAddresss    | get N ipAddresss with an offset|
| PUT    |/v1/ipAddress     | add / update ipAddress|
| DELETE |/v1/ipAddress     | delete API