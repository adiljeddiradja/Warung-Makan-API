package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.TransTypeEnum;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.repository.TransTypeRepository;
import com.enigma.wmb_api.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransTypeServiceImpl implements TransTypeService {
    private final TransTypeRepository transTypeRepository;


    @Override
    public TransType getOrSave(TransTypeEnum transType) {
        return transTypeRepository.findByTransType(transType)
                .orElseGet(() -> transTypeRepository.saveAndFlush(TransType.builder().transType(transType).build()));
    }
}
