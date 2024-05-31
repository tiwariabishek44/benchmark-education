package com.benchmark.education.dto.Reponse;

import com.benchmark.education.entity.Manual;
import lombok.Data;

@Data
public class TeacherManualResponsee {

    private Manual manual;
    private boolean assigned;
}
