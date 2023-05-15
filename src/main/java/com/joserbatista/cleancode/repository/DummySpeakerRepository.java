package com.joserbatista.cleancode.repository;

import com.joserbatista.cleancode.domain.Speaker;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DummySpeakerRepository implements SpeakerRepository {

    private static final Map<Long, Speaker> speakerMap = Map.of(
        1L, Speaker.builder().firstName("José").lastName("Batista").build(),
        2L, Speaker.builder().firstName("Maria").lastName("Doe").build(),
        3L, Speaker.builder().firstName("João").lastName("Lopes").build()
    );

    @Override
    public Speaker get(Long id) {
        return speakerMap.get(id);
    }

    @Override
    public Speaker save(Speaker speaker) {
        return speakerMap.get(1L);
    }
}
