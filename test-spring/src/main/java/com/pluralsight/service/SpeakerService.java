package com.pluralsight.service;

import com.pluralsight.model.Speaker;

import java.util.List;

public interface SpeakerService {
    List<Speaker> findAll();
}
