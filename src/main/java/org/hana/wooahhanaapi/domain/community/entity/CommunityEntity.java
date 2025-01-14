package org.hana.wooahhanaapi.domain.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="community")
public class CommunityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID managerId;
    private String name;
    private String accountNumber;
    private Long credits;
    private Long fee;
    private Long feePeriod;

    @OneToMany(mappedBy = "community")
    private List<MembershipEntity> memberships = new ArrayList<>();

    public static CommunityEntity create(UUID managerId, String name, String accountNumber, Long credits, Long fee, Long feePeriod) {
        return new CommunityEntity(null, managerId, name, accountNumber, credits, fee, feePeriod, null);
    }

}
