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
    public String redirecting() {
        return "redirect:/main";
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

    @GetMapping("/invite")
    public String inviteUser(Model model, @RequestParam Long id, Principal principal) {

        User logedUser = ur.findByUsername(principal.getName()).get();
        if (logedUser.getId() == id) {
            return "redirect:/main";
        } else if (!ur.findById(id).isPresent()) {
            return "redirect:/main";
        } else if (!rr.checkSet(logedUser, ur.findById(id).get()).isEmpty()) {
            return "redirect:/main";
        }
        User invitedUser = ur.findById(id).get();
        rr.save(new Relation(logedUser, invitedUser, false));
        model.addAttribute("userInfo", logedUser);
        return "redirect:/invitations";
    }

    @GetMapping("/invitations")
    public String showAllInvitation(Model model, Principal principal) {

        User user = ur.findByUsername(principal.getName()).get();

        model.addAttribute("usersInviting", rr.listAllByUser_invitedAndConfirmedIsFalse(user));
        model.addAttribute("usersInvited", rr.listAllByUser_initialAndConfirmedIsFalse(user));
        model.addAttribute("userInfo", ur.findByUsername(principal.getName()).get());

        return "invitationsList";
    }

    @GetMapping("/confirm")
    public String confirmInvitattion(Model model, @RequestParam Long id, Principal principal) {

        Optional<User> invitingUser = ur.findById(id);
        Optional<User> invitedUser = ur.findByUsername(principal.getName());


        Optional<Relation> relation = rr.findRelationByUserAndUser(invitingUser.get(), invitedUser.get());

        if (relation.isPresent()) {
            Relation reltionConf = relation.get();
            reltionConf.setConfirmed(true);
            rr.save(reltionConf);

            return "redirect:/allFriends";
        }

        return "redirect:/invitations";
    }

    @GetMapping("/refuse")
    public String refuseInvitation(Model model, @RequestParam Long id, Principal principal) {

        Optional<User> invitingUser = ur.findById(id);
        Optional<Relation> relation = rr.findRelationByUserAndUser(invitingUser.get(), ur.findByUsername(principal.getName()).get());

        if (relation.isPresent()) {
            Relation reltionConf = relation.get();
            rr.delete(reltionConf);

            return "redirect:/invitations";
        }
        return "redirect:/invitations";
    }

    @GetMapping("/adminManager")
    public String showAdminManager(@RequestParam(required = false) String name, @RequestParam(required = false) String surname,
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

        return "usersList";
    }

    @GetMapping("/blockUser")
    public String blockUser(@RequestParam Long id) {

        Optional<User> user = ur.findById(id);

        if (user.isPresent() && !user.get().getUsername().equals("admin")) {
            User blockedUsed = user.get();
            blockedUsed.setEnabled(false);
            blockedUsed.setId(id);
            ur.save(blockedUsed);
        }
        return "redirect:/adminManager";
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {

        Optional<User> user = ur.findById(id);

        if (user.isPresent() && !user.get().getUsername().equals("admin")) {
            User blockedUsed = user.get();
            blockedUsed.setId(id);
            ur.delete(blockedUsed);
        }
        return "redirect:/adminManager";
    }


    @RequestMapping("/userDetails")
    public String showUserDetail(Model model, Principal principal) {

        Optional<User> userOptional = ur.findByUsername(principal.getName());
        model.addAttribute("userDetails", userOptional.get());

        return "userDetails";
    }

    @GetMapping("/editProfile")
    public String editUserDetail(Model model, Principal principal) {

        Optional<User> userOptional = ur.findByUsername(principal.getName());
        User userEdit = userOptional.get();

        model.addAttribute("userEdit", userEdit);
        model.addAttribute("cities", cr.findAll());

        return "userProfileForm";
    }

    @PostMapping("/executeEdition")
    public String editUser(User user, Principal principal, @RequestParam Long id) {
        User logedUser = ur.findByUsername(principal.getName()).get();

        if (!logedUser.getUsername().equals(user.getUsername())) {
           List<UserRole> roleList = urr.findByUsername(logedUser.getUsername());
           UserRole userRole = null;

           for (int i = 0; i < roleList.size(); i++) {
                userRole = roleList.get(i);
                userRole.setUsername(user.getUsername());
                urr.save(userRole);
            }

        }
        user.setEnabled(true);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));

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

    @GetMapping("/registrationForm")
    public String showRegistrationForm(Model model) {

        model.addAttribute("newUser", new User());
        model.addAttribute("cities", cr.findAll());
        return "registrationForm";
    }

    @PostMapping("/register")
    public String registerUser(User user) {

        user.setEnabled(true);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        ur.save(user);

        urr.save(new UserRole(user.getUsername(), "ROLE_USER"));

        return "redirect:/login";
    }
}