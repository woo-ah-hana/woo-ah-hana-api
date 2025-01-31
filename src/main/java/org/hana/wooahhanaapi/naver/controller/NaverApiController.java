package org.hana.wooahhanaapi.naver.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.naver.port.NaverSearchPort;
import org.hana.wooahhanaapi.naver.dto.SearchResponseDto;
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
