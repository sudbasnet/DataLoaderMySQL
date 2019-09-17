/*
 * Author: Hariharan Arunachalam
 * Date: May 30, 2016 (3:32:26 PM)
 * Explicit author permission required before this code is reused for any purpose - more like please let me know :)
 */
package dataloadermysql;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 *
 * @author Hariharan
 */
public class EntityManagerHelpers {
	/** Logging */
    //private static final Log LOG = LogFactory.getLog(EntityManagerUtil.class);
    
	/**
	 * A private constructor.
	 */
	private EntityManagerHelpers() { }
	
	/**
     * Persists the given entity in the DB.
     *
     * @param <T> The type of entity to persist
     * @param entity the entity to persist
     * @param em The {@link EntityManager} to use
     */
    public static <T> void createEntity(T entity, EntityManager em) {
    	em.persist(entity);
    }

    /**
     * Updates a given entity in the DB.
     *
     * @param <T> The type of entity to persist
     * @param entity the entity to update 
     * @param em The {@link EntityManager} to use
     */
    public static <T> T updateEntity(T entity, EntityManager em) {
    	return em.merge(entity);
    }
    
    /**
     * Deletes the given entity from the DB.
     *
     * @param <T> The type of entity to persist
     * @param entity the entity to update 
     * @param em The {@link EntityManager} to use
     */
    public static <T> void deleteEntity(T entity, EntityManager em) {
    	em.remove(entity);
    }
    
    /**
     * Returns a single object of a given class type based
     * on a given query.
     * 
     * @param <T> The type of entity to return
     * @param query The query to run
     * @param type The {@link Class} representation of {@code T}
     * @param em The {@link EntityManager} to use
     * 
     * @return An object of type{@code T} returned by {@code query}
     */
    @SuppressWarnings("unchecked")
	public static <T> T getSingleResult(String query, Class<T> type, EntityManager em) {
    	T entity = null;
    	
    	try {
    		// grab the entity from the DB
    		entity = (T) em.createQuery(query).setMaxResults(1).getSingleResult();
    	} catch (NoResultException ex) {
    		// this entity does not exist
    		//LOG.error("GetSingleResult failed for: " + query);
    	}
    	
        return entity; 
    }
    
    /**
     * Returns a single object of a given class type based
     * on a given query.
     * 
     * @param <T> The type of entity to return
     * @param query The query to run
     * @param params The parameters to {@code query}
     * @param type The {@link Class} representation of {@code T}
     * @param em The {@link EntityManager} to use
     * 
     * @return An object of type{@code T} returned by {@code query}
     */
    @SuppressWarnings("unchecked")
	public static <T> T getSingleResult(String query, Map<String, Object> params, Class<T> type, EntityManager em) {
    	T entity = null;
    	
    	try {
    		// build the query
    		Query q = em.createQuery(query).setMaxResults(1);
    		for (Map.Entry<String, Object> param : params.entrySet()) {
    			q.setParameter(param.getKey(), param.getValue());
    		}
    		
    		// grab the entity from the DB
    		entity = (T) q.getSingleResult();
    	} catch (NoResultException ex) {
    		// this entity does not exist
    		//LOG.error("GetSingleResult failed for: " + query);
    	}
    	
        return entity; 
    }
    
    /**
     * Returns all a list of entities of a given class type based
     * on a given query.
     * 
     * @param <T> The type of entity to return
     * @param query The query to run
     * @param type The {@link Class} representation of {@code T}
     * @param em The {@link EntityManager} to use
     * 
     * @return A list of {@code T} entities returned by {@code query}
     */
    @SuppressWarnings("unchecked")
	public static <T> List<T> getList(String query, Class<T> type, EntityManager em) {
    	List<T> list = new ArrayList<T>();
    	
    	try {
    		// grab the entity from the DB
    		list = (List<T>) em.createQuery(query).getResultList();
    	} catch (NoResultException ex) {
    		// this entity does not exist
    		//LOG.error("GetList failed for: " + query);
    	}
    	
        return list; 
    }
       
    
    /**
     * Returns all a list of entities of a given class type based
     * on a given query with parameters.
     * 
     * @param <T> The type of entity to return
     * @param query The query to run
     * @param params The parameters to {@code query}
     * @param type The {@link Class} representation of {@code T}
     * @param em The {@link EntityManager} to use
     * 
     * @return A list of {@code T} entities returned by {@code query}
     */
    @SuppressWarnings("unchecked")
	public static <T> List<T> getList(String query, Map<String, Object> params, Class<T> type, EntityManager em) {
    	List<T> list = new ArrayList<T>();
    	
    	try {
    		// build the query
    		Query q = em.createQuery(query);
    		for (Map.Entry<String, Object> param : params.entrySet()) {
    			q.setParameter(param.getKey(), param.getValue());
    		}
    		
    		// grab the entity from the DB
    		list = (List<T>) q.getResultList();
    	} catch (NoResultException ex) {
    		// this entity does not exist
    		//LOG.error("GetList failed for: " + query);
    	}
    	
        return list; 
    }
}