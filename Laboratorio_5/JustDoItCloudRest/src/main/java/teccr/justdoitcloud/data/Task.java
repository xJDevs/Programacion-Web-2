package teccr.justdoitcloud.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@Table("tasks")
public class Task {
    @Id
    private Long id;
    @Size(min=3, message = "Descripcion debe tener al menos 3 caracteres")
    private String description;
    @Column("created_at")
    private LocalDateTime createdAt;
    private LocalDate deadline;
    @NotNull
    private Status status;

    @Column("user_id")
    private Long userId;

    public enum Status {
        PENDING,
        INPROGRESS,
        DONE
    }
}
