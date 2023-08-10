package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.util.Commands;

import java.util.List;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        Commands com;

        while (true) {
            String command = userController.prompt("Введите команду: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT) return;
            String userId;
            switch (com) {
                case SHOW_LIST -> {
                    List<User> users = userController.readAll();
                    System.out.println(users);
                }
                case CREATE -> {
                    User u = userController.createUser();
                    userController.saveUser(u);
                }
                case READ -> {
                    userId = userController.prompt("Введите id пользователя для поиска: ");
                    try {
                        User user = userController.readUser(Long.parseLong(userId));
                        System.out.println(user);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case UPDATE -> {
                    userId = userController.prompt("Введите id пользователя для редактирования: ");
                    userController.updateUser(userId, userController.createUser());
                }
                case DELETE -> {
                    userId = userController.prompt("Введите id пользователя для удаления: ");
                    userController.delete(userId);
                }
            }
        }
    }
    public static void massage(String massage){
        System.out.print(massage);
    }

}
