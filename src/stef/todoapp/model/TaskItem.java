package stef.todoapp.model;

import java.time.LocalDate;

//using TaskItem name instead of just Task,
// to avoid confusion and name clashing with Task class from javafx.concurrent package
public class TaskItem {

    private String taskTitle;
    private String taskDescription;
    private LocalDate taskDate;
    
    public TaskItem(String taskTitle, String taskDescription, LocalDate taskDate) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
    }
    
    public String getTaskTitle() {
        return taskTitle;
    }
    
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
    
    public String getTaskDescription() {
        return taskDescription;
    }
    
    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
    
    public LocalDate getTaskDate() {
        return taskDate;
    }
    
    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }
}
