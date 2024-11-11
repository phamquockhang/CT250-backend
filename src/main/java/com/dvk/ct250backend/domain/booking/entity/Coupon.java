package com.dvk.ct250backend.domain.booking.entity;

import com.dvk.ct250backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "coupons")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coupon_id_seq")
    @SequenceGenerator(name = "coupon_id_seq", sequenceName = "coupons_seq", allocationSize = 1)
    Integer couponId;

    @Column(unique = true)
    String couponCode;

    BigDecimal discountAmount;
    BigDecimal discountPercentage;

    LocalDate validFrom;
    LocalDate validTo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "coupon", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<Booking> bookings;
}
