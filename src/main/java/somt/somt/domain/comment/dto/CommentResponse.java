package somt.somt.domain.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import somt.somt.domain.comment.entity.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private Long memberId;
    private String nickname;
    private String content;
    private Integer grade;
    private LocalDateTime modifyAt;
    private List<CommentResponse> reply = new ArrayList<>();

    public CommentResponse(Comment comment){
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.nickname = comment.getMember().getNickname();
        this.content = comment.getContent();
        this.modifyAt  = comment.getModifyAt();
        this.grade = comment.getGrade();
    }

    public CommentResponse(Comment comment,List<CommentResponse> reply){
        this.commentId = comment.getId();
        this.memberId = comment.getMember().getId();
        this.nickname = comment.getMember().getNickname();
        this.content = comment.getContent();
        this.modifyAt  = comment.getModifyAt();
        this.reply = reply;
        this.grade=0;
    }

}
