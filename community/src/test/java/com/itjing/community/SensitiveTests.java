package com.itjing.community;

import com.itjing.community.utils.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: lijing
 * @Date: 2021年08月02日 12:48
 * @Description: 测试敏感词
 */
@SpringBootTest
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * 测试敏感词过滤
     */
    @Test
    public void testSensitiveFilter() {
        String text = "我在测试敏感词，这里可以赌博，可以嫖娼，可以吸毒，可以开票！";
        System.out.println(sensitiveFilter.filter(text));
    }

}
