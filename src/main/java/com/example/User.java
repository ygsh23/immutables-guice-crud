package com.example;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(as = ImmutableUser.class)
public interface User {
    Optional<String> id();
    String name();
    String email();
}