package com.example.springlv5.dto;

import com.example.springlv5.entity.Comment;
import com.example.springlv5.entity.Crud;
import com.example.springlv5.entity.UserRoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class CrudResponseDto implements CrudAndComment{
    //Crud와 Comment의 테이블에서 가져옴
    private Long id;
    private String title;
    private String username;
    private UserRoleEnum role;
    private String content;
//    private List<Comment> commentList;
    private int crudLikeCount;
    private boolean crudLike;
    //Comment Entity list : Crud에 선언되어있음



    public CrudResponseDto(Crud crud) {
        this.id = crud.getId();
        this.title = crud.getTitle();
        this.username = crud.getUser().getUsername();
        this.role = crud.getUser().getRole();
        this.content = crud.getContent();
//        this.commentList = crud.getCommentList();
        //Crud에 선언된 List<Comment> CommentList를 가져옴
    }

    private  List <CommentResponseDto> commentList = new ArrayList<>();
    public CrudResponseDto(Crud crud, List <CommentResponseDto> commentList, boolean crudLike) {
        this.id = crud.getId();
        this.title = crud.getTitle();
        this.username = crud.getUser().getUsername();
        this.role = crud.getUser().getRole();
        this.content = crud.getContent();
        this.commentList = commentList;
        this.crudLikeCount = crud.getCrudLikeList().size();
        this.crudLike = crudLike;
    }

    //    private List<CommentResponseDto> commentList = new ArrayList<>();
}
