import manager.InMemoryTaskManager;
import manager.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
       Task task = new Task("Поход в магазин", "Покупка молока", Status.NEW);
       Task task1 = new Task("Уборка", "Только кухня", Status.IN_PROGRESS);
       Epic epic = new Epic("Поход в автосалон", "Тест драйв 3-х китайце", Status.NEW);
       SubTask subTask1 = new SubTask("Haval", "в 10.00", Status.NEW, epic.getId());
       SubTask subTask2 = new SubTask("Changan", "14.00", Status.NEW, epic.getId());
       SubTask subTask3 = new SubTask("Cherry", "Лучше совсем не ходить", Status.NEW, epic.getId());

       taskManager.createTask(task);
       taskManager.createTask(task1);
       taskManager.createEpic(epic);
       taskManager.createSubTask(subTask1, epic);
        taskManager.createSubTask(subTask2, epic);
        taskManager.createSubTask(subTask3, epic);
       taskManager.getListTasks();
       taskManager.getSubTasks();

    }
}
