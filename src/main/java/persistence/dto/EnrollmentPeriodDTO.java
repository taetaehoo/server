package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
@Getter
@Setter
@ToString

public class EnrollmentPeriodDTO {
    private int grade;
    private Date period;
}
