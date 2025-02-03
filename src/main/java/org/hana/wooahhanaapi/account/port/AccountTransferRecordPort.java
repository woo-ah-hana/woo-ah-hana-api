package org.hana.wooahhanaapi.account.port;

import org.hana.wooahhanaapi.account.dto.AccountTransferRecordReqDto;
import org.hana.wooahhanaapi.account.dto.AccountTransferRecordRespDto;

public interface AccountTransferRecordPort {

    AccountTransferRecordRespDto getTransferRecord(AccountTransferRecordReqDto reqDto);
}
