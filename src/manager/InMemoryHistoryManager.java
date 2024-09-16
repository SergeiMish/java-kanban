package manager;

import interfaces.HistoryManager;
import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node first;
    private Node last;
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private final LinkedList<Task> history = new LinkedList<>();

    static class Node {
        Node previous;
        Node next;
        Task value;

        Node(Task value) {
            this.value = value;
        }
    }

    @Override
    public void add(Task task) {
        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        Node newNode = new Node(task);
        linkLast(newNode);
        nodeMap.put(task.getId(), newNode);
    }

    private void linkLast(Node newNode) {
        if (last == null) {
            first = newNode;
        } else {
            last.next = newNode;
            newNode.previous = last;
        }
        last = newNode;
    }

    public void remove(int id) {
        Node node = nodeMap.remove(id);
        if (node != null) {
            deleteLink(node);
        }
    }

    private void deleteLink(Node node) {
        Node prev = node.previous;
        Node next = node.next;

        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            node.previous = null;
        }

        if (next == null) {
            last = prev;
        } else {
            next.previous = prev;
            node.next = null;
        }

        node.value = null;
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node current = first;
        while (current != null) {
            history.add(current.value);
            current = current.next;
        }
        return history;
    }
}
