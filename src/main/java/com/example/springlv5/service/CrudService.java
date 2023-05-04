package com.example.springlv5.service;

import com.example.springlv5.dto.*;
import com.example.springlv5.entity.*;
import com.example.springlv5.exception.CustomException;
import com.example.springlv5.jwt.JwtUtil;
import com.example.springlv5.repository.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.springlv5.entity.StatusEnum.AUTHORIZATION;
import static com.example.springlv5.entity.StatusEnum.NOT_FOUND_CRUD;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrudService {

    private final CrudRepository crudRepository;
    private final CrudRequestDto crudRequestDto;
    private final UserRepository userRepository;
    private final CrudLikeRepository crudLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final JwtUtil jwtUtil;

    //글 생성하기
    @Transactional
    public CrudResponseDto createCrud(CrudRequestDto requestDto, User user) {
//        Crud crud = new Crud(requestDto);
//        crud.addUser(user);
        Crud crud = crudRepository.save(new Crud(requestDto, user));
//        crudRepository.save(crud);
        return new CrudResponseDto(crud);
    }


    //글 전체 조회
    @Transactional(readOnly = true)
    //JPA 사용시, 변경감지 작업을 수행하지 않아 성능 이점
    public List<CrudResponseDto> getListCrud(User user) {
        // 데이터 베이스에 저장 된 전체 게시물 전부다 가져오는 API
        // 테이블에 저장 되어 있는 모든 게시물 목록 조회
        List<Crud> crudList = crudRepository.findAllByOrderByCreatedAtDesc();
        List<CrudResponseDto> crudResponseDto = new ArrayList<>();
        for(Crud crud : crudList){
            List<CommentResponseDto> commentList = new ArrayList<>();
            for(Comment comment : crud.getComments()){
                commentList.add(new CommentResponseDto(comment, commentLikeRepository.countAllByCommentId(comment.getId())));
            }
            crudResponseDto.add(new CrudResponseDto(crud, commentList,(checkCrudLike(crud.getId(),user))));
        }
        // DB 에서 가져온 것
//        return crudList.stream().map(CrudResponseDto::new).collect(Collectors.toList());
        return crudResponseDto;
//        return crudRepository.findAllByOrderByCreatedAtDesc().stream().map(CrudResponseDto::new).collect(Collectors.toList());
    }

//
//
//    //전체목록 말고 하나씩 보기
//    @Transactional(readOnly = true)
//    public CrudResponseDto getCrud(Long id) {
//        //조회하기 위해 받아온 crud의 id를 사용해서 해당 crud인스턴스가 테이블에 존재 하는지 확인하고 가져오기
//        //Crud crud = table.get(id);->repository한테서 id를 가져오면 됨
//        Crud crud = checkCrud(id);
//        return new CrudResponseDto(crud);
//    }
//선택한 게시글 조회
    @Transactional(readOnly = true)
    public CrudResponseDto getCrud(Long id, User user) {
        //boardRepository 와 연결해서, 게시글의 id 를 찾는다
        //boardRepository 와 연결? DB 에 접근하여, 이미 저장돼있는 id 가 있는지 확인하기 위함
        Crud crud = crudRepository.findById(id).orElseThrow (
                () -> new CustomException(StatusEnum.INVALID_AUTH_TOKEN)
        );

        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment comment : crud.getComments()) {
            commentList.add(new CommentResponseDto(comment,commentLikeRepository.countAllByCommentId(comment.getId())));
        }
        return new CrudResponseDto(crud, commentList, (checkCrudLike(crud.getId(), user)));
    }
    //선택한 게시글 수정
    @Transactional
    //Long id: 담을 데이터,
    //HttpServletRequest request 객체: 로그인 토큰을 담고 있음
    public CrudResponseDto updateCrud(Long id, CrudRequestDto requestDto, User user) {
        Crud crud;
        //user 의 권한이 ADMIN 와 같다면,
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            crud = crudRepository.findById(id).orElseThrow(
                    //글이 id로 찾아서 존재하지 않을 때 예외처리
                    () -> new CustomException(NOT_FOUND_CRUD)
            );
            //user 의 권한이 ADMIN 이 아니라면, 아이디가 같은 유저만 수정 가능
        } else {
            crud = crudRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION) //400
            );
        }

        crud.update(requestDto);
        List<CommentResponseDto> commentList = new ArrayList<>();
        for(Comment comment : crud.getComments()){
            commentList.add(new CommentResponseDto(comment, commentLikeRepository.countAllByCommentId(comment.getId())));
        }
        return new CrudResponseDto(crud, commentList,(checkCrudLike(crud.getId(),user)));

    }

    //선택한 게시글 삭제
    @Transactional
    //MsgResponseDto 반환 타입, deleteCrud 메소드 명
    public void deleteCrud (Long id, User user) {
        Crud crud;
        //user 의 권한이 ADMIN 와 같다면,
        if(user.getRole().equals(UserRoleEnum.ADMIN)) {
            crud = crudRepository.findById(id).orElseThrow(
                    () -> new CustomException(NOT_FOUND_CRUD)
            );

        } else {
            //user 의 권한이 ADMIN 이 아니라면, 아이디가 같은 유저만 수정 가능
            crud = crudRepository.findByIdAndUserId(id, user.getId()).orElseThrow(
                    () -> new CustomException(AUTHORIZATION)
            );
        }
        crudRepository.delete(crud);
    }

    // 게시글 좋아요 확인
    @Transactional(readOnly = true)
    public boolean checkCrudLike(Long crudId, User user) {
        // DB 에서 해당 유저의 게시글 좋아요 여부 확인
        //exists : 데이터가 존재하는지 확인. countBy 보다 성능측면에서 우수. existsBy 의 반환 타입은 boolean. countBy 는 반환 타입이 Long
        //이유? 검색 도중 중복되는 게 하나라도 존재하면, 그 즉시 쿼리를 종료하기때문에, 모든 개수를 세는 count 보다 압도적으로 좋다
        //쿼리: DB 에게 정보를 요청하는 행위. 보통 DB 에서 특정 주제어, 어귀를 찾기위해 사용됨
        return crudLikeRepository.existsByCrudIdAndUserId(crudId, user.getId());
    }

    // 게시글 좋아요 생성 및 삭제
    @Transactional
    public MsgResponseDto saveCrudLike(Long crudId, User user) {
        // 1. 요청한 글이 DB 에 존재하는지 확인
        Crud crud = crudRepository.findById(crudId).orElseThrow(
                () -> new CustomException(NOT_FOUND_CRUD)
        );

        // 2. 해당 유저의 게시글 좋아요 여부를 확인해서 false 라면
        if (!checkCrudLike(crudId, user)) {
            // 3. 즉시 해당 유저의 게시글 좋아요를 DB 에 저장
            //saveAndFlush(): 즉시 DB 에 변경사항을 적용
            //save(): 바로 DB 에 저장되지 않고, 영속성 컨텍스트에 저장되었다가, flush() 또는 commit() 수행 시 DB에 저장
            crudLikeRepository.saveAndFlush(new CrudLike(crud, user));
            return new MsgResponseDto("좋아요 완료", HttpStatus.OK.value());
            // 4. 게시글 좋아요 여부가 true 라면, 해당 유저의 게시글 좋아요를 DB 에서 삭제
        } else {
            crudLikeRepository.deleteByCrudIdAndUserId(crudId, user.getId());
            return new MsgResponseDto("좋아요 취소", HttpStatus.OK.value());
        }
    }


    //글 존재 여부 확인
    private Crud checkCrud(Long id) {
        Crud crud = crudRepository.findById(id).orElseThrow(
                () -> new CustomException(NOT_FOUND_CRUD)
        );
        return crud;
    }

    //권한 여부
    private void isCrudUsers(User user, Crud crud){
        if(!crud.getUser().getUsername().equals(user.getUsername()) && !user.getRole().equals(UserRoleEnum.ADMIN)){
            throw new CustomException(AUTHORIZATION);
        }
    }

}



