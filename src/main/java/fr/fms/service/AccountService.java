package fr.fms.service;

import fr.fms.entities.AppRole;
import fr.fms.entities.AppUser;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AccountService {
    AppUser saveUser(AppUser user);

    AppRole saveRole(AppRole role);

    void addRoleToUser(String username, String rolename);

    ResponseEntity<List<AppUser>> listUsers();

    AppUser findUserByUsername(String username);
}
