package cs1302.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;

import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;

/**
 * A class that fetches a background color image from a placeholder service.
 */
public class ImageParser {

    Image image;

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    /**
     * Creates an image from the hex color code by calling a placeholder API.
     * @param color the hex code for the background (e.g., "fcba03")
     */
    public ImageParser(String color) {
        
        // 1. Build the correct, working placeholder URL
        String imageUrl = String.format("https://php-noise.com/noise.php?hex=%s", color);
        
        try {
            // 2. Create a standard request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(imageUrl))
                .build();
            
            // 3. Use our HTTP_CLIENT to get the response as an InputStream
            HttpResponse<InputStream> response = HTTP_CLIENT
                .send(request, BodyHandlers.ofInputStream());

            // 4. Create the Image from the InputStream of bytes
            this.image = new Image(response.body());

            // 5. Check if the image *still* failed
            if (this.image.isError()) {
                alertError(imageUrl, this.image.getException());
            } 
            
        } catch (Exception e) {
            // This will catch any errors (network, etc.)
            e.printStackTrace();
            alertError(imageUrl, e);
            this.image = null; // Set a null image so the app doesn't crash
        }
    } // ImageParser

    /**
     * Returns the image that was loaded.
     * @return image the image in the class
     */
    public Image getImage() {
        return this.image;
    } // getImage

    /**
     * Show a modal error alert.
     * @param url the URL that was being fetched
     * @param cause a {@link java.lang.Throwable} that caused the alert
     */
    public static void alertError(String url, Throwable cause) {
        Runnable runnable = () -> {
            TextArea text = new TextArea("URI: " + url +
                                         "\n\nException: "  + cause.toString());
            text.setEditable(false);
            text.setWrapText(true);
            Alert alert = new Alert(AlertType.ERROR);
            alert.getDialogPane().setContent(text);
            alert.setResizable(true);
            alert.showAndWait();
        };
        Platform.runLater(runnable);
    } // alertError

} // ImageParser

