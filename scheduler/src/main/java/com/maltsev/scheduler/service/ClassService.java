package com.maltsev.scheduler.service;

public interface ClassService {
    void startClass(String userId, String classId);

    void endClass(String userId, String classId);
}
