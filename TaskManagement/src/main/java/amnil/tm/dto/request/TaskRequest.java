package amnil.tm.dto.request;

import amnil.tm.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private Long id;

    private String title;

    private String description;

    private TaskStatus status; /* Pending, In_Progress, Complete */

    public TaskRequest(String title, String description, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
}
