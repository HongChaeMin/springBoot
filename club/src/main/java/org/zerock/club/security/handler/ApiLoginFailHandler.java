package org.zerock.club.security.handler;

import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j2
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("login fail handler ------------------------------");
        log.info(exception.getMessage());
        log.info("-------------------------------------------------");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json;Charset=utf-8");
        JSONObject json = new JSONObject();
        String message = exception.getMessage();
        json.put("code", 401);
        json.put("message", message);

        PrintWriter out = response.getWriter();
        out.print(json);
    }

}
