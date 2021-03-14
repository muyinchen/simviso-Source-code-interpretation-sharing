## Domain layer

Heart of the application. Here goes actual business logic. 
It's crucial to properly identify domain models and their responsibilities.
In anemic model pattern domain is flat, does not contain methods, no business logic. 
It't the application layer (services, facades) that holds business logic.
DDD tells to drop that approach. There are many reasons for that. Best explained by Martin Fowler:
https://martinfowler.com/bliki/AnemicDomainModel.html
