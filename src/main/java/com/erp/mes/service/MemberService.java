package com.erp.mes.service;

import com.erp.mes.dto.MemberDTO;
import com.erp.mes.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public List<MemberDTO> findAll(){
        return memberMapper.findAll();
    }

    public MemberDTO find(MemberDTO memberDTO){
        return memberMapper.find(memberDTO);
    }

    public MemberDTO login(String id){
        return memberMapper.login(id);
    }

    public int join(MemberDTO memberDTO){
        return memberMapper.join(memberDTO);
    }

    public int update(MemberDTO memberDTO){
        return memberMapper.update(memberDTO);
    }

    public int delete(MemberDTO memberDTO) {
        return memberMapper.delete(memberDTO);
    }

    public int checkEmail(String email) {
        return memberMapper.checkEmail(email);
    }
}
