package app.entities;

public class Task {
    private int taskId;
    private String name;
    private boolean done;
    private int userId;

    public Task(int taskId, String name, boolean done, int userId) {
        this.taskId = taskId;
        this.name = name;
        this.done = done;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", done=" + done +
                ", userId=" + userId +
                '}';
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
