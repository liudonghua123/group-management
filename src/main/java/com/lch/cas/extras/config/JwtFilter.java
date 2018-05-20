package com.lch.cas.extras.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtFilter extends GenericFilterBean {

	public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
	public static final String GENERAL_AUTHORIZATION_HEADER_PARAM = "Authorization";
	public static final String JWT_TOKEN_REQUEST_PARAM = "token";

	@Autowired
	private AppConfig appConfig;

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			chain.doFilter(req, res);
		} else {
			String token = null;
			String authorizationHeader = request.getHeader(JWT_TOKEN_HEADER_PARAM);
			if(authorizationHeader == null) {
				authorizationHeader = request.getHeader(GENERAL_AUTHORIZATION_HEADER_PARAM);
			}
			if (!StringUtils.isBlank(authorizationHeader)) {
				token = authorizationHeader;
			}
			else {
				token = request.getParameter(JWT_TOKEN_REQUEST_PARAM);
				if(StringUtils.isBlank(token)) {
					throw new ServletException("Authorization header or request parameter cannot be blank!");
				}
			}
			try {
			    // Fuck, Autowired appConfig is always null
				final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();
				request.setAttribute("claims", claims);
			} catch (final SignatureException e) {
				throw new ServletException("Invalid token");
			}

			chain.doFilter(req, res);
		}
	}
}
