package io.quarkiverse.jpastreamer.deployment;

import com.speedment.jpastreamer.application.JPAStreamer;

import io.quarkus.builder.item.MultiBuildItem;

public class JPAstreamerEntityClassBuildItem extends MultiBuildItem {

    private final JPAStreamer jpaStreamer;

    public JPAstreamerEntityClassBuildItem(JPAStreamer jpaStreamer) {
        this.jpaStreamer = jpaStreamer;
    }

    public JPAStreamer get() {
        return jpaStreamer;
    }

}
