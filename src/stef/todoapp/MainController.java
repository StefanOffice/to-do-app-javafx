package stef.todoapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import stef.todoapp.model.TaskItem;
import stef.todoapp.model.Tasks;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;


public class MainController {
    
    //define references to elements in UI
    //and connect objects defined in mainview.xml
    @FXML
    private ListView<TaskItem> taskListView;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label dateArea;
    @FXML
    private BorderPane mainViewPane;
    
    public void initialize() {
        
        taskListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                //grab the selected task
                TaskItem task = taskListView.getSelectionModel().getSelectedItem();
                //display the description
                descriptionArea.setText(task.getTaskDescription());
                //display formatted date
                dateArea.setText("Deadline: " + task.getTaskDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
            }
        });
        
        //connect the list view with data
        taskListView.getItems().setAll(Tasks.getInstance().getTaskList());
        taskListView.getSelectionModel().selectFirst();
    }
    
    @FXML
    public void showNewItemWindow(){
        Dialog<ButtonType> newTaksDialog = new Dialog<>();
        newTaksDialog.initOwner(mainViewPane.getScene().getWindow());
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newtaskview.fxml"));
        
        try {
            //instruct the dialog to use definition in newtaskview.fxml to create itself
            Parent parent = FXMLLoader.load(getClass().getResource("newtaskview.fxml"));
            newTaksDialog.getDialogPane().setContent(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        newTaksDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        newTaksDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
    
        Optional<ButtonType> result = newTaksDialog.showAndWait();
    }
    
}
