package notebook.model.repository.impl;

import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    private final String fileName;
    private List<User> cache;

    public UserRepository(String fileName) {
        this.mapper = new UserMapper();
        this.fileName = fileName;
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(fileName);
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }
            while (line != null) {
                // считываем остальные строки в цикле
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }
    private void cacheUpdate(){
        cache = findAll();
    }

    @Override
    public void create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id) {
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
    }

    @Override
    public User findById(Long userId) {
        cacheUpdate();
        List<User> users = cache;
        return users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void update(Long userId, User update) {
        User editUser = findById(userId);

        editUser.setFirstName(update.getFirstName().isEmpty() ? editUser.getFirstName() : update.getFirstName());
        editUser.setLastName(update.getLastName().isEmpty() ? editUser.getLastName() : update.getLastName());
        editUser.setPhone(update.getPhone().isEmpty()  ? editUser.getPhone() : update.getPhone());

        write(cache);
    }


    @Override
    public void delete(int id) {
        cacheUpdate();
        if (id <= cache.size()){
            cache.remove(id - 1);
            for (int i = 1; i <= cache.size(); i++) {
                cache.get(i - 1).setId((long) i);
            }
            write(cache);
        }
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(mapper.toInput(u));
        }
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (String line : lines) {
                // запись всей строки
                writer.write(line);
                // запись по символам
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
