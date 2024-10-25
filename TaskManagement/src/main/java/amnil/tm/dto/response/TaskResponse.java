package amnil.tm.dto.response;

import amnil.tm.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskResponse {
    private Long id;

    private String title;

    private String description;

    private TaskStatus status;

    private AssignedUserResponse assignedUser;

    @Data
    @AllArgsConstructor
    public static class AssignedUserResponse {
        private Long userId;
        private String fullname;
        private String email;
    }
}
