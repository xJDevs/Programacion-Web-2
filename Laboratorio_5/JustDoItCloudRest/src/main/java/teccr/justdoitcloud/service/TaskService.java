package teccr.justdoitcloud.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import teccr.justdoitcloud.data.Task;
import teccr.justdoitcloud.data.User;
import teccr.justdoitcloud.repository.TaskRepository;
import teccr.justdoitcloud.repository.UserRepository;
import teccr.justdoitcloud.service.external.taskgenerator.TaskGenerator;
import teccr.justdoitcloud.service.internal.taskarchiver.TaskArchiver;

// se importan los exceptions creados para manejar errores de forma mas clara y especifica en el servicio
import teccr.justdoitcloud.exception.TaskNotFoundException;
import teccr.justdoitcloud.exception.TaskForbiddenException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskGenerator taskGenerator;
    private final UserRepository userRepository;
    private final TaskArchiver taskArchiver;

    public TaskService(TaskRepository taskRepository,
                       TaskGenerator taskGenerator,
                       UserRepository userRepository,
                       TaskArchiver taskArchiver) {
        this.taskRepository = taskRepository;
        this.taskGenerator = taskGenerator;
        this.userRepository = userRepository;
        this.taskArchiver = taskArchiver;
    }

    public List<Task> getTasksForUser(User user) {
        return taskRepository.findByUserId(user.getId());
    }

    public Task addTaskToUser(User user, Task task) {
        task.setUserId(user.getId());
        task.setCreatedAt(LocalDateTime.now());
        Task taskCreated = taskRepository.save(task);

        Optional<User> maybeUser = userRepository.findById(user.getId());
        if (maybeUser.isPresent()) {
            taskArchiver.archiveTask("tasks-new", maybeUser.get(), taskCreated);
        }

        return taskCreated;
    }

    public Task autogenerateTaskForUser(User user) {
        // Pendiente: archivar la tarea en categoria "tasks-new" despues de creada

        Task task = taskGenerator.generateTask();
        if (task == null) {
            throw new RuntimeException("No se pudo generar la tarea automáticamente");
        }

        task.setUserId(user.getId());
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public Optional<Task> getTaskById(Long taskId, Long userId) { // se agrega param de userid

        // valida que el id del task sea valido antes de continuar
        if (taskId == null || taskId < 0) {
            return Optional.empty();
        }

        // luego obtenemos el task del repository
        // reminder: se usa optional porque findById puede devolver algo o vacio
        Optional<Task> possibleTask = taskRepository.findById(taskId);

        if (possibleTask.isEmpty()) {
            return Optional.empty();
        }

        // desempaquetamos la caja que devuelve optional para obtener el id del usuario asociado a ese task
        Long user = possibleTask.get().getUserId();

        // defensive coding: aunque la db obligue un task a tener usuario asociado, igual lo validamos
        // para evitar que se rompa la app en caso de algun fallo

        if (user != null && userId.equals(user)) {
            return possibleTask;
        }

        return Optional.empty();
    }

    public Task updateTaskFields(Long taskId, Long userId, Task updatedTask) {

        // valida que el id del task sea valido antes de continuar
        if (taskId == null || taskId < 0) {
            throw new TaskNotFoundException("El task con id: " + taskId + " es invalido");
        }

        // verificamos que el objeto Optional me devuelva contenido y no vacio
        Optional<Task> taskValido = taskRepository.findById(taskId);
        if (taskValido.isEmpty()) {
            throw new TaskNotFoundException("El task con id: " + taskId + " no existe");
        }

        // desempaquetamos el objeto Optional
        Task existingTask = taskValido.get();
        Long validUser = existingTask.getUserId();

        if (validUser == null) {
            throw new TaskNotFoundException("El task con id: " + taskId + " no tiene un usuario asociado, por lo que no se puede modificar");
        }

        // lanzamos una excepcion si la tarea no pertenece al usuario indicado en el path
        // para evitar que un usuario pueda modificar un task que no es suyo
        if (!userId.equals(validUser)) {
            throw new TaskForbiddenException("El task con id: " + taskId + " no pertenece al usuario con ID: " + userId);
        }


        if (updatedTask.getDescription() != null && !updatedTask.getDescription().trim().isEmpty()) {
            existingTask.setDescription(updatedTask.getDescription().trim());
        }
        if (updatedTask.getStatus() != null) {
            existingTask.setStatus(updatedTask.getStatus());
        }

        return taskRepository.save(existingTask);

    }

    public void deleteTaskById(Long taskId, Long userId) {

        if (taskId == null || taskId < 0) {
            throw new TaskNotFoundException("El task con id: " + taskId + " no existe");
        }
        Optional<Task> maybeTask = taskRepository.findById(taskId);
        if (maybeTask.isEmpty()) {
            throw new TaskNotFoundException("El task con ID: " + taskId + "no existe");
        }

        Task task = maybeTask.get();
        Long taskUserId = task.getUserId();

        if (userId == null || !userId.equals(taskUserId)) {
            throw new TaskForbiddenException("El task con id: " + taskId + " no pertenece al usuario con ID: " + userId);
        }


        Optional<User> maybeUser = userRepository.findById(userId);

        // defensive coding: si por alguna razon no se encuentra usuario, la tarea no se borra
        if (maybeUser.isEmpty()) {
            throw new TaskNotFoundException("El usuario con id: " + userId + " no existe");
        }

        maybeUser.ifPresent( user -> {
            try {
                taskArchiver.archiveTask("tasks-deleted", user, task);
            } catch (Exception ignored) {
                log.error("Error archiving task with id {} for user id {}: {}", task.getId(), user.getId(), ignored.getMessage());
            }
        });

        taskRepository.deleteById(taskId);
    }

}
