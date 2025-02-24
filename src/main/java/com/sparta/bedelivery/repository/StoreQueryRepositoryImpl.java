package com.sparta.bedelivery.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bedelivery.entity.QLocationCategory;
import com.sparta.bedelivery.entity.QStore;
import com.sparta.bedelivery.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepositoryImpl implements StoreQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QStore store = QStore.store;
    private final QLocationCategory locationCategory = QLocationCategory.locationCategory;

    @Override
    public List<Store> findStoresWithCategory(String industryName, String locationName) {
        return queryFactory
                .selectFrom(store)
                .innerJoin(store.storeIndustryCategories).fetchJoin()
                .innerJoin(store.locationCategory, locationCategory).fetchJoin()
                .where(
                        store.status.ne(Store.Status.DELETE),
                        store.status.ne(Store.Status.DELETE_REQUESTED),
                        store.isHidden.isFalse(),
                        industryNameEq(industryName),
                        locationNameCondition(locationName)
                )
                .orderBy(store.createAt.desc())
                .fetch();
    }

    private BooleanExpression industryNameEq(String industryName) {
        if (!StringUtils.hasText(industryName)) {
            return null;
        }
        return store.storeIndustryCategories.any().industryCategory.name.eq(industryName);
    }
    private BooleanExpression locationNameCondition(String locationName) {
        if (!StringUtils.hasText(locationName)) {
            return null;
        }

        // "서울"이 입력되면 조건을 null로 반환하여 모든 매장이 조회되도록 함
        if ("서울".equals(locationName)) {
            return null;
        }

        // 그 외의 경우는 해당 지역의 매장만 조회
        return store.locationCategory.name.eq(locationName);
    }
}