package tfip_project.financial_analysis.Controllers;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import tfip_project.financial_analysis.Payload.MsgResponse;
import tfip_project.financial_analysis.Repositories.AppUserRepository;
import tfip_project.financial_analysis.Repositories.RoleRepository;
import tfip_project.financial_analysis.Security.Models.AppUser;
import tfip_project.financial_analysis.Security.Models.Role;
import tfip_project.financial_analysis.Services.CalendarService;
import tfip_project.financial_analysis.Services.EmailService;
import tfip_project.financial_analysis.Services.UserService;

@RestController
@RequestMapping(path ="/auth")
public class AuthController {

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    @Autowired
    UserService userSvc;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    AppUserRepository userRepo;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    EmailService emailSvc;

    @Autowired
    CalendarService calSvc;

    
    @PostMapping(path = "/register")
    @Transactional
    public ResponseEntity<MsgResponse> register(@RequestBody AppUser user){

        if(userRepo.existsByUsername(user.getUsername())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MsgResponse("Username already in use, please enter a new username."));

        }else if(userRepo.existsByEmail(user.getEmail())){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new MsgResponse("This email is already registered!!"));
        }

        Role role = roleRepo.findByName("USER");
        user.getRoles().add(role);

        // do NOT use the method below, it will cause Hibernate "object references an unsaved transient instance - save the transient instance before flushing" error. Need to find the saved role in database before we can save it but NOT creating a new 1 for the already existed entity.
        // Role role1 = new Role();
		// role1.setName("USER");
		// user.getRoles().add(role1);

		AppUser savedUser = userSvc.saveUser(user);

        try{
            String newCalId = calSvc.createNewCalendar(savedUser.getId());
            System.out.println(newCalId);
            userSvc.updateUserCalId(newCalId, savedUser.getId());
            System.out.println("calendarId has been successfully updated.");
        } catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgResponse("Server error. Please contact your administrator."));
        }

        return ResponseEntity.ok(new MsgResponse("User is saved."));
    }

    // Quite sure that will get an AppUser because frontend can only send an username after login, which the username is obtained from server during authentication.
    @GetMapping(path = "user/{username}")
    public ResponseEntity<AppUser> getUser(@PathVariable String username){
        return ResponseEntity.ok(userSvc.getUser(username));
    }

    @PostMapping(path = "verify")
    public ResponseEntity<MsgResponse> verifyUser(@RequestBody Map<String, String> userData){

        String username = userData.get("username");
        String password = userData.get("password");

        AppUser user = userSvc.getUser(username);
            
        if(encoder.matches(password, user.getPassword())){
            return ResponseEntity.ok().body(new MsgResponse("Password is correct."));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MsgResponse("Password incorrect!"));
        }
    }

    @PostMapping(path = "update")
    @Transactional
    public ResponseEntity<MsgResponse> updateProfile(@RequestBody AppUser user){

        try{
            userSvc.updateUser(user);
            return ResponseEntity.ok(new MsgResponse("Profile is successfully update."));
        } catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new MsgResponse("Update fail...Server error:\n" + ex.getMessage()));
        }
    }

    @GetMapping(path="forgetPassword")
    public ResponseEntity<MsgResponse> sendPasswordResetEmail(
        @RequestParam(value = "email") String add, 
        @RequestParam(value="domain") String domain ){

        if(!userRepo.existsByEmail(add)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MsgResponse("This email is not registered."));
        }

        try{
            String resetCode = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(add, resetCode, Duration.ofHours(1));  
    
            String resetUrl = domain + "/resetPassword/" + resetCode;
    
            String emailContent = """
                    Hi there,
    
                    We have received your request to reset your app password.
                    Please reset your password at the link below within 1 hour:
                    %s\n
                    Ignore this email if this is NOT requested by you.\n\n
                    Best Regards,
                    Trading Journal Team
                    """.formatted(resetUrl);
    
            emailSvc.sendSimpleMessage(add, "Important - Password Reset", emailContent);
        } catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgResponse("Server error: " + ex.getMessage() +
                             "\nPlease contacts administrator."));
        }

        return ResponseEntity.ok(new MsgResponse("Password reset email is sent."));
    } 

    @GetMapping(path="resetPassword")
    @Transactional
    public ResponseEntity<MsgResponse> resetPassword(
        @RequestParam(value = "email") String add, 
        @RequestParam(value="password") String password,
        @RequestParam(value="resetCode") String resetCode){
            
        String storedCode = redisTemplate.opsForValue().get(add);

        if( storedCode != null && storedCode.equals(resetCode) ){

            try{
                AppUser user = userRepo.findByEmail(add);
                user.setPassword(password);
                userSvc.updateUser(user);

                String emailContent = """
                    Hi %s,
    
                    Your password has been successfully reset!!!
                    Please login to your account again with the username below:
                    %s\n\n
                    Best Regards,
                    Trading Journal Team
                    """.formatted(user.getName(), user.getUsername());
    
                emailSvc.sendSimpleMessage(add, "Important - Successful Password Reset!", emailContent);

                return ResponseEntity.ok(new MsgResponse("Please check your email for your username."));

            } catch (Exception ex){
                ex.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MsgResponse("Server error: " + ex.getMessage() +
                             "\nPlease contacts administrator."));
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MsgResponse("Please request for new password reset email."));
        }
    }

}
