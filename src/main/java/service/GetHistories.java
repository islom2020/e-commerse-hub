package service;

import java.util.UUID;

@FunctionalInterface
public interface GetHistories {
    StringBuilder getHistories(UUID id); // Seller's or User's id
}
