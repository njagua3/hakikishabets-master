🎲 Hakikisha Bets – Betting Platform Backend
This is the backend API for Hakikisha Bets, a digital sports betting platform where users can place bets on matches using defined markets and odds.

It is built with Spring Boot and follows a clean architectural structure using DTOs, Mappers, Service, and Repository layers.

🚀 Features Implemented
✅ Users can place bets if they exist and have sufficient balance

✅ Stake is validated (positive and non-zero)

✅ Potential win is calculated based on selected odd

✅ Bets are saved with initial status as PENDING

✅ All user bets and all platform bets can be retrieved

✅ Admins can update a bet’s status to WON, LOST, or CANCELLED

✅ DTOs and Mappers are used to separate domain logic from data transfer logic

🔁 End-to-End Flow

1. 🧍 User Onboarding (outside scope)
   Users are assumed to be created via an external system.

Each user has an associated balance.

2. 💸 Placing a Bet


   Endpoint: POST /api/bets/place

Input: BetRequestDTO with userId, matchId, marketId, oddId, and stake

Validation:

User must exist

Stake must be positive

User must have enough balance.

Processing:

The stake is deducted from the user's balance

Potential win = stake × odd value

A new bet is created with status PENDING and saved

Output: BetResponseDTO

3. 🧾 Retrieving Bets
   All Bets: GET /api/bets/all

User Bets: GET /api/bets/user/{userId}
These endpoints return a list of bets using BetResponseDTO.

4. 🏁 Updating Bet Status
   Endpoint: PUT /api/bets/{betId}/status

Admin sets the final result of a bet as WON, LOST, or CANCELLED.

🧱 Module Responsibilities
1. 📂 controller – API Layer
   Handles incoming HTTP requests, validates input, and delegates processing to the service layer.

Example:
BetController handles /place, /all, /user/{id}, and status update routes.

2. 📂 service – Business Logic
   Contains the core logic for:

Checking user existence and balance

Deducting stake from the user's wallet

Calculating potential win

Updating bet status

Example:
BetService.placeBet() ensures all validations are done before saving the bet.

3. 📂 dto – Data Transfer Objects
   DTOs prevent direct exposure of entity models. They structure how data is:

Received from the client (BetRequestDTO)

Sent back to the client (BetResponseDTO)

Why this matters: Security, clarity, and maintainability.

4. 📂 mapper – Data Mapping
   BetMapper translates between entity and DTO:

BetResponseDTO toDTO(Bet bet);
Purpose: Decouples internal data representation from external data contracts.

5. 📂 model – JPA Entities
   Defines the actual database schema using Java classes.

Example:
Bet entity contains relationships to User, Match, Market, and Odd.

6. 📂 repository – Data Access Layer
   Provides CRUD operations on entities using Spring Data JPA.

Example:
BetRepository, UserRepository allow fetching and saving data to MySQL.

🧠 Validation Logic Summary
✅ Stake must be @Positive and not null

✅ User must exist

✅ User balance must be ≥ stake

❌ Bets cannot be placed with zero or negative stake

🔍 What’s Missing / Can Be Improved

Area	                     Suggested Improvement

🔐 Authentication	Integrate Spring Security and JWT to authenticate users
🧾 Wallet Module	Create a separate wallet module to track all balance changes
🕒 Bet History	Add timestamps to track status changes
📈 Reporting	Implement admin dashboards for winnings/losses/stakes per day
💬 Notifications	Notify users when bet results are updated
🧪 Testing	Add unit + integration tests for all layers
📄 Swagger Docs	Generate interactive API documentation
🌍 Match Module	Enhance match integration with real-time sports feeds

🛠️ Tech Stack
Java 17+

Spring Boot 3

Spring Data JPA

MySQL

Hibernate Validator

Lombok

Maven

📦 How to Run,

Clone the repo
git clone https://github.com/your-org/hakikisha-bets-backend.git

Configure DB in application.properties

Build the app
./mvnw clean install

Run it
./mvnw spring-boot:run


✅ User Creation Flow (Using DTOs and Mapper)

