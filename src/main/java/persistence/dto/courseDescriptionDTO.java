package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class courseDescriptionDTO {
    private int id;
    private String subjectCode;
    private int week;
    private String plan;
}
