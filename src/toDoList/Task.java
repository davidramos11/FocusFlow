package toDoList;
import java.util.ArrayList; // eliana




import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;




public class Task {
  String name;
  LocalDate dueDate;
  LocalTime dueTime;
  Level priority;
  Category category;
  boolean completed;
  ArrayList<Subtask> subtasks;




  public Task(String name, String date, String priority, String category) {
      this.name = name;
      this.priority = Level.valueOf(priority.toUpperCase());
      this.category = Category.valueOf(category.toUpperCase());
      completed = false;
      subtasks = new ArrayList<>();
      try {
          if (!date.isEmpty()) {
              this.dueDate = LocalDate.parse(date.substring(0, 10));
              this.dueTime = LocalTime.parse(date.substring(11, 16));
          }
      } catch (Exception e) {
          this.dueDate = null;
          this.dueTime = null;
      }
  }




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


  public LocalDate getDueDate() {
      return dueDate;
  }


  public LocalTime getDueTime() {
      return dueTime;
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


  public double getSubtaskCompletion() {
      if (subtasks.isEmpty()) return 0.0;
      return (double) (countCompletedSubtasks() / subtasks.size());
  }




  // setters ========================================
  public void setTaskName(String name) {
      this.name = name;
  }


  public void setDueDate(LocalDate dueDate) {
      this.dueDate = dueDate;
  }


  public void setDueTime(LocalTime dueTime) {
      this.dueTime = dueTime;
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
      changeCompleted();
  }


  public void removeSubtask(String subName) {
      subtasks.remove(new Subtask(subName));
      changeCompleted();
  }


  public void markSubComplete(String subName) {
      int index = subtasks.indexOf(new Subtask(subName));
      if (index == -1) {
          System.out.print("subtask not found");
      } else {
          subtasks.get(index).setStatus(true);
          changeCompleted();
      }
  }


  public void markSubIncomplete(String subName) {
      int index = subtasks.indexOf(new Subtask(subName));
      if (index == -1) {
          System.out.print("subtask not found");
      } else {
          subtasks.get(index).setStatus(false);
          changeCompleted();
      }
  }


  public void changeCompleted() {
      if (countCompletedSubtasks() == subtasks.size()) {
          completed = true;
      } else {
          completed = false;
      }
  }


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


  private int countCompletedSubtasks() {
      int numbCompleted = 0;
      for (int i = 0; i < subtasks.size(); i++) {
          if (subtasks.get(i).getStatus()) {
              numbCompleted++;
          }
      }
      return numbCompleted;
  }


   public ArrayList<Subtask> getSubtasks() { // eliana
       return subtasks;
   }
}
 
