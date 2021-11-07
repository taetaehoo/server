package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubjectDTO {
    String sCode;
    String sName;
    int credit;
    int grade;
    int semester;
}
