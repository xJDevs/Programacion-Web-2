package teccr.justdoitcloud.service.external.taskgenerator;

import teccr.justdoitcloud.data.Task;

/**
 * Interfaz para generar tareas
 */
public interface TaskGenerator {

    /**
     * Genera y devuelve un objeto Task. No recibe parámetros.
     */
    Task generateTask();
}
