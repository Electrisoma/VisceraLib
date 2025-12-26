package net.electrisoma.visceralib.api.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ServiceLoader;

public class ServiceHelper {

    private static final Logger LOG = LoggerFactory.getLogger("VisceraLib-ServiceHelper");

    /**
     * Loads an optional service that can exist or not.
     * Only logs that it doesn't exist.
     * No runtime exception.
     *
     * @param <T> The type of the service interface.
     * @param clazz The Class object of the service interface
     * @return An Optional containing the service instance if found, otherwise an empty Optional.
     */
    public static <T> Optional<T> find(Class<T> clazz) {
        Optional<T> loadedService = ServiceLoader
                .load(clazz)
                .findFirst();

        if (loadedService.isPresent()) {
            LOG.debug("Found implementation {} for service {}",
                    loadedService.get().getClass().getName(), clazz.getName()
            );
        } else {
            LOG.debug("No implementation found for optional service {}",
                    clazz.getName()
            );
        }

        return loadedService;
    }

    /**
     * Loads a mandatory service that must exist.
     * Otherwise, it will throw a runtime exception.
     *
     * @param <T> The type of the service interface.
     * @param clazz The Class object of the service interface.
     * @return The first available implementation of the service.
     * @throws IllegalStateException if no implementation is found.
     */
    public static <T> T load(Class<T> clazz) {
        return find(clazz).orElseThrow(() ->
                new IllegalStateException("Failed to load mandatory service for class: " + clazz.getName())
        );
    }
}
