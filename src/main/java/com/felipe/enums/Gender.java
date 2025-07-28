package com.felipe.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Gender {
    @JsonProperty("MASCULINO")
    MASCULINO,
    @JsonProperty("FEMININO")
    FEMININO
}
