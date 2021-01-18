package com.maltsev.clas.controller;

import com.maltsev.clas.request.EditUserRequest;
import com.maltsev.clas.request.PasswordRequest;
import com.maltsev.clas.response.ChangePasswordResponse;
import com.maltsev.clas.response.UserResponse;
import com.maltsev.jwt.Auth;
import com.maltsev.jwt.UserAccount;
import com.maltsev.clas.entity.UserEntity;
import com.maltsev.clas.request.LoginRequest;
import com.maltsev.clas.request.RegistrationRequest;
import com.maltsev.clas.response.AuthResponse;
import com.maltsev.clas.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.maltsev.clas.controller.constants.URIConstants.*;


@AllArgsConstructor
@RestController
@CrossOrigin(value = "*")
public class AuthController {

    private final UserService userService;

    @PostMapping(REGISTER)
    public AuthResponse registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return userService.saveUser(registrationRequest);
    }

    @PostMapping(AUTH)
    public AuthResponse auth(@RequestBody LoginRequest request) {
        return userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
    }

    @GetMapping(PROFILE)
    public UserEntity getProfileInfo(@Auth UserAccount userAccount) {
        return userService.findByUserId(userAccount.getUserId());
    }
    @PostMapping(EDIT_USER)
    public UserResponse editUser(@Auth UserAccount user,
                                 @RequestBody EditUserRequest request) {
        return userService.editUser(user, request);
    }
    @PostMapping(CHANGE_PASSWORD)
    public ChangePasswordResponse changePassword(@Auth UserAccount userAccount,
                                                 @RequestBody @Valid PasswordRequest passwordRequest) {
        return userService.changePassword(userAccount, passwordRequest);
    }


}
