package org.example;

import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;

public class BookService extends BookServiceGrpc.BookServiceImplBase {

    @Override
    public void addBook(AddBookRequest request, StreamObserver<AddBookResponse> responseObserver) {

        // Add book to the BookStore
        Book book = request.getBook();
        BookStore store = BookStore.getInstance();
        store.addBook(book);

        // Send response to the client
        AddBookResponse response = AddBookResponse.newBuilder()
                .setUserId(request.getUserId())
                .setIsbn(book.getIsbn())
                .setResponse("Book with ISBN " + book.getIsbn() + " was added successfully")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> responseObserver) {

        UpdateBookResponse.Builder builder = UpdateBookResponse.newBuilder();
        builder.setUserId(request.getUserId());

        Book book = request.getBook();
        BookStore store = BookStore.getInstance();

        builder.setIsbn(book.getIsbn());

        if (!store.hasBook(book.getIsbn())) {
            builder.setResponse("Book with ISBN " + book.getIsbn() + " was not found");
        } else {
            store.addBook(book);
            builder.setResponse("Book with ISBN " + book.getIsbn() + " was updated successfully");
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {

        DeleteBookResponse.Builder builder = DeleteBookResponse.newBuilder();
        builder.setUserId(request.getUserId());
        builder.setIsbn(request.getIsbn());

        String isbn = request.getIsbn();

        BookStore store = BookStore.getInstance();
        if (store.removeBook(isbn)) {
            builder.setResponse("Book with ISBN " + isbn + " was deleted successfully");
        } else {
            builder.setResponse("Book with ISBN " + isbn + " was not found");
        }

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getBooks(GetBooksRequest request, StreamObserver<GetBooksResponse> responseObserver) {

        BookStore store = BookStore.getInstance();
        List<Book> bookList = new ArrayList<>();

        for (String isbn: request.getIsbnList()) {

            Book book = store.getBook(isbn);
            if (book != null) {
                bookList.add(book);
            }
        }

        GetBooksResponse.Builder builder = GetBooksResponse.newBuilder();
        builder.setUserId(request.getUserId());
        builder.addAllBook(bookList);
        builder.setResponse("Books found");

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
