package org.example;

import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.*;
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
            logger.info("Book with ISBN: " + isbn + " added to server");
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC failed: {0}", e.getStatus());
            return;
        }

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
            logger.info("Book with ISBN: " + isbn + " updated in server");
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC failed: {0}", e.getStatus());
        }

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
            logger.info("Book with ISBN: " + isbn + " deleted from server");
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC failed: {0}", e.getStatus());
        }

    }

    private void listBooks(List<String> isbn) {
        logger.info("Listing books with given ISBNs");

        GetBooksRequest request = GetBooksRequest.newBuilder()
                .setUserId(userId)
                .addAllIsbn(isbn)
                .build();

        GetBooksResponse response;
        try {
            response = blockingStub.getBooks(request);
            logger.info("Listing books with given ISBNs");
            List<Book> books = response.getBookList();
            printBooks(books);
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC failed: {0}", e.getStatus());
        }
    }

    private void printBooks(List<Book> books ) {
        for (Book book : books) {
            System.out.println("ISBN: " + book.getIsbn());
            System.out.println("Title: " + book.getTitle());

            List<String> authors = book.getAuthorsList();

            System.out.print("Authors: ");
            for (String author : authors) {
                System.out.print(author + ", ");
            }
            System.out.println();

            System.out.println("PageCount: " + book.getPageCount());
            System.out.println();
        }
    }

    private void displayPrompt() {
        System.out.println("Please choose one of the following options:");
        System.out.println("1. Add Book");
        System.out.println("2. Update Book");
        System.out.println("3. Delete Book");
        System.out.println("4. List Books");
        System.out.println("5. Exit");

        System.out.print("Enter your choice: ");
    }

    private void runApp() {

        Scanner reader = new Scanner(System.in);
        String isbn;
        String title;
        String authors;
        List<String> authorsList;
        int pageCount;

        while (true) {
            displayPrompt();

            int choice = -1;
            choice = reader.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ISBN: ");
                    isbn = reader.next();

                    System.out.print("Enter title: ");
                    title = reader.next();

                    System.out.print("Enter authors: ");
                    authors = reader.next();

                    authorsList = Arrays.asList(authors.split(","));

                    System.out.print("Enter pageCount: ");
                    pageCount = reader.nextInt();

                    addBook(isbn, title, authorsList, pageCount);
                    break;
                case 2:
                    System.out.print("Enter ISBN: ");
                    isbn = reader.next();

                    System.out.print("Enter title: ");
                    title = reader.next();

                    System.out.print("Enter authors: ");
                    authors = reader.next();

                    authorsList = Arrays.asList(authors.split(","));

                    System.out.print("Enter pageCount: ");
                    pageCount = reader.nextInt();

                    updateBook(isbn, title, authorsList, pageCount);
                    break;
                case 3:
                    System.out.print("Enter ISBN: ");
                    isbn = reader.next();

                    deleteBook(isbn);
                    break;
                case 4:
                    System.out.print("Enter ISBN: ");
                    isbn = reader.next();

                    List<String> isbnList = Arrays.asList(isbn.split(","));
                    listBooks(isbnList);
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    public static void main(String[] args) throws Exception {

        String target = "localhost:8080";
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create()).build();

        try {
            BookClient bookClient = new BookClient(channel);
            bookClient.runApp();
        } finally {
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }


    }
}
