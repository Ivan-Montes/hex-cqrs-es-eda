package dev.ime.application.usecase.query;

import java.util.UUID;

import dev.ime.domain.query.Query;

public record GetByIdPlaneQuery( UUID planeId ) implements Query {

}
