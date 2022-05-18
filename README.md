# Tuum home assignment

## Requirements
* Docker (and `docker-compose`)

## Running the app

### 1. Build the application

    ./gradlew build

### 2. Create docker network

    docker network create tuum

### 3. Build docker container

    docker-compose build

### 4. Run the whole application stack

    docker-compose -f docker-compose.yml up

## Important choices in my solution
Due to the simplicity of the application, there weren't many critical decisions to make.
However, there were some cases in which I could have gone a different route.

### UUID vs Long ID
I decided to use UUIDs as the ID columns in the database due to the sheer amount of transactions stored in banking systems.
I have read about banking systems having problems with Long datatype and the database table reaching the limit.
Better to avoid this issue in my opinion than to have to go through with migrating later on.

### Currencies as database entity vs enum 
For currencies, I could have gone the route of putting the allowed values in an enum, 
but I decided to have them as a separate database table.
This might not be so critical in this application but in general made sense
since currencies could be added to the system and having them as an enum would mean restarting the whole system.
However, enums made sense for transaction directions since transactions can only have two directions 
(Although now thinking about it could be better to have the transactionDirection field as a boolean instead). 

## Estimated transactions per second
It is rather difficult to say the transactions per second number without doing any load testing.
If I had to guess based on Postman testing, I would say around 50 transactions per second,
but this probably depends on the development machine. 

## Scaling services
Generally things to consider for applications to be scalable is:
* **Keep the app stateless** - this will be a bottleneck when trying to scale the application 
since it will be reliant on a single node in the system.
* **Using microservices** - this is pretty much the de facto standard in software development
because it allows to scale applications better horizontally since the functional responsibilities
are split between multiple services.
