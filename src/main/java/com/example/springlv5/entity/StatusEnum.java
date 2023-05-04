package com.example.springlv5.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusEnum {

    // 200
    OK(HttpStatus.OK, "OK"),

    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰 입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST,"토큰이 유효하지 않습니다."),

    AUTHORIZATION(HttpStatus.BAD_REQUEST,"작성자만 수정/삭제 할 수 있습니다."),
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST,"중복된 username 입니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST,"username과 password의 형식이 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다"),

    NOT_FOUND_CRUD(HttpStatus.BAD_REQUEST, "글이 존재하지 않습니다."),
    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "댓글이 존재하지 않습니다.");

    /*
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST"),

    TOKEN_ERROR(HttpStatus.BAD_REQUEST, "토큰 에러"),


    // 404

    NOT_AUTHENTICATION(HttpStatus.NOT_FOUND, "사용자만 게시글을 삭제할 수 있습니다."),

    TOKEN_NULL(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND"),


    // 500
    INTERNAL_SERER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
*/
    private final HttpStatus status;
    private final String msg;
}