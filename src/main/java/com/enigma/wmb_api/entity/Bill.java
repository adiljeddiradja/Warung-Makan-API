package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.BILL)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private Tables tables;

    @ManyToOne
    @JoinColumn(name = "trans_type_id")
    private TransType transType;

    @OneToMany(mappedBy = "bill")
    @JsonManagedReference
    private List<BillDetail> billDetails;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "trans_date", updatable = false)
    private Date transDate;

    @OneToOne
    @JoinColumn(name = "payment_id", unique = true)
    private Payment payment;

}
