package com.joserbatista.cleancode.repository;

import com.joserbatista.cleancode.domain.Speaker;

public interface SpeakerRepository  {

    Speaker get(Long id);

    Speaker save(Speaker speaker);
}
