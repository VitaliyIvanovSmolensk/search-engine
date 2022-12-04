package searchengine.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import searchengine.model.enums.Status;


import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Component
@Table(name = "sites")
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    @Temporal(TemporalType.DATE)
    @Column(name = "status_time", nullable = false)
    private Date statusTime;
    @Column(name = "last_error", columnDefinition = "TEXT")
    private String lastError;
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String url;
    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

}
