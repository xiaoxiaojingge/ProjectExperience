package com.itjing.community;

import com.itjing.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author: lijing
 * @Date: 2021年08月03日 14:58
 * @Description: 本地缓存测试
 */
@SpringBootTest
public class CaffeineTests {

    @Autowired
    private DiscussPostService postService;

    /**
     * 这里初始化数据是为了后面做压力测试
     */

    public static void main(String[] ages) {
        //1.导入驱动jar包
        //2.注册驱动(mysql5之后的驱动jar包可以省略注册驱动的步骤)
        //Class.forName("com.mysql.jdbc.Driver");
        //3.获取数据库连接对象
        Connection conn = null;
        PreparedStatement pstmt = null;
        {
            try {
                //"&rewriteBatchedStatements=true",一次插入多条数据，只插入一次,所以插入比较快。
                conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/community?" + "&rewriteBatchedStatements=true", "root", "root");
                //4.定义sql语句
                String sql = "insert into discuss_post values(default,?,?,?,?,?,?,?,?)";
                //5.获取执行sql的对象PreparedStatement
                pstmt = conn.prepareStatement(sql);
                //6.不断产生sql
                for (int i = 0; i < 300000; i++) {
                    pstmt.setString(1, "111");
                    pstmt.setString(2, "互联网求职暖春计划");
                    pstmt.setString(3, "今年的就业形势，确实不容乐观。过了个年，仿佛跳水一般，整个讨论区哀鸿遍野！19届真的没人要了吗？！18届被优化真的没有出路了吗？！大家的“哀嚎”与“悲惨遭遇”牵动了每日潜伏于讨论区的牛客小哥哥小姐姐们的心，于是牛客决定：是时候为大家做点什么了！为了帮助大家度过“寒冬”，牛客网特别联合60+家企业，开启互联网求职暖春计划，面向18届&19届，拯救0 offer！");
                    pstmt.setInt(4, 0);
                    pstmt.setInt(5, 0);
                    pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
                    pstmt.setInt(7, 0);
                    pstmt.setDouble(8, Math.random() * 2000);
                    pstmt.addBatch();
                }
                //7.往数据库插入一次数据
                pstmt.executeBatch();
                System.out.println("添加300000条信息成功！");

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                //8.释放资源
                //避免空指针异常
                if (pstmt != null) {
                    try {
                        pstmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 测试本地缓存
     */
    @Test
    public void testCache() {
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 1));
        System.out.println(postService.findDiscussPosts(0, 0, 10, 0));
    }

}