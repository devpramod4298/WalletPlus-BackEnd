package com.training.WalletPlus.service;

import com.training.WalletPlus.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.training.WalletPlus.model.User;
import com.training.WalletPlus.repo.UserRepository;
import com.training.WalletPlus.security.UserDetailsImpl;
import com.training.WalletPlus.config.JwtTokenUtil;

import java.util.Optional;

@Service
public class UserService {

	@Autowired(required=true)
	UserRepository userRepo;

	@Autowired
	JwtTokenUtil jwtTokenUtil;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	EmailService emailService;

	public ResponseEntity<Object> signup(User user) {
		if (userRepo.existsById(user.getUserName())) {
			return new ResponseEntity<>(new ErrorResponse("User Already exists for this username"),
					HttpStatus.BAD_REQUEST);
		}

		if (userRepo.existsByEmailId(user.getEmailId())) {
			return new ResponseEntity<>(new ErrorResponse("User Already exists for this email"),
					HttpStatus.BAD_REQUEST);
		}

		try {
			user = new User(user.getUserName(), user.getFullName(), user.getEmailId(),
					passwordEncoder.encode(user.getPassword()), 0,user.getAddress(),user.getPhoneNumber(), user.getAccountNo());
			 userRepo.save(user);
			String token=jwtTokenUtil.generateTokenFromUsername(user.getUserName());
			UserSignUpResponse userSignUpResponse=new UserSignUpResponse(user.getUserName(), user.getFullName(), user.getEmailId(),token);
			return new ResponseEntity<>(userSignUpResponse, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Object> login(LogInDTO loginDTO) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword()));
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		String token = jwtTokenUtil.generateTokenFromUsername(loginDTO.getUserName());
		return ResponseEntity.ok().body(new LoginResponse(userDetails.getUsername(), userDetails.getEmail(), token));
	}
	public String sendPasswordRequestEmail(String emailId) {
		User user = userRepo.findByEmailId(emailId).get();
		String token = jwtTokenUtil.generateTokenFromUsername(user.getUserName());
		String passwordResetLink = "http://localhost:3000/reset-password?token=" + token+"&name="+ user.getFullName();
		String msg = "Dear " + user.getFullName() + ",\n" + "\n"
				+ "We have received your request to change your password for the Wallet Plus. Your security is important to us, and we are here to assist you with this process.\n"
				+ "\n" + "To complete the password change, please follow these steps:\n" + "\n"
				+ "1. Click on the following link to reset your password:\n" + passwordResetLink + " \n" + "\n"
				+ "   Note: This link is valid for a limited time and will expire in 10 minutes. \n" + "\n"
				+ "2. You will be directed to a secure page where you can create a new password for your account.\n"
				+ "\n"
				+ "3. Ensure that your new password is strong and unique. Avoid using easily guessable information such as common words or phrases.\n"
				+ "\n"
				+ "4. After successfully changing your password, you will be able to log in to your WalletPlus account with your new credentials.\n"
				+ "\n"
				+ "If you did not initiate this password change request, please contact our support team immediately at support@waletplus.com . Your security is our priority, and we will investigate any unauthorized activity.\n"
				+ "\n" + "Thank you for using WalletPlus!\n" + "\n" + "Sincerely,\n" + "The Wallet Plus Team";
		EmailDetails emailDetails = new EmailDetails(emailId, msg, "Password Change Request Confirmation");
		emailService.sendSimpleMail(emailDetails);
		return "Mail Sent Successfully";
	}
	public ResponseEntity<Object> resetpassword(String token, String password) {
		try{
			String username = jwtTokenUtil.getUsernameFromToken(token);
			Optional<User> userOptional = userRepo.findById(username);
			if (userOptional.isPresent()) {
				User user = userOptional.get();
				user.setPassword(passwordEncoder.encode(password));
				userRepo.save(user);
				return new ResponseEntity<>("Password changes successfully!", HttpStatus.OK);
			}
		}
		catch(Exception ex)
		{
			return new ResponseEntity<>(new ErrorResponse("Something went wrong, Please try again!"), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(new ErrorResponse("Something went wrong, Please try again!"), HttpStatus.BAD_REQUEST);
	}
}
