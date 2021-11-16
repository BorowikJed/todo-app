package eu.kijora.todoapp.controller.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class LoggerFilter implements Filter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, //Defines an object to assist a servlet in sending a response to the client.
                         FilterChain chain) throws IOException, ServletException {
        if(request instanceof HttpServletRequest){
            var httpRequest = (HttpServletRequest) request;
            logger.info("[do Filter] " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());
        }
        chain.doFilter(request, response); //here we have access to these classes and pass augmented one
        logger.info("[do Filter 2]");

    }

    @Override
    public int getOrder() {
        return 1; //executes before the one with value 2. The lower, the faster. Instead of @Override and implementing you can use @Order(1) over class
    }
}
