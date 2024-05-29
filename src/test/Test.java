package test;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import jakarta.persistence.PersistenceException;

import static java.time.LocalDateTime.now;


public class Test {

	public static void main(String[] args) {
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
		
		try {
			SessionFactory sessionFactory = new MetadataSources(registry).addAnnotatedClass(Event.class).buildMetadata()
					.buildSessionFactory();
			sessionFactory.inTransaction(session -> {   
			    session.persist(new Event("Our very first event!", now()));   
			    session.persist(new Event("A follow up event", now()));
			});
			
			sessionFactory.inTransaction(session -> {
			    session.createSelectionQuery("from Event", Event.class)   
			            .getResultList()   
			            .forEach(event -> System.out.println("Event (" + event.getDate() + ") : " + event.getTitle()));
			});
		} catch (Exception e) {
			e.printStackTrace();
			// The registry would be destroyed by the SessionFactory, but we
			// had trouble building the SessionFactory so destroy it manually.
			StandardServiceRegistryBuilder.destroy(registry);
		}
		
		
	}

}
