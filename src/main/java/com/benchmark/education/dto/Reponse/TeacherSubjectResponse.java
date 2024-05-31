package com.benchmark.education.dto.Reponse;

import com.benchmark.education.entity.Subject;
import lombok.Data;

@Data
public class TeacherSubjectResponse {
    private Subject subject;
    private boolean assigned;
}
