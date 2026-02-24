package com.ifpe.mmogame.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.ifpe.mmogame.dto.NewPasswordDTO;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.repositories.UserRepository;
import com.ifpe.mmogame.security.JwtUtils;
import com.ifpe.mmogame.dto.UserDTO;

@Component
public class UserService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> getUser() {

        try {
            String email = jwtUtils.getAuthorizedId();
            Optional<User> userOpt = userRepo.findByEmail(email);
            if (userOpt.isPresent()) {
                User u = userOpt.get();
                UserDTO dto = new UserDTO();
                dto.setEmail(u.getEmail());
                return ResponseEntity.ok(dto);
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    public ResponseEntity<?> updatePassword(NewPasswordDTO passDto) {
        try {
            User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();
            if (encoder.matches(passDto.getOldPassword(), u.getPassword())) {
                u.setPassword(encoder.encode(passDto.getNewPassword()));
                this.userRepo.save(u);
                return ResponseEntity.ok().build();
            }

            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

}
