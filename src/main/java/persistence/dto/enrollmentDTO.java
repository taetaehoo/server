package persistence.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
public class enrollmentDTO {
    private String sCode;
    private int sNumber;
    private LocalDateTime regdate;
}
