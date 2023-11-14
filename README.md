## Prerequisites
1. Java  
This is the Java version I am using,
```
openjdk version "11.0.20.1" 2023-08-24  
OpenJDK Runtime Environment (build 11.0.20.1+1-post-Ubuntu-0ubuntu122.04)  
OpenJDK 64-Bit Server VM (build 11.0.20.1+1-post-Ubuntu-0ubuntu122.04, mixed mode, sharing)
```

3. Docker  
The project uses docker to create the DB containers. Here is the link I used to install and set Docker up,  
https://dev.to/bowmanjd/install-docker-on-windows-wsl-without-docker-desktop-34m9  

Make sure you add your user to the docker group. This will allow you to use docker without mentioning sudo everytime.

3. Maven  
The project uses Maven to pull dependencies and build the project. Here's the link I used to install Maven using apt,
https://www.hostinger.com/tutorials/how-to-install-maven-on-ubuntu  

## Running the Project
Run ./run.sh in the root directory

## Warning!
The run script prunes all live containers. So if you have Stopped containers you may use later, don't run this script.
