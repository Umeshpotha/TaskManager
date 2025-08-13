import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private static int nextId = 1;
    private int id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private LocalDate createdDate;

    public Task(String title, String description, TaskPriority priority, LocalDate dueDate) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.status = TaskStatus.TODO;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdDate = LocalDate.now();
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getCreatedDate() { return createdDate; }

    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate) && status != TaskStatus.COMPLETED;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.format("[%d] %s | %s | %s | Due: %s | Created: %s\n    %s",
            id, title, status, priority,
            dueDate != null ? dueDate.format(formatter) : "No due date",
            createdDate.format(formatter),
            description);
    }
}