public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task newTa = new Task("Имя", "Детали", 5);
        Task newTa2 = new Task("Имя", "Детали", 5);
        taskManager.createTask(newTa);
        taskManager.createTask(newTa2);
        taskManager.addNewTask(newTa);
        System.out.println(newTa);
    }
}
