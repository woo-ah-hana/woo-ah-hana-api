package org.hana.wooahhanaapi.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hana.wooahhanaapi.domain.naver.adaptor.NaverSearchAdaptor;
import org.hana.wooahhanaapi.domain.naver.adaptor.dto.SearchResponseDto;
import org.hana.wooahhanaapi.domain.naver.exception.InvalidJsonMappingException;
import org.hana.wooahhanaapi.domain.naver.exception.InvalidSearchQueryException;
import org.hana.wooahhanaapi.domain.naver.exception.NaverApiException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
public class NaverSearchAdaptorTest {

    @Autowired
    private NaverSearchAdaptor naverSearchAdaptor;

    @BeforeEach
    void seed() {
    }

    @Test
    void testGetSearchResultList_ValidQueries() {
        // given
        List<String> queries = List.of("강남 카페", "강릉 레스토랑", "강릉 레스토랑");

        Set<String> uniqueQueries = new HashSet<>();
        long validQueryCount = queries.stream()
                .filter(query -> query != null && !query.trim().isEmpty() && uniqueQueries.add(query))
                .count();

        // when
        List<SearchResponseDto> results = naverSearchAdaptor.getSearchResultList(queries);

        // then
        Assertions.assertNotNull(results, "결과 리스트는 null이면 안됩니다.");
        Assertions.assertFalse(results.isEmpty(), "결과 리스트가 비어 있으면 안됩니다.");
        Assertions.assertEquals(validQueryCount, results.size(), "결과 개수는 유효한 검색어 개수와 동일해야 합니다.");
    }


    @Test
    void testGetSearchResultList_InvalidQueries() {
        // given
        List<String> invalidQueries = Arrays.asList("", "  ", null);

        // when & then
        Assertions.assertThrows(InvalidSearchQueryException.class,
                () -> naverSearchAdaptor.getSearchResultList(invalidQueries),
                "유효하지 않은 검색어 예외가 발생해야 합니다.");
    }

//    @Test
//    void testGetSearchResultList_InvalidJsonMapping() {
//        // given
//        List<String> queries = Arrays.asList("강남 카페", "강릉 레스토랑");
//
//        // 조건: SearchResponseDto가 응답값과 다른 경우
//        // SearchResponseDto를 실제 네이버 api 응답값과 다르게 설정해야 테스트 가능
//        //ex) items -> item으로 변경
//
//        // when & then
//        Assertions.assertThrows(InvalidJsonMappingException.class,
//                () -> naverSearchAdaptor.getSearchResultList(queries),
//                "잘못된 JSON 맵핑예외가 발생해야 합니다.");
//    }
//
//
//    @Test
//    void testGetSearchResult_ApiError() {
//        // given
//        List<String> queries = Arrays.asList("강남 카페", "강릉 레스토랑");
//
//        // 조건: NAVER API Key가 잘못된 경우
//        // application-test.yml에서 클라이언트 ID/Secret을 빈값 또는 잘못된 값으로 설정해야 테스트 가능
//
//        // when & then
//        Assertions.assertThrows(NaverApiException.class,
//                () -> naverSearchAdaptor.getSearchResultList(queries),
//                "API 오류 예외가 발생해야 합니다.");
//    }
}