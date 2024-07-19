package dev.ime.application.usecase.query;

import dev.ime.domain.query.Query;

public record GetAllPlaneQuery(Integer page, Integer size) implements Query {

}
