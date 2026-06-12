package toDoList;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
/*
* File Format (tasks.txt):
*
* taskName
* dueDate (LocalDateTime string, or "null")
* priority (HIGH / MEDIUM / LOW)
* category (SCHOOL / WORK / PERSONAL / OTHER)
* completionStatus (true / false)
* subtaskCount
* subtaskName
* subtaskStatus (true / false)
* ... (repeated for each subtask)
* ---
* (repeated for each task)
*
* Assumptions:
* - Task names are stored one per line.
* - File format must remain consistent between save and load.
* - Corrupted tasks may be skipped to prevent loading invalid data.
*/

public class TaskFileIO {
   // saves all tasks to a text file so they can be loaded back later
   public static void saveTasks(TaskManager manager, String filename) {
       if (manager == null) {
           System.out.println("Cannot save: task manager is null");
           return;
       }
       if (filename == null || filename.trim().isEmpty()) {
           System.out.println("Cannot save: filename is missing");
           return;
       }
       try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
           ArrayList<Task> tasks = manager.getTaskList();
           if (tasks.isEmpty()) {
               System.out.println("No tasks to save");
               // still write the file so old data does not linger on next load
           }
           for (int i = 0; i < tasks.size(); i++) {
               Task t = tasks.get(i);
               if (t == null) {
                   System.out.println("Skipping null task at index " + i);
                   continue;
               }
               // skip tasks with no name because tasks should have a valid display name
               String taskName = t.getTaskName();
               if (taskName == null || taskName.trim().isEmpty()) {
                   System.out.println("Skipping task at index " + i + ": name is empty");
                   continue;
               }
               writeTask(writer, t);
           }
           System.out.println("Tasks saved to '" + filename + "'");
       } catch (IOException e) {
           System.out.println("Error writing to file '" + filename + "': " + e.getMessage());
       }
   }
   // reads tasks back from file and loads them into the manager
   public static void loadTasks(TaskManager manager, String filename) {
       if (manager == null) {
           System.out.println("Cannot load: task manager is null");
           return;
       }
       if (filename == null || filename.trim().isEmpty()) {
           System.out.println("Cannot load: filename is missing");
           return;
       }
       try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
           String line;
           while ((line = reader.readLine()) != null) {
               // skip separators and blank lines between tasks
               if (line.equals("---") || line.trim().isEmpty()) {
                   continue;
               }
               // first non-separator line is the task name
               String name = line.trim();
               if (name.isEmpty()) {
                   System.out.println("Skipping entry: task name is empty");
                   continue;
               }
               Task t = readTask(reader, name);
               if (t != null) {
                   manager.addTask(t);
               }
           }
           System.out.println("Tasks loaded from '" + filename + "'");
       } catch (FileNotFoundException e) {
           System.out.println("File not found: '" + filename + "'");
       } catch (IOException e) {
           System.out.println("Error reading file '" + filename + "': " + e.getMessage());
       }
   }
   // writes a single task and its subtasks to the file
   private static void writeTask(PrintWriter writer, Task t) {
       writer.println(t.getTaskName());
       writer.println(t.getDueDate() != null ? t.getDueDate().toString() : "null");
       writer.println(t.getPriority() != null ? t.getPriority().toString() : "null");
       writer.println(t.getCategory() != null ? t.getCategory().toString() : "null");
       writer.println(t.getCompletionStatus());
       // accessing t.subtasks directly since Task does not have getSubtasks() yet
       // adding public ArrayList<Subtask> getSubtasks() to Task would be cleaner OOP
       ArrayList<Subtask> subtasks = t.subtasks;
       if (subtasks == null) {
           writer.println(0);
       } else {
           // count valid subtasks first so the count matches what actually gets written
           int validCount = 0;
           for (int j = 0; j < subtasks.size(); j++) {
               if (subtasks.get(j) != null) {
                   validCount++;
               }
           }
           writer.println(validCount);
           for (int j = 0; j < subtasks.size(); j++) {
               Subtask sub = subtasks.get(j);
               if (sub == null) {
                   continue;
               }
               writer.println(sub.getSubName() != null ? sub.getSubName() : "");
               writer.println(sub.getStatus());
           }
       }
       writer.println("---");
   }
   // reads and reconstructs a single task from the file
   // returns null if file is cut off or data is unreadable
   private static Task readTask(BufferedReader reader, String name) throws IOException {
       // --- due date ---
       String dateStr = reader.readLine();
       LocalDateTime dueDate = null;
       if (dateStr != null && !dateStr.trim().equals("null")) {
           try {
               dueDate = LocalDateTime.parse(dateStr.trim());
           } catch (DateTimeParseException e) {
               System.out.println("Warning: bad date format for task '" + name + "', setting to null");
               dueDate = null;
           }
       }
       // --- priority ---
       String priorityStr = reader.readLine();
       if (priorityStr == null) {
           System.out.println("File ended unexpectedly at priority for task '" + name + "', stopping");
           return null;
       }
       Level priority;
       try {
           priority = Level.valueOf(priorityStr.trim().toUpperCase());
       } catch (IllegalArgumentException e) {
           System.out.println("Warning: unrecognized priority '" + priorityStr + "' for task '" + name + "', defaulting to LOW");
           priority = Level.LOW;
       }
       // --- category ---
       String categoryStr = reader.readLine();
       if (categoryStr == null) {
           System.out.println("File ended unexpectedly at category for task '" + name + "', stopping");
           return null;
       }
       Category category;
       try {
           category = Category.valueOf(categoryStr.trim().toUpperCase());
       } catch (IllegalArgumentException e) {
           System.out.println("Warning: unrecognized category '" + categoryStr + "' for task '" + name + "', defaulting to OTHER");
           category = Category.OTHER;
       }
       // --- completion status ---
       String completedStr = reader.readLine();
       if (completedStr == null) {
           System.out.println("File ended unexpectedly at completion status for task '" + name + "', stopping");
           return null;
       }
       boolean completed = Boolean.parseBoolean(completedStr.trim());
       // build the task with the fields we have
       Task t = new Task(name, dueDate, priority, category);
       // --- subtasks ---
       String subtaskCountStr = reader.readLine();
       if (subtaskCountStr == null) {
           System.out.println("Warning: file ended before subtask count for task '" + name + "', adding task without subtasks");
           t.setCompletionStatus(completed);
           return t;
       }
       int subtaskCount;
       try {
           subtaskCount = Integer.parseInt(subtaskCountStr.trim());
       } catch (NumberFormatException e) {
           // skip task entirely rather than loading misaligned subtask data
           System.out.println("Warning: bad subtask count for task '" + name + "', skipping task to avoid data loss");
           return null;
       }
       for (int i = 0; i < subtaskCount; i++) {
           String subName = reader.readLine();
           String subStatusStr = reader.readLine();
           if (subName == null || subStatusStr == null) {
               System.out.println("Warning: file ended mid-subtask for task '" + name + "', adding task with partial subtasks");
               break;
           }
           Subtask sub = new Subtask(subName.trim());
           boolean subStatus = Boolean.parseBoolean(subStatusStr.trim());
           sub.setStatus(subStatus);
           // addSubtask keeps the subtask list in sync via changeCompleted()
           t.addSubtask(sub);
       }
       // set completion status after subtasks are loaded so the saved
       // status is preserved and not overwritten by changeCompleted()
       t.setCompletionStatus(completed);
       return t;
   }
   /*
    * Example GUI integration:
    *
    * saveBtn.addActionListener(e -> {
    *     TaskFileIO.saveTasks(manager, "tasks.txt");
    *     JOptionPane.showMessageDialog(this, "Tasks saved!");
    * });
    *
    * loadBtn.addActionListener(e -> {
    *     manager.getTaskList().clear();
    *     TaskFileIO.loadTasks(manager, "tasks.txt");
    *     refreshList();
    *     refreshCalendar();
    *     refreshWeekView();
    *     refreshDueDateView();
    *     JOptionPane.showMessageDialog(this, "Tasks loaded!");
    * });
    */
}

