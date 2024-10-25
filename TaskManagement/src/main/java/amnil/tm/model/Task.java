package amnil.tm.model;

import amnil.tm.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private TaskStatus status; /* Pending, In_Progress, Complete */

    private LocalDate assignedOn;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    public Task(String title, String description, TaskStatus status, LocalDate assignedOn, User assignedUser) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedOn = assignedOn;
        this.assignedUser = assignedUser;
    }

    public Task(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
