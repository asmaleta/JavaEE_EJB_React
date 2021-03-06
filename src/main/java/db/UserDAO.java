package db;

import business.Hash;
import models.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Stateful
public class UserDAO {
    private final EntityManager entityManager = Persistence.
            createEntityManagerFactory("default").
            createEntityManager();

    public User getUser(long userId) {
        return entityManager.find(User.class, userId);
    }

    public User findUser(@NotNull String username) {
        return entityManager.createNamedQuery("users.findByUsername", User.class)
                .setParameter("username", username).getResultStream()
                .findAny().orElse(null);
    }

    public User createUser(@NotNull @Size(min = 2) @NotBlank String username, @NotNull @NotBlank String password) {
        final String hash = Hash.SHA(password);

        try {
            final User entity = new User();
            entity.setLogin(username);
            entity.setPassword(hash);
            entityManager.persist(entity);
            entityManager.flush();

            return entity;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public boolean removeUser(@NotNull User user) {
        try {
            entityManager.remove(entityManager.getReference(User.class, user.getId()));
            entityManager.flush();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public boolean saveUser(@NotNull User user) {
        try {
            entityManager.merge(user);
            entityManager.flush();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean checkPassword(@NotNull User user, @NotNull String password) {
        return user.getPassword().equals(Hash.SHA(password));
    }
}
