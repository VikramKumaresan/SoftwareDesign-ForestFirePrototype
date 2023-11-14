CREATE TABLE Temperature (
    id int NOT NULL AUTO_INCREMENT,
    sensorId varchar(10) NOT NULL,
    reading varchar(10) NOT NULL,
    timestamp varchar(30) NOT NULL,
    PRIMARY KEY (id)
);

Grant SELECT on *.* to appuser@'%';
flush privileges;