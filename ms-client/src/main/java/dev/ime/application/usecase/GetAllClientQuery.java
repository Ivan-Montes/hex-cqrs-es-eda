package dev.ime.application.usecase;

import dev.ime.domain.query.Query;

public record GetAllClientQuery(Integer page, Integer size) implements Query  {

}
