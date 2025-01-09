package org.hana.wooahhanaapi.domain.account.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateRespDto;
import org.springframework.stereotype.Service;

@Service
public interface BankCreatePort {
    BankCreateRespDto createNewBank(BankCreateReqDto requestDto);
}
