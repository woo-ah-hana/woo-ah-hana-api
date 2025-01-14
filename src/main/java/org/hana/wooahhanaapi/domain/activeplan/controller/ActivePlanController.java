package org.hana.wooahhanaapi.domain.activeplan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.activeplan.adaptor.NaverSearchAdaptor;
import org.hana.wooahhanaapi.domain.activeplan.adaptor.dto.SearchResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/active_plan")
public class ActivePlanController {

    private final NaverSearchAdaptor naverSearchAdaptor;

    @PostMapping("/search")
    public List<SearchResponseDto> getNaverSearch(@RequestBody List<String> queries) {
        return naverSearchAdaptor.getSearchResultList(queries);
    }

}
