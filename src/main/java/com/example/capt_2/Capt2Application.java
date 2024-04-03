package com.example.capt_2;

import com.example.capt_2.models.RoleEntity;
import com.example.capt_2.models.UserEntity;
import com.example.capt_2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class Capt2Application {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(Capt2Application.class, args);
    }
    @Bean
    CommandLineRunner init(){
        return args -> {
            UserEntity user = UserEntity.builder()
                    .email("userAdm@email.com")
                    .username("user")
                    .password(passwordEncoder.encode("1234"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.valueOf(ERole.ADMIN.name()))
                            .build()))
                    .build();
            UserEntity user2 = UserEntity.builder()
                    .email("userUsr@email.com")
                    .username("userUsr")
                    .password(passwordEncoder.encode("1234"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.valueOf(ERole.USER.name()))
                            .build()))
                    .build();
            UserEntity user3 = UserEntity.builder()
                    .email("userInv@email.com")
                    .username("userInv")
                    .password(passwordEncoder.encode("1234"))
                    .roles(Set.of(RoleEntity.builder()
                            .name(ERole.valueOf(ERole.INVITED.name()))
                            .build()))
                    .build();
            userRepository.save(user);
            userRepository.save(user2);
            userRepository.save(user3);
        };
    }
}
