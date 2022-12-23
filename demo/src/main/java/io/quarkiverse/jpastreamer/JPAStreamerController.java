package io.quarkiverse.jpastreamer;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.quarkiverse.jpastreamer.repository.FilmRepository;

@RestController
@RequestMapping("/io/quarkiverse/jpastreamer")
public class JPAStreamerController {

    @Inject
    FilmRepository filmRepository;
    /*
     * @GetMapping(value = "/description")
     * public String description(@RequestParam String title) {
     * return filmRepository.findDescriptionByTitleStreamer(title);
     * }
     *
     * @GetMapping(value = "/list")
     * public String list(@RequestParam int limit) {
     * return filmRepository.listFilmsStreamer(limit).map(Film::getTitle).collect(Collectors.joining(", "));
     * }
     *
     * @GetMapping(value = "listAndSort")
     * public String listAndSort(@RequestParam int limit) {
     * return filmRepository.listFilmsSortedByRatingAndLengthStreamer(limit)
     * .map(f -> String.format("<p> %s (%s): %s min </p>", f.getTitle(), f.getRating(), f.getLength()))
     * .collect(Collectors.joining());
     * }
     *
     * @GetMapping(value = "page")
     * public String paging(@RequestParam int page) {
     * return filmRepository.pagingStreamer(page)
     * .map(f -> String.format("<p> %s: %s min </p>", f.getTitle(), f.getLength()))
     * .collect(Collectors.joining());
     * }
     *
     * @GetMapping(value = "startsWithSort")
     * public String startsWithSort(@RequestParam String startsWith) {
     * return filmRepository.titleStartsWithSortedByLengthLimitedStreamer(startsWith, 10)
     * .map(f -> String.format("<p> %s: %s min </p>", f.getTitle(), f.getLength()))
     * .collect(Collectors.joining());
     * }
     *
     * @GetMapping(value = "update")
     * public String updateStream(@RequestParam String desc, @RequestParam int length) {
     * // Update descriptions for films longer than "length"
     * filmRepository.updateDescriptionStreamer(desc, (short) length);
     *
     * // List all effected films
     * return filmRepository.listLongerStreamer((short) length)
     * .map(f -> String.format("<p> %s: %s </p>", f.getTitle(), f.getDescription()))
     * .collect(Collectors.joining());
     * }
     *
     * @GetMapping(value = "actors")
     * public String listActors(@RequestParam String start) {
     * final StringBuilder sb = new StringBuilder();
     * return filmRepository.getCastStreamer(start)
     * .entrySet()
     * .stream()
     * .map(entry -> {
     * return String.format("<p> %s: %s </p>", entry.getKey().getTitle(),
     * (entry.getValue().stream().map(a -> {
     * return String.format("%s %s", StringUtils.capitalize(a.getFirstName().toLowerCase()),
     * StringUtils.capitalize(a.getLastName().toLowerCase()));
     * }).collect(Collectors.joining(", "))));
     * })
     * .collect(Collectors.joining());
     * }
     */
}
