package stef.todoapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import stef.todoapp.model.TaskItem;
import stef.todoapp.model.Tasks;

import java.io.IOException;
import java.time.LocalDate;
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
            if (newValue != null) {
                //grab the selected task
                TaskItem task = taskListView.getSelectionModel().getSelectedItem();
                //display the description
                descriptionArea.setText(task.getTaskDescription());
                //display formatted date
                dateArea.setText("Deadline: " + task.getTaskDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));
            }
        });
        
        //connect the list view with data
        taskListView.setItems(Tasks.getInstance().getTaskList());
        taskListView.getSelectionModel().selectFirst();
        
        //setting up the custom cellFactory
        //takes care of how the individual cells of the ListView are displayed
        taskListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<TaskItem> call(ListView<TaskItem> taskItemListView) {
                ListCell<TaskItem> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(TaskItem task, boolean empty) {
                        super.updateItem(task, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            //display the title of the task(toString in TaskItem is no longer required)
                            setText(task.getTaskTitle());
                            //color the text of the cell based on the deadline
                            //if deadline is overdue, color it red
                            if (task.getTaskDate().isBefore(LocalDate.now())) {
                                setTextFill(Color.RED);
                                //if task's deadline is today color it blue
                            } else if (task.getTaskDate().equals(LocalDate.now())) {
                                setTextFill(Color.BLUE);
                            }
                        }
                    }
                };
                
                return cell;
            }
        });
    }
    
    @FXML
    public void showNewItemWindow() {
        Dialog<ButtonType> newTaksDialog = new Dialog<>();
        newTaksDialog.initOwner(mainViewPane.getScene().getWindow());
        newTaksDialog.setTitle("Create a new task...");
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newtaskview.fxml"));
        
        try {
            //instruct the dialog to use definition in newtaskview.fxml to create itself
            newTaksDialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        newTaksDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        newTaksDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        
        //display the dialog and wait for a button to be clicked
        Optional<ButtonType> result = newTaksDialog.showAndWait();
        //if the clicked button is ok then save entered data
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            NewTaskController newTaskController = fxmlLoader.getController();
            TaskItem newTask = newTaskController.saveNewTaskData();
            //once user gets back to main window the newly created task will be selected
            taskListView.getSelectionModel().select(newTask);
        }
    }
    
}
