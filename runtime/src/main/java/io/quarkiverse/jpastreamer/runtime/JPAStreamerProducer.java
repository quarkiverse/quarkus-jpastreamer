package io.quarkiverse.jpastreamer.runtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManagerFactory;

import com.speedment.jpastreamer.application.JPAStreamer;

/**
 * A CDI class that adds support for JPAStreamer injection.
 *
 * @author Julia Gustafsson
 */
@ApplicationScoped
public class JPAStreamerProducer {

    @Produces
    JPAStreamer jpaStreamer(EntityManagerFactory entityManagerFactory) {
        return JPAStreamer.createJPAStreamerBuilder(entityManagerFactory).build();
    }

}
