package db;

import models.Entry;
import models.User;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Stateful
public class EntryDAO {
    private final EntityManager entityManager = Persistence.
            createEntityManagerFactory("default").
            createEntityManager();

    public Entry getEntry(long entryId) {
        return entityManager.find(Entry.class, entryId);
    }

    public List<Entry> findEntries(@NotNull User owner) {
        return entityManager.createNamedQuery("entries.findByOwner", Entry.class)
                .setParameter("owner", owner).getResultStream().collect(Collectors.toList());
    }

    public Entry createEntry(@NotNull Double x, @NotNull Double y, @NotNull Double r, @NotNull User owner) {

        try {
            final Entry entity = new Entry();
            entity.setOwner(owner);
            entity.setR(r);
            entity.setX(x);
            entity.setY(y);
            entity.check();
            entity.setTimestamp(System.currentTimeMillis());
            entityManager.persist(entity);
            entityManager.flush();

            return entity;
        } catch (PersistenceException e) {
            return null;
        }
    }

    public boolean removeEntry(@NotNull Entry entry) {
        try {
            entityManager.remove(entityManager.getReference(User.class, entry.getId()));
            entityManager.flush();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean saveEntry(@NotNull Entry entry) {
        try {
            entityManager.merge(entry);
            entityManager.flush();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
