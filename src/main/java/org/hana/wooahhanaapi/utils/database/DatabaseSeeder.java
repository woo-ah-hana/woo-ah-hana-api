package org.hana.wooahhanaapi.utils.database;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.utils.exception.SeederException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

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
    }

}

