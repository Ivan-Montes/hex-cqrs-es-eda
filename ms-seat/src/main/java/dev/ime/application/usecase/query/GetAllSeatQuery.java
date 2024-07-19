package dev.ime.application.usecase.query;

import dev.ime.domain.query.Query;

public record GetAllSeatQuery(Integer page, Integer size) implements Query {

}
