package stef.todoapp;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import stef.todoapp.model.TaskItem;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    
    private List<TaskItem> tasks;
    
    //define references to elements in UI
    //and connect objects defined in mainview.xml
    @FXML
    private ListView<TaskItem> taskListView;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private Label dateArea;
    
    public void initialize() {
        
        //create example 'dummy' data
        TaskItem task1 = new TaskItem("Review the new technology",
                "Read through the documentation, take notes and listen to the Martin's presentation about it.",
                LocalDate.of(2021, Month.FEBRUARY, 15));
        TaskItem task2 = new TaskItem("Reformat the code on x project",
                "Rewrite the marked parts of the code to make the code clean concise and easy to read",
                LocalDate.of(2021, Month.FEBRUARY, 25));
        TaskItem task3 = new TaskItem("Design the data model in y project",
                "Draw the UML diagram for all Entities and their relationships",
                LocalDate.of(2021, Month.MARCH, 1));
        TaskItem task4 = new TaskItem("Prepare the presentation",
                "Write the slides, add the diagram, add more illustrations, transfer it to USB",
                LocalDate.of(2021, Month.MARCH, 5));
        TaskItem task5 = new TaskItem("John's birthday",
                "Call John to wish him a happy birthday.",
                LocalDate.of(2021, Month.JUNE, 11));
        
        tasks = new ArrayList<>(List.of(task1, task2, task3, task4, task5));
        
        //connect the list view with data
        taskListView.getItems().setAll(tasks);
    }
    
    public void handleClickTaskView(){
        //grab the selected item
        TaskItem task = taskListView.getSelectionModel().getSelectedItem();
        //display the properties from selected item
        // in appropriate views
        descriptionArea.setText(task.getTaskDescription());
        dateArea.setText("Deadline: " + task.getTaskDate().toString());
    }
}
