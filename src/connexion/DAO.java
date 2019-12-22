package connexion;

import java.util.List;

public interface DAO {
	public <T> T find(long id);
	public long create(Object obj);
	public void delete(Object obj);
	public void update(Object obj);
	public <T> List<T> findAll();
	public <T> List<T> findAll(String key);
}
