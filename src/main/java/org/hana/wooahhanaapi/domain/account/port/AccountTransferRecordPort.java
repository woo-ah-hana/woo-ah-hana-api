package org.hana.wooahhanaapi.domain.account.port;

import org.hana.wooahhanaapi.domain.account.dto.AccountTransferRecordReqDto;
import org.hana.wooahhanaapi.domain.account.dto.AccountTransferRecordRespDto;

public interface AccountTransferRecordPort {

    AccountTransferRecordRespDto getTransferRecord(AccountTransferRecordReqDto reqDto);
}
