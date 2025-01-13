package org.hana.wooahhanaapi.domain.community.adapter;

import org.hana.wooahhanaapi.domain.community.adapter.dto.SendValidationCodeReqDto;

public interface SendValidCodePort {
    void sendValidCode(SendValidationCodeReqDto reqDto);
}
