package fr.fms.service;

import fr.fms.dao.AppRoleRepository;
import fr.fms.dao.AppUserRepository;
import fr.fms.entities.AppRole;
import fr.fms.entities.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AppRoleRepository appRoleRepository;

    @Autowired
    BCryptPasswordEncoder bcryptPasswordEncoder;

    @Override
    public AppUser saveUser(AppUser user) {
        String hashPW = bcryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(hashPW);
        log.info("Sauvegarde d'un nouveau utilisateur", user);

        return appUserRepository.save(user);
    }

    @Override
    public AppRole saveRole(AppRole role) {
        log.info("Sauvegarde d'un nouveau role");
        return appRoleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String rolename) {
        AppRole role = appRoleRepository.findByRolename(rolename);
        AppUser user = appUserRepository.findByUsername(username);
        user.getRoles().add(role);
        log.info("Association d'un rôle à un utilisateur");

    }

    @Override
    public ResponseEntity<List<AppUser>> listUsers() {
        return ResponseEntity.ok().body(appUserRepository.findAll());
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }
}
