package cs1302.api;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.layout.BackgroundFill;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;


/**
 * A component to display the weather conditions.
 */
public class DisplayComponent extends HBox {
    Image image;
    Text text;
    ImageView imageView;

    /**
     * A class that creates a {@code HBox} with an {@code Image} and {@code Text} in them.
     * @param image the {@code Image} being added to the object
     * @param text the {@code Text} being added to the object
     */
    public DisplayComponent (Image image,Text text) {
        this.image = image;
        this.text = new Text();
        this.text.setText(text.getText());
        this.text.setTextAlignment(TextAlignment.LEFT);
        this.imageView = new ImageView();
        this.imageView.setImage(image);
        this.imageView.setFitWidth(70);
        this.imageView.setFitHeight(70);
        this.setSpacing(10);
        this.setPrefWidth(205);
        this.setHgrow(text, Priority.ALWAYS);
        this.setAlignment(Pos.TOP_LEFT);
        this.getChildren().addAll(this.imageView,this.text);
        this.setBackground(new Background(new BackgroundFill(Color.TURQUOISE,
                                                             CornerRadii.EMPTY,
                                                             Insets.EMPTY)));
        this.setAlignment(Pos.CENTER);

    } // DisplayComponent


    /**
     * Sets the text of the {@code text} object.
     * @param text the string that the {@code text} objects text is being changed to
     */
    public void setText(String text) {
        this.text.setText(text);
    } // setText


    /**
     * Changes the {@code Image} to the new provided image.
     * @param image is the new image
     */
    public void setImage(Image image) {
        this.imageView.setImage(image);
    } // setImage

    /**
     * Returns the text of the {@code Text}.
     * @return String the text of the {@code Text} object
     */
    public String getText() {
        return text.getText();
    } // getText()


} // DisplayComponent
