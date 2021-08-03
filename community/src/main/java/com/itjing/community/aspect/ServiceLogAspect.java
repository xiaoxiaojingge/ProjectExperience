package com.itjing.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: lijing
 * @Date: 2021年08月01日 14:43
 * @Description: 业务日志方面组件
 */
/*@Component
@Aspect*/
public class ServiceLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * 定义切点 ， execution 中为切点表达式
     */
    @Pointcut("execution(* com.itjing.community.service.*.*(..))")
    public void pointcut() {

    }

    /**
     *
     * @param joinPoint
     */
    @Before("pointcut()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 此处要判断是否为空，可能有情况导致此处为null，比如生产消费
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 获取 类名.方法名
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        logger.info(String.format("用户[%s],在[%s],访问了[%s].", ip, now, target));

        // 用户[127.0.0.1],在[2021-08-01 14:53:38],访问了[com.itjing.community.service.impl.UserServiceImpl.findUserById]
    }

}
