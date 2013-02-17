package be.kdg.trips.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Robin
 * Date: 17/02/13
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */
public class LogFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        if (((HttpServletRequest) req).getSession().getAttribute("user")!=null) {
            chain.doFilter(req, resp);
        } else {
            ((HttpServletResponse) resp).sendRedirect("/errors/loginError");
        }
    }

    public void init(FilterConfig config) throws ServletException {

    }

    public void destroy() {

    }

}
