package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class openedSubjectDTO {
    private final int MAX_STUDENT = 3;
    private String csCode;
    private String pNumber;
    private int maxStudent = MAX_STUDENT;
    private int lectureStartTime;
    private int lectureEndTime;
    private String lectureDay;
    private String lectureRoom;
    private Date periodCourseDescription;
}
