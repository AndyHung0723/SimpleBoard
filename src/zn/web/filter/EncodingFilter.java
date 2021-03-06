package zn.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;

import zn.web.wrapper.EncodingWrapper;

@WebFilter(
		urlPatterns = {"/*"},
		initParams = {
				@WebInitParam(name = "ENCODING", value = "UTF-8")
		}
)

public class EncodingFilter implements Filter{

	private String Encoding;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		Encoding = config.getInitParameter("ENCODING");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		if("GET".equals(req.getMethod())) {
			req = new EncodingWrapper(req, Encoding);
		}else {
			req.setCharacterEncoding(Encoding);
		}
		chain.doFilter(req, response);
	}
	
	@Override
	public void destroy() {
		Encoding = null;
	}
	
}
