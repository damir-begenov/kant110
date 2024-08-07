package kz.dossier.security.models;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "log")
@AllArgsConstructor
@NoArgsConstructor
public class log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;

    private String username;
    @ElementCollection
    @Transient
    private List<String> request_body;
    @ElementCollection
    @Transient
    private List<String> request_rels;
    private Integer limit_;
    private Integer depth_;

    private String obwii;

    private String approvement_data;

    public static void error(String s, Exception e) {
        System.out.println(s + " Error: " + e );
    }
}