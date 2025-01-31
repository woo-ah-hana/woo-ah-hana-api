package org.hana.wooahhanaapi.domain.account.port;

import org.hana.wooahhanaapi.domain.account.dto.AccountTransferRespDto;
import org.hana.wooahhanaapi.domain.account.dto.SimplifiedTransferReqDto;

public interface AccountTransferPort {

    AccountTransferRespDto createAccountTransfer(SimplifiedTransferReqDto reqDto);
}
