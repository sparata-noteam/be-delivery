package com.sparta.bedelivery.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bedelivery.dto.AdminOrderCondition;
import com.sparta.bedelivery.dto.CustomerOrderRequest;
import com.sparta.bedelivery.dto.OwnerOrderRequest;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.QOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {

    private QOrder qOrder = QOrder.order;
    private final JPAQueryFactory query;

    @Override
    public Page<Order> findAllOrdersByCustomer(Pageable pageable, CustomerOrderRequest request) {
        String userId = request.getUserId();


        UUID storeId = null;

        if (request.getStoreId() != null) {
            storeId = UUID.fromString(request.getStoreId());
        }

        List<Order> content = query.select(qOrder)
                .where(qOrder.userId.eq(userId)
                        .and(qOrder.deleteAt.isNull())
                        .and(checkStore(storeId))
                        .and(checkStatus(request.getStatus())))
                .from(qOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query.select(qOrder.count())
                .where(qOrder.userId.eq(userId)
                        .and(qOrder.deleteAt.isNull())
                        .and(checkStore(storeId))
                        .and(checkStatus(request.getStatus())))
                .from(qOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);


    }

    @Override
    public Page<Order> findAllOwner(Pageable pageable, OwnerOrderRequest condition) {
        UUID storeId = condition.getStoreId();
        Order.OrderStatus status = condition.getStatus();
        List<Order> content = query.select(qOrder)
                .where(
                        qOrder.deleteAt.isNull()
                                .and(checkStore(storeId))
                                .and(checkStatus(status)))
                .from(qOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query.select(qOrder.count())
                .where(qOrder.deleteAt.isNull()
                        .and(checkStore(storeId))
                        .and(checkStatus(status)))
                .from(qOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<Order> findByAdmin(Pageable pageable, AdminOrderCondition condition) {
        UUID storeId = null;

        if (condition.getStoreId() != null) {
            storeId = UUID.fromString(condition.getStoreId());
        }

        Order.OrderStatus status = condition.getStatus();
        List<Order> content = query.select(qOrder)
                .where(deletedCheck(condition.getIsDeleted()), checkStore(storeId), checkStatus(status))
                .from(qOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query.select(qOrder.count())
                .where(deletedCheck(condition.getIsDeleted()), checkStore(storeId), checkStatus(status))
                .from(qOrder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private Predicate deletedCheck(Boolean isDeleted) {
        return isDeleted == null ? null : isDeleted.equals(Boolean.TRUE)
                ? qOrder.deleteAt.isNotNull()
                : qOrder.deleteAt.isNull();
    }

    private Predicate checkStatus(Order.OrderStatus status) {
        return status == null ? null : qOrder.status.eq(status);
    }

    private Predicate checkStore(UUID storeId) {
        return storeId == null ? null : qOrder.store.id.eq(storeId);
    }
}
