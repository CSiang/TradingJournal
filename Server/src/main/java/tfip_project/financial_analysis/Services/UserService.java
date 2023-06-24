package tfip_project.financial_analysis.Services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tfip_project.financial_analysis.Repositories.AppUserRepository;
import tfip_project.financial_analysis.Repositories.RoleRepository;
import tfip_project.financial_analysis.Security.Models.AppUser;
import tfip_project.financial_analysis.Security.Models.Role;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private AppUserRepository userRepo;
    
    @Autowired
    private RoleRepository roleRepo;

    // Must instantiate the encoder by using new. @Autowired will lead to circular-dependencies issue.
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    Logger logger = Logger.getLogger(UserService.class.getName());

    public AppUser saveUser(AppUser user) {
        logger.info("Saving new user %s to the database".formatted(user.getName()));
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public Role saveRole(Role role) {
        logger.info("Saving new role, %s, to the database".formatted(role.getName()));
        return roleRepo.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        
        logger.info("Add role %s to user %s to the database".formatted(roleName, username));

        AppUser user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);

        user.getRoles().add(role);
    }

    public AppUser getUser(String username) {
        logger.info("Getting user %s from the database".formatted(username));

        return userRepo.findByUsername(username);
    }

    public List<AppUser> getUsers() {
        logger.info("Get all users from the database");
        return userRepo.findAll();
    }

    // From UserDetailsService.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username);
        if(user == null){
            logger.info("User not found in the database.");
            throw new UsernameNotFoundException("User not found in the database.");
        } else {
            logger.info("User found in the database: %s".formatted(username));
        }

        // here to return a Spring Security User.
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        System.out.println("Userpassword from userService: " + user.getPassword());
        return new User(username, user.getPassword(), authorities);
    }

    @Transactional
    public void updateUser(AppUser user){
        // encode the password before saving into database
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.updateUser(user.getEmail(), user.getName(), user.getPassword(), 
                            user.getUsername(), user.getId());
    }

    public void updateUserCalId(String calendarId, Long userId){
        try{
            userRepo.updateUserCalId(calendarId, userId);
        } catch (Exception ex){
            System.out.println("Error when updating user calendarId.");
            ex.printStackTrace();
        }
    }


}
