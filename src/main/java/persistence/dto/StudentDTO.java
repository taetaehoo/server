package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class StudentDTO {
    int sNumber;
    String sName;
    String password;
    int grade;
}
