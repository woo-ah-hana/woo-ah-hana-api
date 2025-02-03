package org.hana.wooahhanaapi.account.port;

import org.hana.wooahhanaapi.account.dto.BankCreateReqDto;
import org.hana.wooahhanaapi.account.dto.BankCreateRespDto;

public interface BankCreatePort {
    BankCreateRespDto createNewBank(BankCreateReqDto requestDto);
}
