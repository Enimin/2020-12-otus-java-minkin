package ru.rt.core.repository;

import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class DataTemplateHibernate<T> implements DataTemplate<T> {

    private final Class<T> clazz;

    public DataTemplateHibernate(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findById(Session session, long id) {
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<T> findAll(Session session) {
        return session.createQuery(String.format("from %s", clazz.getSimpleName()), clazz).getResultList();
    }

    @Override
    public void insert(Session session, T object) {
        session.persist(object);
    }

    @Override
    public void update(Session session, T object) {
        session.merge(object);
    }

    @Override
    public List<T> findByField(Session session, String field, String value) {
        var cb = session.getCriteriaBuilder();
        var cr = cb.createQuery(clazz);
        var root = cr.from(clazz);
        cr.select(root).where(cb.equal(root.get(field), value));
        var query = session.createQuery(cr);
        return query.getResultList();
    }
}
