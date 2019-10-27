# Weather Service - A Distributed System
The task of this project was to create a distributed system consisting out of three big parts; multiple sensors, a weather station and a weather service. A team of two was working on this project and the tasks divided.
### Sensors
The sensors simulate realistic data and send it every few seconds via MQTT to the designated weather station.
### Weather Station
The weather station receives the data the sensors sends and creates an HTTP server that uses REST to represent the data. The station also creates a weather report which is send to a designated service via Thrift.
### Weather Service
The Weather Service receives the weather report and stores it in a database. To achieve high consistency and high availability the weather service runs in three parallel instances.
### My part
I created the sensors, the Unit-Tests for those and the communication via MQTT between the sensors and the weather station. Another task I took was deploying the project. I made everything work with docker and docker-compose and deployed the sensors in an AWS environment. I created our Gitlab pipeline with an own Gitlab Runner and I also made use of the Gitlab Registry for quick deployment.
