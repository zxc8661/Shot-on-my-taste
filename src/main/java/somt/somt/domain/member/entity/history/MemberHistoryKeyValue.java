package somt.somt.domain.member.entity.history;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberHistoryKeyValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "db_value", nullable = false)
    private Integer databaseValue;

    @Column(name = "back_value",nullable = false)
    private String domainValue;

}
