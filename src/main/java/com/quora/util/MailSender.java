package com.quora.util;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * 邮件发送服务
 *
 * @author: leon
 * @date: 2018/5/8 19:51
 * @version: 1.0
 */
@Service
public class MailSender implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    private JavaMailSenderImpl mailSender;

    /**
     *
     * @param to 收件人
     * @param subject 标题
     * @param message 内容
     * @param model 模型数据
     * @return
     */
    public boolean sendWithHTMLTemplate(String to, String subject,
                                        Template template, Map<String, Object> model) {
        try {
            String nick = MimeUtility.encodeText("leon");
            InternetAddress from = new InternetAddress(nick + "<lyzhu0318@163.com>");
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);

            String res = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            mimeMessageHelper.setText(res, true);
            mailSender.send(mimeMessage);
            return true;

        } catch (Exception e) {
            logger.error("发送邮件失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender = new JavaMailSenderImpl();
        mailSender.setUsername("lyzhu0318");
        mailSender.setPassword("***"); //注意: 这个是授权码, 不是密码
        mailSender.setHost("smtp.163.com");
        mailSender.setPort(25); //端口
        mailSender.setProtocol("smtp"); //smtp
        mailSender.setDefaultEncoding("utf8");
    }
}
