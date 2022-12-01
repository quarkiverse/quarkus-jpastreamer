package io.quarkiverse.jpastreamer.runtime;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManagerFactory;

import com.speedment.jpastreamer.application.JPAStreamer;

/**
 * A CDI class that adds support for JPAStreamer injection.
 *
 * @author Julia Gustafsson
 * @since 1.1.0
 */
@ApplicationScoped
public class JPAStreamerQuarkusProducer {

    private JPAStreamer jpaStreamer;
    /*
     * @Produces
     * JPAStreamer jpaStreamerSupplier(Supplier<EntityManager> supplier) {
     * jpaStreamer = JPAStreamer.createJPAStreamerBuilder(supplier).build();
     * return jpaStreamer;
     * }
     *
     * @Produces
     * JPAStreamer jpaStreamerSupplier(Class<?> klass implements PanacheRepositoryBase) {
     * jpaStreamer = JPAStreamer.createJPAStreamerBuilder(klass.)
     * }
     *
     */

    @Produces
    JPAStreamer jpaStreamerEM(EntityManagerFactory emf) {
        jpaStreamer = JPAStreamer.createJPAStreamerBuilder(emf).build();
        return jpaStreamer;
    }

    @PreDestroy
    public void destroy() {
        jpaStreamer.close();
    }

}
