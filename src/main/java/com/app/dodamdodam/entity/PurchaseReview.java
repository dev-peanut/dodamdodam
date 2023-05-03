package com.app.dodamdodam.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@ToString
@Table(name = "TBL_PURCHASE_REVIEW")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseReview {
    @Id @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;
    private String reviewContents;
    private Integer reviewGrade;

}
