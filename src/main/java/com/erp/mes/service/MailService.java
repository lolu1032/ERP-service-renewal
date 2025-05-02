package com.erp.mes.service;

import com.erp.mes.dto.EmailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    /**
     * @info    : 지정된 주소로 이메일 전송 - (지원 거절 메세지)
     * @name    : sendMail
     * @data    : 2024/09/06
     * @author  : justin77748292
     * @version : 1.0.0
     * @param
     * @return
     * @Description
     */
    @Async
    public boolean sendMail(EmailDTO emailDTO){
        boolean msg = false;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(emailDTO.getTargetMail());
        simpleMailMessage.setSubject("[발주서 통보] 발주 신청 및 발행 내역입니다.");
        simpleMailMessage.setFrom("justin77748292@gmail.com");
        simpleMailMessage.setText("테스트로 발주서 신청 및 발행 진행 합니다.~~~~~~~~~~~~~~");

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception e){
            e.printStackTrace();
            return msg;
        }

        return msg = true;
    }

}
