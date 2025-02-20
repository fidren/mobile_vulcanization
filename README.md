# Mobile Vulcanization

## Project Description
Mobile Vulcanization is an application that enables the management of vulcanization services, such as scheduling service appointments, updating client information, and offering services for both individual and corporate clients. The project aims to streamline the appointment management process and customer service through a RESTful API interface.

## Admin Panel Features
- **Client Management** – the ability to add, update, delete, and filter individual and corporate clients.
- **Appointment Management** – adding, updating, and deleting service appointments.
- **Viewing Available Appointments** – filtering and viewing available service appointments.

## Client Features
- **Scheduling Appointments** – the ability to schedule appointments for both individual and corporate clients.
- **Viewing Available Appointments** – filtering and viewing available service appointments during the scheduling process.
- **Service Category Selection and Problem Description** – selecting service categories and providing detailed problem descriptions.

## Technologies
- **Backend**: Java, Spring Boot
- **Frontend**: HTML, CSS, Bootstrap, JavaScript
- **Database**: PostgreSQL
- **Testing**: JUnit 5, Mockito
- **Build Tool**: Maven
- **Version Control System**: Git

## Requirements
- Java 21 or newer
- Maven 3.6+
- PostgreSQL

## Installation and Running the Project with Docker

To run the project using Docker, follow these steps:

1. **Clone the repository:**
    ```
    git clone https://github.com/fidren/mobile_vulcanization
    ```

2. **Navigate to the project directory:**
    ```
    cd mobilna-wulkanizacja
    ```

3. **Run the project using Docker Compose:**
    ```
    docker-compose up --build
    ```

4. **Access the application:**
   - The application will be available at: [http://localhost:8080](http://localhost:8080)
   - The admin panel will be accessible after logging in at: [http://localhost:8080/login](http://localhost:8080/login)

## Running Tests
**To run the tests, use the command:**
 ```
    mvn test
 ```

## API Endpoints
| HTTP Method | Endpoint | Description |
| --- | --- | --- |
| GET | /allClients | Get all clients |
| GET | /filteredClients | Get filtered clients |
| POST | /addClient | Add a new client |
| PUT | /client/{clientId}/update | Update client information |
| DELETE | /client/{clientId}/delete | Delete a client |
| GET | /allDates | Get all appointments |
| GET | /filteredDates | Get filtered appointments |
| GET | /allDates/free/current/{localDate} | Get available appointments by date |
| POST | /addDate | Add a new appointment |
| PUT | /date/{localDate}/update | Update appointment status |
| DELETE | /date/{localDate}/delete | Delete an appointment |

## Author
**Wojciech Mikula** - main project creator
