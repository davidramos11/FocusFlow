package toDoList;
import java.time.LocalDateTime;
import java.util.*;


public class Task {
   String name;
   LocalDateTime dueDate;
   Level priority;
   Category category;
   boolean completed;
   ArrayList<Subtask> subtasks;


  
   public Task(String name, LocalDateTime dueDate, Level priority, Category category) {
       this.name = name;
       this.dueDate = dueDate;
       this.priority = priority;
       this.category = category;
       completed = false;
       subtasks = new ArrayList<Subtask>();
   }


   // QUESTION - do we wanna display all the subtasks too
   public String toString() {
       String status = getCompletionDisplay();


       return "[" + priority + "] "
               + name + " | "
               + dueDate + " | "
               + category
               + status;
   }
  
  
   // getters ========================================
   public String getTaskName() {
       return name;
   }
  
   public LocalDateTime getDueDate() {
       return dueDate;
   }
  
   public Level getPriority() {
       return priority;
   }
  
   public Category getCategory() {
       return category;
   }
  
   public boolean getCompletionStatus() {
       return completed;
   }
  
   //called helper method instead of having to rewrite the entire loop - Aidan
   public double getSubtaskCompletion() {
       if (subtasks.isEmpty()) return 0.0;
       return (double) (countCompletedSubtasks() / subtasks.size());
   }
  
  
  
   // setters ========================================
   public void setTaskName(String name) {
       this.name = name;
   }
  
   public void setDueDate(LocalDateTime dueDate) {
       this.dueDate = dueDate;
   }
  
   public void setPriority(Level priority) {
       this.priority = priority;
   }
  
   public void setCompletionStatus(boolean completed) {
       this.completed = completed;
   }
  
   public void setCategory(Category category) {
       this.category = category;
   }
  
   public void addSubtask(Subtask subtask) {
       subtasks.add(subtask);
       changeCompleted(); // keeps completion status in sync - Aidan
   }
  
   public void removeSubtask(String subName) {
       subtasks.remove(new Subtask(subName));
       changeCompleted(); // keeps completion status in sync - Aidan
   }
  
   public void markSubComplete(String subName) {
       int index = subtasks.indexOf(new Subtask(subName));
       if (index == -1) {
           System.out.print("subtask not found");
       }
       else {
           subtasks.get(index).setStatus(true);
           changeCompleted(); // keeps completion status in sync - Aidan
       }
   }
  
   public void markSubIncomplete(String subName) {
       int index = subtasks.indexOf(new Subtask(subName));
       if (index == -1) {
           System.out.print("subtask not found");
       }
       else {
           subtasks.get(index).setStatus(false);
           changeCompleted(); // keeps completion status in sync - Aidan
       }
   }
      
   // change completed status if subtasks edited
   //called helper method instead of having to rewrite the entire loop - Aidan
  public void changeCompleted() {
   if (countCompletedSubtasks() == subtasks.size()) {
       completed = true;
   } else {
       completed = false;
   }
}
  
  
  
   // get display for completion (for toString)
   //called helper method instead of having to rewrite the entire loop - Aidan
   public String getCompletionDisplay() {
   if (subtasks.isEmpty()) {
       String status = completed ? " ✓" : "";
       return status;
   } else {
       int numComp = countCompletedSubtasks();
       String result = numComp + "/" + subtasks.size();
       if (numComp == subtasks.size()) {
           return (result + " ✓");
       } else {
           return result;
       }
   }
}


   //added this to remove the loop repetitiveness
   private int countCompletedSubtasks(){
       int numbCompleted = 0;
       for (int i = 0; i < subtasks.size(); i++ ){
           // check each subtask and count the ones marked complete
           if (subtasks.get(i).getStatus()){
               numbCompleted++;
           }
       }
       return numbCompleted;
   }
  
  
  
  
//  LocalDateTime specificDateTime = LocalDateTime.of(2026, 6, 2, 11, 30);


}





