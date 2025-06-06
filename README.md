## Pre-requisites

* JDK version 21.

* Docker-Compose

## Event sourcing / CQRS powered by Axon Framework and Axon-Server
The application uses Axon Framework for event sourcing and CQRS (Command Query Responsibility Segregation) pattern.
It requires Axon-Server to be running, which is a dedicated event store and message routing service for Axon Framework applications.

Note: Axon-Server is not free for real production use. See: https://www.axoniq.io/pricing 

## Start Coupon Service
Run `pl.newicom.axon.coupon.CouponApplication`.  
This will start a docker image of Axon-Server using the docker-compose.yaml file found in the root of the project. 
Once you have the service started you can see it connected to Axon-Server at http://localhost:8024/#overview


## Running sample http requests

See [requests.http](/requests.http) file.

## TODOs
* Implement production-ready ByIpCountryResolver
* Implement tests for the REST layer (controller)

## Features to consider
* Query endpoints (e.g. get all coupons, get coupon by id)
Query endpoints are not implemented, but can be easily added. They require projections that populate the read model with data from the events. Axon Framework provides a way to create projections using event handlers. One can create a projection that listens to the `CouponRegistered` and `CouponUsed` events and populates a read model with the coupon data.