package cs1302.api;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Platform;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Example using Open Library Search API.
 *
 * <p>
 * To run this example on Odin, use the following commands:
 *
 * <pre>
 * $ mvn clean compile
 * $ mvn exec:java -Dexec.mainClass=cs1302.api.OpenLibrarySearchApi
 * </pre>
 */
public class OpenLibrarySearchApi {


    /**
     * Represents an Open Library Search API document.
     */
    private static class OpenLibraryDoc {
        String temp;
        String tempmax;
        String tempmin;
        String uvindex;
        String sunrise;
        String sunset;
        String conditions;
    } // OpenLibraryDoc

    /** Amount of queries remaining.*/
    public static int queryCount = 1000;

    private static final String API_KEY = "QZNVY2FUF2VBFZZK92W3BFV2G";

    /**
     * Represents an Open Library Search API result.
     */
    private static class OpenLibraryResult {
        int queryCost;
        String description;
        OpenLibraryDoc[] days;
    } // OpenLibraryResult

    /** HTTP client. */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)           // uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL)  // always redirects, except from HTTPS to HTTP
        .build();                                     // builds and returns a HttpClient object

    /** Google {@code Gson} object for parsing JSON-formatted strings. */
    public static Gson GSON = new GsonBuilder()
        .setPrettyPrinting()                          // enable nice output when printing
        .create();                                    // builds and returns a Gson object

    private static final String ENDPOINT =
        "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/%20";


    /**
     * Creates the DisplayComponents for the javafx and searches for the inputed string
     at the given date.
     * @param input the zip code of the weather
     * @param date the date of when the weather from that day to 3 days in the future
     * @param color the Text used to store the color in
     * @return a DisplayComponent array houseing all of the information.
     */
    public static DisplayComponent[]  openLibraryResultReturn(
        String input, String date,Text color) throws
        IOException,InterruptedException,IllegalArgumentException {
        DisplayComponent[] temp = new DisplayComponent[6];
        try {
            OpenLibrarySearchApi.search(input.strip(), date.strip())
                .ifPresent(response -> setDisplayComponentArr( groupResult(response,color),temp));
            return temp;
        } catch  (IllegalArgumentException | IOException | InterruptedException e) {
            throw e;
        } // try
    } // OpenLibraryResultReturn



    /**
     * copys the information from one array to the other array.
     * @param temp the array that is getting copied
     * @param actual the array
     */
    public static void setDisplayComponentArr (DisplayComponent[] temp, DisplayComponent[] actual) {
        for (int i = 0; i < temp.length; i++ ) {
            actual[i] = temp[i];
        } // for
    } // setDisplayComponentArr



    /**
     * Takes in the parsed information and stores it in an array of DisplayComponents.
     * @param result the ope library search result
     * @param color the Text used to store the color value
     * @return DisplayComponet[] used to store all of the information being displayed
     */
    private static DisplayComponent[] groupResult(OpenLibraryResult result, Text color) {
        color.setText(returnColorHex(result));
        queryCount -= result.queryCost;
        DisplayComponent[] displayComponentArr = new DisplayComponent[6];
        double tempTempMax = 0.0;
        double tempTemp = 0.0;
        double tempTempMin = 0.0;
        double tempUvIndex = 0.0;
        int daysCount = 0;
        for (OpenLibraryDoc doc: result.days) {
            daysCount++;
            tempTempMax += Double.parseDouble(doc.tempmax);
            tempTemp += Double.parseDouble(doc.temp);
            tempTempMin += Double.parseDouble(doc.tempmin);
            tempUvIndex += Double.parseDouble(doc.uvindex);
        } // for
        tempTempMax = ((int)(tempTempMax / daysCount * 100.0)) / 100.0;
        tempTempMin = ((int)(tempTempMin / daysCount * 100.0)) / 100.0;
        tempTemp = ((int)(tempTemp / daysCount * 100.0)) / 100.0;
        tempUvIndex = ((int)(tempUvIndex / daysCount * 100.0)) / 100.0;
        displayComponentArr[0] = new DisplayComponent(ApiApp.TEMP_IMG,
                                                      new Text("Temperature:\n" + tempTemp));
        displayComponentArr[1] =
            new DisplayComponent(ApiApp.MAX_TEMP_IMG,new Text("Max Temperature:\n" + tempTempMax));
        displayComponentArr[2] =
            new DisplayComponent(ApiApp.MIN_TEMP_IMG,new Text("Min Temperature:\n" + tempTempMin));
        displayComponentArr[3] = new DisplayComponent(ApiApp.UVINDEX_IMG,
                                                      new Text("UV Index:\n" + tempUvIndex));
        displayComponentArr[4] =
            new DisplayComponent(ApiApp.SUNRISE_IMG,
                                 new Text("Sunrise:\n" + result.days[
                                              (int)(result.days.length / 2.0)].sunrise));
        displayComponentArr[5] =
            new DisplayComponent(ApiApp.SUNSET_IMG,new Text(
                                     "Sunset:\n" + result.days[
                                         (int)(result.days.length / 2.0)].sunset));
        return displayComponentArr;
    } // groupResult



    /**
     * Takes in the data and checks to see what color corrosponds with it.
     * @param input the zip code being searched
     * @param date the date at which the weather being checked is at
     * @return a string represtation of the color in the form of hex numbers.
     */
    public static String returnColor(String input, String date) {
        try {
            String[] color = {""};
            OpenLibrarySearchApi.search(input.strip(), date.strip())
                .ifPresent(response -> color[0] = returnColorHex(response));
            return color[0];
        } catch  (IllegalArgumentException | IOException | InterruptedException e) {
            return "#ADD8E6."; // remeber to change back to ""
        } // try
    } // OpenLibraryResultReturn



    /**
     * Returns a color hex code of a color depending on the result.
     * @param result the results from the weather api
     * @return a string representation of the color hex code of the chosen color
     */
    public static String returnColorHex(OpenLibraryResult result) {
        String[] temp = new String[result.days.length];
        int i = 0;
        for (OpenLibraryDoc doc: result.days) {
            temp[i] = doc.conditions;
            i++;
        } // for
        String color;
        final String lightBlue = "ADD8E6.";
        final String white = "f8f8ff.";
        final String darkGray = "47494a.";
        final String lightGray = "D3D3D3.";
        final String black = "070708.";
        if (temp[0].indexOf(",") != -1) {
            temp[0] = temp[0].substring(0,temp[0].indexOf(","));
        } // if
        if (temp[0].equals("Clear")) {
            color = lightBlue;
        } else if (temp[0].equals("Rain")) {
            color = darkGray;
        } else if (temp[0].equals("Partially cloudy")) {
            color = white;
        }  else if (temp[0].equals("Overcast")) {
            color = lightGray;
        } else {
            color = black;
        } // if
        return color;
    } // returnConditions


    /**
     * Return an {@code Optional} describing the root element of the JSON
     * response for a "search" query.
     * @param q query string
     * @param date the date at which the weather is taken at
     * @return an {@code Optional} describing the root element of the response
     * @throws IllegalArgumentException if the url is not constructed properly
     * @throws IOException if the http response has a status code of anything but 200
     * @throws InterruptedException if the HTTP client's {@code send} method is
     *    interrupted
     */
    public static Optional<OpenLibraryResult> search(String q, String date) throws
        IllegalArgumentException, IOException, InterruptedException {
        try {
            String url =  String.format(
                "%s%s" + date +
                "?unitGroup=us&key=" + API_KEY + "&contentType=json",
                OpenLibrarySearchApi.ENDPOINT,
                URLEncoder.encode(q, StandardCharsets.UTF_8));
            String json = OpenLibrarySearchApi.fetchString(url);
            OpenLibraryResult result = GSON.fromJson(json, OpenLibraryResult.class);
            return Optional.<OpenLibraryResult>ofNullable(result);
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            throw e;

        } // try
    } // search


    /**
     * Returns a string of the url being searched.
     * @param q the zip code being searched for
     * @param date the date at which the weather is being accesed at
     * @return a string from of the url being searched
     */

    public static String returnUrl(String q, String date) {
        String url =  String.format(
            "%s%s" + date +
            "?unitGroup=us&key=" + API_KEY + "&contentType=json",
            OpenLibrarySearchApi.ENDPOINT,
            URLEncoder.encode(q, StandardCharsets.UTF_8));
        return url;
    } // returnUrl

    /**
     * Returns the response body string data from a URI.
     * @param uri location of desired content
     * @return response body string
     * @throws IOException if an I/O error occurs when sending or receiving
     * @throws InterruptedException if the HTTP client's {@code send} method is
     *    interrupted
     */
    private static String fetchString(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        HttpResponse<String> response = HTTP_CLIENT
            .send(request, BodyHandlers.ofString());
        final int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new IOException("response status code not 200:" + statusCode);
        } // if
        return response.body().trim();
    } // fetchString


    /**
     * Show a modal error alert based on {@code cause} idea modified from previous homework.
     * @param q the zip code inputed in the search bar
     * @param date is the category inputed in the url
     * @param cause a {@link java.lang.Throwable Throwable} that caused the alert
     */
    public static void alertError(String q, String date,  Throwable cause) {
        Runnable runnable = () -> {
            TextArea text = new TextArea("URI: " + OpenLibrarySearchApi.returnUrl(q,date) +
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



} // OpenLibrarySearchApi
