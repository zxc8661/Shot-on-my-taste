package somt.somt.domain.member.entity.history;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.member.entity.Member;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MemberHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;


    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;


    @Column(name = "history_type", nullable = false)
    private Integer historyType;

    @Column(name="message")
    private String message;

    @Column(name = "happen_Time", nullable = false)
    private LocalDateTime happenTime;
}
