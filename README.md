# 🚗 Car Rental Web Application

Welcome to the Car Rental Application! This application allows users to search and rent cars, participate in auctions, and manage transactions using blockchain technology. The system is built using a microservices architecture with Spring Boot, and includes features like JWT authentication, real-time notifications, and geolocation-based car listings.


## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
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

![Architecture Diagram](path/to/your/architecture/image)

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Spring Boot** 3.x or higher
- **Docker** (for containerization)
- **PostgreSQL** as DBMS

### Installation

1. **Clone the Repository**:
    ```sh
    git clone <url>
    cd car-rental-app
    ```

2. **Run RabbitMQ**:
    ```sh
    docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management
    ```   

3. **Build and Run with Docker**:
    ```sh
    docker-compose up --build
    ```

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

