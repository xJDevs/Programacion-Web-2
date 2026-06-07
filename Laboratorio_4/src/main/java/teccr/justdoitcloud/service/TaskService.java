package teccr.justdoitcloud.service;

import org.springframework.stereotype.Service;
import teccr.justdoitcloud.data.Task;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.repository.TaskRepository;
import java.util.List;

// Optional es un guardrail contra valores inexistentes, es decir, en vez de retornar null, retornamos un Optional vacío.
// Esto nos obliga a manejar el caso de que el valor no exista, evitando NullPointerExceptions.
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUserId(user.getId());
    }

    public void addTaskToUser(User user, Task task) {
        task.setUserId(user.getId());
        taskRepository.save(task);
    }

    // metodo para avanzar el estado de una tarea
    // solo si la tarea existe, pertenece al usuario y no está en estado DONE
    public void advanceTask(User user, Long taskId) {

        // findById devuelve un Optional, lo que nos obliga a manejar el caso de que la tarea no exista y por eso
        // usamos el java.util.Optional
        Optional<Task> taskOpt = taskRepository.findById(taskId);

        if (taskOpt.isEmpty()) {
            return;
        }

        Task task = taskOpt.get();

        // validar que user id de la tarea coincide con el id del usuario y que la tarea no esté en estado DONE
        if (!task.getUserId().equals(user.getId()) || task.getStatus() == Task.Status.DONE) {
            return;
        }

        // como la clase Task tiene sus atributos final, no se pueden modificar despues de crear el objeto
        // por eso se crea una nueva instancia Task con los mismos datos, pero cambiando el estado al actualizado
        Task updatedTask = new Task(
                task.getId(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getDeadline(),
                nextStatus(task.getStatus())
        );
        updatedTask.setUserId(task.getUserId());

        taskRepository.save(updatedTask);
    }

    private Task.Status nextStatus(Task.Status status) {
        return switch (status) {
            case PENDING -> Task.Status.INPROGRESS;
            case INPROGRESS, DONE -> Task.Status.DONE;
        };
    }
}
