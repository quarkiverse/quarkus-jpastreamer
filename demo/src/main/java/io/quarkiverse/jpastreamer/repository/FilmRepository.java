package io.quarkiverse.jpastreamer.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import com.speedment.jpastreamer.application.JPAStreamer;

import io.quarkiverse.jpastreamer.model.Actor;
import io.quarkiverse.jpastreamer.model.Film;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

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

    // Panache
    public Film findByTitle(String title) {
        return find("title", title)
                .firstResult();
    }

    /*
     * // JPAStreamer
     * public Film findByTitleStreamer(String title) {
     * return filmStream()
     * .filter(Film$.title.equal(title))
     * .findFirst().orElse(null);
     * }
     */
    /**
     * Returns the description of the film with the provided {@code title}.
     *
     * @param title title of the film
     * @return the description of the first film with a matching title, or null if no film matches the title
     */

    // Panache
    public String findDescriptionByTitle(String title) {
        return find("select f.description from Film f where f.title like :title", Parameters.with("title", title))
                .project(String.class)
                .firstResult();
    }

    /*
     * // JPAStreamer
     * public String findDescriptionByTitleStreamer(String title) {
     * return filmStream()
     * .filter(Film$.title.equal(title))
     * .map(Film$.description)
     * .findFirst().orElse(null);
     * }
     */
    /**
     * Lists all films sorted first by reversed rating and then by title
     *
     * @return list of films sorted first by rating and then by title
     */

    // Panache
    public List<Film> listFilmsSortedByRatingAndLength(int limit) {
        return findAll(Sort.by("rating", Sort.Direction.Descending).and("title", Sort.Direction.Ascending))
                .range(0, limit - 1)
                .list();
    }

    /*
     * // JPAStreamer
     * public Stream<Film> listFilmsSortedByRatingAndLengthStreamer(int limit) {
     * return filmStream()
     * .sorted(Film$.rating.reversed().thenComparing(Film$.title.comparator()))
     * .limit(limit);
     * }
     */
    /**
     * Sorts the films by length, and pages the result.
     *
     * @param page the page number
     * @return a stream of the paged results
     */

    // Panache
    public Stream<Film> paging(int page) {
        return findAll(Sort.by("length"))
                .page(page, PAGE_SIZE)
                .stream();
    }

    /*
     * // JPAStreamer
     * public Stream<Film> pagingStreamer(int page) {
     * return filmStream()
     * .sorted(Film$.length)
     * .skip((long) PAGE_SIZE * page)
     * .limit(PAGE_SIZE);
     * }
     */
    /**
     * Lists {@code limit} films that starts with the provided String {@code startsWith},
     * sorted from shortest to longest.
     *
     * @param start start string
     * @param limit maximum number of results to retrieve
     * @return a list of films sorted in ascending length
     */

    // Panache
    public Stream<Film> titleStartsWithSortedByLengthLimited(String start, int limit) {
        return find("title like :start",
                Sort.by("length"),
                Parameters.with("start", start + "%"))
                .range(0, limit)
                .stream();
    }

    /*
     * // JPAStreamer
     * public Stream<Film> titleStartsWithSortedByLengthLimitedStreamer(String start, int limit) {
     * return filmStream()
     * .filter(Film$.title.startsWith(start))
     * .sorted(Film$.length)
     * .limit(limit);
     * }
     */
    /**
     * Lists {@code limit} films that are at least as long as the provided {@code length}.
     *
     * @param length minimum film length
     * @return films that are at least as long as the provided length
     */

    // Panache
    public List<Film> listLonger(short length) {
        return list("length >= ?1", length);
    }

    /*
     * // JPAStreamer
     * public Stream<Film> listLongerStreamer(short length) {
     * return filmStream()
     * .filter(Film$.length.greaterOrEqual(length));
     * }
     */
    /**
     * Updates the description of all films that are longer than the provided length to desc.
     *
     * @param desc the new description
     * @param length the minimum length of films that should be updated, exclusive
     */

    // Panache - executes a single update query
    @Transactional
    public void updateDescription(String desc, short length) {
        update("description = ?1 where length > ?2", desc, length);
    }

    // Panache - executes one update query for every film to be updated
    @Transactional
    public void updateDescriptionStream(String desc, short length) {
        try (Stream<Film> films = streamAll()) {
            films
                    .filter(t -> t.getLength() > length)
                    .forEach(f -> {
                        f.setDescription(desc);
                        persist(f);
                    });
        }
    }

    /*
     * // JPAStreamer - executes one update query for every film to be updated in its current state
     *
     * @Transactional
     * public void updateDescriptionStreamer(String desc, short length) {
     * filmStream()
     * .filter(Film$.length.greaterThan(length))
     * .forEach(f -> {
     * f.setDescription(desc);
     * persist(f);
     * });
     * }
     */
    /**
     * Returns a map containing all films with a title that starts with the provided String.
     *
     * @param startsWith the start of the film title
     * @return a mapping between films and their cast
     */

    // Panache
    public Map<Film, Set<Actor>> getCast(String startsWith) {
        return find("from Film f LEFT JOIN fetch f.actors where title like :title",
                Parameters.with("title", startsWith + "%"))
                .stream()
                .collect(
                        Collectors.toMap(Function.identity(),
                                Film::getActors));
    }

    /*
     * // JPAStreamer
     * public Map<Film, Set<Actor>> getCastStreamer(String startsWith) {
     * // Left join is default
     * return jpaStreamer.stream(StreamConfiguration.of(Film.class).joining(Film$.actors))
     * .filter(Film$.title.startsWith(startsWith))
     * .collect(
     * Collectors.toMap(Function.identity(),
     * Film::getActors));
     * }
     */
    private Stream<Film> filmStream() {
        return jpaStreamer.stream(Film.class);
    }

}
