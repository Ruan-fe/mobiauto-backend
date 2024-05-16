# mobiauto-backend

### Endpoints


```bash
Usuário admin para testar a aplicação:

{
  "email": "admin@mobiautobackend.com",
  "password": "admin"
}
```
### Autenticação:
```bash
Endpoint para autenticação:

POST http://localhost:8080/api/authenticate

Request Body:
{
  "email": "admin@mobiautobackend.com",
  "password": "admin"
}

Response Status: 200 OK

Response Body:
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJtb2JpYXV0by1iYWNrZW5kIiwic3ViIjoiYWRtaW5AbW9iaWF1dG9iYWNrZW5kLmNvbSIsImV4cCI6MTcxNTgyNjIwMn0.DnSwBt6BdeCQYCxoapiuoplfd5jbx9dhsQy2CJA8_uI"
}
```
### Members (Usuários):
```bash
Endpoint para criação de membros (usuarios):

POST http://localhost:8080/api/members

Request Headers=
'Authorization': 'Bearer + token'

Request Body=
{
  "name": "Ruan Felipe",
  "email": "ruanfelipe@gmail.com",
  "password": "mypass"
}

Response Status: 201 CREATED

Response Headers =
Location: http://localhost:8080/api/members/2e46d4b9-ab1c-48c8-aa31-fd39a4651653
```
```bash
Endpoint para busca de membro (usuario) por id:

GET http://localhost:8080/api/members{memberId}

Request Headers=
'Authorization': 'Bearer + token'

Response Status: 200 OK

Response Body=
{
    "id": "200f7229-101b-4ba4-bf91-8598cab82180",
    "name": "Colt",
    "email": "Colt@gmail.com",
    "role": "ASSISTANT",
    "creationDate": "2024-05-15T21:23:50.462-03:00",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/members/200f7229-101b-4ba4-bf91-8598cab82180"
        }
    }
}
```
### Dealerships (Revendas):
```bash
Endpoint para criação de dealerships (revendas):

POST http://localhost:8080/api/dealerships

Request Headers=
'Authorization': 'Bearer + token'

Request Body=
{
  "tradeName": "RUAN VEÍCULOS",
  "cnpj": "09967055000140",
  "memberId": "200f7229-101b-4ba4-bf91-8598cab82180"
}

Response Status: 201 CREATED

Response Headers =
Location: http://localhost:8080/api/dealerships/bee19490-a913-4231-8f42-5e61a6ccfc18
```
```bash
Endpoint para busca por id de dealerships (revendas):

GET http://localhost:8080/api/dealerships/bee19490-a913-4231-8f42-5e61a6ccfc18

Request Headers=
'Authorization': 'Bearer + token'

Response Status: 200 OK

Response Body=
{
    "id": "bee19490-a913-4231-8f42-5e61a6ccfc18",
    "tradeName": "RUAN VEÍCULOS",
    "cnpj": "09967055000140",
    "members": [
        {
            "id": "200f7229-101b-4ba4-bf91-8598cab82180",
            "name": "Colt",
            "email": "Colt@gmail.com",
            "role": "ASSISTANT",
            "creationDate": "2024-05-15T21:23:50.462-03:00"
        },
        {
            "id": "b3e54d7e-a1e6-462c-ada7-9b1e0c846cc0",
            "name": "Ruan Felipe",
            "email": "ruanfelipe@gmail.com",
            "role": "OWNER",
            "creationDate": "2024-05-15T21:23:29.929-03:00"
        }
    ],
    "creationDate": "2024-05-15T21:26:03.575-03:00",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/dealerships/bee19490-a913-4231-8f42-5e61a6ccfc18"
        }
    }
}
```

