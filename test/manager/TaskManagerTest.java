package manager;

import interfaces.TaskManager;
import org.junit.jupiter.api.BeforeEach;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    /*
   Не понял прикола с этим классом, и без него все тесты работают и дублирования нет.
     */

    @BeforeEach
    protected abstract void setUp();


}