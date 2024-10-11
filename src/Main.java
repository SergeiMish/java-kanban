import interfaces.TaskManager;
import manager.FileBackedTaskManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        InMemoryTaskManager manager = new InMemoryTaskManager();
        File filesOfTasks = new File("filesOfTasks.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(filesOfTasks);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalDate date1 = LocalDate.of(2024, 10, 23);
        LocalTime time1 = LocalTime.of(15, 30);
        LocalDate date2 = LocalDate.of(2024, 11, 23);
        LocalTime time2 = LocalTime.of(15, 30);
        LocalDate date3 = LocalDate.of(2024, 12, 23);
        LocalTime time3 = LocalTime.of(15, 30);
        Task task = new Task("Поход в магазин", "Покупка молока", date, time, 30, Status.NEW);
        Task task1 = new Task("Уборка", "Только кухня", date1, time1, 30, Status.IN_PROGRESS);
        Epic epic = new Epic("Поход в автосалон", "Тест драйв 3-х китайцев", date, time, 30, Status.NEW);
        SubTask subTask1 = new SubTask("Haval", "в 10.00", date2, time2, 40, Status.NEW, 3);
        SubTask subTask2 = new SubTask("Changan", "14.00", date3, time3, 50, Status.NEW, 3);
//        SubTask subTask3 = new SubTask("Cherry", "Лучше совсем не ходить", date, time, 30, Status.NEW, 3);

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubTask(subTask1);
        fileBackedTaskManager.createSubTask(subTask2);
//        fileBackedTaskManager.createSubTask(subTask3);
        fileBackedTaskManager.getPrioritizedTasks();
        fileBackedTaskManager.getPrioritizedTasks();
        printAllTasks(fileBackedTaskManager);

    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getListTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getListEpic()) {
            System.out.println(epic);

            List<SubTask> subTaskIds = manager.getListSubTasksOfEpic(epic.getId());
            for (SubTask subtaskId : subTaskIds) {
                System.out.println("--> " + subtaskId);
            }
            System.out.println("Подзадачи:");
            for (SubTask subtask : manager.getListSubTask()) {
                System.out.println(subtask);
            }
            System.out.println("История:");

            for (Task task : manager.getHistory()) {
                System.out.println(task);
            }
        }
    }
}
