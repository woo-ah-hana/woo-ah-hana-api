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
import org.hana.wooahhanaapi.utils.exception.SeederException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;
    private final MembershipRepository membershipRepository;
    private final AccountService accountService;

    @Override
    @Transactional
    public void run(String... args) throws SeederException {
        //은행 시딩
        BankCreateReqDto hanaBank = new BankCreateReqDto("하나은행","001","097U12345");
        BankCreateReqDto nonghyupBank = new BankCreateReqDto("농협은행","002","097U12345");
        BankCreateReqDto wooriBank = new BankCreateReqDto("우리은행","003","097U12345");
        BankCreateReqDto shinhanBank = new BankCreateReqDto("신한은행","004","097U12345");
        BankCreateReqDto giupBank = new BankCreateReqDto("기업은행","005","097U12345");
        BankCreateReqDto kakaoBank = new BankCreateReqDto("카카오뱅크","006","097U12345");
        BankCreateReqDto tossBank = new BankCreateReqDto("토스뱅크","007","097U12345");
        BankCreateReqDto kbBank = new BankCreateReqDto("KB국민은행", "008", "097U12345");
        BankCreateReqDto busanBank = new BankCreateReqDto("부산은행", "009", "097U12345");
        BankCreateReqDto daeguBank = new BankCreateReqDto("대구은행", "010", "097U12345");
        BankCreateReqDto gwangjuBank = new BankCreateReqDto("광주은행", "011", "097U12345");
        BankCreateReqDto jeonbukBank = new BankCreateReqDto("전북은행", "012", "097U12345");
        BankCreateReqDto jejuBank = new BankCreateReqDto("제주은행", "013", "097U12345");
        List<BankCreateReqDto> banks = Arrays.asList(hanaBank, nonghyupBank, wooriBank, shinhanBank, giupBank, kakaoBank, tossBank, kbBank, busanBank, daeguBank,gwangjuBank,jeonbukBank,jejuBank,jejuBank);
        for (BankCreateReqDto bank : banks) {
            try {
                accountService.createBank(bank);
            } catch (Exception e) {
                System.out.println("은행 중복 생성");
            }
        }
        //계좌 시딩
        AccountCreateReqDto hjAccount = new AccountCreateReqDto("002","0","3560000000000","자유입출금계좌");//함형주 계좌 생성
        AccountCreateReqDto sjAccount = new AccountCreateReqDto("003","0","3561111111111","자유입출금계좌");
        AccountCreateReqDto yhAccount = new AccountCreateReqDto("002","0","3562222222222","자유입출금계좌");
        AccountCreateReqDto shAccount = new AccountCreateReqDto("001","0","3563333333333","자유입출금계좌");
        AccountCreateReqDto cwAccount = new AccountCreateReqDto("001","0","3564444444444","자유입출금계좌");
        AccountCreateReqDto mkAccount = new AccountCreateReqDto("004","0","3565555555555","자유입출금계좌");
        AccountCreateReqDto dtAccount = new AccountCreateReqDto("001","0","3560603895413","자유입출금계좌");
        AccountCreateReqDto matJipTamBangAccount = new AccountCreateReqDto("001","0","3561111222222","자유입출금계좌");
        AccountCreateReqDto goTravelAccount = new AccountCreateReqDto("001","0","3561234123412","자유입출금계좌");
        AccountCreateReqDto bagAccount = new AccountCreateReqDto("001","0","3564321432143","자유입출금계좌");
        AccountCreateReqDto goGapyeongAccount = new AccountCreateReqDto("001","0","3561111222233","자유입출금계좌");
        AccountCreateReqDto goJapanAccount = new AccountCreateReqDto("001","0","3563333222211","자유입출금계좌");

        List<AccountCreateReqDto> accounts = Arrays.asList(hjAccount,sjAccount,yhAccount,shAccount,cwAccount,mkAccount,dtAccount,matJipTamBangAccount,goTravelAccount,bagAccount,goGapyeongAccount,goJapanAccount);
        for (AccountCreateReqDto account : accounts) {
            try {
                accountService.createAccount(account);
            } catch (Exception e) {
                System.out.println("계좌 중복 생성");
            }
        }

        //member seed
        MemberEntity member1 = MemberEntity.create("hj1234","함형주",passwordEncoder.encode("hj1234!"),"01026530957","3560000000000","002");//농협:bank_tran_id="002"
        memberRepository.save(member1);
        MemberEntity member2 = MemberEntity.create("sj1234","최선정",passwordEncoder.encode("sj1234!"),"01012341234","3561111111111","003");//우리:bank_tran_id="003"
        memberRepository.save(member2);
        MemberEntity member3 = MemberEntity.create("yh1234","윤영헌",passwordEncoder.encode("yh1234!"),"01012341234","3562222222222","002");//농협:bank_tran_id="002"
        memberRepository.save(member3);
        MemberEntity member4 = MemberEntity.create("sh1234","김상현",passwordEncoder.encode("sh1234!"),"01012341234","3563333333333","001");//하나:bank_tran_id="001"
        memberRepository.save(member4);
        MemberEntity member5 = MemberEntity.create("cw1234","김채운",passwordEncoder.encode("cw1234!"),"01012341234","3564444444444","001");//하나:bank_tran_id="001"
        memberRepository.save(member5);
        MemberEntity member6 = MemberEntity.create("mk1234","김미강",passwordEncoder.encode("mk1234!"),"01012341234","3565555555555","004");//신한:bank_tran_id="004"
        memberRepository.save(member6);
        MemberEntity member7 = MemberEntity.create("dt1234","데이터",passwordEncoder.encode("dt1234!"),"01012341234","3560603895413","001"); //하나:bank_tran_id="001"
        memberRepository.save(member7);

        //community seed
        MemberEntity yh = memberRepository.findByUsername("yh1234").orElse(null);
        CommunityEntity yhCommunity1 = CommunityEntity.create(yh.getId(),"맛집탐방","3561111222222",3L,200000L,60L);
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

