package com.maltsev.clas.service;

import com.maltsev.clas.entity.ClassEntity;
import com.maltsev.clas.request.AddSubscribersRequest;
import com.maltsev.clas.request.CreateClassRequest;
import com.maltsev.clas.response.AddSubscribersResponse;
import com.maltsev.clas.response.ClassInfoResponse;
import com.maltsev.clas.response.ReminderResponse;
import com.maltsev.clas.response.UserResponse;
import com.maltsev.cross.message.ClassStatusMessage;
import com.maltsev.cross.message.EmailMessage;
import com.maltsev.jwt.Auth;
import com.maltsev.jwt.UserAccount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.maltsev.clas.constants.URIConstants.CLASS_SUBSCRIBERS;

public interface ClassService {
    ClassInfoResponse createClass(UserAccount user, CreateClassRequest createClassRequest);

    List<ClassInfoResponse> getStudentClass(@Auth UserAccount user, String id);

    ClassInfoResponse getClassInfo(String id);

    List<ClassInfoResponse> getAllClass();

    List<ClassInfoResponse> getMyClass(UserAccount user);

    List<UserResponse> getMyClassSubscribers(UserAccount user);

    AddSubscribersResponse addSubscribers(UserAccount user, AddSubscribersRequest request);

    void setStatus(ClassStatusMessage message);

    List<ClassInfoResponse> getSubscriptions(UserAccount user);

    List<UserResponse> getClassSubscribers(String id);

    void startClass(ClassStatusMessage message);

    List<ReminderResponse> getReminders(UserAccount user);

    ReminderResponse editReminder(UserAccount user, String id);

}
