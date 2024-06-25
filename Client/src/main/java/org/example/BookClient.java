package org.example;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookClient {
    private static final Logger logger = Logger.getLogger(BookClient.class.getName());
    private final String userId;
    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

    public BookClient(Channel channel) {
        userId = UUID.randomUUID().toString();
        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    private void addBook(String isbn, String title, List<String> authors, int page_count) {

        logger.info("Adding book with ISBN: " + isbn + " to server");
        Book book = Book.newBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .addAllAuthors(authors)
                .setPageCount(page_count)
                .build();

        AddBookRequest request = AddBookRequest.newBuilder()
                .setUserId(userId)
                .setBook(book)
                .build();

        AddBookResponse response;
        try {
            response = blockingStub.addBook(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC failed: {0}", e.getStatus());
            return;
        }

        logger.info("Book with ISBN: " + isbn + " added to server");
    }

    private void updateBook(String isbn, String title, List<String> authors, int page_count) {

        logger.info("Updating book with ISBN: " + isbn + " in server");
        Book book = Book.newBuilder()
                .setIsbn(isbn)
                .setTitle(title)
                .addAllAuthors(authors)
                .setPageCount(page_count)
                .build();

        UpdateBookRequest request = UpdateBookRequest.newBuilder()
                .setUserId(userId)
                .setBook(book)
                .build();

        UpdateBookResponse response;
        try {
            response = blockingStub.updateBook(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC failed: {0}", e.getStatus());
        }

        logger.info("Book with ISBN: " + isbn + " updated in server");
    }

    private void deleteBook(String isbn) {
        logger.info("Deleting book with ISBN: " + isbn);

        DeleteBookRequest request = DeleteBookRequest.newBuilder()
                .setUserId(userId)
                .setIsbn(isbn)
                .build();

        DeleteBookResponse response;
        try {
            response = blockingStub.deleteBook(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC failed: {0}", e.getStatus());
        }

        logger.info("Book with ISBN: " + isbn + " deleted from server");
    }

    private void listBooks(List<String> isbn) {
        logger.info("Listing books with given ISBNs");

    }


    public static void main(String[] args) throws Exception {

        String target = "localhost:8080";
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();



    }
}
