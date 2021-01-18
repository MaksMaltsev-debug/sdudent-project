package com.maltsev.cross.message;

import com.maltsev.cross.constatns.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassStatusMessage {
    private String userId;
    private String classId;
    private Status status;
}
