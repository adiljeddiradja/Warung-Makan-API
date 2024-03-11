package com.enigma.wmb_api.service;

import com.enigma.wmb_api.constant.TransTypeEnum;
import com.enigma.wmb_api.entity.TransType;

public interface TransTypeService {
    TransType getOrSave(TransTypeEnum transType);
}
