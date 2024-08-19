import Interfaces.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import Interfaces.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Поход в магазин", "Покупка молока", Status.NEW);
        Task task1 = new Task("Уборка", "Только кухня", Status.IN_PROGRESS);
        Epic epic = new Epic("Поход в автосалон", "Тест драйв 3-х китайцев", Status.NEW);
        SubTask subTask1 = new SubTask("Haval", "в 10.00", Status.NEW);
        SubTask subTask2 = new SubTask("Changan", "14.00", Status.NEW);
        SubTask subTask3 = new SubTask("Cherry", "Лучше совсем не ходить", Status.NEW);

        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask1, epic);
        taskManager.createSubTask(subTask2, epic);
        taskManager.createSubTask(subTask3, epic);
        taskManager.getListTasks();
        taskManager.getListEpic();
        taskManager.getSubTasks();
        historyManager.getHistory();
        printAllTasks(taskManager);
        history(historyManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getListTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getListEpic()) {
            System.out.println(epic);

            List<Integer> subTaskIds = manager.getAllSubTasksOfEpic(epic.getId());
            for (Integer subtaskId : subTaskIds) {
                System.out.println("--> " + subtaskId);
            }
            System.out.println("Подзадачи:");
            for (Task subtask : manager.getSubTasks()) {
                System.out.println(subtask);
            }
        }
    }

    public static void history(HistoryManager historyManager) {
        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task.toString());
        }
    }
}
