package com.iva.banking_api.service;

import com.iva.banking_api.model.BankAccount;
import com.iva.banking_api.model.User;
import com.iva.banking_api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Сервис для управления пользователями.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Получает пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return объект пользователя, если найден, иначе null
     */
    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);
    }

    /**
     * Создает нового пользователя.
     *
     * @param user новый пользователь
     */
    public void createUser(User user) {
        userRepository.save(user);
    }

    /**
     * Изменяет телефон пользователя.
     *
     * @param id       идентификатор пользователя
     * @param newPhone новый номер телефона
     * @return true, если изменение прошло успешно, иначе false
     */
    public boolean editUserPhone(long id, String newPhone) {
        Optional<User> optionalUser = userRepository.findById(id);
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

    /**
     * Изменяет дополнительный телефон пользователя.
     *
     * @param id       идентификатор пользователя
     * @param newPhone новый дополнительный номер телефона
     * @return true, если изменение прошло успешно, иначе false
     */
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

    /**
     * Изменяет электронную почту пользователя.
     *
     * @param userId идентификатор пользователя
     * @param email  новый адрес электронной почты
     * @return true, если изменение прошло успешно, иначе false
     */
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

    /**
     * Изменяет дополнительный адрес электронной почты пользователя.
     *
     * @param userId идентификатор пользователя
     * @param email  новый дополнительный адрес электронной почты
     * @return true, если изменение прошло успешно, иначе false
     */
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

    /**
     * Обновляет информацию о пользователе.
     *
     * @param id   идентификатор пользователя, информацию о котором нужно обновить
     * @param user новая информация о пользователе
     */
    public void updateUser(Long id, User user) {
        if (userRepository.existsById(id)) {
            user.setId(id);
            userRepository.save(user);
        }
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

    /**
     * Поиск пользователей с использованием заданных критериев.
     *
     * @param dateOfBirth дата рождения для поиска
     * @param phone       телефон для поиска
     * @param username    имя пользователя для поиска
     * @param email       электронная почта для поиска
     * @param page        номер страницы результатов
     * @param size        количество результатов на странице
     * @param sortBy      поле для сортировки результатов
     * @param sortOrder   порядок сортировки (ASC или DESC)
     * @return список пользователей, удовлетворяющих заданным критериям
     */
    public List<User> searchUsers(String dateOfBirth, String phone, String username, String email,
                                  int page, int size, String sortBy, String sortOrder) {
        Specification<User> specification = Specification.where(null);

        if (dateOfBirth != null) {
            LocalDate parsedDateOfBirth = LocalDate.parse(dateOfBirth);
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfBirth"), parsedDateOfBirth));
        }

        //Искать телефон нужно без +
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

    /**
     * Проверяет, владеет ли пользователь указанным идентификатором.
     *
     * @param username имя пользователя
     * @param id       идентификатор, который нужно проверить
     * @return true, если пользователь владеет указанным идентификатором, иначе false
     */
    public boolean isUserOwnsId(String username, long id) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getId() == id;
    }

    /**
     * Обновляет балансы пользователей
     */
    public void updateUserBalances() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            BankAccount bankAccount = user.getBankAccount();
            double currentBalance = bankAccount.getBalance();
            double initialBalance = bankAccount.getBalance();
            double updatedBalance = currentBalance * 1.05;
            double maxBalance = initialBalance * 2.07;
            if (updatedBalance > maxBalance) {
                updatedBalance = maxBalance;
            }
            bankAccount.setBalance(updatedBalance);
        }
        userRepository.saveAll(users);
    }
}
