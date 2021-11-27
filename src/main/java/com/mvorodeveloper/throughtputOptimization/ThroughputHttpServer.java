package com.mvorodeveloper.throughtputOptimization;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * HttpServer that runs on localhost:8080/search?word={word}
 * Given a word returns the count of occurrences in the book "War and Peace"
 *
 * To run Apache JMeter
 * 1. Run on the command line "java -jar ~\apache-jmeter-5.4.1\bin\ApacheJMeter.jar"
 * 2. Create a test plan with a thread group of X users
 * 3. Add a while controller with a CSV Data Set Config - a CSV file with a list of words that we want to pass. Set the variable name as "WORD".
 * 4. Add "${__jexl3("${WORD}" != "<EOF>")}" condition to the while controller.
 * 5. Add an HTTP Request to the While Controller with server name "localhost", port number 8080 and path "/search?word=${WORD}"
 * 6. Add Summary Report and View Results Tree listeners to the While Controller to see the results
 */
public class ThroughputHttpServer {

    private static final String BOOK_DESTINATION = "src/main/resources/books/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 10;

    public static void main(String[] args) throws IOException {
        String bookText = Files.readString(Path.of(BOOK_DESTINATION));
        startServer(bookText);
    }

    private static void startServer(String context) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/search", new WordCountHttpHandler(context));
        // Setting number of threads that will be reused
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        httpServer.setExecutor(executor);
        httpServer.start();
    }

    private static class WordCountHttpHandler implements HttpHandler {
        private final String text;

        public WordCountHttpHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String searchQuery = exchange.getRequestURI().getQuery();
            String[] searchAttributes = searchQuery.split("=");
            String searchWordQuery = searchAttributes[0];
            String searchByWordQuery = searchAttributes[1];

            if (!searchWordQuery.equals("word")) {
                exchange.sendResponseHeaders(400, 0);
                return;
            }

            long countOfWords = countWords(searchByWordQuery);

            byte[] response = String.valueOf(countOfWords).getBytes();
            exchange.sendResponseHeaders(200, response.length);

            // Showing the response...
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response);
            outputStream.close();
        }

        private long countWords(String word) {
            int count = 0;
            int index = 0;
            while (index >= 0) {
                index = text.indexOf(word, index);

                if(index >= 0) {
                    count++;
                    index++;
                }
            }
            return count;
        }
    }
}
