import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    private static TaskManager taskManager = new TaskManager();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🚀 Welcome to Task Management System!");
        System.out.println("=====================================");

        while (true) {
            showMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1 -> addNewTask();
                case 2 -> taskManager.listTasks();
                case 3 -> listTasksByStatus();
                case 4 -> updateTaskStatus();
                case 5 -> deleteTask();
                case 6 -> taskManager.listOverdueTasks();
                case 7 -> taskManager.getTaskStats();
                case 8 -> {
                    System.out.println("Thank you for using Task Management System! 👋");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void showMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Add New Task");
        System.out.println("2. List All Tasks");
        System.out.println("3. List Tasks by Status");
        System.out.println("4. Update Task Status");
        System.out.println("5. Delete Task");
        System.out.println("6. List Overdue Tasks");
        System.out.println("7. View Statistics");
        System.out.println("8. Exit");
        System.out.println("==================");
    }

    private static void addNewTask() {
        System.out.println("\n=== ADD NEW TASK ===");
        System.out.print("Task title: ");
        String title = scanner.nextLine();
        System.out.print("Task description: ");
        String description = scanner.nextLine();
        System.out.println("Priority levels: 1-LOW, 2-MEDIUM, 3-HIGH, 4-URGENT");
        int priorityChoice = getIntInput("Select priority (1-4): ");
        TaskPriority priority = TaskPriority.values()[Math.min(Math.max(priorityChoice - 1, 0), 3)];
        System.out.print("Due date (YYYY-MM-DD) or press Enter for no due date: ");
        String dueDateStr = scanner.nextLine();
        LocalDate dueDate = null;
        if (!dueDateStr.trim().isEmpty()) {
            try {
                dueDate = LocalDate.parse(dueDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. No due date set.");
            }
        }
        taskManager.addTask(title, description, priority, dueDate);
    }

    private static void listTasksByStatus() {
        System.out.println("\n=== FILTER BY STATUS ===");
        System.out.println("1. To Do");
        System.out.println("2. In Progress");
        System.out.println("3. Completed");
        System.out.println("4. Cancelled");
        int choice = getIntInput("Select status (1-4): ");
        if (choice >= 1 && choice <= 4) {
            TaskStatus status = TaskStatus.values()[choice - 1];
            taskManager.listTasksByStatus(status);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void updateTaskStatus() {
        taskManager.listTasks();
        int taskId = getIntInput("Enter task ID to update: ");
        System.out.println("\n=== UPDATE STATUS ===");
        System.out.println("1. To Do");
        System.out.println("2. In Progress");
        System.out.println("3. Completed");
        System.out.println("4. Cancelled");
        int choice = getIntInput("Select new status (1-4): ");
        if (choice >= 1 && choice <= 4) {
            TaskStatus status = TaskStatus.values()[choice - 1];
            taskManager.updateTaskStatus(taskId, status);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private static void deleteTask() {
        taskManager.listTasks();
        int taskId = getIntInput("Enter task ID to delete: ");
        System.out.print("Are you sure you want to delete this task? (y/N): ");
        String confirmation = scanner.nextLine();
        if (confirmation.toLowerCase().startsWith("y")) {
            taskManager.deleteTask(taskId);
        } else {
            System.out.println("Task deletion cancelled.");
        }
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}