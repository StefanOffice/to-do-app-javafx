package stef.todoapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import stef.todoapp.model.TaskItem;
import stef.todoapp.model.Tasks;
import java.time.format.DateTimeFormatter;


public class Controller {
    
    //define references to elements in UI
    //and connect objects defined in mainview.xml
    @FXML
    private ListView<TaskItem> taskListView;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label dateArea;
    
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
    
}
