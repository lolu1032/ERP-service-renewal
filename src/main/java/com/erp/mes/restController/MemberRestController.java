package com.erp.mes.restController;
import com.erp.mes.dto.MemberDTO;
import com.erp.mes.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(value = "/member/*") // /member/~ 경로로 들어오는 모든 API 처리
@Slf4j
public class MemberRestController {
    @Autowired
    MemberService memberService;
    /**
     * 회원 찾기 1명
     *
     * @param memberDTO
     * @return
     */
    @PostMapping(value = "find")
    public Map<String, Object> find(@RequestBody MemberDTO memberDTO) {
        Map<String, Object> map = new HashMap<>();
        try {
            memberDTO = memberService.find(memberDTO);
            map.put("memberDTO", memberDTO);
            map.put("msg", "회원 찾기 성공");
        } catch (Exception e) {
            map.put("msg", "회원 찾기 실패");
        }
        return map;
    }
    /**
     * 로그인
     * @param memberDTO
     * @return
     */
    @PostMapping(value = "login")
    public ResponseEntity<Map<String,Object>> login(
            @RequestBody MemberDTO memberDTO,
            HttpServletRequest request
    ) {
        Map<String,Object> response = new HashMap<>();
        String id = memberDTO.getId();
        String pw = memberDTO.getPwd();
        MemberDTO member = memberService.login(id);
        if(member == null) {
            response.put("message","회원정보가 없습니다.");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        if(!pw.equals(member.getPwd())) {
            response.put("message","비밀번호가 일치하지않습니다.");
            return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
        }
        HttpSession session = request.getSession();
        session.setAttribute("id",member.getId());
        session.setAttribute("name",member.getName());
        response.put("message","로그인 성공했습니다.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    /**
     * 회원가입
     * @param memberDTO
     * @return
     */
    @PostMapping(value = "join")
    public ResponseEntity<Map<String,Object>> join(
            @RequestBody MemberDTO memberDTO
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> map = new HashMap<>();
            vaildateUsersDTO(memberDTO);
            map.put("email",memberDTO.getEmail());
            checkEmail(map);
            memberService.join(memberDTO);
            response.put("message", "회원가입 성공했습니다.");
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 성공시 201보냄
        }catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); // 400 클라이언트 오류
        }catch (IllegalStateException e) {
            response.put("message", "회원가입 실패했습니다.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED); // 401 클라이언트 오류
        }catch (Exception e) {
            log.error("서버 오류: {}", e.getMessage(), e);
            response.put("error","서버오류");
            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR); // 500 서버오류
        }
    }
    /**
     * 회원 수정
     * @param memberDTO
     * @return
     */
    @PutMapping(value = "update")
    public Map<String, Object> update(@RequestBody MemberDTO memberDTO) {
        Map<String, Object> map = new HashMap<>();
        try {
            int status = memberService.update(memberDTO);
            map.put("status", status);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "회원 수정 실패");
        }
        return map;
    }
    /**
     * 회원 탈퇴(비활성 상태 변환)
     * @param memberDTO
     * @return
     */
    @PutMapping(value = "delete")
    public Map<String, Object> delete(@RequestBody MemberDTO memberDTO) {
        Map<String, Object> map = new HashMap<>();
        System.out.println(memberDTO);
        try {
            int status = memberService.delete(memberDTO);
            map.put("status", status);
            map.put("msg", "회원 삭제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "회원 삭제 실패");
        }
        return map;
    }
    private void vaildateUsersDTO(MemberDTO memberDTO) {
        String emailRegExp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
        String passwordRegExp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&+=]).{8,15}$";
        String nameRegExp = "^[가-힣]*$";
        if(memberDTO.getName() == null || memberDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("이름이 비어있습니다.");
        }
        if(!memberDTO.getName().matches(nameRegExp)) {
            throw new IllegalArgumentException("한국어로 입력해주세요.");
        }
        if(memberDTO.getEmail() == null || memberDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("이메일이 비어있습니다.");
        }
        if(!memberDTO.getEmail().matches(emailRegExp)) {
            throw new IllegalArgumentException("example@naver.com 형식에 맞게 입력해주세요.");
        }
        if(memberDTO.getPwd() == null || memberDTO.getPwd().trim().isEmpty()) {
            throw new IllegalArgumentException("비밀번호가 비어있습니다.");
        }
        if(!memberDTO.getPwd().matches(passwordRegExp)) {
            log.warn("비밀번호 검증 실패: {} (정규 표현식: {})", memberDTO.getPwd(), passwordRegExp);
            throw new IllegalArgumentException("비밀번호 (숫자, 문자, 특수문자 포함 8~15자리 이내) 입력하세요.");
        }
    }
    private void checkEmail(Map<String,Object> map) {
        String email = (String) map.get("email");
        int n = memberService.checkEmail(email);
        log.info("n={}",n);
        if (n > 0){
            throw new IllegalArgumentException("이미 있는 이메일입니다.");
        }
    }
}