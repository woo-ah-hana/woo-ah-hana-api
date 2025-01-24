package org.hana.wooahhanaapi.utils.database;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateReqDto;
import org.hana.wooahhanaapi.domain.account.service.AccountService;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.community.repository.MembershipRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.utils.exception.SeederException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
@Profile("!test")
public class DatabaseSeeder implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final MembershipRepository membershipRepository;
    private final AccountService accountService;
    private final PlanRepository planRepository;

    @Override
    @Transactional
    public void run(String... args) throws SeederException {

        //member seed
        MemberEntity member1 = MemberEntity.create("01026530957","함형주",passwordEncoder.encode("hj1234!"),"01026530957","3561057204496","002");//농협:bank_tran_id="002"
        memberRepository.save(member1);
        MemberEntity member2 = MemberEntity.create("01012345678","최선정",passwordEncoder.encode("sj1234!"),"01012345678","2150094621845","003");//우리:bank_tran_id="003"
        memberRepository.save(member2);
        MemberEntity member3 = MemberEntity.create("01039388377","윤영헌",passwordEncoder.encode("yh1234!"),"01039388377","3561024215509","002");//농협:bank_tran_id="002"
        memberRepository.save(member3);
        MemberEntity member4 = MemberEntity.create("01089400634","김상현",passwordEncoder.encode("sh1234!"),"01089400634","1462665915101","001");//하나:bank_tran_id="001"
        memberRepository.save(member4);
        MemberEntity member5 = MemberEntity.create("01099153691","김채운",passwordEncoder.encode("cw1234!"),"01099153691","0181011629531","005");//기업:bank_tran_id="005"
        memberRepository.save(member5);
        MemberEntity member6 = MemberEntity.create("01029011957","김미강",passwordEncoder.encode("mk1234!"),"01029011957","3333057204496","006");//카카오:bank_tran_id="006"
        memberRepository.save(member6);
        MemberEntity member7 = MemberEntity.create("01092469330","안유진",passwordEncoder.encode("yj1234!"),"01092469330","3561084512201","002"); //농협:bank_tran_id="002"
        memberRepository.save(member7);
        MemberEntity member8 = MemberEntity.create("01026483859","김가을",passwordEncoder.encode("ge1234!"),"01092469330","3561084512201","001"); //하나:bank_tran_id="001"
        memberRepository.save(member8);
        MemberEntity member9 = MemberEntity.create("01024029471","장원영",passwordEncoder.encode("wy1234!"),"01092469330","3561084512201","001"); //하나:bank_tran_id="001"
        memberRepository.save(member9);
        MemberEntity member10 = MemberEntity.create("01099123592","김레이",passwordEncoder.encode("re1234!"),"01092469330","3561084512201","008"); //국민:bank_tran_id="008"
        memberRepository.save(member10);
        MemberEntity member11 = MemberEntity.create("01084595281","김지원",passwordEncoder.encode("jw1234!"),"01092469330","3561084512201","009"); //부산:bank_tran_id="009"
        memberRepository.save(member11);
        MemberEntity member12 = MemberEntity.create("01052548974","이현서",passwordEncoder.encode("hs1234!"),"01092469330","3561084512201","010"); //대구:bank_tran_id="010"
        memberRepository.save(member12);

        //community, membership seed
        MemberEntity hj = memberRepository.findByUsername("01026530957").orElse(null); // 계주 : 함형주
        CommunityEntity community = CommunityEntity.create(hj.getId(),"맛집탐방","1468152645150",3L,200000L,10L);
        communityRepository.save(community);
        MembershipEntity m1 = MembershipEntity.create(member1, community); membershipRepository.save(m1); // 1팀 전원
        MembershipEntity m2 = MembershipEntity.create(member2, community); membershipRepository.save(m2);
        MembershipEntity m3 = MembershipEntity.create(member3, community); membershipRepository.save(m3);
        MembershipEntity m4 = MembershipEntity.create(member4, community); membershipRepository.save(m4);
        MembershipEntity m5 = MembershipEntity.create(member5, community); membershipRepository.save(m5);
        MembershipEntity m6 = MembershipEntity.create(member6, community); membershipRepository.save(m6);

        MemberEntity yj = memberRepository.findByUsername("01092469330").orElse(null); // 계주 : 안유진
        CommunityEntity community2 = CommunityEntity.create(yj.getId(),"어쩌다6인조","1463056220188",3L,50000L,20L);
        communityRepository.save(community2);
        MembershipEntity m7 = MembershipEntity.create(member7, community2); membershipRepository.save(m7); // 아이브
        MembershipEntity m8 = MembershipEntity.create(member8, community2); membershipRepository.save(m8);
        MembershipEntity m9 = MembershipEntity.create(member9, community2); membershipRepository.save(m9);
        MembershipEntity m10 = MembershipEntity.create(member10, community2); membershipRepository.save(m10);
        MembershipEntity m11 = MembershipEntity.create(member11, community2); membershipRepository.save(m11);
        MembershipEntity m12 = MembershipEntity.create(member12, community2); membershipRepository.save(m12);

        MemberEntity yh = memberRepository.findByUsername("01039388377").orElse(null); // 계주 : 윤영헌
        CommunityEntity community3 = CommunityEntity.create(yh.getId(),"솔바람산악회","1464493512314",3L,100000L,5L);
        communityRepository.save(community3);
        MembershipEntity m13 = MembershipEntity.create(member3, community3); membershipRepository.save(m13); // 영헌
        MembershipEntity m14 = MembershipEntity.create(member4, community3); membershipRepository.save(m14); // 상현
        MembershipEntity m16 = MembershipEntity.create(member6, community3); membershipRepository.save(m16); // 미강
        MembershipEntity m21 = MembershipEntity.create(member8, community3); membershipRepository.save(m21); // 가을
        MembershipEntity m22 = MembershipEntity.create(member11, community3); membershipRepository.save(m22); // 지원
        MembershipEntity m23 = MembershipEntity.create(member12, community3); membershipRepository.save(m23); // 현서

        MemberEntity mk = memberRepository.findByUsername("01029011957").orElse(null); // 계주 : 김미강
        CommunityEntity community4 = CommunityEntity.create(mk.getId(),"가평가자","1465510634457",3L,200000L,60L);
        communityRepository.save(community4);
        MembershipEntity m17 = MembershipEntity.create(member3, community4); membershipRepository.save(m17); // 영헌
        MembershipEntity m18 = MembershipEntity.create(member2, community4); membershipRepository.save(m18); // 선정
        MembershipEntity m19 = MembershipEntity.create(member1, community4); membershipRepository.save(m19); // 형주
        MembershipEntity m20 = MembershipEntity.create(member6, community4); membershipRepository.save(m20); // 미강
        //CommunityEntity sjCommunity2 = CommunityEntity.create(sj.getId(),"일본가자","3563333222211",3L,50000L,30L);
        //communityRepository.save(sjCommunity1); communityRepository.save(sjCommunity2);

        // 커뮤니티 3개에 속하는 인원 : 영헌, 미강
        // 2개 : 선정, 형주, 상현
        // 1개 : 채운

        // 모임 : 맛집탐방, 계획 : 수리고등학교 동창 강릉 여행, 일정 : 2025/1/10 - 2025/1/11
        ArrayList<String> gangneung = new ArrayList<>();
        gangneung.add("강릉 순두부마을");
        gangneung.add("강릉 횟집");
        gangneung.add("강릉 카페거리");

        ArrayList<UUID> p1members = new ArrayList<>();
        p1members.add(member1.getId()); // 형주
        p1members.add(member2.getId()); // 선정
        p1members.add(member3.getId()); // 영헌
        p1members.add(member4.getId()); // 상현

        LocalDateTime p1from = LocalDateTime.of(2025, 1, 10, 7, 0, 0);
        LocalDateTime p1to = LocalDateTime.of(2025, 1, 11, 13, 0, 0);

        PlanEntity p1 = PlanEntity.create(community.getId(), "수리고등학교 동창 강릉 여행",
                p1from, p1to,"여행", gangneung, p1members);
        planRepository.save(p1);

        // 모임 : 맛집탐방, 계획 : 성수 나들이, 일정 : 2025/1/15
        ArrayList<String> seongsu = new ArrayList<>();
        gangneung.add("성수 소금빵");
        gangneung.add("성수 맛집");
        gangneung.add("성수 무신사스토어");

        ArrayList<UUID> p2members = new ArrayList<>();
        p1members.add(member2.getId()); // 선정
        p1members.add(member3.getId()); // 영헌
        p1members.add(member4.getId()); // 상현

        LocalDateTime p2from = LocalDateTime.of(2025, 1, 15, 7, 0, 0);
        LocalDateTime p2to = LocalDateTime.of(2025, 1, 15, 20, 0, 0);

        PlanEntity p2 = PlanEntity.create(community.getId(), "성수 나들이",
                p2from, p2to,"나들이", seongsu, p2members);
        planRepository.save(p2);

        // 모임 : 가평가자, 계획 : 여름 빠지, 일정 : 2025/7/23 - 2025/7/25
        ArrayList<String> gapyeong = new ArrayList<>();
        gapyeong.add("가평 빠지");
        gapyeong.add("남이섬");
        gapyeong.add("가평 닭갈비");

        ArrayList<UUID> p3members = new ArrayList<>();
        p3members.add(member1.getId()); // 형주
        p3members.add(member2.getId()); // 선정
        p3members.add(member3.getId()); // 영헌
        p3members.add(member6.getId()); // 미강

        LocalDateTime p3from = LocalDateTime.of(2025, 7, 23, 7, 0, 0);
        LocalDateTime p3to = LocalDateTime.of(2025, 7, 25, 20, 0, 0);

        PlanEntity p3 = PlanEntity.create(community4.getId(), "여름 빠지",
                p3from, p3to,"여행", gapyeong, p3members);
        planRepository.save(p3);

        // 모임 : 어쩌다6인조, 계획 : 서울 디저트 탐방, 일정 : 2025/2/11
        ArrayList<String> seoul = new ArrayList<>();
        seoul.add("서울 딸기 뷔페");
        seoul.add("마포 디저트");
        seoul.add("서울 크로플");

        ArrayList<UUID> p4members = new ArrayList<>();
        p4members.add(member7.getId()); // 유진
        p4members.add(member8.getId()); // 가을
        p4members.add(member9.getId()); // 원영

        LocalDateTime p4from = LocalDateTime.of(2025, 2, 11, 7, 0, 0);
        LocalDateTime p4to = LocalDateTime.of(2025, 2, 11, 20, 0, 0);

        PlanEntity p4 = PlanEntity.create(community2.getId(), "서울 디저트 탐방",
                p4from, p4to,"디저트", seoul, p4members);
        planRepository.save(p4);

        // 모임 : 솔바람산악회, 계획 : 3월 관악산 등반, 일정 : 2025/3/8
        ArrayList<String> gwanak = new ArrayList<>();
        gwanak.add("서울대입구 맛집");
        gwanak.add("신림 맛집");

        ArrayList<UUID> p5members = new ArrayList<>();
        p5members.add(member3.getId()); // 영헌
        p5members.add(member4.getId()); // 상현
        p5members.add(member6.getId()); // 미강
        p5members.add(member8.getId()); // 가을
        p5members.add(member11.getId()); // 지원
        p5members.add(member12.getId()); // 현서

        LocalDateTime p5from = LocalDateTime.of(2025, 3, 8, 7, 0, 0);
        LocalDateTime p5to = LocalDateTime.of(2025, 3, 8, 20, 0, 0);

        PlanEntity p5 = PlanEntity.create(community3.getId(), "3월 관악산 등반",
                p5from, p5to,"디저트", gwanak, p5members);
        planRepository.save(p5);

    }
}

