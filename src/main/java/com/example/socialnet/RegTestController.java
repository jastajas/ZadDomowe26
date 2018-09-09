package com.example.socialnet;

import org.springframework.mail.MailException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;
import java.util.*;

@Controller
public class RegTestController {

    private UserRepository ur;
    private UserRoleRepository urr;
    private CityRepository cr;
    private RelationRepository rr;

    // private MailSenderSocial mss;


    public RegTestController(UserRepository userRepository, UserRoleRepository userRoleRepository,
                             CityRepository cityRepository, RelationRepository relationRepository /*MailSenderSocial mailSenderSocial*/) {
        this.ur = userRepository;
        this.urr = userRoleRepository;
        this.cr = cityRepository;
        this.rr = relationRepository;
        //  this.mss = mailSenderSocial;
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

        model.addAttribute("searchedUsers", getUsersList(name, surname));

        return "index";
    }

//    @GetMapping("/sendMail")
//    public String sendMail() {
//        try {
//            mss.sendSimpleMessage();
//        }catch (MailException e){
//            e.printStackTrace();;
//        }
//        return "redirect:/main";
//    }

    public List<User> getUsersList(String name, String surname) {
        if (name == null) {
            return ur.findAll();
        } else if (!name.equals("") && !surname.equals("")) {
            return ur.findByNameAndSurname(name, surname);
        } else if (!name.equals("") && surname.equals("")) {
            return ur.findByName(name);
        } else if (name.equals("") && !surname.equals("")) {
            return ur.findBySurname(surname);
        }
        return null;
    }

    @GetMapping("/allFriends")
    public String showAllFriends(Model model, Principal principal, SurnameComparator surnameComparator, HttpServletRequest request) {

        User user = ur.findByUsername(principal.getName()).get();

        List<User> allfriends = new ArrayList<>();

        for (Relation relation : rr.listAllByUser_initialAndConfirmedIsTrue(user)) {
            allfriends.add(relation.getUser_invited());
        }
        for (Relation relation : rr.listAllByUser_invitedAndConfirmedIsTrue(user)) {
            allfriends.add(relation.getUser_initial());
        }

        Collections.sort(allfriends, surnameComparator);

        model.addAttribute("allfriends", allfriends);
        model.addAttribute("userInfo", ur.findByUsername(principal.getName()).get());
        //request.isUserInRole("ROLE_USER");

        //Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();

//        for (SimpleGrantedAuthority authority : authorities) {
//            System.out.println(authority.toString());
//
//        }

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
    public String confirmInvitattion(@RequestParam Long id, Principal principal) {

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
    public String refuseInvitation(@RequestParam Long id, Principal principal) {

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

        model.addAttribute("searchedUsers", getUsersList(name, surname));

        return "usersList";
    }

   /* @GetMapping("/delRel")
    public String deleteRelation(@RequestParam Long id){
        Optional<User> invitedUser = ur.findById(id);
        Optional<Relation>
        if (invitedUser.isPresent()){

        }
    }*/

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
    public String showUserDetail(Model model, Principal principal, @RequestParam(required = false) Long id, @RequestParam(required = false) MultipartFile pathName) throws IOException {

        Optional<User> userLoged = ur.findByUsername(principal.getName());
        model.addAttribute("userLoged", userLoged.get());

        if (id == null) {
            model.addAttribute("userDetails", userLoged.get());
        } else {
            Optional<User> userDetails = ur.findById(id);
            model.addAttribute("userDetails", userDetails.get());
        }
        if (pathName != null) {

            OutputStream outputStream = new FileOutputStream("C:\\Users\\Madi_i_Jack\\IdeaProjects\\socialnet\\src\\main\\resources\\static\\" + userLoged.get().getId() + ".jpg");
            outputStream.write(pathName.getBytes());
            outputStream.close();
        }
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
    public String editUser(User user, Principal principal) {
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