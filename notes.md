# Notes from developing the project
These are just some notes I wrote along the way when doing the assignment.

## MyBatis 

### XML or programmatic config?
Since I do not have previous experience with MyBatis, I dug into the documentation.
Found out that it can be used with an XML based configuration or with programmatic configuration.
I decided to go with the programmatic configuration as it seemed to me that it would be easier to debug cleaner to read. 

### not displaying feedback
Had a problem where my database initialization sql script was not matching up to the 
Java classes that I had and was throwing an error on account creation.
Took a lot of head scratching and manually poking around to 
finally realize that the problem was not with the account mapper or model 
but rather with currency - sql script had UUID as ID field type, Java model had int.

# Integration tests
## Test instances of dependencies
For the Integration tests I decided to use `testcontainers` for spinning up postgresql and RabbitMQ.
This seemed the easiest way to implement integration tests without much hassle. 
`Testcontainers` spins up docker instances for programmatically configured services and allows to easily use them in tests.
Only thing that I spent quite some time on was getting the Spring configuration to recognize which port RabbitMQ was running on.
I ended up going for dynamically setting Spring properties in both of the test classes 
since I could not get the sharing of test containers working nicely otherwise. 