```bash
Endpoint para cadastrar membros a uma concessionária

POST http://localhost:8080/api/dealerships/bee19490-a913-4231-8f42-5e61a6ccfc18/registerMember

Request Headers=
'Authorization': 'Bearer + token'

Request Body=
{
  "memberId": "200f7229-101b-4ba4-bf91-8598cab82180",
  "role": "ASSISTANT"
}

Response Status: 200 OK

Response Headers =
Location: http://localhost:8080/api/dealerships/bee19490-a913-4231-8f42-5e61a6ccfc18
```
### Vehicles:
```bash
Endpoint para criação de veículos:

POST http://localhost:8080/api/vehicles

Request Headers=
'Authorization': 'Bearer + token'

Request Body=
{
  "dealershipId": "bee19490-a913-4231-8f42-5e61a6ccfc18",
  "licensePlate": "ABC-147",
  "brandType": "ACURA",
  "model": "Acura 1s2",
  "manufacturingYear": 2024,
  "modelYear": 2024,
  "version": "GVT",
  "transmissionType": "MANUAL",
  "fuelType": "GASOLINE",
  "doors": 4,
  "color": "YELLOW"
}

Response Status: 201 CREATED

Response Headers =
Location: http://localhost:8080/api/vehicles/9e737424-04a8-4897-9553-6026e97e4977
```
```bash
Endpoint para busca de veículos por id:

GET http://localhost:8080/api/vehicles/{vehicleId}

Request Headers=
'Authorization': 'Bearer + token'

Response Status: 200 OK

Response Body=
{
    "id": "88901369-7806-458e-9ee0-f4650ce16a29",
    "dealershipId": "33e7c864-5197-42f4-98d1-ab81cdf0349e",
    "licensePlate": "ABC-147",
    "brandType": "ACURA",
    "model": "Acura 1s2",
    "manufacturingYear": 2024,
    "modelYear": 2024,
    "version": "GVT",
    "transmissionType": "MANUAL",
    "fuelType": "GASOLINE",
    "doors": 4,
    "color": "YELLOW",
    "creationDate": "2024-05-15T19:41:41.438-03:00",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/vehicles/88901369-7806-458e-9ee0-f4650ce16a29"
        },
        "dealership": {
            "href": "http://localhost:8080/api/dealerships/33e7c864-5197-42f4-98d1-ab81cdf0349e"
        }
    }
}
```
```bash
Endpoint para busca de veículos de uma concessionária:

GET http://localhost:8080/api/vehicles/{vehicleId}?dealershipId={dealershipId}

Request Headers=
'Authorization': 'Bearer + token'

Response Status: 200 OK

Response Body=
{
    "_embedded": {
        "vehicles": [
            {
                "id": "88901369-7806-458e-9ee0-f4650ce16a29",
                "dealershipId": "33e7c864-5197-42f4-98d1-ab81cdf0349e",
                "licensePlate": "ABC-147",
                "brandType": "ACURA",
                "model": "Acura 1s2",
                "manufacturingYear": 2024,
                "modelYear": 2024,
                "version": "GVT",
                "transmissionType": "MANUAL",
                "fuelType": "GASOLINE",
                "doors": 4,
                "color": "YELLOW",
                "creationDate": "2024-05-15T19:41:41.438-03:00",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/vehicles/88901369-7806-458e-9ee0-f4650ce16a29"
                    },
                    "dealership": {
                        "href": "http://localhost:8080/api/dealerships/33e7c864-5197-42f4-98d1-ab81cdf0349e"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/vehicles?dealershipId=33e7c864-5197-42f4-98d1-ab81cdf0349e&page=0&size=10"
        }
    },
    "page": {
        "size": 10,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
```
```bash
Endpoint para busca de veículos de todas concessionárias:

GET http://localhost:8080/api/vehicles/{vehicleId}

Request Headers=
'Authorization': 'Bearer + token'

Response Status: 200 OK

Response Body=
{
    "_embedded": {
        "vehicles": [
            {
                "id": "88901369-7806-458e-9ee0-f4650ce16a29",
                "dealershipId": "33e7c864-5197-42f4-98d1-ab81cdf0349e",
                "licensePlate": "ABC-147",
                "brandType": "ACURA",
                "model": "Acura 1s2",
                "manufacturingYear": 2024,
                "modelYear": 2024,
                "version": "GVT",
                "transmissionType": "MANUAL",
                "fuelType": "GASOLINE",
                "doors": 4,
                "color": "YELLOW",
                "creationDate": "2024-05-15T19:41:41.438-03:00",
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/api/vehicles/88901369-7806-458e-9ee0-f4650ce16a29"
                    },
                    "dealership": {
                        "href": "http://localhost:8080/api/dealerships/33e7c864-5197-42f4-98d1-ab81cdf0349e"
                    }
                }
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/vehicles?dealershipId=33e7c864-5197-42f4-98d1-ab81cdf0349e&page=0&size=10"
        }
    },
    "page": {
        "size": 10,
        "totalElements": 1,
        "totalPages": 1,
        "number": 0
    }
}
```
### Opportunities:
```bash
Endpoint para cadastro de oportunidades:

POST http://localhost:8080/api/opportunities

No Auth

Request Body=
{
  "vehicleId": "9e737424-04a8-4897-9553-6026e97e4977",
  "customer": {
    "name": "Tiago Barbosa",
    "email": "tiagobarbosa@gmail.com",
    "phone": "1547856354"
  }
}

Response Status: 200 OK

Response Headers=
Location: http://localhost:8080/api/opportunities/249fb6e3-d52f-4c90-bda5-19b392e8cce2
```
```bash
Endpoint para busca de oportunidades por id:

GET http://localhost:8080/api/opportunities/249fb6e3-d52f-4c90-bda5-19b392e8cce2

Request Headers=
'Authorization': 'Bearer + token'

Response Status: 200 OK

Response Body=
{
    "id": "249fb6e3-d52f-4c90-bda5-19b392e8cce2",
    "dealershipId": "bee19490-a913-4231-8f42-5e61a6ccfc18",
    "vehicleId": "9e737424-04a8-4897-9553-6026e97e4977",
    "customer": {
        "name": "Tiago Barbosa",
        "email": "tiagobarbosa@gmail.com",
        "phone": "1547856354"
    },
    "status": "APPROVED",
    "memberId": "b3e54d7e-a1e6-462c-ada7-9b1e0c846cc0",
    "reason": "Venda concluída com sucesso",
    "assignDate": "2024-05-15T21:40:41.608-03:00",
    "conclusionDate": "2024-05-15T21:43:45.518-03:00",
    "creationDate": "2024-05-15T21:34:29.328-03:00",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/opportunities/249fb6e3-d52f-4c90-bda5-19b392e8cce2"
        },
        "dealership": {
            "href": "http://localhost:8080/api/dealerships/bee19490-a913-4231-8f42-5e61a6ccfc18"
        }
    }
}
```
```bash
Endpoint para busca de oportunidades por filtros:

GET http://localhost:8080/api/opportunities?dealershipId=246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1&statuses=NEW,APPROVED

Request Headers=
'Authorization': 'Bearer + token'

Response Status: 200 OK

Response Body=
{
   "_embedded":{
      "opportunities":[
         {
            "id":"d172d900-55d6-45b1-aaf6-70d7d05928b5",
            "dealershipId":"246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1",
            "vehicleId":"2e55b038-b7af-41b7-b1f8-3c5023f237ac",
            "customer":{
               "name":"Vitor Hugo",
               "email":"vitorhugo@gmail.com",
               "phone":"18997845412"
            },
            "status":"NEW",
            "creationDate":"2024-04-23T23:01:40.619-03:00",
            "_links":{
               "self":{
                  "href":"http://localhost/api/opportunities/d172d900-55d6-45b1-aaf6-70d7d05928b5"
               },
               "dealership":{
                  "href":"http://localhost/api/dealerships/246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"
               }
            }
         },
         {
            "id":"caf7322e-8129-4a34-b285-33f3f8614d20",
            "dealershipId":"246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1",
            "vehicleId":"5c0dda90-b6e8-4f87-9189-b4cfc5be369d",
            "customer":{
               "name":"Diego Silva",
               "email":"diegosilva@gmail.com",
               "phone":"18997845414"
            },
            "status":"APPROVED",
            "memberId":"a5993416-4255-11ec-71d3-0242ac130004",
            "reason":"Venda concluÃ­da com sucesso",
            "creationDate":"2024-04-23T23:01:40.619-03:00",
            "_links":{
               "self":{
                  "href":"http://localhost/api/opportunities/caf7322e-8129-4a34-b285-33f3f8614d20"
               },
               "dealership":{
                  "href":"http://localhost/api/dealerships/246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"
               }
            }
         },
         {
            "id":"3ba22468-db41-470c-adde-727aa66327d6",
            "dealershipId":"246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1",
            "vehicleId":"2e55b038-b7af-41b7-b1f8-3c5023f237ac",
            "customer":{
               "name":"Martin Costa",
               "email":"martincosta@gmail.com",
               "phone":"18997845415"
            },
            "status":"NEW",
            "creationDate":"2024-04-23T23:01:40.619-03:00",
            "_links":{
               "self":{
                  "href":"http://localhost/api/opportunities/3ba22468-db41-470c-adde-727aa66327d6"
               },
               "dealership":{
                  "href":"http://localhost/api/dealerships/246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1"
               }
            }
         }
      ]
   },
   "_links":{
      "self":{
         "href":"http://localhost/api/opportunities?dealershipId=246e30ed-6f82-4da7-bb09-c9f7ca9bf4e1&statuses=NEW,APPROVED&page=0&size=10"
      }
   },
   "page":{
      "size":10,
      "totalElements":3,
      "totalPages":1,
      "number":0
   }
}
```
```bash
Endpoint para criação de atribuição de oportunidades:

POST http://localhost:8080/api/opportunities/249fb6e3-d52f-4c90-bda5-19b392e8cce2/assign

Request Headers=
'Authorization': 'Bearer + token'

Request Body=
{
  "memberId": "b3e54d7e-a1e6-462c-ada7-9b1e0c846cc0",
  "reason": "Em progresso",
  "status": "IN_PROGRESS"
}

Response Status: 200 OK

Response Headers =
Location: http://localhost:8080/api/opportunities/249fb6e3-d52f-4c90-bda5-19b392e8cce2
```