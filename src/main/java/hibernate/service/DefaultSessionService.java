package hibernate.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DefaultSessionService {

    //mamy tu wzorzec Singletona

    private static final DefaultSessionService instance = new DefaultSessionService();     // to jest prywatne pole zawierające instancje
    private final SessionFactory factory;

    private DefaultSessionService() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    public static Session getSession() {
        return instance.factory.openSession();
    }




}
