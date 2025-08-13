import java.util.*;
import java.util.stream.Collectors;
import java.io.*;
import java.time.LocalDate;

public class TaskManager {
    private List<Task> tasks;
    private final String DATA_FILE = "tasks.txt";

    public TaskManager() {
        tasks = new ArrayList<>();
        loadTasksFromFile();
    }

    public void addTask(String title, String description, TaskPriority priority, LocalDate dueDate) {
        Task task = new Task(title, description, priority, dueDate);
        tasks.add(task);
        saveTasksToFile();
        System.out.println("Task added successfully: " + task.getTitle());
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        System.out.println("\n=== ALL TASKS ===");
        for (Task task : tasks) {
            System.out.println(task);
            if (task.isOverdue()) {
                System.out.println("    ⚠️  OVERDUE!");
            }
            System.out.println();
        }
    }

    public void listTasksByStatus(TaskStatus status) {
        List<Task> filteredTasks = tasks.stream()
            .filter(task -> task.getStatus() == status)
            .collect(Collectors.toList());

        if (filteredTasks.isEmpty()) {
            System.out.println("No tasks with status: " + status);
            return;
        }
        System.out.println("\n=== TASKS - " + status.toString().toUpperCase() + " ===");
        for (Task task : filteredTasks) {
            System.out.println(task);
            System.out.println();
        }
    }

    public void updateTaskStatus(int taskId, TaskStatus newStatus) {
        Task task = findTaskById(taskId);
        if (task != null) {
            task.setStatus(newStatus);
            saveTasksToFile();
            System.out.println("Task status updated: " + task.getTitle() + " -> " + newStatus);
        } else {
            System.out.println("Task not found with ID: " + taskId);
        }
    }

    public void deleteTask(int taskId) {
        Task task = findTaskById(taskId);
        if (task != null) {
            tasks.remove(task);
            saveTasksToFile();
            System.out.println("Task deleted: " + task.getTitle());
        } else {
            System.out.println("Task not found with ID: " + taskId);
        }
    }

    public void listOverdueTasks() {
        List<Task> overdueTasks = tasks.stream()
            .filter(Task::isOverdue)
            .collect(Collectors.toList());

        if (overdueTasks.isEmpty()) {
            System.out.println("No overdue tasks!");
            return;
        }
        System.out.println("\n=== OVERDUE TASKS ===");
        for (Task task : overdueTasks) {
            System.out.println(task);
            System.out.println("    ⚠️  OVERDUE!");
            System.out.println();
        }
    }

    public void getTaskStats() {
        long totalTasks = tasks.size();
        long todoTasks = tasks.stream().filter(t -> t.getStatus() == TaskStatus.TODO).count();
        long inProgressTasks = tasks.stream().filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS).count();
        long completedTasks = tasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        long overdueTasks = tasks.stream().filter(Task::isOverdue).count();

        System.out.println("\n=== TASK STATISTICS ===");
        System.out.println("Total Tasks: " + totalTasks);
        System.out.println("To Do: " + todoTasks);
        System.out.println("In Progress: " + inProgressTasks);
        System.out.println("Completed: " + completedTasks);
        System.out.println("Overdue: " + overdueTasks);
        
        if (totalTasks > 0) {
            double completionRate = (completedTasks * 100.0) / totalTasks;
            System.out.printf("Completion Rate: %.1f%%\n", completionRate);
        }
    }

    private Task findTaskById(int id) {
        return tasks.stream()
            .filter(task -> task.getId() == id)
            .findFirst()
            .orElse(null);
    }

    private void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Task task : tasks) {
                writer.println(taskToString(task));
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    private void loadTasksFromFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = parseTaskFromString(line);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
    }

    private String taskToString(Task task) {
        return String.format("%d|%s|%s|%s|%s|%s|%s",
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            task.getStatus(),
            task.getPriority(),
            task.getDueDate(),
            task.getCreatedDate());
    }

    private Task parseTaskFromString(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length != 7) return null;

            String title = parts[1];
            String description = parts[2];
            TaskStatus status = TaskStatus.valueOf(parts[3]);
            TaskPriority priority = TaskPriority.valueOf(parts[4]);
            LocalDate dueDate = parts[5].equals("null") ? null : LocalDate.parse(parts[5]);

            Task task = new Task(title, description, priority, dueDate);
            task.setStatus(status);
            return task;
        } catch (Exception e) {
            System.err.println("Error parsing task: " + line);
            return null;
        }
    }
}