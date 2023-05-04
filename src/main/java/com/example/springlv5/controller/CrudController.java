package com.example.springlv5.controller;
import com.example.springlv5.dto.CrudRequestDto;
import com.example.springlv5.dto.CrudResponseDto;
import com.example.springlv5.dto.MsgResponseDto;
import com.example.springlv5.entity.Crud;
import com.example.springlv5.security.UserDetailsImpl;
import com.example.springlv5.service.CrudService;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//Spring MVC Controller에 @ResponseBody가 추가 됨
//@RestController는 클래스의 각 메서드마다 @ResponseBody를 추가할 필요가 없어짐(@Controller와의 차이점)
@RequiredArgsConstructor
//@RequiredArgsConstructor : 생성자 주입(final 이나 Notnull이 붙은 필드의 생성자를 자동생성 해줌)
//새로운 필드를 추가할 때 다시 생성자를 만들어서 관리해야 하는 번거로움을 없애줌(@Autowired사용x)
@RequestMapping("/api")
//@RequestMapping : controller에서 사용됨
//요청이 왔을 때 어떤 컨트롤러가 호출되어야 하는지 알려줌
public class CrudController {
    private final CrudService crudService;

    //글 생성하기
    @PostMapping("/post")
    //@RequestBody: HTTP Method 안의 body 값을 Mapping(key:value 로 짝지어줌)
    //@AuthenticationPrincipal: 인증 객체(Authentication)의 Principal 부분의 값을 가져옴
    //UserDetailsImpl userDetails: 인증 객체를 만들 때, Principal 부분에 userDetails 를 넣었기 때문에, userDetails 를 파라미터로 받아올 수 있었음
//    public CrudResponseDto createCrud(@RequestBody CrudRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        public ResponseEntity<CrudResponseDto> createCrud(@RequestBody CrudRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        //브라우저에서 요청해온 데이터를 잘 불러와서 서비스에 던져줌
        //@Principal : 계정 상태 정보

//        Crud crud = new Crud(requestDto);
//        crud.addUser(userDetails.getUser());

//        CrudRepository.save(crud);
//        return crudService.createCrud(requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(crudService.createCrud(requestDto,userDetails.getUser()));
    }

    //메인 페이지
    @GetMapping("/posts")
//    public List<CrudResponseDto> list(){
    public ResponseEntity<List<CrudResponseDto>> getListCrud(@AuthenticationPrincipal UserDetailsImpl userDetails){
        //List로 선언(글들을 list해서 가져옴)
//        return crudService.getCrudList();
        return ResponseEntity.ok().body(crudService.getListCrud(userDetails.getUser()));
    }

//    //전체목록 말고 하나씩 보기
//    @GetMapping("/post/{id}")
////    public CrudResponseDto getCrud(@PathVariable Long id) {
//    public ResponseEntity<CrudResponseDto> getCrud(@PathVariable Long id){
//        return ResponseEntity.ok().body(crudService.getCrud(id));
////        return crudService.getCrud(id);
//        // service에서 id를 받아서 확인후 response를 가져옴
//
//    }

    //선택한 게시글 조회
    @GetMapping("/post/{id}")
    //미리 말해두기
    //@PathVariable: URL 경로에 변수를 넣기
    //예시: localhost:8080/api/board/{12}
    public ResponseEntity<CrudResponseDto> getCrud(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        //다시 돌려줘
        return ResponseEntity.ok().body(crudService.getCrud(id, userDetails.getUser()));
    }
    //수정하기
    @PutMapping("/post/{id}")
    //@PathVariable: URL 경로에 변수를 넣기, Long id: 담을 데이터,
    //@RequestBody: HTTP Method 안의 body 값을 Mapping(key:value 로 짝지어줌)
//    public CrudResponseDto updateCrud(@PathVariable Long id, @RequestBody CrudRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    public ResponseEntity<CrudResponseDto> updateCrud(@PathVariable Long id, @RequestBody CrudRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return crudService.updateCrud(id,requestDto,userDetails.getUser());
        return ResponseEntity.ok().body(crudService.updateCrud(id,requestDto,userDetails.getUser()));
    }


    //삭제
    @DeleteMapping("/post/{id}")
//    public MsgResponseDto deleteCrud(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    public ResponseEntity<MsgResponseDto> deleteCrud(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        crudService.deleteCrud(id,userDetails.getUser());
        return ResponseEntity.ok().body(new MsgResponseDto("삭제 성공", HttpStatus.OK.value()));
//        return new MsgResponseDto("삭제 성공", HttpStatus.OK.value());
    }


    @PostMapping("/post/like/{crudId}")
    public ResponseEntity<MsgResponseDto> saveCrudLike(@PathVariable Long crudId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok().body(crudService.saveCrudLike(crudId, userDetails.getUser()));
    }


}
