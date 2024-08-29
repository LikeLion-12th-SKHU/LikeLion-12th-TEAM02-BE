package com.skhu.moodfriend.app.service.auth;

import com.skhu.moodfriend.app.dto.auth.reqDto.EmailSendReqDto;
import com.skhu.moodfriend.app.dto.auth.reqDto.EmailVerifyReqDto;
import com.skhu.moodfriend.global.exception.CustomException;
import com.skhu.moodfriend.global.exception.code.ErrorCode;
import com.skhu.moodfriend.global.exception.code.SuccessCode;
import com.skhu.moodfriend.global.template.ApiResponseTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate redisTemplate;

    private static final int CODE_EXPIRATION_MINUTES = 5;
    private static final String VERIFIED_EMAIL_PREFIX = "verified:";

    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int type = random.nextInt(3);
            switch (type) {
                case 0 -> code.append((char) (random.nextInt(26) + 97));
                case 1 -> code.append((char) (random.nextInt(26) + 65));
                case 2 -> code.append(random.nextInt(10));
            }
        }
        return code.toString();
    }

    private MimeMessage createMessage(String to, String code) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("[MoodFriend] 회원가입 이메일 인증");

        String msgContent = """
                <div style='margin: 0 auto; padding: 20px; max-width: 600px; font-family: Arial, sans-serif;'>
                    <h2 style='text-align: center; color: #333;'>회원가입 인증 코드</h2>
                    <p style='text-align: center; color: #555;'>안녕하세요,</p>
                    <p style='text-align: center; color: #555;'>아래 인증 코드를 회원가입 창에 입력하여 이메일 인증을 완료해 주세요.</p>
                    <div style='text-align: center; margin: 40px 0;'>
                        <span style='font-size: 24px; font-weight: bold; color: #4CAF50; border: 2px dashed #4CAF50; padding: 10px 20px;'>
                            %s
                        </span>
                    </div>
                    <p style='text-align: center; color: #999;'>이 코드는 5분 동안 유효합니다.</p>
                    <p style='text-align: center; color: #999;'>감사합니다.<br>MoodFriend 팀</p>
                </div>
                """.formatted(code);

        message.setText(msgContent, "utf-8", "html");
        message.setFrom(new InternetAddress("kduoh99@gmail.com", "MoodFriend"));

        return message;
    }

    @Transactional
    public ApiResponseTemplate<Void> sendVerificationCode(EmailSendReqDto reqDto) {
        String code = generateCode();
        redisTemplate.opsForValue().set(reqDto.email(), code, CODE_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        try {
            MimeMessage message = createMessage(reqDto.email(), code);
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException | MailException e) {
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILURE_EXCEPTION, ErrorCode.EMAIL_SEND_FAILURE_EXCEPTION.getMessage());
        }

        return ApiResponseTemplate.success(SuccessCode.EMAIL_SEND_CODE_SUCCESS, null);
    }

    @Transactional
    public ApiResponseTemplate<Void> verifyCode(EmailVerifyReqDto reqDto) {
        String storedCode = redisTemplate.opsForValue().get(reqDto.email());

        if (storedCode == null || !storedCode.equals(reqDto.code())) {
            throw new CustomException(ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH_EXCEPTION, ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH_EXCEPTION.getMessage());
        }

        redisTemplate.delete(reqDto.email());
        redisTemplate.opsForValue().set(VERIFIED_EMAIL_PREFIX + reqDto.email(), "true", 1, TimeUnit.HOURS);

        return ApiResponseTemplate.success(SuccessCode.EMAIL_VERIFICATION_SUCCESS, null);
    }

    protected boolean isEmailVerified(String email) {
        Boolean isVerified = redisTemplate.hasKey(VERIFIED_EMAIL_PREFIX + email);
        return Boolean.TRUE.equals(isVerified);
    }

    protected void removeVerifiedEmail(String email) {
        redisTemplate.delete(VERIFIED_EMAIL_PREFIX + email);
    }
}
