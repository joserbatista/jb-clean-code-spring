package com.joserbatista.cleancode.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/// <summary>
/// Represents a single speaker
/// </summary>

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Speaker {

    Long departmentId;
    String firstName;
    String lastName;
    String email;
    int exp;
    boolean hasBlog;
    String blogURL;
    WebBrowser browser;
    List<String> certifications;
    String employer;
    int registrationFee;
    List<Session> sessions;
}