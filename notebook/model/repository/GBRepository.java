package notebook.model.repository;

import notebook.model.User;

import java.util.List;
import java.util.Optional;

public interface GBRepository {
    List<User> findAll();
    User create(User user);
    User findById(Long id);
    Optional<User> update(Long userId, User update);
    boolean delete(int id);

}