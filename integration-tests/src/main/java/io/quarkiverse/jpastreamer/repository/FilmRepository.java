package io.quarkiverse.jpastreamer.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import com.speedment.jpastreamer.application.JPAStreamer;
import com.speedment.jpastreamer.streamconfiguration.StreamConfiguration;

import io.quarkiverse.jpastreamer.model.Actor;
import io.quarkiverse.jpastreamer.model.Film;
import io.quarkiverse.jpastreamer.model.Film$;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class FilmRepository implements PanacheRepository<Film> {

    private final static int PAGE_SIZE = 20;
    private final JPAStreamer jpaStreamer = JPAStreamer.of(this::getEntityManager);

    /**
     *
     * Lists limit number of films
     *
     * @param limit the maximum number of films to list
     * @return a list of all films
     */

    // Panache
    public List<Film> listFilms(int limit) {
        return findAll().range(0, limit - 1).list();
    }

    // JPAStreamer
    public Stream<Film> listFilmsStreamer(int limit) {
        return filmStream().limit(limit);
    }

    /**
     * Finds the first film with a title that matches the provided {@code title}.
     *
     * @param title title of the film
     * @return first film with a matching title, or null if no film matches the query
     */

    // JPAStreamer
    public Film findByTitleStreamer(String title) {
        return filmStream()
                .filter(Film$.title.equal(title))
                .findFirst().orElse(null);
    }

    /**
     * Returns the description of the film with the provided {@code title}.
     *
     * @param title title of the film
     * @return the description of the first film with a matching title, or null if no film matches the title
     */

    // JPAStreamer
    public String findDescriptionByTitleStreamer(String title) {
        return filmStream()
                .filter(Film$.title.equal(title))
                .map(Film$.description)
                .findFirst().orElse(null);
    }

    // JPAStreamer
    public Stream<Film> listFilmsSortedByRatingAndLengthStreamer(int limit) {
        return filmStream()
                .sorted(Film$.rating.reversed().thenComparing(Film$.title.comparator()))
                .limit(limit);
    }

    // JPAStreamer
    public Stream<Film> pagingStreamer(int page) {
        return filmStream()
                .sorted(Film$.length)
                .skip((long) PAGE_SIZE * page)
                .limit(PAGE_SIZE);
    }

    /**
     * Lists {@code limit} films that starts with the provided String {@code startsWith},
     * sorted from shortest to longest.
     *
     * @param start start string
     * @param limit maximum number of results to retrieve
     * @return a list of films sorted in ascending length
     */

    // JPAStreamer
    public Stream<Film> titleStartsWithSortedByLengthLimitedStreamer(String start, int limit) {
        return filmStream()
                .filter(Film$.title.startsWith(start))
                .sorted(Film$.length)
                .limit(limit);
    }

    /**
     * Lists {@code limit} films that are at least as long as the provided {@code length}.
     *
     * @param length minimum film length
     * @return films that are at least as long as the provided length
     */

    // JPAStreamer
    public Stream<Film> listLongerStreamer(short length) {
        return filmStream()
                .filter(Film$.length.greaterOrEqual(length));
    }

    /**
     * Returns a map containing all films with a title that starts with the provided String.
     *
     * @param startsWith the start of the film title
     * @return a mapping between films and their cast
     */

    // JPAStreamer
    public Map<Film, Set<Actor>> getCastStreamer(String startsWith) {
        // Left join is default
        return jpaStreamer.stream(StreamConfiguration.of(Film.class).joining(Film$.actors))
                .filter(Film$.title.startsWith(startsWith))
                .collect(
                        Collectors.toMap(Function.identity(),
                                Film::getActors));
    }

    private Stream<Film> filmStream() {
        return jpaStreamer.stream(Film.class);
    }

}
