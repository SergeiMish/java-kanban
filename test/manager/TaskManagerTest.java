package manager;

import interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    private LocalDate date;
    private LocalTime time;
    private LocalDate date1;
    private LocalTime time1;
    private LocalDate date2;
    private LocalTime time2;

    @BeforeEach
    void setUp() {
        date = LocalDate.now();
        time = LocalTime.now();
        date1 = LocalDate.of(2024, 10, 23);
        time1 = LocalTime.of(15, 30);
        date2 = LocalDate.of(2024, 11, 23);
        time2 = LocalTime.of(12, 30);
    }

    @Test
    void testCreateAndGetTask() {
        Task task = new Task("Task 1", "Task Detail", date, time, 30, Status.NEW);
        Task createdTask = taskManager.createTask(task);
        assertNotNull(createdTask);
        assertEquals(task, taskManager.getTaskId(createdTask.getId()));
    }

    @Test
    void testUpdateTask() {
        Task task = new Task("Task 1", "Task Detail", date, time, 30, Status.NEW);
        Task createdTask = taskManager.createTask(task);
        createdTask.setName("Updated Task");
        Task updatedTask = taskManager.updateTask(createdTask);
        assertEquals("Updated Task", updatedTask.getName());
    }

    @Test
    void testDeleteTask() {
        Task task = new Task("Task 1", "Task Detail", date, time, 30, Status.NEW);
        Task createdTask = taskManager.createTask(task);
        taskManager.deleteTask(createdTask.getId());
        assertNull(taskManager.getTaskId(createdTask.getId()));
    }

    @Test
    void testCreateAndGetEpic() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        Epic createdEpic = taskManager.createEpic(epic);
        assertNotNull(createdEpic);
        assertEquals(epic, taskManager.getEpicId(createdEpic.getId()));
    }

    @Test
    void testUpdateEpic() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        Epic createdEpic = taskManager.createEpic(epic);
        createdEpic.setName("Updated Epic");
        Epic updatedEpic = taskManager.updateEpic(createdEpic);
        assertEquals("Updated Epic", updatedEpic.getName());
    }

    @Test
    void testDeleteEpic() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        Epic createdEpic = taskManager.createEpic(epic);
        taskManager.deleteEpic(createdEpic.getId());
        assertNull(taskManager.getEpicId(createdEpic.getId()));
    }

    @Test
    void testCreateAndGetSubTask() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask 1", "SubTask Detail", date1, time1, 30, Status.NEW, epic.getId());
        SubTask createdSubTask = taskManager.createSubTask(subTask);
        assertNotNull(createdSubTask);
        assertEquals(subTask, taskManager.getSubTaskId(createdSubTask.getId()));
    }

    @Test
    void testUpdateSubTask() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask 1", "SubTask Detail", date1, time1, 30, Status.NEW, epic.getId());
        SubTask createdSubTask = taskManager.createSubTask(subTask);
        createdSubTask.setName("Updated SubTask");
        SubTask updatedSubTask = taskManager.updateSubTask(createdSubTask);
        assertEquals("Updated SubTask", updatedSubTask.getName());
    }

    @Test
    void testDeleteSubTask() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("SubTask 1", "SubTask Detail", date1, time1, 30, Status.NEW, epic.getId());
        SubTask createdSubTask = taskManager.createSubTask(subTask);
        taskManager.deleteSubTask(createdSubTask.getId());
        assertNull(taskManager.getSubTaskId(createdSubTask.getId()));
    }

    @Test
    void testGetListTasks() {
        Task task1 = new Task("Task 1", "Task Detail", date, time, 30, Status.NEW);
        Task task2 = new Task("Task 2", "Task Detail", date1, time1, 30, Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        List<Task> tasks = taskManager.getListTasks();
        assertEquals(2, tasks.size());
    }

    @Test
    void testDeleteAllTasks() {
        Task task1 = new Task("Task 1", "Task Detail", date, time, 30, Status.NEW);
        Task task2 = new Task("Task 2", "Task Detail", date1, time1, 50, Status.NEW);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.deleteAllTask();
        assertTrue(taskManager.getListTasks().isEmpty());
    }

    @Test
    void testGetListEpics() {
        Epic epic1 = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic Detail", date1, time1, 30, Status.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        List<Epic> epics = taskManager.getListEpic();
        assertEquals(2, epics.size());
    }

    @Test
    void testDeleteAllEpics() {
        Epic epic1 = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        Epic epic2 = new Epic("Epic 2", "Epic Detail", date1, time1, 30, Status.NEW);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.deleteAllEpic();
        assertTrue(taskManager.getListEpic().isEmpty());
    }

    @Test
    void testGetListSubTasks() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask Detail", date1, time1, 30, Status.NEW, epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask Detail", date2, time2, 30, Status.NEW, epic.getId());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        List<SubTask> subTasks = taskManager.getListSubTask();
        assertEquals(2, subTasks.size());
    }

    @Test
    void testDeleteAllSubTasks() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask Detail", date1, time1, 30, Status.NEW, epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask Detail", date2, time2, 30, Status.NEW, epic.getId());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        taskManager.deleteAllSubTask();
        assertTrue(taskManager.getListSubTask().isEmpty());
    }

    @Test
    void testGetListSubTasksOfEpic() {
        Epic epic = new Epic("Epic 1", "Epic Detail", date, time, 30, Status.NEW);
        taskManager.createEpic(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "SubTask Detail", date1, time1, 30, Status.NEW, epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "SubTask Detail", date2, time2, 30, Status.NEW, epic.getId());
        taskManager.createSubTask(subTask1);
        taskManager.createSubTask(subTask2);
        List<SubTask> subTasks = taskManager.getListSubTasksOfEpic(epic.getId());
        assertEquals(2, subTasks.size());
    }

    @Test
    void testGetHistory() {
        Task task = new Task("Task 1", "Task Detail", date, time, 30, Status.NEW);
        taskManager.createTask(task);
        taskManager.getTaskId(task.getId());
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }
}