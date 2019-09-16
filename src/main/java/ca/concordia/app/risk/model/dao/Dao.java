package ca.concordia.app.risk.model.dao;

public interface Dao<T> {
	T findByName(String name) throws Exception;
    void save(T t) throws Exception;
    void update(T t, String[] params);
    void delete(T t);
}