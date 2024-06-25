package org.example;

import java.util.HashMap;
import java.util.Map;

public class BookStore {

    private final static BookStore instance;
    Map<String, Book> books = new HashMap<>();

    private BookStore() {}

    static {
        try {
            instance = new BookStore();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred while initializing BookStore");
        }
    }

    public static BookStore getInstance() {
        return instance;
    }

    public void addBook(Book book) {
        books.put(book.getIsbn(), book);
    }

    public Book getBook(String isbn) {

        if (books.containsKey(isbn)) {
            return books.get(isbn);
        }

        return null;
    }

    public Boolean removeBook(String isbn) {
        if (books.containsKey(isbn)) {
            books.remove(isbn);
            return true;
        }

        return false;
    }

    public Boolean hasBook(String isbn) {
        return books.containsKey(isbn);
    }
}
