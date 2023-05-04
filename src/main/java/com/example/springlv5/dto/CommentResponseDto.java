package com.example.springlv5.dto;


import com.example.springlv5.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
//받아온 데이터들을 교환 해줌
//@Getter : 접근자생성
@NoArgsConstructor
public class CommentResponseDto{
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Integer CommentLike;


    public  CommentResponseDto(Comment comment, int cnt) {
        //Comment Entity에서 데이터들을 가져옴
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username =comment.getUser().getUsername();
        //Comment의 User테이블을 가져오고 거기서 username을 가져옴
        this.CommentLike = cnt;
        this.createAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
