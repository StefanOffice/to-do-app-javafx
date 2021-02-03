package stef.todoapp;

import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import stef.todoapp.model.TaskItem;
import stef.todoapp.model.Tasks;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
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
    @FXML
    private ContextMenu taskContextMenu;
    @FXML
    private CheckBox btnFilterTasksByTime;
    private FilteredList<TaskItem> filteredTaskList;
    
    public void initialize() {
        
        //display the description based on the selected task
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
    
        //show everything by default
        filteredTaskList = new FilteredList<>(Tasks.getInstance().getTaskList(), taskItem -> true);
        
        //sort the list of tasks
        SortedList<TaskItem> sortedTaskList =
                new SortedList<>(filteredTaskList,
                        Comparator.comparing(TaskItem::getTaskDate));
        
        //connect the list view with data
        taskListView.setItems(sortedTaskList);
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
                            if (task.getTaskDate().isBefore(LocalDate.now()))
                                setTextFill(Color.RED);
                                //if task's deadline is today color it blue
                            else if (task.getTaskDate().equals(LocalDate.now()))
                                setTextFill(Color.BLUE);
                            else
                                setTextFill(Color.BLACK);
                        }
                    }
                };
                
                //show context menu when task is right-clicked
                cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                    if (isNowEmpty)
                        cell.setContextMenu(null);
                    else
                        cell.setContextMenu(taskContextMenu);
                });
                
                return cell;
            }
        });
        
        //Context menu setup
        taskContextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("Delete...");
        delete.setOnAction(event -> {
            TaskItem task = taskListView.getSelectionModel().getSelectedItem();
            deleteTask(task);
        });
        taskContextMenu.getItems().add(delete);
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
    
    public void deleteTask(TaskItem task) {
        //first display the confirmation box to give the user a chance to canel
        Alert confirmationBox = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationBox.setTitle("Delete task...");
        confirmationBox.setHeaderText("Are you sure?");
        confirmationBox.setContentText(String.format("If you press ok the task with title: '%s' will be deleted. \nPress 'cancel' to go back.", task.getTaskTitle()));
        
        //launch the confirmation window and wait for the user to click a button
        Optional<ButtonType> clickedButton = confirmationBox.showAndWait();
        //if user clicked ok then continue with deletion
        if (clickedButton.isPresent() && clickedButton.get().equals(ButtonType.OK)) {
            Tasks.getInstance().deleteTask(task);
        }
    }
    
    /**
     * alternative to context menu, runs when user releases a key
     *
     * @param keyEvent - contains information the event that triggered the method
     */
    @FXML
    public void handleKeyReleased(KeyEvent keyEvent) {
        TaskItem selectedTask = taskListView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteTask(selectedTask);
            }
        }
    }
    
    @FXML
    public void handleToggleFilter(){
        if(btnFilterTasksByTime.isSelected()){
            //show only today's items
            filteredTaskList.setPredicate(taskItem -> taskItem.getTaskDate().equals(LocalDate.now()));
            taskListView.getSelectionModel().selectFirst();
            if(taskListView.getSelectionModel().getSelectedItem() == null) {
                descriptionArea.setText("");
                dateArea.setText("");
            }
        }else{
            filteredTaskList.setPredicate(taskItem -> true);
            taskListView.getSelectionModel().selectFirst();
        }
    }
    
    @FXML
    public void handleClickExit(){
        Platform.exit();
    }
    
}
