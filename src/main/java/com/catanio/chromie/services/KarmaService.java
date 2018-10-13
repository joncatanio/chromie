package com.catanio.chromie.services;

import com.catanio.chromie.dm.repositories.KarmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KarmaService {

    private KarmaRepository karmaRepository;

    @Autowired
    public KarmaService(KarmaRepository karmaRepository) {
        this.karmaRepository = karmaRepository;
    }
}
