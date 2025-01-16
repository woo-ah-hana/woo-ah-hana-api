package org.hana.wooahhanaapi.domain.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="auto_deposit")
public class AutoDepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String memberBankTranId;
    private String memberAccNum;
    private String communityAccNum;
    private String fee;
    private int depositDay;

}
