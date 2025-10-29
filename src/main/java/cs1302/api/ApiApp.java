package cs1302.api;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import java.io.IOException;


/**
 * A weather app that displays facts about the weather.
 */
public class ApiApp extends Application {
    Stage stage;
    Scene scene;
    VBox root;
    Text text;
    TextField addressInput;
    TextField dateStartInput;
    TextField dateStopInput;
    Text infoText;
    DisplayComponent[] displayComponentArr;
    Image image;
    Button button;
    HBox hbox;
    HBox topHbox;
    HBox middleHbox;
    HBox bottomHbox;
    String input;
    String dateStart;
    String monthStart;
    String dayStart;
    String yearStart;
    String date;
    String dateStop;
    String dayStop;
    String yearStop;
    String monthStop;
    Label label;
    Label endLabel;
    Label queryLabel;

    /** Image displaying a temperature icon.*/
    public static final Image TEMP_IMG = new Image(
        "file:resources/m2i8A0m2N4m2m2i8.jpg");

    /** Image displaying a max temperature icon.*/
    public static final Image MAX_TEMP_IMG = new Image("file:resources/thermometer-icon-high-" +
                                                     "temperature-symbol-260nw-255255913.jpg");
    /** Image displaying a minimum temperature icon.*/
    public static final Image MIN_TEMP_IMG = new Image("file:resources/103729981" +
                                                     "-thermometer-vector-icon" +
                                                     "-with-blue-cold-low-" +
                                                     "temperature-scale-for-weather" +
                                                     "-or-medicine.jpg");

    /** Image displaying a uv index icon.*/
    public static final Image UVINDEX_IMG = new Image("file:resources/sun-symbol-" +
                                                     "emoji-1024x1024-ys649vi7.png");

    /** Image displaying a sunrise icon.*/
    public static final Image SUNRISE_IMG = new Image("file:resources/74037-200.png");

    /** Image displaying a sunset icon.*/
    public static final Image SUNSET_IMG = new Image("file:resources/360_F_571087114_" +
                                                    "yo64iLvQiqjZ6FbrPNoIdsuEcjG3nwRQ.jpg");


    /**
     * Constructs an {@code ApiApp} object. This default (i.e., no argument)
     * constructor is executed in Step 2 of the JavaFX Application Life-Cycle.
     */
    public ApiApp() {
        queryLabel = new Label("Queries left: 1000");
        text = new Text("Weather App");
        root = new VBox(7);
        hbox = new HBox(3);
        button = new Button("See Forcast");
        topHbox = new HBox(5);
        middleHbox = new HBox(5);
        bottomHbox = new HBox(5);
        image = new Image("file:resources/readme-banner.png");
        addressInput = new TextField("Enter zip code here:");
        dateStartInput = new TextField("xx/xx/xxxx");
        dateStopInput = new TextField("xx/xx/xxxx");
        label = new Label("Type start Month/Day/Year then the stop date. " +
                          "Ex: 30051 01/15/2022 01/18/2022");
        endLabel = new Label("Api's provided by https://php-noise.com" +
                             " and https://www.visualcrossing.com");
        infoText = new Text("https://weather.visualcrossing.com");
        this.displayComponentArr = new DisplayComponent[6];
        this.displayComponentArr[0] =
            new DisplayComponent(ApiApp.TEMP_IMG,new Text("Temperature:"));
        this.displayComponentArr[1] =
            new DisplayComponent(ApiApp.MAX_TEMP_IMG,new Text("Max Temperature:"));
        this.displayComponentArr[2] =
            new DisplayComponent(ApiApp.MIN_TEMP_IMG, new Text("Min Temperature:"));
        this.displayComponentArr[3] =
            new DisplayComponent(ApiApp.UVINDEX_IMG,new Text("UV Index:"));
        this.displayComponentArr[4] =
            new DisplayComponent(ApiApp.SUNRISE_IMG,new Text("Sunrise:"));
        this.displayComponentArr[5] =
            new DisplayComponent(ApiApp.SUNSET_IMG,new Text("Sunset:"));
    } // ApiApp


