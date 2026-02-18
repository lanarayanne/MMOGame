package com.ifpe.mmogame.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.ifpe.mmogame.dto.NewPasswordDTO;
import com.ifpe.mmogame.entities.Photo;
import com.ifpe.mmogame.entities.User;
import com.ifpe.mmogame.repositories.PhotoRepository;
import com.ifpe.mmogame.repositories.UserRepository;
import com.ifpe.mmogame.security.JwtUtils;

import java.io.IOException;
import java.util.Optional;


@Component
public class UserService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PhotoRepository photoRepo;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> getUser() {

        String id = jwtUtils.getAuthorizedId();

        User u = userRepo.findById(Integer.parseInt(id)).get();

        u.setEmail(null);
        u.setPassword(null);

        return ResponseEntity.ok(u);

    }

    public ResponseEntity<?> updatePassword(NewPasswordDTO passDto){
        User u = this.userRepo.findByEmail(this.jwtUtils.getAuthorizedId()).get();

        if(encoder.matches(passDto.getOldPassword(), u.getPassword())){
            u.setPassword(encoder.encode(passDto.getNewPassword()));

            this.userRepo.save(u);

            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();

    }

    

    // public ResponseEntity<?> getPerfil() {
    //     String userId = this.jwtUtils.getAuthorizedId();

    //     Optional<Photo> photoOpt = this.photoRepo.findByEmailUser(userId);

    //     if (photoOpt.isPresent()) {
    //         Photo p = photoOpt.get();
    //         p.setUser(null);
    //         return ResponseEntity.ok(photoOpt.get());
    //     }

    //     return ResponseEntity.notFound().build();
    // }

}
