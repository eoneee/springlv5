package com.example.springlv5.entity;
import com.example.springlv5.dto.CrudRequestDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.config.web.servlet.CsrfDsl;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor
public class Crud extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "crudId")
    private Long id;


    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;

//    ////// Comment 테이블과 연관관계 //////
//    @OneToMany(mappedBy = "crud", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    //@OneToMany(1대N) Comment에서도 N:1 이므로 양방향, mappedBy : crud로 주인 설정
//    //CascadeType.All : 모든 종속 관계 영속성 전이. PERSIST & REMOVE 수행
//    @OrderBy("createdAt desc")
//    //@OrderBy : 정렬 , desc : 내림차순
//    @JsonManagedReference
//    //순환참조 막아줌
//    //부모 class - @JsonManagedReference
//    private List<Comment> commentList;
//    /////////////////////////////////
    @OneToMany(mappedBy = "crud", cascade = CascadeType.REMOVE)
    //@OrderBy("필드명 [ASC | DESC] ") 형식
    @OrderBy("createdAt DESC")          //--> repository 에서 사용하자.
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "crud",cascade = CascadeType.REMOVE)
    private List<CrudLike> crudLikeList = new ArrayList<>();

    //// Users 테이블과 연관 관계 ////
    @ManyToOne
    //@ManyToOne : 다대일 단방향, 주인은 N = Crud
    @JoinColumn(name = "userId", nullable = false)
    //userId를 가지고 참조할 것이며, JoinColumn 할 것
    private User user;
    /////////////////////////////



    public Crud(CrudRequestDto requestDto, User user)  {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = user.getUsername();
        this.user = user;
        //request 요청된 데이터들로 생성
    }
    public void update(CrudRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void addUser(User user){
        this.user = user;
        //addUser로 받은 users를 생성
    }

}

