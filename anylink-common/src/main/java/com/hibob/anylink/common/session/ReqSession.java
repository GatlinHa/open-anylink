package com.hibob.anylink.common.session;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Data
@Slf4j
public class ReqSession {
    /**
     * 账号
     */
    private String account;

    /*
    * 客户端ID
     */
    private String clientId;

    public static ReqSession getSession() {
        // 从上下文中提取Request对象
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        ReqSession session = (ReqSession) request.getAttribute("session");
        return session;
    }

}
