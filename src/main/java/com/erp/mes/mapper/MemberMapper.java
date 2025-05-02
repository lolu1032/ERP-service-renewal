package com.erp.mes.mapper;

import com.erp.mes.dto.MemberDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MemberMapper {

    // 모든 회원 정보
    @Select("SELECT * FROM member")
    List<MemberDTO> findAll();
    //활성 상태(활성화 = 1, 비활성 = 0) 회원찾기
    @Select("SELECT * FROM member WHERE id = #{id} AND status = 1")
    MemberDTO find(MemberDTO memberDTO);
    // 로그인
    @Select("SELECT * FROM member WHERE id = #{id}")
    MemberDTO login(String id);
    // 회원가입
    @Insert("INSERT INTO member VALUES (#{m_code}, #{id}, #{pwd}, #{name}, #{phone_number}, #{email}, #{dept_name}, #{auth}, #{status})")
    int join(MemberDTO memberDTO);
    // 회원수정
    @Update("UPDATE member SET id = #{id}, pwd = #{pwd}, name = #{name}, phone_number = #{phone_number}, email = #{email}, dept_name = #{dept_name}, auth = 0, status = 1 WHERE m_code = #{m_code}")
    int update(MemberDTO memberDTO);
    // 회원삭제(비활성으로 변경 => status = 0)
    @Update("UPDATE member SET status = 0 WHERE m_code = #{m_code}")
    int delete(MemberDTO memberDTO);
    // 중복체크
    @Select("select count(*) from member where email = #{email}")
    int checkEmail(String email);

}
