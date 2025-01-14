package org.hana.wooahhanaapi.utils.database;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.community.repository.MembershipRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.utils.exception.SeederException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final MembershipRepository membershipRepository;

    @Override
    @Transactional
    public void run(String... args) throws SeederException {
        //member seed
        MemberEntity member1 = MemberEntity.create("hj1234","함형주",passwordEncoder.encode("hj1234!"),"01026530957","3560000000000","농협");//농협:bank_tran_id="002"
        memberRepository.save(member1);
        MemberEntity member2 = MemberEntity.create("sj1234","최선정",passwordEncoder.encode("sj1234!"),"01012341234","3561111111111","우리");//우리:bank_tran_id="003"
        memberRepository.save(member2);
        MemberEntity member3 = MemberEntity.create("yh1234","윤영헌",passwordEncoder.encode("yh1234!"),"01012341234","3562222222222","농협");//농협:bank_tran_id="002"
        memberRepository.save(member3);
        MemberEntity member4 = MemberEntity.create("sh1234","김상현",passwordEncoder.encode("sh1234!"),"01012341234","3563333333333","하나");//하나:bank_tran_id="001"
        memberRepository.save(member4);
        MemberEntity member5 = MemberEntity.create("cw1234","김채운",passwordEncoder.encode("cw1234!"),"01012341234","3564444444444","하나");//하나:bank_tran_id="001"
        memberRepository.save(member5);
        MemberEntity member6 = MemberEntity.create("mk1234","김미강",passwordEncoder.encode("mk1234!"),"01012341234","3565555555555","신한");//신한:bank_tran_id="004"
        memberRepository.save(member6);
        MemberEntity member7 = MemberEntity.create("dt1234","데이터",passwordEncoder.encode("dt1234!"),"01012341234","3560603895413","하나"); //하나:bank_tran_id="001"
        memberRepository.save(member7);

        //community seed
        MemberEntity yh = memberRepository.findByUsername("yh1234").orElse(null);
        CommunityEntity yhCommunity1 = CommunityEntity.create(yh.getId(),"맛집탐방","3560603895413",3L,200000L,60L);
        CommunityEntity yhCommunity2 = CommunityEntity.create(yh.getId(),"떠나요 여행","3561234123412",3L,50000L,30L);
        CommunityEntity yhCommunity3 = CommunityEntity.create(yh.getId(),"가방","3564321432143",3L,100000L,20L);
        communityRepository.save(yhCommunity1); communityRepository.save(yhCommunity2); communityRepository.save(yhCommunity3);

        MemberEntity sj = memberRepository.findByUsername("sj1234").orElse(null);
        CommunityEntity sjCommunity1 = CommunityEntity.create(sj.getId(),"가평가자","3561111222233",3L,200000L,60L);
        CommunityEntity sjCommunity2 = CommunityEntity.create(sj.getId(),"일본가자","3563333222211",3L,50000L,30L);
        communityRepository.save(sjCommunity1); communityRepository.save(sjCommunity2);

        //membership seed
        List<CommunityEntity> communityList = communityRepository.findAllByManagerId(yh.getId());
        communityList.forEach(communityEntity -> {
            MembershipEntity membership = MembershipEntity.create(yh, communityEntity);
            membershipRepository.save(membership);
        });

        List<CommunityEntity> communityList2 = communityRepository.findAllByManagerId(sj.getId());
        communityList2.forEach(communityEntity -> {
            MembershipEntity membership = MembershipEntity.create(sj, communityEntity);
            membershipRepository.save(membership);
        });

        List<MemberEntity> memberList = memberRepository.findAll();
        memberList.forEach(memberEntity -> {
            CommunityEntity communityEntity = communityRepository.findByName("맛집탐방");
            MembershipEntity membership = MembershipEntity.create(memberEntity,communityEntity);
            membershipRepository.save(membership);
        });

        //

    }
}