Client Sends Request:
A client sends a POST request to create a user, with the request body containing the user details.

Controller Receives UserRequestDTO:
The UserController receives the JSON payload and maps it to a UserRequestDTO object.

DTO to Entity Conversion (Mapper):
The controller uses a mapper (like UserMapper) to convert the UserRequestDTO into a User entity object.

Service Handles Business Logic:
The UserService receives the User entity. It checks:

Whether the username or email already exists.

If validation passes, it saves the user in the database using the repository.

Entity to ResponseDTO (Mapper):
After saving, the service returns the User entity back to the controller.
The controller then uses the mapper to convert the User entity into a UserResponseDTO.

Safe Response Sent to Client:
The UserResponseDTO, which excludes sensitive data like passwords, is returned in the HTTP response.



✅ User Retrieval Flow (GET request)

Client Sends GET Request:
The client (frontend or API consumer) sends a GET request — e.g., /api/users/getuser/5.

Controller Invokes Service:
The UserController calls userService.getUserById(id).

Service Returns User Entity:
The UserService fetches the User entity from the database via userRepository.findById(id) and 
returns it to the controller (wrapped in Optional<User>).

Entity to DTO Conversion:
The controller uses UserMapper.toUserResponseDto(user) to convert the User entity into a UserResponseDTO.

DTO Sent as Response:
The UserResponseDTO, containing only safe and relevant data, is returned as the HTTP response body.

✅ Bet Creation Flow (Placing a bet)

- This section explains the complete flow for creating a new bet in the system. 
- The process involves multiple components working together, from receiving the user's request to saving
the bet in the database and returning the response.

1. Client Sends a Request to Place a Bet
   Endpoint: POST /api/bets/place

Request Body: A BetRequestDTO object containing the bet details. The client (user)
sends this information in the request body.

BetRequestDTO has:

stake: The amount the user is betting (must be greater than zero).

userId: The ID of the user placing the bet.

matchId: The ID of the match on which the user is placing the bet.

marketId: The ID of the market (e.g., over/under) for the bet.

oddId: The ID of the odd (e.g., 1.5, 2.0) associated with the bet.


Validation:

The request body is validated using annotations in BetRequestDTO. This ensures that:

- The stake is not null and is a positive value.

- All other fields (userId, matchId, marketId, oddId) are not null.

- If the validation fails, a 400 Bad Request response is sent back to the client with an appropriate error message.

2. Controller Receives the Request
   Controller: BetController

Method: placeBet()

The request is handled by the BetController class. 
Specifically, the placeBet() method is invoked when a POST request is made to the /api/bets/place endpoint.

The BetRequestDTO is received in the method as the request body.

After validation, the controller delegates the actual processing to the BetService class.


3. Service Layer - Core Business Logic
   Service: BetService

Method: placeBet()

The BetService class contains the business logic for placing a bet. The placeBet() method performs the following steps:

Retrieve Related Data: It fetches the related entities (User, Game, Market, Odd) from the database based on their IDs:

userRepository.findById(userId)

gameRepository.findById(matchId)

marketRepository.findById(marketId)

oddRepository.findById(oddId)

Create the Bet Entity: Once all the necessary data is retrieved, a new Bet entity is created. The Bet entity includes:

stake: The amount the user is betting.

potentialWin: The potential winning amount, which is calculated as stake * odd.getValue().

status: Initially set to PENDING.

placedAt: The timestamp when the bet is placed (using LocalDateTime.now()).

Relationships to the User, Game, Market, and Odd entities.

Save the Bet to the Database: The new Bet entity is saved to the database using betRepository.save(bet).

Prepare Response DTO: 
A BetResponseDTO is created by mapping the saved Bet entity to the BetResponseDTO using the BetMapper.toDTO() method. 
This DTO contains the details to be sent back to the client.


4. Mapping Bet Entity to BetResponseDTO
   Mapper: BetMapper

Method: toDTO()

The BetMapper class converts the Bet entity into a BetResponseDTO object, which is returned as the 
response to the client.

Mapped Fields:

id: The unique identifier of the bet.

stake: The amount the user is betting.

