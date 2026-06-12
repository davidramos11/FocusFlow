// added to git
// edited version of David’s taskmanager class based on Task class edits I made  - Maddie

package toDoList;

import java.util.*;
import java.time.LocalDateTime;
public class TaskManager {
	
	private ArrayList<Task> taskList;
	
	
	// constructor
	public TaskManager(){
		this.taskList = new ArrayList<>();
	}
	
	
	// i misread the comment and accidently made getIndex instead of getTask lol
	// but ill keep it anyway in case it could be used
	public int getIndex(String name) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTaskName();
			if (taskName.equals(name)) {
				// task found! Remove and break
				return i;
			}
		}
		// if not found, return -1
		return -1;
	}
	
	
	// returns task based off index
	public Task getTask (int index){
		return taskList.get(index);
	}
	
	// in case needed for other classes
	public ArrayList<Task> getTaskList() {
	    return taskList;
	}
	
	// adding a task
	public void addTask(Task t) {
		taskList.add(t);
	}
	
	
	// view all tasks (called after sorting the list possibly
	public void viewAllTasks() {
		for (Task element : taskList) {
			System.out.println(element); // assuming toString in task class
		}
	}
	
	
	// removing a task
	public void deleteTask(String name) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTaskName();
			if (taskName.equals(name)) {
				// task found! Remove and break
				taskList.remove(i);
				break;
			}
		}
	}
	
	// updating a task's name
	public void updateTaskName(String name, String newName) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTaskName();
			if (taskName.equals(name)) {
				// task found! update name and break
				taskList.get(i).setTaskName(newName);
				break;
			}
		}
	}
	
	
	// updating a task's due date (extension or misinput)
	public void updateDueDate(String name, LocalDateTime dueDate) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTaskName();
			if (taskName.equals(name)) {
				// task found! update due date and break
				taskList.get(i).setDueDate(dueDate);
				break;
			}
		}
	}
	
	
	// updating a task's category
	public void updateCategory(String name, Category newCategory) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTaskName();
			if (taskName.equals(name)) {
				// task found! update category and break
				taskList.get(i).setCategory(newCategory);
				break;
			}
		}
	}
	
	
	// updating a tasks's priority
	public void updatePriority(String name, Level newPriority) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTaskName();
			if (taskName.equals(name)) {
				// task found! update priority and break
				taskList.get(i).setPriority(newPriority);
				break;
			}
		}
	}
	
	// to mark a task as complete
	public void markComplete(String name) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTaskName();
			if (taskName.equals(name)) {
				// task found! update completion and break
				taskList.get(i).setCompletionStatus(true);
				break;
			}
		}
	}
	
	// undo a mark complete
	public void markUnComplete(String name) {
		for (int i = 0; i < taskList.size(); i++) {
			String taskName = taskList.get(i).getTaskName();
			if (taskName.equals(name)) {
				// task found! update completion and break
				taskList.get(i).setCompletionStatus(false);
				break;
			}
		}
	}
	
	// sorting list by deadline
	public void sortByDeadline() {
	    Collections.sort(taskList, (t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()));
	}
	
	// sorting list by priority. important: this will compare based off how the enum is declared
	// if we want it sorted by high -> med -> low, the enum must be declared in that order

	public void sortByPriority() {
	    Collections.sort(taskList, (t1, t2) -> t1.getPriority().compareTo(t2.getPriority()));
	}
	
}


