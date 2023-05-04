package com.example.springlv5.exception;

import com.example.springlv5.entity.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException{
    private final StatusEnum statusEnum;
    //StatusEnum Entity로 선언해놓은 예외들
}
