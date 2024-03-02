package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.TransTypeEnum;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "m_trans_type")
public class TransType {
   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private String id;

    @Column(name = "description")
    @Enumerated(EnumType.STRING)
    private TransTypeEnum transType;



}
