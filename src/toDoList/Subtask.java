package toDoList;
public class Subtask {
	String name;
	boolean status;


	// constructor
	public Subtask(String name) {
		this.name = name;
		status = false;
	}
	
	
	// getters
	public String getSubName() {
		return name;
	}
	
	public boolean getStatus() {
		return status;
	}
	
	
	// setters
	public void setSubName(String name) {
		this.name = name;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Subtask) ) {
			return false;
		}
		else {
			Subtask subtask = (Subtask) obj;
			return (this.name.equals(subtask.name));
		}
	}
}



