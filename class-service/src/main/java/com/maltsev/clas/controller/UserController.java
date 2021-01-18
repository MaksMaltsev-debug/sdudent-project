package com.maltsev.clas.controller;

import com.maltsev.clas.request.EditUserRequest;
import com.maltsev.clas.request.SetNotificationsRequest;
import com.maltsev.clas.response.DeleteUserResponse;
import com.maltsev.clas.response.NotificationsResponse;
import com.maltsev.clas.response.UserResponse;
import com.maltsev.clas.service.UserService;
import com.maltsev.jwt.Auth;
import com.maltsev.jwt.UserAccount;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.maltsev.clas.constants.URIConstants.*;

@AllArgsConstructor
@RestController
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @GetMapping(ALL_USERS)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping(USER)
    public DeleteUserResponse deleteUser(@Auth UserAccount user) {
        return userService.deleteUserById(user);
    }

    @GetMapping(USER)
    public UserResponse getUser(@Auth UserAccount user) {
        return userService.getUserById(user);
    }

    @GetMapping(USER_INFO)
    public UserResponse getUserInfoById(@Auth UserAccount user, String id) {
        return userService.getUserInfoById(user,id);
    }

    @PostMapping(NOTIFICATIONS)
    public NotificationsResponse setNotifications(@Auth UserAccount user,
                                                  @RequestBody SetNotificationsRequest request) {
        return userService.setNotifications(user, request);
    }

    @PostMapping(EDIT_DESCRIPTION)
    public UserResponse editDescription(@Auth UserAccount user,
                                        @RequestBody EditUserRequest request) {
        return userService.editDescription(user, request);
    }

    @GetMapping(NOTIFICATIONS)
    public NotificationsResponse getNotifications(@Auth UserAccount user) {
        return userService.getNotifications(user);
    }


}
