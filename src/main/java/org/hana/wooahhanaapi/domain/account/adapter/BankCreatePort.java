package org.hana.wooahhanaapi.domain.account.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateRespDto;

public interface BankCreatePort {
    BankCreateRespDto createNewBank(BankCreateReqDto requestDto);
}
