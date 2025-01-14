package org.hana.wooahhanaapi.domain.activeplan.controller;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.activeplan.adaptor.NaverSearchAdaptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/activeplan")
public class ActivePlanController {

    private final NaverSearchAdaptor naverSearchAdaptor;

    @GetMapping("/naver")
    public String getNaverSearch(@RequestParam String word) {
        return naverSearchAdaptor.getSearchResult(word);
    }

}
