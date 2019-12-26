package wopen.albumservice.app.account;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wopen.albumservice.app.exception.WrongCaptchaException;
import wopen.albumservice.domain.model.user.User;
import wopen.albumservice.domain.model.user.UserRepo;
import wopen.albumservice.messaging.EmailSender;
import wopen.albumservice.utils.$;

import java.time.Duration;

@Service
public class AccountServiceImpl implements AccountService {
    private static final String CAPTCHA_PREFIX = "captcha:";
    private final UserRepo userRepo;
    private final EmailSender emailSender;
    private final PasswordEncoder passwordEncoder;
    private final StringRedisTemplate redisTemplate;

    public AccountServiceImpl(UserRepo userRepo, EmailSender emailSender, PasswordEncoder passwordEncoder, StringRedisTemplate redisTemplate) {
        this.userRepo = userRepo;
        this.emailSender = emailSender;
        this.passwordEncoder = passwordEncoder;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void sendSignUpCaptcha(String email) {
        String captcha = RandomStringUtils.randomNumeric(6);
        redisTemplate
                .opsForValue()
                .set(CAPTCHA_PREFIX + email, captcha, Duration.ofMinutes(2));
        emailSender.send(
                email,
                "注册验证码",
                "<html><body><h3>" + captcha + "</h3></body></html>"
        );
    }

    @Override
    public void signUp(String email, String password, String captcha) {
        String key = CAPTCHA_PREFIX + email;
        String correctCaptcha = redisTemplate.opsForValue().get(key);
        if (!StringUtils.equals(captcha, correctCaptcha)) {
            throw new WrongCaptchaException();
        }

        User user = new User($.uuidString(), passwordEncoder.encode(password), email);
        userRepo.save(user);

        redisTemplate.delete(key);
    }
}
