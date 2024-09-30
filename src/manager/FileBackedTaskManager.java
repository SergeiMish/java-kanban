package manager;

import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.io.*;
import java.util.Map;
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
            Pattern pattern = Pattern.compile("(Task|Epic|SubTask)\\{Название: '(.+?)', детали: '(.+?)', id :([0-9]+), статус: (\\w+)(?:, epicId: ([0-9]+))?\\}");

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
                            Task task = new Task(name, detail, status);
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
            for (Map.Entry<Integer, Task> entry : getIdToTask().entrySet()) {
                writer.write((entry.getValue()) + System.lineSeparator());
            }
            for (Map.Entry<Integer, Epic> entry1 : getIdToEpic().entrySet()) {
                writer.write(entry1.getValue() + System.lineSeparator());
            }
            for (Map.Entry<Integer, SubTask> entry2 : getIdToSubTask().entrySet()) {
                writer.write(entry2.getValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время записи файла.");
        }
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public Task createTask(Task newTask) {
        super.createTask(newTask);
        save();
        return newTask;
    }

    @Override
    public Task updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
        return updateTask;
    }

    @Override
    public Task deleteTask(Integer taskId) {
        Task task = super.deleteTask(taskId);
        save();
        return task;
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        super.createEpic(newEpic);
        save();
        return newEpic;
    }

    @Override
    public void updateEpicStatus(int epicId) {
        super.updateEpicStatus(epicId);
        save();
    }

    @Override
    public Epic updateEpic(Epic updateEpic) {
        super.updateEpic(updateEpic);
        save();
        return updateEpic;
    }

    @Override
    public Epic deleteEpic(Integer id) {
        Epic epic = super.deleteEpic(id);
        save();
        return epic;
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public SubTask createSubTask(SubTask newSubTask) {
        super.createSubTask(newSubTask);
        save();
        return newSubTask;
    }

    @Override
    public SubTask updateSubTask(SubTask updateSubTask) {
        super.updateSubTask(updateSubTask);
        save();
        return updateSubTask;
    }

    @Override
    public SubTask deleteSubTask(Integer id) {
        SubTask subTask = super.deleteSubTask(id);
        save();
        return subTask;
    }
}
