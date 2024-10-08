package manager;

import manager.exeptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            Pattern pattern = Pattern.compile("(Task|Epic|SubTask)\\{Название: '(.+?)', детали: '(.+?)', " +
                    "LocalDate\\.of\\((\\d{4}), (\\d{1,2}), (\\d{1,2})\\), \" +\n" +
                    "    \"LocalTime\\.of\\((\\d{1,2}), (\\d{1,2})\\), (\\d+) , " +
                    "id :([0-9]+), статус: (\\w+)(?:, epicId: ([0-9]+))?\\}");

            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String type = matcher.group(1);
                    String name = matcher.group(2);
                    String detail = matcher.group(3);
                    int id = Integer.parseInt(matcher.group(4));
                    Status status = Status.valueOf(matcher.group(5));


                    switch (type) {
                        case "Task" -> {
                            Task task = new Task(name, detail, duration, startTime, status);
                            task.setId(id);
                            manager.createTask(task);
                        }
                        case "Epic" -> {
                            Epic epic = new Epic(name, detail, status);
                            epic.setId(id);
                            manager.createEpic(epic);
                        }
                        case "SubTask" -> {
                            int epicId = Integer.parseInt(matcher.group(6));
                            SubTask subTask = new SubTask(name, detail, status, epicId);
                            subTask.setId(id);
                            manager.createSubTask(subTask);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении файла", e);
        }
        return manager;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(file)) {
            List<Task> tasks = getListTasks();
            for (Task task : tasks) {
                writer.write(task.toString() + System.lineSeparator());
            }

            List<Epic> epics = getListEpic();
            for (Epic epic : epics) {
                writer.write(epic.toString() + System.lineSeparator());
            }

            List<SubTask> subTasks = getListSubTask();
            for (SubTask subTask : subTasks) {
                writer.write(subTask.toString() + System.lineSeparator());
            }
        }  catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла: " + e.getMessage());
        }
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public Task createTask(Task newTask) {
        Task savedTask = super.createTask(newTask);
        save();
        return savedTask;
    }

    @Override
    public Task updateTask(Task updateTask) {
        Task savedTask = super.updateTask(updateTask);
        save();
        return savedTask;
    }

    @Override
    public Task deleteTask(Integer taskId) {
        Task savedTask = super.deleteTask(taskId);
        save();
        return savedTask;
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        Epic savedEpic = super.createEpic(newEpic);
        save();
        return savedEpic;
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public Epic updateEpic(Epic updateEpic) {
        Epic savedEpic =  super.updateEpic(updateEpic);
        save();
        return savedEpic;
    }

    @Override
    public Epic deleteEpic(Integer id) {
        Epic savedEpic = super.deleteEpic(id);
        save();
        return savedEpic;
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public SubTask createSubTask(SubTask newSubTask) {
        SubTask savedSubTask = super.createSubTask(newSubTask);
        save();
        return savedSubTask;
    }

    @Override
    public SubTask updateSubTask(SubTask updateSubTask) {
        SubTask savedSubTask = super.updateSubTask(updateSubTask);
        save();
        return savedSubTask;
    }

    @Override
    public SubTask deleteSubTask(Integer id) {
        SubTask savedSubTask = super.deleteSubTask(id);
        save();
        return savedSubTask;
    }
}
