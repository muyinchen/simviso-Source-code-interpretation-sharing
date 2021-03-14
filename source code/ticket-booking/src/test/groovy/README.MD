## Tests

Some rules:
 - while creating new domain: start with class stub - what it represents, what attributes it has. Create a tests for class, thinking what domain really does. Use ubiquitous language. Best to use technology that allows writing full sentences when testing (e.g. Spock)
 - focus on very good unit tests, not necessary on integration tests. treat integration tests a bonus / showcase; also measure test coverage with unit tests only
 - tests from one layer should not have dependencies on other layers e.g. test of application service should use a mocks of persistence, test of presentation should use a mocks of application service
 - use Mocks or Stubs, avoid Spy's, use it only if you work with legacy code