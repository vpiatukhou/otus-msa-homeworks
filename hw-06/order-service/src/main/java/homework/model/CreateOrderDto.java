package homework.model;

public record CreateOrderDto(
        String idempotencyKey,
        String pickup,
        String destination,
        String orderedBy) {
}
