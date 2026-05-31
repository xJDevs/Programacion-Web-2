package teccr.justdoitcloud.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class User {
    private final String userName;
    private final String name;
    private final String email;
    private final Type type;
    private List<Task> tasks = new ArrayList<>();

    public enum Type {
        ADMIN,
        REGULAR
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }
}
