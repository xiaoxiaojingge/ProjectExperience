package com.itjing.community;

import com.itjing.community.utils.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author: lijing
 * @Date: 2021年08月02日 12:49
 * @Description: 测试邮箱发送
 */
@SpringBootTest
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("1045455624@qq.com", "Test Send Text Mail", "我是李晶，我在测试发送文本邮件！");
    }

    @Test
    public void testHtmlMail() {

        Context context = new Context();
        // 设置参数，自动填充到下面设置的html中的动态参数位置
        context.setVariable("username", "lijing");

        // 使用 templateEngine 根据现有的 html 文件生成 html 文本
        // 这里使用的是 templates 下 mail 中的 demo.html 文件
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("1045455624@qq.com", "Test Send Html Mail", content);
    }
}
