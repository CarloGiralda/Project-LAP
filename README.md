# 🚗 Car Rental Web Application

Welcome to the Car Rental Application! This application allows users to search and rent cars, participate in auctions, and manage transactions using blockchain technology. The system is built using a microservices architecture with Spring Boot, and includes features like JWT authentication, real-time notifications, and geolocation-based car listings.


## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
  - [Warning](#-warning)
  - [Installation](#installation)
  - [Running the Application](#running-the-application)
- [Microservices Overview](#-microservices-overview)
- [Usage](#-usage)
  - [Searching for Cars](#searching-for-cars)
  - [Renting a Car](#renting-a-car)
  - [Auction System](#auction-system)
  - [Notifications](#notifications)
  - [Chat System](#chat-system)
- [Technologies](#-technologies)


## ✨ Features

- **Search and Rent Cars**: Users can search for cars and rent them for specified periods.
- **Blockchain Transactions**: Secure storage of transactions and payments using blockchain technology.
- **Auction System**: Renters can auction their cars, and users can bid to win the rental rights.
- **Real-time Chat**: Integrated chat for negotiating rental details between searcher and renter.
- **Geolocation Services**: Map-based car listing showing available cars near the user's location.
- **Notifications**: Subscription-based notifications for car availability.

## 🏗️ Architecture

This application is built using a microservices architecture. Key components include:

- **API Gateway**: Routes requests and handles JWT authentication.
- **Eureka Server**: Service discovery for microservices.
- **Microservices**: Independently deployable services for different functionalities.

![LAP - architecture drawio](https://github.com/CarloGiralda/Project-LAP/assets/92364167/e84e18a6-f206-46a4-ac27-1911e59b9d9e)

## 🚀 Getting Started

### Warning:
   - This application will run a **total of 16 containers**, which will consume **approximately 4139 MB (4.1 GB) of disk space**.
   - The two blockchain nodes included in this setup use a type of **Proof of Work (PoW)** algorithm, which is computationally intensive. 
     - **Ensure you have sufficient available RAM and processing power** before running the application.
     - Running these containers on systems with limited memory or CPU resources could degrade performance or cause the system to become unresponsive.
   - **System Requirements**:
     - At least **8 GB of RAM** is recommended.
     - A modern multi-core CPU is suggested for handling the blockchain nodes.

   - If your system doesn't meet these requirements, you may want to avoid running the blockchain services, or consider reducing the load by running fewer containers (in that case some service will no longer be available depending on which ones you stopped)

### Installation
1. **Install Docker**:
   - If Docker is not already installed, follow these steps based on your operating system:
     
     - **For Windows**:
       - Download Docker Desktop from the official [Docker website](https://docs.docker.com/engine/install/).
       - Follow the installation instructions.
       - After installation, make sure to enable Docker Desktop and WSL 2 (if applicable).
     
     - **For macOS**:
       - Download Docker Desktop for Mac from [here](https://docs.docker.com/engine/install/).
       - Follow the installation instructions.

     - **For Linux**:
       - Follow the Docker Engine installation guide for your Linux distribution. For example, on Ubuntu:
         ```sh
         sudo apt-get update
         sudo apt-get install docker-ce docker-ce-cli containerd.io
         ```
       - Refer to the official Docker documentation for detailed instructions for other Linux distributions: [Install Docker Engine](https://docs.docker.com/engine/install/).

   - After installation, verify Docker is running:
     ```sh
     docker --version
     ```


2. **Clone the repository or simply download the [docker-compose](/docker-compose-hub.yml) file**:
    ```sh
    git clone <url>
    cd car-rental-app
    ```
3. **Changing Ports (if necessary)**:
   - If any of the ports specified in the `docker-compose-hub.yml` file are already in use on your machine, you can change them by editing the `ports` section in the `docker-compose-hub.yml` file. 
   
   - For example, if port `8080` is already being used, change the following line:
     ```yaml
     ports:
       - "8080:8080"
     ```
     to another available port, such as `8082`:
     ```yaml
     ports:
       - "8082:8080"
     ```

   - Repeat this process for any other services whose ports conflict with ones already in use.


4. **Build and Run with Docker**:
    ```sh
    docker compose -f docker-compose-hub.yml up -d
    ```
   This command will pull the required images from [Docker Hub](https://hub.docker.com/repositories/gabriele2000) and start the application. If you changed any ports, access the services via the new ports you've configured.
5. **Check status**: visit `localhost:8761` to check the availability of the services.
   

### Running the Application

Once the application is set up and running, you can access the frontend through `http://localhost:8081` (or your configured port) and interact with the backend services through the API Gateway.

## 🗂️ Microservices Overview

| Microservice          | Description                                      | Port  |
|-----------------------|--------------------------------------------------|-------|
| **API Gateway**       | Routes requests and handles authentication       | 8080  |
| **Service Discovery** | Service discovery for microservices              | 8761  |
| **Frontend**          | User interface                                   | 8081  |
| **User Service**      | Handles user registration, authentication, etc.  | 9000  |
| **Send Email**        | Manages email notifications                      | 9001  |
| **Car Insertion**     | Manages car listings and availability            | 9002  |
| **Car Search**        | Searches for available cars                      | 9003  |
| **Car Rating**        | Manages car ratings and reviews                  | 9004  |
| **Car Book**          | Handles car booking transactions                 | 9005  |
| **Chat**              | Handles real-time chat between users             | 9006  |
| **Notification**      | Sends notifications for car availability         | 9007  |
| **Select Area**       | Manages area selection for geolocation           | 9008  |
| **Payment Service**   | Manages payment transactions                     | 9010  |
| **Auction**           | Auction service                     | 9013  |
| **Blockchain (miner 1)**        | Manages blockchain transactions                  | 9011  |
| **Blockchain (miner 2)**       | Secondary blockchain service                     | 9012  |



Each service is independently deployable and can be scaled based on demand.

## 💡 Usage

### Searching for Cars

1. Navigate to the search page.
2. Enter your search criteria (location, car model, rental period).
3. View and filter the list of available cars.

### Renting a Car

1. Select a car from the search results.
2. Click on "Rent Now" and complete the rental details.
3. Confirm the transaction. Payment is handled via blockchain.

### Auction System

1. Renters can list their cars for auction.
2. Users can place bids on available cars.
3. The highest bidder wins the rental rights when the auction ends (RAFT Consensus).

### Notifications

- Users can subscribe to notifications for cars that are currently unavailable.
- Notifications are sent when subscribed cars become available.

### Chat System

- Users can chat with car owners to discuss rental details such as time, date, and pick-up location.

## 🛠️ Technologies

- **Spring Boot** 
- **Blockchain** (Transaction and Payment)
- **RabbitMQ** (MS communications)
- **Docker** (Containerization)
- **PostgreSQL** (Database)
- **WebSocket** (Real-time Notification)
- **Open StreetMap API** (Geolocation)

