This is a distributed system with three parts; weather sensors, a weather station and a weather service.  
The sensors send the data they are measuring using MQTT to the weather station, which creates  
a weather report that it sends using thrift to the the weather service, which stores these reports.  
Everything is started with docker-compose.
