package org.hana.wooahhanaapi.domain.account.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespDto;

public interface AccountTransferRecordPort {

    AccountTransferRecordRespDto getTransferRecord(AccountTransferRecordReqDto reqDto);
}
