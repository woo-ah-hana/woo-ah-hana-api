package org.hana.wooahhanaapi.plan.service;

import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferRecordPort;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespDataDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespListDto;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.community.exception.CommunityNotFoundException;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.plan.dto.GetReceiptResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class PlanServiceTest {

    @InjectMocks
    private PlanService planService;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private CommunityRepository communityRepository;

    @Mock
    private AccountTransferRecordPort accountTransferRecordPort;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPlanReceipt_Success() {
        UUID planId = UUID.randomUUID();
        PlanEntity mockPlan = PlanEntity.builder()
                .communityId(UUID.randomUUID())
                .startDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2025, 1, 5, 23, 59))
                .build();

        CommunityEntity mockCommunity = CommunityEntity.builder()
                .accountNumber("123-456-789")
                .memberships(List.of(new MembershipEntity(), new MembershipEntity()))
                .build();

        AccountTransferRecordRespDto mockResponse = AccountTransferRecordRespDto.builder()
                .data(AccountTransferRecordRespDataDto.builder()
                        .apiTranId("apiTranIdExample")
                        .resCode("200")
                        .resMessage("Success")
                        .apiTranDtm("2023-01-01T00:00:00")
                        .bankTranId("001")
                        .bankTranDate("2023-01-01")
                        .bankCodeTran("bankCodeExample")
                        .bankRspCode("0000")
                        .bankRspMessage("Success")
                        .fintechUseNum("fintechUseNumExample")
                        .balanceAmt("10000")
                        .pageRecordCnt("2")
                        .nextPageYn("N")
                        .beforeInquiryTraceInfo("traceInfoExample")
                        .resList(List.of(
                                AccountTransferRecordRespListDto.builder()
                                        .tranDate("2023-01-01")
                                        .tranTime("120000")
                                        .inoutType("OUT")
                                        .tranType("TRANSFER")
                                        .printContent("Test1")
                                        .tranAmt("1000")
                                        .afterBalanceAmt("9000")
                                        .branchName("Branch1")
                                        .build(),
                                AccountTransferRecordRespListDto.builder()
                                        .tranDate("2023-01-02")
                                        .tranTime("130000")
                                        .inoutType("OUT")
                                        .tranType("TRANSFER")
                                        .printContent("Test2")
                                        .tranAmt("2000")
                                        .afterBalanceAmt("7000")
                                        .branchName("Branch2")
                                        .build()
                        ))
                        .build())
                .build();

        when(planRepository.findById(planId)).thenReturn(Optional.of(mockPlan));
        when(communityRepository.findById(mockPlan.getCommunityId())).thenReturn(Optional.of(mockCommunity));
        when(accountTransferRecordPort.getTransferRecord(any())).thenReturn(mockResponse);

        GetReceiptResponseDto response = planService.getPlanReceipt(planId);

        assertThat(response.getRecords()).hasSize(2);
        assertThat(response.getTotalAmt()).isEqualTo(3000L);
        assertThat(response.getPerAmt()).isEqualTo(1500L);

        verify(planRepository).findById(planId);
        verify(communityRepository).findById(mockPlan.getCommunityId());
        verify(accountTransferRecordPort).getTransferRecord(any());
    }
}
