# Flink Job Unit Testing

Sources for my article [Flink Job Unit Testing](https://github.com/antonbakalets/demo-flink-wiki-edits).

The article describes an approach to unit test Apache Flink Job as a whole. 
To achieve that: 
- refactor the Job to be able to plug source and sink functions; 
- implement a source function providing a decent amount of test data; 
- implement a sink function able to collect data from different threads 
- write a unit test using Flink mini-cluster simulating execution of the Job by multiple task managers.

