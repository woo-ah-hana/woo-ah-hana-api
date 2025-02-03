package org.hana.wooahhanaapi.account.port;

import org.hana.wooahhanaapi.account.dto.AccountTransferRespDto;
import org.hana.wooahhanaapi.account.dto.SimplifiedTransferReqDto;

public interface AccountTransferPort {

    AccountTransferRespDto createAccountTransfer(SimplifiedTransferReqDto reqDto);
}
