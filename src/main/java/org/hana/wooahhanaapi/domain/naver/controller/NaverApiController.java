package org.hana.wooahhanaapi.domain.naver.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.naver.adaptor.NaverSearchPort;
import org.hana.wooahhanaapi.domain.naver.adaptor.dto.SearchResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/naver")
public class NaverApiController {

    private final NaverSearchPort naverSearchPort;

    @PostMapping("/search")
    public List<SearchResponseDto> getNaverSearch(@RequestBody List<String> queries) {
        return naverSearchPort.getSearchResultList(queries);
    }

}
