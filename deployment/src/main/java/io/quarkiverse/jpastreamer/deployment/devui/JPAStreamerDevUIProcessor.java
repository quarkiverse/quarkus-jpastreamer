package io.quarkiverse.jpastreamer.deployment.devui;

import io.quarkus.deployment.IsDevelopment;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.devui.spi.page.CardPageBuildItem;
import io.quarkus.devui.spi.page.Page;

public class JPAStreamerDevUIProcessor {

    @BuildStep(onlyIf = IsDevelopment.class)
    public CardPageBuildItem cards() {

        CardPageBuildItem cardPageBuildItem = new CardPageBuildItem();

        cardPageBuildItem.addPage(Page.externalPageBuilder("jpastreamer.org")
                .url("https://www.jpastreamer.org")
                .doNotEmbed()
                .icon("font-awesome-solid:bookmark"));

        cardPageBuildItem.addPage(Page.externalPageBuilder("Complete Docs")
                .url("https://speedment.github.io/jpa-streamer")
                .doNotEmbed()
                .icon("font-awesome-solid:book"));

        return cardPageBuildItem;
    }
}
