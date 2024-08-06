package tasks;

import manager.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> listSubTask = new ArrayList<>();
    /*
    Я уже всю голову сломал как в эпик перетянуть данные по статусу сабтаски используя только
    ID. Не создавая тут дублирования через новый массив List<SubTask> в таком духе. Нашел вариант
    создать объект ТаскМенеджера и вызвать метод поиска по ID. Был еще вариант через геттер вызвать
    мапу с сабтасками, но он не получился как я хотел.
    Вероятно по хорошему надо создавать отдельный класс в виде dataStorage и туда засунуть вообще все массивы. Но
    возможно это усложнит задачу.
     */
    private final TaskManager taskManager = new TaskManager();

    public Epic(String name, String detail, Status status) {
        super(name, detail, status);
    }

    public void addSubTask(SubTask subTask) {
        listSubTask.add(subTask.getId());
    }

    public void removeSubTask(int subTaskId) {
        listSubTask.remove(Integer.valueOf(subTaskId));
    }

    public List<Integer> getListSubTask() {
        return listSubTask;
    }

    public void updateEpicStatus() {
        if (listSubTask.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }

        boolean allNew = true;
        boolean allDone = true;

        for (Integer subTaskId : listSubTask) {
            SubTask subTask = taskManager.findSubTask(subTaskId);
            if (subTask == null) continue;

            if (subTask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subTask.getStatus() != Status.DONE) {
                allDone = false;
            }
        }

        if (allNew) {
            setStatus(Status.NEW);
        } else if (allDone) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    public void removeAllSubtasks() {
        listSubTask.clear();
    }
}

