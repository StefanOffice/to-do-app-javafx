package stef.todoapp.model;

import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Tasks {
    
    private static final Tasks instance = new Tasks();
    private static final String filename = "tasks.txt";
    
    private List<TaskItem> taskList;
    
    //restrict access to the constructor
    private Tasks() {}
    //enable access to only the single (eagerly) created instance (singleton)
    public static Tasks getInstance() {
        return instance;
    }
    
    public List<TaskItem> getTaskList() {
        return taskList;
    }
    
    public void setTaskList(List<TaskItem> todoItems) {
        this.taskList = todoItems;
    }
    
    /**
     * loads tasks from memory, runs on application start
     */
    public void loadTasks() throws IOException {
        
        taskList = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
    
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String input;
            while ((input = br.readLine()) != null) {
                //break the data from one line of the file
                String[] currentTaskData = input.split("\t;");
                String title = currentTaskData[0];
                String description = currentTaskData[1];
                String dateString = currentTaskData[2];
                LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MMMM d, yyyy"));
                
                //Use the data to re-create the task object
                TaskItem currentTask = new TaskItem(title, description, date);
                //add it to the list of tasks
                taskList.add(currentTask);
            }
        
        }
    }
    
    /**
    *   Saves all current tasks to memory
     *   runs when closing the application
     */
    public void saveTasks() throws IOException {
        // path to the file where tasks should be saved
        Path path = Paths.get(filename);
        //initialize the object in charge of writing the data
        //using 'try with resources' auto-closes the writer once writing is done
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (TaskItem currentTask : taskList) {
                //reverse process of loading tasks,
                //take the data from the task file extract the data to a String, and write it to the file
                bw.write(String.format("%s\t;%s\t;%s\n",
                        currentTask.getTaskTitle(),
                        currentTask.getTaskDescription(),
                        currentTask.getTaskDate().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"))));
            }
        
        }
        
    }
    
}
