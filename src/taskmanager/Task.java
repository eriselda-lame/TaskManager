/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package taskmanager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 *
 * @author User
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;
    private String title,category,priority;
    private LocalDate dueDate,createdDate,completedDate;
    private boolean  completed;
      public Task(String title, String category,
                String priority, LocalDate dueDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdDate = LocalDate.now();
        this.completed = false;
    }
     public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getCategory() {
        return category;
    }
    public String getPriority() {
        return priority;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    public LocalDate getCompletedDate() {
        return completedDate;
    }
    public boolean isCompleted() {
        return completed;
    }
    public void setCompleted(boolean completed) {
        this.completed = completed;
        this.completedDate =
            completed ? LocalDate.now() : null;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}

