package com.Spring.Main.Repository;

import com.Spring.Main.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

    User findByName(String name);
}
