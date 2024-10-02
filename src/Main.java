import interfaces.TaskManager;
import manager.FileBackedTaskManager;
import manager.Managers;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        File filesOfTasks = new File("filesOfTasks.csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(filesOfTasks);
        Task task = new Task("Поход в магазин", "Покупка молока", Status.NEW);
        Task task1 = new Task("Уборка", "Только кухня", Status.IN_PROGRESS);
        Epic epic = new Epic("Поход в автосалон", "Тест драйв 3-х китайцев", Status.NEW);
        SubTask subTask1 = new SubTask("Haval", "в 10.00", Status.NEW, 3);
        SubTask subTask2 = new SubTask("Changan", "14.00", Status.NEW, 3);
        SubTask subTask3 = new SubTask("Cherry", "Лучше совсем не ходить", Status.NEW, 3);

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createTask(task1);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubTask(subTask1);
        fileBackedTaskManager.createSubTask(subTask2);
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(filesOfTasks);
        printAllTasks(manager);

//        printAllTasks(fileBackedTaskManager);
//        taskManager.createSubTask(subTask3);
//
//
//        taskManager.getTaskId(task.getId());
//        taskManager.getTaskId(task1.getId());
//        taskManager.getEpicId(epic.getId());
//        taskManager.getSubTaskId(subTask1.getId());
//        taskManager.getSubTaskId(subTask2.getId());
//        taskManager.getSubTaskId(subTask3.getId());
//
//        printAllTasks(taskManager);
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
