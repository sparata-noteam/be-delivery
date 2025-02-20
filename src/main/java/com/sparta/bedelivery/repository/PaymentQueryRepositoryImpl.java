package com.sparta.bedelivery.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.bedelivery.dto.AdminPaymentCondition;
import com.sparta.bedelivery.entity.Payment;
import com.sparta.bedelivery.entity.QPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepositoryImpl implements PaymentQueryRepository {

    private final JPAQueryFactory query;
    private final QPayment payment = QPayment.payment;


    @Override
    public Page<Payment> findAllCondition(Pageable pageable, AdminPaymentCondition condition) {
        List<Payment> content = query.select(payment)
                .where(deletedCheck(condition.getIsDeleted()), checkMethod(condition.getMethod()), checkStatus(condition.getStatus()))
                .from(payment)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = query.select(payment.count())
                .where(deletedCheck(condition.getIsDeleted()), checkMethod(condition.getMethod()), checkStatus(condition.getStatus()))
                .from(payment)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);

    }

    private Predicate checkMethod(Payment.Method method) {
        return method == null ? null : payment.method.eq(method);
    }

    private Predicate checkStatus(Payment.Status status) {
        return status == null ? null : payment.status.eq(status);
    }


    private Predicate deletedCheck(Boolean isDeleted) {
        return isDeleted == null ? null : isDeleted.equals(Boolean.TRUE)
                ? payment.deleteAt.isNotNull()
                : payment.deleteAt.isNull();
    }


}
