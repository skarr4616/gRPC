# gRPC Server

This repository contains the basic implementation of CRUD operations using gRPC and Protobuf.

## Versions
- JDK Version: 17.0.10
- Gradle: 8.5
- gRPC Version: 1.64.0
- Protobuf Version: 0.9.4
- Protoc Version: 3.25.1

## Build
To build the application run the following commands.
```
    git clone https://github.com/skarr4616/gRPC.git
    cd gRPC/Server
    ./gradlew clean build
```
## Run
To the run the application, run the following command after building the project.
```
   ./gradlew run
```
## Source Code
The relevant source code file are mentioned below:
- `./src/main/proto/server.proto` contains the message and RPC description.
- `./src/main/java/org/example/BookServer.java` is the starting point of the server.
- `./src/main/java/org/example/BookService.java` implements the gRPC API endpoints.
- `./src/main/java/org/example/BookStore.java` is the in-memory data storage that store the books. It also has other relevant methods related to the querying of books.

## Expected Behaviour

Every request must contain an `userId`. Every `Book` should contain the following details:
1. ISBN
2. Title
3. List of Authors
4. Page Count

### Add Book
- Accepts an userId and Book as parameters.
- Adds the book to the `BookStore`.
- If successful, returns a response with the ISBN and success message to the client.

### Update Book
- Accepts an userId and Book as parameters.
- Updates the book only if the book is present in the `BookStore`.
- If successful, returns a response with the ISBN and success message to the client.

### Delete Book
- Accepts an userId and the ISBN as parameters.
- Deletes the book from the `BookStore` if present.
- If successful, returns a response with the ISBN and success message to the client.

### Get Book
- Accepts an userId and a list of ISBNs as parameters.
- Retrieves a list of books of the ISBNs available.
- If successful, returns a response with the list of books and success message to the client.

## To-Do
- Need to figure out behaviour if no ISBN is sent in `getBook`.
- Add ISBN validity checks for the books.
- Add unit test cases for the classes.