potentialWin: The calculated potential win amount.

status: The current status of the bet (e.g., PENDING).

placedAt: The timestamp when the bet was placed.

userId, matchId, marketId, oddId: The IDs of the associated entities.

5. Response Sent Back to Client
   Once the BetResponseDTO is prepared by the BetService, it is returned to the client by the BetController.

Response: The response body contains the bet details, such as id, stake, potentialWin, status,
and the associated entity IDs.

Summary of the Bet Creation Flow:

Client sends a POST request to /api/bets/place with a BetRequestDTO containing bet details (stake, userId, matchId,
marketId, oddId).

BetController validates the request and delegates the processing to BetService.

BetService retrieves related entities from the database, creates a new Bet entity, calculates the potential win,
saves the bet, and maps it to a BetResponseDTO.

BetMapper maps the Bet entity to a BetResponseDTO.

BetController returns the BetResponseDTO as a response to the client.

                    LAYERS

1. Controller
   Role: Handles HTTP requests and responses.

What it does:

Receives incoming HTTP requests (GET, POST, PUT, DELETE).

Calls the service layer to perform business logic.

Returns HTTP responses (with data or status codes) back to clients (like browsers or apps).

Example:
If a client asks for market data via /api/markets/, the controller processes that request.

2. DTO (Data Transfer Object)
   Role: Defines how data is sent to or from clients.

What it does:

Contains only the fields needed for the client, hiding internal details.

Helps separate internal model/entity structure from what is exposed via APIs.

Example:
Your MarketResponseDTO might contain id, name, and odds, but hide database IDs, timestamps, or internal metadata.

3. Mapper
   Role: Converts between Model (Entity) and DTO.

What it does:

Maps data from database entities to DTOs before sending to clients.

Maps incoming DTO data to entities for saving/updating the database.

Example:
You might have a MarketMapper that converts a Market entity to a MarketResponseDTO and vice versa.

4. Model (Entity)
   Role: Represents your data structure as it exists in the database.

What it does:

Defines the database table structure via fields and annotations (e.g., @Entity, @Column).

Models relationships between tables (OneToMany, ManyToOne, etc.).

Contains full data including fields you don’t want to expose directly via APIs.

Example:
The Market entity corresponds to the markets table in your database.

5. Repository
   Role: Provides database access methods.

What it does:

Abstracts data access logic using Spring Data JPA or similar.

Performs CRUD operations (Create, Read, Update, Delete).

Can define custom queries (e.g., find markets by name and match).

Example:
MarketRepository has methods like findById, findByName, or findByNameAndMatchId.

6. Service
   Role: Contains business logic of your application.

What it does:

Calls repositories to fetch/save data.

Performs any computations, validations, or processing before returning results.

Acts as a bridge between controllers and repositories.

Example:
Your MarketService calls MarketRepository to fetch market entities, transforms them with mappers, and returns DTOs to controllers.

🧱 1. MultiBet Entity (Parent)
This represents the entire multi-bet ticket a customer places.

What it stores:
A unique id

The total stake placed

The potentialWin (calculated by multiplying all odds together and then by stake)

The status (PENDING, WON, LOST, CANCELLED)

The placedAt timestamp

A reference to the User who placed it

A list of MultiBetSelections (children)

👉 Think of it as the bet slip itself.

🧩 2. MultiBetSelection Entity (Child)
This represents one individual pick (selection) within a multi-bet.

What it stores:
A unique id

A reference to the associated Game

A reference to the selected Market

A reference to the selected Odd

A reference to the parent MultiBet

The status of this individual selection (useful later when grading)

👉 Think of it as one line in the slip, like:
Chelsea vs Arsenal → Match Winner → Chelsea @ 2.10

🧠 Why Split Them?
Data Normalization: Cleanly separates the main bet from its parts.

Reusability: Makes it easier to update individual selections later (e.g., mark one selection as WON).

Scalability: You can easily support 2-leg, 5-leg, or even 20-leg multi-bets.

Analogy:
If you think of a multi-bet like a receipt:

MultiBet is the whole receipt.

MultiBetSelection is each item on the receipt.


