/* Copyright rememberjava.com. Licensed under GPL 3. See http://rememberjava.com/license */
package acpcommander.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

@SuppressWarnings("restriction")
public class StaticFileHandler implements HttpHandler {

    private final String baseDir;

    public StaticFileHandler(String baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        URI uri = ex.getRequestURI();
        String name = new File(uri.getPath()).getName();
        File path = new File(baseDir, name);

        Headers h = ex.getResponseHeaders();
        // Could be more clever about the content type based on the filename here.
        h.add("Content-Type", "text/html");

        OutputStream out = ex.getResponseBody();

        if (path.exists()) {
            ex.sendResponseHeaders(200, path.length());
            out.write(Files.readAllBytes(path.toPath()));
        } else {
            System.err.println("File not found: " + path.getAbsolutePath());

            ex.sendResponseHeaders(404, 0);
            out.write("404 File not found.".getBytes());
        }

        out.close();
    }

}
