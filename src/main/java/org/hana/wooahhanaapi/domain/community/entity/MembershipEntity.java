package org.hana.wooahhanaapi.domain.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="membership")
public class MembershipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name="community_id")
    private CommunityEntity community;

    public static MembershipEntity create(MemberEntity member, CommunityEntity community) {
        return new MembershipEntity(null,member,community);
    }
}
