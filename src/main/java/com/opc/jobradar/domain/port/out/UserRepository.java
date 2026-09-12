package com.opc.jobradar.domain.port.out;

import com.opc.jobradar.domain.model.User;

public interface UserRepository {

    boolean existsByEmail(String email);

    User findByEmail(String email);

    void save(User user);
}