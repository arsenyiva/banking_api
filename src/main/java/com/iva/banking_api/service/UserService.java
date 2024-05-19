package com.iva.banking_api.service;

import com.iva.banking_api.model.User;
import com.iva.banking_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public boolean editUserPhone(long userId, String newPhone) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (isPhoneAvailable(newPhone) && isExtraPhoneAvailable(newPhone)) {
                user.setPhone(newPhone);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean editExtraUserPhone(long id, String newPhone) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (isPhoneAvailable(newPhone) && isExtraPhoneAvailable(newPhone)) {
                user.setExtraPhone(newPhone);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean editEmail(long userId, String email) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (isEmailAvailable(email) && isExtraEmailAvailable(email)) {
                user.setEmail(email);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean editExtraEmail(long userId, String email) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (isEmailAvailable(email) && isExtraEmailAvailable(email)) {
                user.setExtraEmail(email);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }


    public User updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            return userRepository.save(user);
        }
        return null;
    }


    public boolean isPhoneAvailable(String phone) {
        return userRepository.findByPhone(phone) == null;
    }

    public boolean isExtraPhoneAvailable(String phone) {
        return userRepository.findByExtraPhone(phone) == null;
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email) == null;
    }

    public boolean isExtraEmailAvailable(String email) {
        return userRepository.findByExtraEmail(email) == null;
    }

    public boolean isValidPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public List<User> searchUsers(String dateOfBirth, String phone, String username, String email,
                                  int page, int size, String sortBy, String sortOrder) {
        Specification<User> specification = Specification.where(null);

        if (dateOfBirth != null) {
            LocalDate parsedDateOfBirth = LocalDate.parse(dateOfBirth);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), parsedDateOfBirth));
        }

        if (phone != null) {
            if (!phone.startsWith("+")) {
                phone = "+" + phone;
            }
            String finalPhone = phone;
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("phone"), finalPhone));
        }


        if (username != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("username")),
                            username.toLowerCase() + "%"
                    )
            );
        }


        if (email != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("email"), "%" + email + "%"));
        }

        if (dateOfBirth == null && phone == null && username == null && email == null) {
            return userRepository.findAll();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortOrder), sortBy);
        Page<User> userPage = userRepository.findAll(specification, pageable);
        return userPage.getContent();
    }
    public boolean isUserOwnsId(String username, long id) {
        // Получаем пользователя по имени пользователя (username)
        User user = userRepository.findByUsername(username);

        // Проверяем, совпадает ли id пользователя с запрашиваемым id
        return user != null && user.getId() == id;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
