package io.quarkiverse.jpastreamer;

import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.quarkiverse.jpastreamer.model.Film;
import io.quarkiverse.jpastreamer.repository.FilmRepository;

@RestController
@RequestMapping("/panache")
public class PanacheController {

    @Inject
    FilmRepository filmRepository;

    @GetMapping(value = "/description")
    public String description(@RequestParam String title) {
        return filmRepository.findDescriptionByTitle(title);
    }

    @GetMapping(value = "/list")
    public String list(@RequestParam int limit) {
        return filmRepository.listFilms(limit).stream().map(Film::getTitle).collect(Collectors.joining(", "));
    }

    @GetMapping(value = "listAndSort")
    public String listAndSort(@RequestParam int limit) {
        return filmRepository.listFilmsSortedByRatingAndLength(limit)
                .stream()
                .map(f -> String.format("<p> %s (%s): %s min </p>", f.getTitle(), f.getRating(), f.getLength()))
                .collect(Collectors.joining());
    }

    @GetMapping(value = "page")
    public String paging(@RequestParam int page) {
        return filmRepository.paging(page)
                .map(f -> String.format("<p> %s: %s min </p>", f.getTitle(), f.getLength()))
                .collect(Collectors.joining());
    }

    @GetMapping(value = "startsWithSort")
    public String startsWithSort(@RequestParam String startsWith) {
        return filmRepository.titleStartsWithSortedByLengthLimited(startsWith, 10)
                .map(f -> String.format("<p> %s: %s min </p>", f.getTitle(), f.getLength()))
                .collect(Collectors.joining());
    }

    @GetMapping(value = "update")
    public String update(@RequestParam String desc, @RequestParam short length) {
        // Update descriptions for films longer than "length"
        filmRepository.updateDescription(desc, length);

        // List all effected films
        return filmRepository.listLonger((short) length)
                .stream()
                .map(f -> String.format("<p> %s: %s </p>", f.getTitle(), f.getDescription()))
                .collect(Collectors.joining());
    }

    @GetMapping(value = "actors")
    public String listActors(@RequestParam String start) {
        final StringBuilder sb = new StringBuilder();
        return filmRepository.getCast(start)
                .entrySet()
                .stream()
                .map(entry -> {
                    return String.format("<p> %s: %s </p>", entry.getKey().getTitle(),
                            (entry.getValue().stream().map(a -> {
                                return String.format("%s %s", StringUtils.capitalize(a.getFirstName().toLowerCase()),
                                        StringUtils.capitalize(a.getLastName().toLowerCase()));
                            }).collect(Collectors.joining(", "))));
                })
                .collect(Collectors.joining());
    }
}
