package Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@ToString
@NoArgsConstructor
public class Activity {

    @Getter @Setter
    private Integer id;

    @Getter @Setter
    private String action;

    @Getter @Setter
    private String message;

    @Getter @Setter
    private String actor;

    @Getter @Setter
    private Timestamp time;
}
