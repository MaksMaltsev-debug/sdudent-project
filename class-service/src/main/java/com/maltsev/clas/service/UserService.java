package com.maltsev.clas.service;

import com.maltsev.clas.entity.UserEntity;
import com.maltsev.clas.request.CreateClassRequest;
import com.maltsev.clas.request.EditUserRequest;
import com.maltsev.clas.request.SetNotificationsRequest;
import com.maltsev.clas.response.DeleteUserResponse;
import com.maltsev.clas.response.NotificationsResponse;
import com.maltsev.clas.response.UserResponse;
import com.maltsev.cross.message.NewUserMessage;
import com.maltsev.jwt.Auth;
import com.maltsev.jwt.UserAccount;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface UserService {
    UserResponse createUser(NewUserMessage message);

    List<UserResponse> getAllUsers();

    DeleteUserResponse deleteUserById(@Auth UserAccount user);

    UserResponse getUserById(@Auth UserAccount user);

    NotificationsResponse setNotifications(@Auth UserAccount user, @RequestBody SetNotificationsRequest request);

    NotificationsResponse getNotifications(@Auth UserAccount user);

    UserResponse editDescription(UserAccount user, EditUserRequest request);

    UserResponse getUserInfoById(@Auth UserAccount user, String id);
}
