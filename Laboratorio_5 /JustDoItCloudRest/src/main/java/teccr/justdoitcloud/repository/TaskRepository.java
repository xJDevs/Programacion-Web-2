package teccr.justdoitcloud.repository;

import org.springframework.data.repository.CrudRepository;
import teccr.justdoitcloud.data.Task;

import java.util.List;

/**
 * Repository abstraction for task operations.
 */
public interface TaskRepository extends CrudRepository<Task, Long> {

    /**
     * Return the list of tasks for a given username. If user doesn't exist, return empty list.
     */
    List<Task> findByUserId(Long userId);

}
