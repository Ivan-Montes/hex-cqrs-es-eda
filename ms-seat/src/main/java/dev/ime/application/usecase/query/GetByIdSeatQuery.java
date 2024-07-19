package dev.ime.application.usecase.query;

import java.util.UUID;

import dev.ime.domain.query.Query;

public record GetByIdSeatQuery( UUID seatId ) implements Query {

}
