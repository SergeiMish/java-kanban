public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task newTa = new Task("Имя", "Детали");
        Task newTa2 = new Task("Имя", "Детали");
        taskManager.createTask(newTa, Status.NEW);
        taskManager.createTask(newTa, Status.NEW);
        System.out.println(newTa);
        System.out.println(newTa2.toString());
    }
}
