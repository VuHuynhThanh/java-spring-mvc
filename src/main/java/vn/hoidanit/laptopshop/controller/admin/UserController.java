package vn.hoidanit.laptopshop.controller.admin;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.repository.UserRepository;
import vn.hoidanit.laptopshop.service.UploadService;
import vn.hoidanit.laptopshop.service.UserService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {

    // DI: dependency injection
    private final UserService userService;
    private final UploadService uploadService;

    public UserController(UserService userService, UserRepository userRepository, UploadService uploadService) {
        this.userService = userService;
        this.uploadService = uploadService;
    }

    @RequestMapping("/")
    public String getHomePage(Model model) {
        List<User> arrUsers = this.userService.getAllUsersByEmail("huynhthanhvu@gmail.com");
        System.out.println(arrUsers);
        return "hello";
    }

    @RequestMapping("/admin/user")
    public String getUserPage(Model model) {
        List<User> users = this.userService.getAllUsers();
        model.addAttribute("users1", users);
        return "/admin/user/show";
    }

    @RequestMapping("/admin/user/{id}")
    public String getUserDetailPage(Model model, @PathVariable Long id) {
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        return "/admin/user/detail";
    }

    @GetMapping(value = "/admin/user/create") // Defaul method: GET
    public String getCreateUserPage(Model model) {
        model.addAttribute("newUser", new User());
        return "/admin/user/create";
    }

    @RequestMapping(value = "/admin/user/update/{id}") // Defaul method: GET
    public String getUpdateUserPage(Model model, @PathVariable Long id) {
        User currentUser = this.userService.getUserById(id);
        model.addAttribute("newUser", currentUser);
        return "/admin/user/update";
    }

    @PostMapping(value = "/admin/user/create")
    // define MultipartFile[] to upload multi files
    public String createUserPage(Model model, @ModelAttribute("newUser") User hoidanit,
            @RequestParam("hoidanitFile") MultipartFile file) {
        // System.out.println("run here " + hoidanit);
        String avatar = this.uploadService.handleSaveUploadFile(file, "avatar");
        // this.userService.handleSaveUser(hoidanit);
        return "redirect:/admin/user";
    }

    @PostMapping(value = "/admin/user/update") // Defaul method: GET
    public String postUpdateUserPage(Model model, @ModelAttribute("newUser") User hodanit) {
        User currentUser = this.userService.getUserById(hodanit.getId());
        if (currentUser != null) {
            currentUser.setAddress(hodanit.getAddress());
            currentUser.setFullName(hodanit.getFullName());
            currentUser.setPhone(hodanit.getFullName());

            this.userService.handleSaveUser(currentUser);

            int dd = 120;
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/user/delete/{id}")
    public String getDeleteUserPage(Model model, @PathVariable long id) {
        User user = new User();
        user.setId(id);
        model.addAttribute("newUser", user);
        return "/admin/user/delete";
    }

    @PostMapping("/admin/user/delete")
    public String postDeleteUserPage(Model model, @ModelAttribute User user) {
        this.userService.deleteAUser(user.getId());
        return "redirect:/admin/user";
    }

}

// @RestController
// public class UserController {

// // DI: dependency injection
// private UserService userService;

// public UserController(UserService userService) {
// this.userService = userService;
// }

// @GetMapping("/")
// public String getHomePage() {
// return this.userService.handleHello();
// }
// }
