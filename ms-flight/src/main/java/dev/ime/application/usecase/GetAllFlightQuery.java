package dev.ime.application.usecase;

import dev.ime.domain.query.Query;

public record GetAllFlightQuery(Integer page, Integer size) implements Query {

}
