# Metro-Madness
In this project, I have redesigned a simulation of Melbourne's train network using LibGDX game development framework. The task is to first provide a detailed analysis of the existing software package, highlighting inappropriate design decisions and providing documentation of the existing Train state simulation. Having completed this design, I then to redesigned the existing package applying good design principles and patterns to improve the flexibility of the simulation platform to cope with Melbourne Metro’s changing needs. Finally, having improved the design, I implemented some missing functionality and modify the existing simulation to match my redesign.

LibGDX as a framework is mainly used for gaming purposes, but in this case, it is only being used for its OpenGL rendering support. The simulation does not use any of the networking, controller or audio libraries. The current simulation provides a 2D render of the train network at any given point in time. 

The provided simulation makes use of Gradle as a build and dependency management tool. Using Gradle avoids the need to manually link any of the libraries or dependencies within the project, and it is much easier to migrate between development environments and IDEs.

## Features
- Reading Train Maps from XML to seed simulation.
- Simulating Multiple Lines with Multiple Stations and Trains.
- Simulating Trains travelling on the network.
- Simulating Trains entering and leaving Stations.
- Simulating Single and Double tracks (with locks on access).
- Simulating Passengers entering the network at Active Stations.
- Simulating Passengers travelling on a single/multi line journey.

## Project Overview

### Controls
The existing simulation provides camera controls to navigate around the train network, specifically it supports the following desktop controls:
- Arrow keys to pan around (up, down, left and right).
- Zooming in and out using Q and A respectively.

### Redesign
As part of the redesign, I modified the existing PassengerGenerator to now generate trip plans to any line and then create a new class that implements the PassengerRouter interface and can route passengers throughout the network. This new feature (including relevant methods and data structures) is also included in the new design documents.
As part of my design I have included the following diagrams and models:
• A static class diagram of all components, complete with visibility modifiers, associations and all methods.
• A sequence diagram showing the behaviour of a passenger travelling from one station to another, including line change.
• A sequence diagram showing the behaviour of a train entering and leaving a station.
• A state machine diagram showing the updated behaviour of a Train.

## Analysis
### General Design Principles
In the initial implementation of the simulation all of the instance variables are public, this results in bad encapsulation. As a result it may cause high coupling if the code is extended in the future, as a result it has been fixed in the new implementation. Furthermore, there was use of polymorphism in Track  and inheritance in Train, and Station both of which has been poorly implemented which will be discussed below.

### Major changes in Implementation
#### Poor implementation for train sizes due to duplication of code
The subclasses of Train have magic numbers for their maximum capacity, and there isn’t a way to specify a new Train with a different capacity other than creating a new subclass (meaning lots of code duplication for the embark and render methods). Moreover, methods that were overridden (embark, render), can easily be generalized and put into the superclass.
Due to these reasons, the subclasses were deleted and an instance variable for maximum capacity was added to the Train class. In an effort to make Train less coupled we made it an abstract superclass and implemented a subclass NormalTrain. Due to the abstract superclass, concrete subclasses can be created in the future to model different update logic for different trains without any change to the code.
#### Redundancy for Station
The previous design uses ActiveStation class to model a specific type of station that allows passengers entering and leaving. This does not allow combinations of other types of station. For example, transfer station that does not allow passengers entering or leaving the station can’t be captured by the previous design. Station class should have two extra boolean flags to determine whether passengers can enter or can leave. PassengerGenerator will not generate new passengers if the station cannot be entered. Furthermore, when generating new passengers for simulation, passengers’ destinations would be checked if passengers are allowed to leave at the station.
#### Code quality of the implementation is bad
The implementation of the Track inheritance design pattern wasn’t good. In the initial implementation, the subclass (DualTrack) overrides all of the methods of its superclass (Track). The sole purpose of the superclass was to store the instance variables, a better implementation would be to make Track an abstract superclass with all abstract methods. The reason for this is that we are able to store the instance variables and our subclasses wouldn’t be overriding all the concrete methods of our superclass.
