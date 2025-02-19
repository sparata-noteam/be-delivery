package com.sparta.bedelivery.repository;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bedelivery.dto.CustomerOrderRequest;
import com.sparta.bedelivery.dto.CustomerOrderResponse;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private QOrder qOrder = QOrder.order;
    private final JPAQueryFactory query;

    @Override
    public Page<Order> findAllUsers(Pageable pageable, CustomerOrderRequest request) {
        String userId = request.getUserId();


        UUID storeId = null;

        if (request.getStoreId() != null) {
            storeId = UUID.fromString(request.getStoreId());
        }
        List<Order> content = query.select(qOrder)
                .where(qOrder.userId.eq(userId)
                        .and(checkStore(storeId))
                        .and(checkStatus(request.getStatus())))
                .from(qOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query.select(qOrder.count())
                .where(qOrder.userId.eq(userId)
                        .and(checkStore(storeId))
                        .and(checkStatus(request.getStatus())))
                .from(qOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);


    }

    private Predicate checkStatus(Order.OrderStatus status) {
        return status == null ? null : qOrder.status.eq(status);
    }

    private Predicate checkStore(UUID storeId) {
        return storeId == null ? null : qOrder.store.eq(storeId);
    }
}
