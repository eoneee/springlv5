package com.example.springlv5.service;

import com.example.springlv5.dto.*;
import com.example.springlv5.entity.*;
import com.example.springlv5.exception.CustomException;
import com.example.springlv5.jwt.JwtUtil;
import com.example.springlv5.repository.CommentLikeRepository;
import com.example.springlv5.repository.CommentRepository;
import com.example.springlv5.repository.CrudRepository;
import com.example.springlv5.repository.UserRepository;
import com.example.springlv5.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

import static com.example.springlv5.entity.StatusEnum.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;
    private final CrudRepository crudRepository;
    private final JwtUtil jwtUtil;
    private CommentService commentService;

    //덧글 생성
//    @Transactional
//    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
//        Crud crud = checkCrud(commentRequestDto.getCrudId());
//        Comment comment = new Comment(commentRequestDto, crud.getId(), user.getId());
//        comment.addUser(user);
//        comment.addCrud(crud);
//        commentRepository.save(comment);
////        Comment comment = commentRepository.save(new Comment(commentRequestDto, crud, user));
//        int cnt = 0;
//        return new CommentResponseDto(comment, cnt);
//    }

    //댓글 작성
    @Transactional
    public CommentResponseDto createComment(Long id, CommentRequestDto commentRequestDto, User user) {
        Crud crud = crudRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_CRUD)
        );

        Comment comment = commentRepository.save(new Comment(commentRequestDto, crud, user));
        //댓글 생성시, 좋아요를 0으로 초기화시켜주기 위함
        int cnt = 0;
        return new CommentResponseDto(comment,cnt);
    }

    //수정하기
    @Transactional
    public CommentResponseDto updateComment(Long crudId, Long commentId, CommentRequestDto requestDto, User user) {

       Crud crud = crudRepository.findById(crudId).orElseThrow(
               ()-> new CustomException(NOT_FOUND_CRUD)
       );
//       Crud crud = checkCrud(user.getId());
       Comment comment;

        //user 의 권한이 ADMIN 와 같다면,
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(commentId).orElseThrow(
                    //글이 id로 찾아서 존재하지 않을 때 예외처리
                    () -> new CustomException(StatusEnum.NOT_FOUND_COMMENT)
            );
            //user 의 권한이 ADMIN 이 아니라면, 아이디가 같은 유저만 수정 가능
        } else {
            comment = commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(
                    () -> new CustomException(StatusEnum.AUTHORIZATION) //400
            );
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment, commentLikeRepository.countAllByCommentId(comment.getId()));
    }

    //삭제
    public CommentResponseDto deleteComment(Long commentId, User user) {
        Comment comment = checkComment(commentId);
        //user 의 권한이 ADMIN 와 같다면,
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            comment = commentRepository.findById(commentId).orElseThrow(
                    () -> new CustomException(StatusEnum.NOT_FOUND_COMMENT)
            );

        } else {
            //user 의 권한이 ADMIN 이 아니라면, 아이디가 같은 유저만 수정 가능
            comment = commentRepository.findByIdAndUserId(commentId, user.getId()).orElseThrow(
                    () -> new CustomException(StatusEnum.AUTHORIZATION)
            );
        }
        commentRepository.deleteById(commentId);
        return new CommentResponseDto(comment, commentLikeRepository.countAllByCommentId(comment.getId()));
    }

    //좋아요
    @Transactional
    public MsgResponseDto CommentLike(Long commentId, User user){
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new CustomException(NOT_FOUND_COMMENT)
        );
        if(commentLikeRepository.findByCommentIdAndUserId(commentId,user.getId()).isEmpty()){
            CommentLike commentLike = CommentLike.builder().comment(comment).user(user).build();
            commentLikeRepository.save(commentLike);
            return new MsgResponseDto("좋아요 완료", HttpStatus.OK.value());
        }else{
            commentLikeRepository.deleteByCommentIdAndUserId(comment.getId(), user.getId());
            return new MsgResponseDto("좋아요 취소",HttpStatus.OK.value());
        }
    }

    //댓글 존재 여부 확인
    private Comment checkComment(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_FOUND_COMMENT)
        );
        return comment;
    }

    //권한 여부
    private void isCommentUsers(User user, Comment comment){
        //게시글에 있는 작성자 / JWT토큰의 작성자와 똑같은지 비교 // JWT토큰이 admin이 아니라면
        if(!comment.getUser().getUsername().equals(user.getUsername()) && !user.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new CustomException(StatusEnum.AUTHORIZATION);
        }

    }

    //권한 확인

    private User checkJwtToken(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        Claims claims;
        // 토큰 검증
        if(token == null){
            throw new CustomException(StatusEnum.INVALID_TOKEN);
        }
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(StatusEnum.AUTHORIZATION);
        }
        claims = jwtUtil.getUserInfoFromToken(token);

        return userRepository.findByUsername(claims.getSubject()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
    }

    private Crud checkCrud(Long crudId){
        Crud crud = crudRepository.findById(crudId).orElseThrow(
                () -> new CustomException(StatusEnum.NOT_FOUND_CRUD)
        );
        return crud;
    }
}
