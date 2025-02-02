package org.hana.wooahhanaapi.domain.account.port;

import org.hana.wooahhanaapi.domain.account.dto.BankCreateReqDto;
import org.hana.wooahhanaapi.domain.account.dto.BankCreateRespDto;

public interface BankCreatePort {
    BankCreateRespDto createNewBank(BankCreateReqDto requestDto);
}
