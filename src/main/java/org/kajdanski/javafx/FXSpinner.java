package org.kajdanski.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class FXSpinner extends AnchorPane
{
    private Text text;
    private Timeline timeline;
    private Rotate rotate = new Rotate(360);

    private boolean needsReset = false;

    private SimpleDoubleProperty delimiter = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty percent = new SimpleDoubleProperty(0);

    public FXSpinner()
    {
        Group group = new Group();

        text = createText();
        Arc arc = createSpinningArc();
        Arc backgroundArc = createBackgroundArc();

        group.getChildren().addAll(text, backgroundArc, arc);
        getChildren().addAll(group);
        getStyleClass().add("spark-spinner");
        setLayoutX(75);
        setLayoutY(75);

        setProgress(-1);
    }

    private Arc createBackgroundArc()
    {
        Arc backgroundArc = new Arc();
        backgroundArc.getStyleClass().add("background-arc");
        backgroundArc.setStrokeWidth(3);
        backgroundArc.setFill(Color.TRANSPARENT);
        backgroundArc.setStroke(Color.WHITE);
        backgroundArc.radiusXProperty().bind(delimiter);
        backgroundArc.radiusYProperty().bind(delimiter);
        backgroundArc.setStartAngle(90);
        backgroundArc.setLength(360);
        backgroundArc.setType(ArcType.OPEN);
        return backgroundArc;
    }

    private Arc createSpinningArc()
    {
        Arc spinningArc = new Arc();
        spinningArc.getStyleClass().add("spinning-arc");
        spinningArc.setStrokeWidth(3);
        spinningArc.setFill(Color.TRANSPARENT);
        spinningArc.setStroke(Color.GRAY);
        spinningArc.radiusXProperty().bind(delimiter);
        spinningArc.radiusYProperty().bind(delimiter);
        spinningArc.setStartAngle(90);
        spinningArc.setType(ArcType.OPEN);
        spinningArc.lengthProperty().bind(Bindings.multiply(percent, 3.6));
        spinningArc.getTransforms().add(rotate);
        return spinningArc;
    }

    private Text createText()
    {
        Text percents = new Text("0");
        percents.setTextAlignment(TextAlignment.CENTER);
        percents.getStyleClass().add("percent-text");
        percents.textProperty().bind(Bindings.multiply(percent, -1).asString("%.0f").concat("%"));
        percents.setX(-14);
        percents.setY(3);
        percents.setWrappingWidth(30);
        percents.setFill(Color.GRAY);
        return percents;
    }

    public void setProgress(int progress)
    {
        if (needsReset)
        {
            reset();
        }

        if (progress < 0)
        {
            indefinite();
        }
        else
        {
            if (progress > 100)
            {
                progress = 100;
            }
            definite(progress);
        }
    }

    private void definite(int progress)
    {
        text.setVisible(true);

        timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), new KeyValue(percent, -progress));
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void indefinite()
    {
        percent.setValue(75);
        text.setVisible(false);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setRate(-1);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(5), new KeyValue(rotate.angleProperty(), 360));

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        needsReset = true;
    }

    private void reset()
    {
        timeline.stop();
        percent.setValue(0);
        rotate.setAngle(0);
        needsReset = false;
    }

    public double getDelimiter()
    {
        return delimiter.get();
    }

    public SimpleDoubleProperty delimiterProperty()
    {
        return delimiter;
    }

    public void setDelimiter(double delimiter)
    {
        this.delimiter.set(delimiter);
    }
}
