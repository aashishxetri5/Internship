package amnil.tm.dto.request;

import amnil.tm.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskRequest {
    private Long id;

    private String title;

    private String description;

    private TaskStatus status; /* Pending, In_Progress, Complete */

}
