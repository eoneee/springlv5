package com.example.springlv5.controller;

import com.example.springlv5.dto.*;
import com.example.springlv5.entity.Comment;
import com.example.springlv5.entity.User;
import com.example.springlv5.security.UserDetailsImpl;
import com.example.springlv5.service.CommentService;
import com.example.springlv5.service.CrudService;
import com.example.springlv5.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
//Json형태의 객체반환
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
/*
    //댓글 작성
    @PostMapping("/comment")
    //@requestBody : json기반의 메세지를 사용하는 경우 http요청의 본문(body)이 그대로 전달 됨
    //@HttpServletRequest : Service등과 같은 메서드에 파라미터로 전달해줌, 서블릿페이지가 실행되는 동안에만 메모리에 존재하는 개체
    //CommentResponseDto 타입의 데이터를 ResponseEntity 형식으로 반환 받을 건데, createComment() 메소드를 사용해서, 그 안에는 id 정보, commentRequestDto 정보, userDetails 가 담겨져있는 상태로 받을것
//    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        Comment comment = new Comment(requestDto);
//        comment.addUser(userDetails.getUser());
//        return commentService.createComment(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(commentService.createComment(commentRequestDto, userDetails.getUser()));
        //ResponseEntity 형식으로 상태코드를 반환 하는데, commentService 에서 createComment() 메소드를 사용해서, id, commentRequestDto, userDetails.getUser() 를 담아서, DB 로부터 return(반환)

    }

 */
    //댓글 작성
    @PostMapping("/comment/{id}")
    //미리 말해두기: CommentResponseDto 타입의 데이터를 ResponseEntity 형식으로 반환 받을 건데, createComment() 메소드를 사용해서, 그 안에는 id 정보, commentRequestDto 정보, userDetails 가 담겨져있는 상태로 받을꺼다!
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //다시 돌려줘: ResponseEntity 형식으로 상태코드를 반환 하는데, commentService 에서 createComment() 메소드를 사용해서, 그 안에는 id, commentRequestDto, userDetails.getUser() 를 담아서, DB 로부터 return(반환) 받는다.
        return ResponseEntity.ok().body(commentService.createComment(id, commentRequestDto, userDetails.getUser()));
    }
    //댓글 수정
    //@PathVariable : URL변수를 받아서 사용
    @PutMapping("/{crudId}/{commentId}")
//    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long crudId, @PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return commentService.updateComment(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(commentService.updateComment(crudId, commentId, commentRequestDto, userDetails.getUser()));
    }
    //댓글 삭제
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<MsgResponseDto> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal  UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, userDetails.getUser());
//        return new MsgResponseDto("삭제 성공", HttpStatus.OK.value());
        return ResponseEntity.ok(new MsgResponseDto("삭제 성공", HttpStatus.OK.value()));
    }

    //댓글 좋아요
    @PostMapping("/comment/like/{commentId}")
    public ResponseEntity<MsgResponseDto> commentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(commentService.CommentLike(commentId, userDetails.getUser()));
    }

}




