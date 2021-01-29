package stef.todoapp;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import stef.todoapp.model.TaskItem;
import stef.todoapp.model.Tasks;

import java.time.LocalDate;

public class NewTaskController {

    @FXML
    private TextField titleArea;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private DatePicker datePicker;
    
    /**
     * Reads Data from the dialog and uses it to create a new task
     * @return - the task that was created
     */
    public TaskItem saveNewTaskData(){
        String title = titleArea.getText();
        String description = descriptionArea.getText();
        LocalDate deadline = datePicker.getValue();
    
        TaskItem task = new TaskItem(title, description, deadline);
        Tasks.getInstance().addTask(task);
        return task;
    }

}
