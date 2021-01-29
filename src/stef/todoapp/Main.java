package stef.todoapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import stef.todoapp.model.Tasks;

public class Main extends Application {
    
    @Override
    public void init() throws Exception {
        Tasks.getInstance().loadTasks();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainview.fxml"));
        primaryStage.setTitle("Task View");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void stop() throws Exception {
        Tasks.getInstance().saveTasks();
    }
    
}
