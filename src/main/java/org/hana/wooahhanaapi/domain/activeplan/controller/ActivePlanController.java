package org.hana.wooahhanaapi.domain.activeplan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.activeplan.adaptor.NaverSearchAdaptor;
import org.hana.wooahhanaapi.domain.activeplan.adaptor.dto.SearchResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/active_plan")
public class ActivePlanController {

    private final NaverSearchAdaptor naverSearchAdaptor;

    @GetMapping("/api_test")
    public SearchResponseDto getNaverSearch(@RequestParam String query) {
        return naverSearchAdaptor.getSearchResult(query);
    }

}
