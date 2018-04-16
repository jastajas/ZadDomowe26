package com.example.socialnet;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class RegTestController {

    UserRepository ur;
    UserRoleRepository urr;
    CityRepository cr;
    RelationRepository rr;

    public RegTestController(UserRepository userRepository, UserRoleRepository userRoleRepository,
                             CityRepository cityRepository, RelationRepository relationRepository) {
        this.ur = userRepository;
        this.urr = userRoleRepository;
        this.cr = cityRepository;
        this.rr = relationRepository;
    }

    @GetMapping("/")
    public String redirecting(){
        return "redirect:/main";
    }

    @GetMapping("/registrationForm")
    public String showRegistrationForm(Model model) {

        model.addAttribute("newUser", new User());
        model.addAttribute("cities", cr.findAll());
        return "registrationForm";
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {

        user.setEnabled(true);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        ur.save(user);

        urr.save(new UserRole(user.getUsername(), "ROLE_USER"));

        return "redirect:/login";
    }


    @RequestMapping("/main")
    public String showMainPage(@RequestParam(required = false) String name, @RequestParam(required = false) String surname,
                               Model model, Principal principal) {

        Optional<User> userOptional = ur.findByUsername(principal.getName());
        model.addAttribute("userInfo", userOptional.get());

        if (name == null) {
            model.addAttribute("searchedUsers", ur.findAll());
        } else if (!name.equals("") && !surname.equals("")) {
            model.addAttribute("searchedUsers", ur.findByNameAndSurname(name, surname));
        } else if (!name.equals("") && surname.equals("")) {
            model.addAttribute("searchedUsers", ur.findByName(name));
        } else if (name.equals("") && !surname.equals("")) {
            model.addAttribute("searchedUsers", ur.findBySurname(surname));
        }

        return "index";
    }

    @GetMapping("/allFriends")
    public String showAllFriends(Model model, Principal principal) {

        User user = ur.findByUsername(principal.getName()).get();

        model.addAttribute("usersInvited", rr.listAllByUser_initialAndConfirmedIsTrue(user));

        model.addAttribute("usersInviting", rr.listAllByUser_invitedAndConfirmedIsTrue(user));
        model.addAttribute("userInfo", ur.findByUsername(principal.getName()).get());

        return "friendsList";
    }

    @RequestMapping("/userDetails")
    public String showUserDetail(Model model, Principal principal) {

        Optional<User> userOptional = ur.findByUsername(principal.getName());
        model.addAttribute("userDetails", userOptional.get());

        return "userDetails";
    }

    @RequestMapping("/editProfile")
    public String editUserDetail(Model model, Principal principal) {

        Optional<User> userOptional = ur.findByUsername(principal.getName());
        model.addAttribute("userEdit", userOptional.get());
        model.addAttribute("cities", cr.findAll());

        return "userProfileForm";
    }

    @PostMapping("/executeEdition")
    public String editUser(User user, @RequestParam Long id) {
        user.setId(id);
        ur.save(user);
        return "redirect:/userDetails";
    }

    @GetMapping("/koduj")
    @ResponseBody
    public String koduj(@RequestParam String sth) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(sth);

        return encode;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}