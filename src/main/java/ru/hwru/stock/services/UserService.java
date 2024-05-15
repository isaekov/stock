package ru.hwru.stock.services;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.hwru.stock.entities.Role;
import ru.hwru.stock.entities.User;
import ru.hwru.stock.enums.RoleEnum;
import ru.hwru.stock.models.UserModel;
import ru.hwru.stock.repositories.RoleRepository;
import ru.hwru.stock.repositories.UserRepository;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public void createUser(UserModel userModel) {

        if (userModel == null) {
            throw new UsernameNotFoundException("Заполните обязательные поля");
        }

        if (!userModel.getPassword().equals(userModel.getConfirmPassword())) {
            throw new RuntimeException("Пароли не совпадают");
        }

        // TODO: пока убрал для тестирования
//        if (userModel.getPassword().length() < 12) {
//            throw new RuntimeException("Пароль не может быть короче 12 символов");
//        }

        if (!EmailValidator.getInstance().isValid(userModel.getEmail())) {
            throw new RuntimeException("Проверьте правильность почты: почта не соответствует");
        }

        Optional<User> userOptional = userRepository.findByEmail(userModel.getEmail());
        if (userOptional.isPresent()) {
            throw new RuntimeException("Данная почта занята");
        }

        User user = new User();
        Optional<Role> roleOptional = roleRepository.findByRole(RoleEnum.ADMIN);

        Role role;
        if (roleOptional.isEmpty()) {
            role = new Role();
            role
                    .setId(UUID.randomUUID())
                    .setRole(RoleEnum.ADMIN);
            role = roleRepository.save(role);
        } else {
            role = roleOptional.get();
        }


        user.setEmail(userModel.getEmail())
                .setPassword(passwordEncoder.encode(userModel.getPassword()))
                .setEmail(userModel.getEmail())
                .setCreatedAt(new Date())
                .setRoles(Collections.singleton(role))
                .setId(UUID.randomUUID())
                .setToken("random")
                .setConfirmEmail(false);

        userRepository.saveAndFlush(user);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Hello World");
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isPresent()) {
            System.out.println(user.get().getUsername());
            return user.get();
        }

        throw new UsernameNotFoundException("Пользователь не найден");
    }
}
