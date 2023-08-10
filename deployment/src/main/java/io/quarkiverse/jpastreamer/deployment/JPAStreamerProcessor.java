package io.quarkiverse.jpastreamer.deployment;

import java.io.IOException;
import java.util.Set;

import com.speedment.jpastreamer.analytics.AnalyticsReporterFactory;
import com.speedment.jpastreamer.announcer.Announcer;
import com.speedment.jpastreamer.appinfo.ApplicationInformation;
import com.speedment.jpastreamer.application.JPAStreamerBuilderFactory;
import com.speedment.jpastreamer.autoclose.AutoCloseFactory;
import com.speedment.jpastreamer.builder.BuilderFactory;
import com.speedment.jpastreamer.criteria.CriteriaFactory;
import com.speedment.jpastreamer.criteria.OrderFactory;
import com.speedment.jpastreamer.criteria.PredicateFactory;
import com.speedment.jpastreamer.exception.JPAStreamerException;
import com.speedment.jpastreamer.interopoptimizer.IntermediateOperationOptimizerFactory;
import com.speedment.jpastreamer.merger.MergerFactory;
import com.speedment.jpastreamer.pipeline.PipelineFactory;
import com.speedment.jpastreamer.pipeline.intermediate.DoubleIntermediateOperationFactory;
import com.speedment.jpastreamer.pipeline.intermediate.IntIntermediateOperationFactory;
import com.speedment.jpastreamer.pipeline.intermediate.IntermediateOperationFactory;
import com.speedment.jpastreamer.pipeline.intermediate.LongIntermediateOperationFactory;
import com.speedment.jpastreamer.pipeline.terminal.DoubleTerminalOperationFactory;
import com.speedment.jpastreamer.pipeline.terminal.IntTerminalOperationFactory;
import com.speedment.jpastreamer.pipeline.terminal.LongTerminalOperationFactory;
import com.speedment.jpastreamer.pipeline.terminal.TerminalOperationFactory;
import com.speedment.jpastreamer.renderer.RendererFactory;
import com.speedment.jpastreamer.streamconfiguration.StreamConfigurationFactory;

import com.speedment.jpastreamer.termopmodifier.TerminalOperationModifierFactory;
import com.speedment.jpastreamer.termopoptimizer.TerminalOperationOptimizerFactory;
import io.quarkiverse.jpastreamer.runtime.JPAStreamerProducer;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.deployment.ValidationPhaseBuildItem.ValidationErrorBuildItem;
import io.quarkus.deployment.Capabilities;
import io.quarkus.deployment.Capability;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ServiceProviderBuildItem;
import io.quarkus.deployment.util.ServiceUtil;

class JPAStreamerProcessor {
    private static final String FEATURE = "jpastreamer";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    // Confirm that JPA Provider is present, otherwise stop build
    // For now, this only checks for Hibernate implementations - are there other Quarkus supported JPA providers?
    @BuildStep
    void checkpointHibernateOrm(Capabilities capabilities,
            BuildProducer<NativeImageResourceBuildItem> jpaModelPersistenceUnitMapping,
            BuildProducer<ValidationErrorBuildItem> validationErrors) {
        if (capabilities.isMissing(Capability.HIBERNATE_ORM)) {
            validationErrors.produce(new ValidationErrorBuildItem(
                    new JPAStreamerException("JPAStreamer requires the presence of a JPA provider.")));
        }
    }

    @BuildStep
    void registerServiceProviders(BuildProducer<NativeImageResourceBuildItem> nativeResource,
            BuildProducer<ServiceProviderBuildItem> services) throws IOException {
        registerServiceProviders(AnalyticsReporterFactory.class, nativeResource, services);
        registerServiceProviders(Announcer.class, nativeResource, services);
        registerServiceProviders(ApplicationInformation.class, nativeResource, services);
        registerServiceProviders(AutoCloseFactory.class, nativeResource, services);
        registerServiceProviders(BuilderFactory.class, nativeResource, services);
        registerServiceProviders(CriteriaFactory.class, nativeResource, services);
        registerServiceProviders(DoubleIntermediateOperationFactory.class, nativeResource, services);
        registerServiceProviders(DoubleTerminalOperationFactory.class, nativeResource, services);
        registerServiceProviders(IntermediateOperationFactory.class, nativeResource, services);
        registerServiceProviders(IntermediateOperationOptimizerFactory.class, nativeResource, services);
        registerServiceProviders(IntIntermediateOperationFactory.class, nativeResource, services);
        registerServiceProviders(IntTerminalOperationFactory.class, nativeResource, services);
        registerServiceProviders(JPAStreamerBuilderFactory.class, nativeResource, services);
        registerServiceProviders(LongIntermediateOperationFactory.class, nativeResource, services);
        registerServiceProviders(LongTerminalOperationFactory.class, nativeResource, services);
        registerServiceProviders(MergerFactory.class, nativeResource, services);
        registerServiceProviders(OrderFactory.class, nativeResource, services);
        registerServiceProviders(PipelineFactory.class, nativeResource, services);
        registerServiceProviders(PredicateFactory.class, nativeResource, services);
        registerServiceProviders(RendererFactory.class, nativeResource, services);
        registerServiceProviders(StreamConfigurationFactory.class, nativeResource, services);
        registerServiceProviders(TerminalOperationFactory.class, nativeResource, services);
        registerServiceProviders(TerminalOperationModifierFactory.class, nativeResource, services);
        registerServiceProviders(TerminalOperationOptimizerFactory.class, nativeResource, services);
    }

    void registerServiceProviders(Class<?> klass, BuildProducer<NativeImageResourceBuildItem> nativeResource,
            BuildProducer<ServiceProviderBuildItem> services) throws IOException {
        String service = "META-INF/services/" + klass.getName();

        Set<String> implementations = ServiceUtil.classNamesNamedIn(Thread.currentThread().getContextClassLoader(), service);

        services.produce(new ServiceProviderBuildItem(klass.getName(),
                implementations.toArray(new String[0])));

        nativeResource.produce(new NativeImageResourceBuildItem(service));
    }

    @BuildStep
    void addBeans(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(AdditionalBeanBuildItem.unremovableOf(JPAStreamerProducer.class));
    }

}
