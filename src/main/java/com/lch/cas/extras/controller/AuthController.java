package com.lch.cas.extras.controller;

import java.util.Date;

import javax.servlet.ServletException;

import com.lch.cas.extras.config.AppConfig;
import com.lch.cas.extras.model.User;
import com.lch.cas.extras.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
@Configuration
public class AuthController {

	public static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AppConfig appConfig;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<TokenEntity> login(@RequestParam String uid, @RequestParam String password) throws ServletException {
		User user = userService.findByUid(uid);
		if (user == null) {
			throw new ServletException(String.format("User logined with %s/%s not found.", uid, password));
		}
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equalsIgnoreCase(user.getPassword())) {
			throw new ServletException("Invalid login. Please check your uid and password.");
		}
		String jwtToken = Jwts.builder().setSubject(uid).claim("roles", "user").setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, appConfig.getSecretkey()).compact();
		return new ResponseEntity<TokenEntity>(new TokenEntity(jwtToken), HttpStatus.OK);
	}


	static class TokenEntity {
		String token;
		TokenEntity(String token) {
			this.token = token;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}
	}
}
