package notebook.controller;

import notebook.model.User;
import notebook.model.repository.GBRepository;
import notebook.view.UserView;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UserController {
    private final GBRepository repository;

    public UserController(GBRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.create(user);
    }

    public User readUser(Long userId) throws Exception {
        return repository.findById(userId);
    }

    public void updateUser(String userId, User update) {
        update.setId(Long.parseLong(userId));
        repository.update(Long.parseLong(userId), update);
    }

    public List<User> readAll() {
        return repository.findAll();
    }

    public void delete(String userId) {
        int id = Integer.parseInt(userId);
        repository.delete(id);
    }

    public String prompt(String message) {
        Scanner in = new Scanner(System.in);
        UserView.massage(message);
        return in.nextLine();
    }

    public User createUser() {
        String firstName = prompt("Имя: ");
        String lastName = prompt("Фамилия: ");
        String phone = prompt("Номер телефона: ");
        return new User(firstName, lastName, phone);
    }
}
