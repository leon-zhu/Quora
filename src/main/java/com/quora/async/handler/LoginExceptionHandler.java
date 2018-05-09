package com.quora.async.handler;

import com.quora.async.EventHandler;
import com.quora.async.EventModel;
import com.quora.async.EventType;
import com.quora.util.MailSender;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description here
 *
 * @author: leon
 * @date: 2018/5/8 20:16
 * @version: 1.0
 */
@Component
public class LoginExceptionHandler implements EventHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginExceptionHandler.class);

    @Autowired
    private MailSender mailSender;

    @Override
    public void doHandler(EventModel model) {
        //ip异常判断
        try {
            Map<String, Object> res = new HashMap<>();
            res.put("name", model.getExt("userName"));
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_27);
            //TODO: 如何直接用相对路径
            cfg.setDirectoryForTemplateLoading(new File("D:\\java\\Quora\\src\\main\\resources\\templates\\mails"));
            Template template = cfg.getTemplate("login_exception_template.ftl");
            template.setOutputEncoding("UTF-8");

            mailSender.sendWithHTMLTemplate(model.getExt("email"),
                    "登陆ip异常", template, res);
        } catch (Exception e) {
            logger.error("读取ftl文件出错: " + e.getMessage());
        }

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
