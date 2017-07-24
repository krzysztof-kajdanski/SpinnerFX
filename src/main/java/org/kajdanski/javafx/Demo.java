package org.kajdanski.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 200, 200);
        stage.setScene(scene);
        stage.setTitle("FXSpinner");
        scene.getStylesheets().add("styles.css");

        VBox vb = new VBox();
        vb.setPadding(new Insets(100, 0, 0, 100));
        vb.setSpacing(10);

        final FXSpinner spinner = new FXSpinner();
        spinner.setDelimiter(50);
        spinner.setProgress(-1);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Callable<Object>() {
            public Object call() throws Exception {
                for (int i = 1; i < 6; i++) {
                    Thread.sleep(5000);
                    spinner.setProgress(i * 20);
                }
                return null;
            }
        });

        vb.getChildren().add(spinner);

        scene.setRoot(vb);
        stage.show();
    }
}