    /** {@inheritDoc} */
    @Override
    public void init() {
        label.setFont(new Font(11));
        endLabel.setFont(new Font(11));
        text.setFont(new Font("Comic Sans MS",20));
        dateStopInput.setMaxWidth(95.0);
        dateStartInput.setMaxWidth(95.0);
        queryLabel.setTextAlignment(TextAlignment.LEFT);
        text.setTextAlignment(TextAlignment.CENTER);
        root.getChildren().addAll(text,queryLabel,
                                  label,hbox,infoText,topHbox,middleHbox,bottomHbox,endLabel);
        topHbox.getChildren().addAll(displayComponentArr[0],displayComponentArr[1]);
        middleHbox.getChildren().addAll(displayComponentArr[2],displayComponentArr[3]);
        bottomHbox.getChildren().addAll(displayComponentArr[4],displayComponentArr[5]);

        button.setOnAction(this.buttonHandler());

        // setup scene
        hbox.setHgrow(addressInput, Priority.ALWAYS);
        hbox.setPadding(new Insets(5));
        hbox.getChildren().addAll(addressInput,dateStartInput,dateStopInput,button);
        root.setAlignment(Pos.CENTER);
        topHbox.setAlignment(Pos.CENTER);
        middleHbox.setAlignment(Pos.CENTER);
        bottomHbox.setAlignment(Pos.CENTER);
        scene = new Scene(root);
        root.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE,
                                                             CornerRadii.EMPTY,
                                                             Insets.EMPTY)));

    } // init


    /** {@inheritDoc} */
    @Override
    public void start(Stage stage) {

        this.stage = stage;

        scene.setFill(Color.AQUA);

        // setup stage
        stage.setTitle("ApiApp!");
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> Platform.exit());
        stage.sizeToScene();
        stage.show();

    } // start


    /**
     * This formats the date in a way to be inserted into the url.
     * @param start if true it formats the start date else the stop date
     */
    public void dateCreator(Boolean start) {
        this.dateStart = dateStartInput.getText();
        this.dateStop = dateStopInput.getText();
        String temp = "";
        if (start) {

            temp = this.dateStart;
            this.monthStart = temp.substring(0,temp.indexOf("/"));
            temp = temp.substring(temp.indexOf("/") + 1);
            this.dayStart = temp.substring(0,temp.indexOf("/"));
            this.yearStart = temp.substring(temp.indexOf("/") + 1);
        } else {

            temp = this.dateStop;
            this.monthStop = temp.substring(0,temp.indexOf("/"));
            temp = temp.substring(temp.indexOf("/") + 1);
            this.dayStop = temp.substring(0,temp.indexOf("/"));
            this.yearStop = temp.substring(temp.indexOf("/") + 1);

        }
    } // dateCreator

    /**
     * Creates an eventHandler for the search button that sets up the scene with the weather and
     * sets up the background color.
     * @return Eventhandler this is what is ran when the button is pressed
     *
     */
    public EventHandler <ActionEvent>  buttonHandler() {
        EventHandler <ActionEvent> eventHandler = (ActionEvent e) -> {
            button.setDisable(true);
            Runnable runner = () -> {
                this.input = addressInput.getText();
                this.dateCreator(true);
                this.dateCreator(false);
                this.date = String.format(
                    '/' + yearStart + '-' + monthStart + '-' +
                    dayStart + '/' + yearStop + '-' + monthStop + '-' + dayStop);
                Runnable runner2 = () -> {
                    this.infoText.setFont(new Font(12));
                    this.infoText.setText("Loading...");
                };
                Platform.runLater(runner2);
                String url = OpenLibrarySearchApi.returnUrl(input,date);
                Text color = new Text();
                try {
                    DisplayComponent[] tempDisplayComponentArr =
                        OpenLibrarySearchApi.openLibraryResultReturn(input,date,color);
                    Runnable runner3 = () -> {
                        infoText.setText(url);
                        infoText.setFont(new Font(4));
                    };
                    Platform.runLater(runner3);
                    this.changeBackground(color.getText());
                    Runnable runnable = () -> {
                        try {
                            for (int i = 0; i < tempDisplayComponentArr.length;i++) {
                                displayComponentArr[i].setText(
                                    tempDisplayComponentArr[i].getText());
                            } // for
                        } catch (NullPointerException f) {
                            OpenLibrarySearchApi.alertError(input,date,f);
                        } // try
                    };
                    Platform.runLater(runnable);
                } catch (IllegalArgumentException | IOException | InterruptedException g) {
                    OpenLibrarySearchApi.alertError(input,date,g);
                    Runnable textRunnable = () -> {
                        infoText.setText("Invalid Url");
                        infoText.setFont(new Font(12));
                    };
                    Platform.runLater(textRunnable);
                } // try
                Runnable pause = () -> {
                    queryLabel.setText("Queries left: " + OpenLibrarySearchApi.queryCount);
                    button.setDisable(false);

                };
                Platform.runLater(pause);
            };
            Thread thread = new Thread(runner);
            thread.setDaemon(true);
            thread.start();
        };

        return eventHandler;
    } // buttonHandler



    /**
     * Changes the background of the app.
     * @param color the color the background is being changed to
     */
    public void changeBackground(String color) {
        ImageParser imageParser = new ImageParser(color);
        Runnable runnable = () -> {
            BackgroundImage backgroundImage = new BackgroundImage(
                imageParser.getImage(),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
                );

            Background background = new Background(backgroundImage);


            this.root.setBackground(background);

        };
        Platform.runLater(runnable);
    } // changeBackground
} // ApiApp
