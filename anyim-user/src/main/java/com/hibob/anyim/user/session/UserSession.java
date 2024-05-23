package com.hibob.anyim.user.session;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Data
@Slf4j
public class UserSession {
    /**
     * 账号
     */
    private String account;

    /*
    * 客户端ID
     */
    private String uniqueId;

    /**
     * 用户名称
     */
    private String nickName;

    public static UserSession getSession() {
        // 从上下文中提取Request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        UserSession session = (UserSession) request.getAttribute("session");
        log.info("session: {}", session);
        return session;
    }

}
