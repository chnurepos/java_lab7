package ua.repository;

@FunctionalInterface
public interface IdentityExtractor<T> {
    Object extractIdentity(T item);
}

