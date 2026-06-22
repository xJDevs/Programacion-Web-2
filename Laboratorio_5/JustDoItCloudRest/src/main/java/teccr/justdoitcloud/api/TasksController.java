package teccr.justdoitcloud.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teccr.justdoitcloud.data.Task;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.service.TaskService;
import teccr.justdoitcloud.exception.TaskNotFoundException;
import teccr.justdoitcloud.exception.TaskForbiddenException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users/{userId}/tasks")
public class TasksController {

    private final TaskService taskService;

    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public Iterable<Task> getTasksForUser(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return taskService.getTasksForUser(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task addTaskToUser(@PathVariable Long userId,
                              @RequestBody(required = false) Task task,
                              @RequestParam(name = "autogenerate", required = false) String autogenerate) {
        User user = new User();
        user.setId(userId);

        boolean auto = autogenerate != null && (autogenerate.isEmpty() || autogenerate.equalsIgnoreCase("true"));

        if (auto) {
            // Ignorar el cuerpo y usar el generador para crear la tarea
            return taskService.autogenerateTaskForUser(user);
        }

        // Flujo normal: crear usando el Task provisto en el body
        if (task == null) {
            throw new IllegalArgumentException("Task body is required when autogenerate is not used");
        }
        return taskService.addTaskToUser(user, task);
    }


        // fix para el lab: actualmente el get solamente devuelve el id de task registrado en la db, sin validar usuario
        // esto porque al metodo de getmapping, no le estamos pasando como parametro el id del usuario, solo el id de task
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable Long id,
            @PathVariable Long userId) { // se agrega el parametro de user id
        Optional<Task> taskOpt = taskService.getTaskById(id,  userId);
        return taskOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // se agrega userId como parametro para validar que el task pertenezca al usario indicado en el path
    @PatchMapping("/{id}")
    // se cambia la firma del metodo a ResponseEntity para poder devolver HTTP status
    public ResponseEntity<Task> updateTask(@PathVariable Long id,
                           @PathVariable Long userId,
                           @RequestBody Task task) {

        try {
            return ResponseEntity.ok(taskService.updateTaskFields(id, userId, task));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (TaskForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    @DeleteMapping("/{id}")
    // la firma oriignal era void, se agrega ResponsEntity<Void> para poder devolver noContent
    public ResponseEntity<Void> deleteTask(@PathVariable Long id,
                           @PathVariable Long userId) {

        try {
            taskService.deleteTaskById(id, userId);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        }  catch (TaskForbiddenException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


    }
}
