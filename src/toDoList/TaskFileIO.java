package toDoList;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
/*
 * File Format (tasks.txt):
 *
 * taskName
 * dueDate (LocalDate string, or "null")
 * dueTime (LocalTime string, or "null")
 * priority (LOW / MEDIUM / HIGH)
 * category (SCHOOL / WORK / PERSONAL / OTHER)
 * completionStatus (true / false)
 * subtaskCount
 * subtaskName
 * subtaskStatus (true / false)
 * ... (repeated for each subtask)
 * ---
 * (repeated for each task)
 */
public class TaskFileIO {
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
            }
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                if (t == null) {
                    System.out.println("Skipping null task at index " + i);
                    continue;
                }
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
                if (line.equals("---") || line.trim().isEmpty()) {
                    continue;
                }
                String name = line.trim();
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

    private static void writeTask(PrintWriter writer, Task t) {
        writer.println(t.getTaskName());
        writer.println(t.getDueDate() != null ? t.getDueDate().toString() : "null");
        writer.println(t.getDueTime() != null ? t.getDueTime().toString() : "null");
        writer.println(t.getPriority() != null ? t.getPriority().toString() : "null");
        writer.println(t.getCategory() != null ? t.getCategory().toString() : "null");
        writer.println(t.getCompletionStatus());
        ArrayList<Subtask> subtasks = t.subtasks;
        if (subtasks == null) {
            writer.println(0);
        } else {
            int validCount = 0;
            for (int j = 0; j < subtasks.size(); j++) {
                if (subtasks.get(j) != null) validCount++;
            }
            writer.println(validCount);
            for (int j = 0; j < subtasks.size(); j++) {
                Subtask sub = subtasks.get(j);
                if (sub == null) continue;
                writer.println(sub.getSubName() != null ? sub.getSubName() : "");
                writer.println(sub.getStatus());
            }
        }
        writer.println("---");
    }

    private static Task readTask(BufferedReader reader, String name) throws IOException {
        // --- due date ---
        String dateStr = reader.readLine();
        if (dateStr == null) {
            System.out.println("File ended unexpectedly at due date for task '" + name + "', stopping");
            return null;
        }
        LocalDate dueDate = null;
        if (!dateStr.trim().equals("null")) {
            try {
                dueDate = LocalDate.parse(dateStr.trim());
            } catch (DateTimeParseException e) {
                System.out.println("Warning: bad date format for task '" + name + "', setting to null");
            }
        }
        // --- due time ---
        String timeStr = reader.readLine();
        if (timeStr == null) {
            System.out.println("File ended unexpectedly at due time for task '" + name + "', stopping");
            return null;
        }
        LocalTime dueTime = null;
        if (!timeStr.trim().equals("null")) {
            try {
                dueTime = LocalTime.parse(timeStr.trim());
            } catch (DateTimeParseException e) {
                System.out.println("Warning: bad time format for task '" + name + "', setting to null");
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
        // build date string for Task constructor
        String dateTimeStr = "";
        if (dueDate != null) {
            dateTimeStr = dueDate.toString() + (dueTime != null ? "T" + dueTime.toString() : "T00:00");
        }
        Task t = new Task(name, dateTimeStr, priority.toString(), category.toString());
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
            System.out.println("Warning: bad subtask count for task '" + name + "', skipping task to avoid misaligned data");
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
            sub.setStatus(Boolean.parseBoolean(subStatusStr.trim()));
            t.addSubtask(sub);
        }
        t.setCompletionStatus(completed);
        return t;
    }
}