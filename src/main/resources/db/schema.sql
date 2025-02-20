
CREATE TABLE IF NOT EXISTS p_ai_interactions (
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    user_id BIGINT NOT NULL,
    id UUID NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    query_text TEXT NOT NULL,
    response_text TEXT NOT NULL,
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_industry_categories (
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    id UUID NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    name VARCHAR(255) NOT NULL UNIQUE,
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_location_categories (
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    id UUID NOT NULL,
    parent_id UUID,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    name VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(255) NOT NULL,
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_menu_images (
    order_index INTEGER NOT NULL,
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    id UUID NOT NULL,
    menu_id UUID,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    image_url VARCHAR(255) NOT NULL,
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_menus (
    is_hidden BOOLEAN NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    id UUID NOT NULL,
    store_id UUID NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    description TEXT,
    name VARCHAR(255) NOT NULL,
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_order_items (
    price NUMERIC(10,2) NOT NULL,
    quantity INTEGER NOT NULL,
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    id UUID NOT NULL,
    order_id UUID,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    menu_id VARCHAR(255),
    menu_name VARCHAR(255),
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_orders (
    total_price NUMERIC(10,2) NOT NULL,
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    ordered_at TIMESTAMP(6) NOT NULL,
    update_at TIMESTAMP(6),
    id UUID NOT NULL,
    store_id UUID,
    order_type VARCHAR(20) NOT NULL CHECK (order_type IN ('TAKEOUT','DELIVERY')),
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING','CONFIRMED','CANCELLED','DELIVERING','COMPLETED')),
    address VARCHAR(255) NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    description TEXT,
    update_by VARCHAR(255),
    user_id VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_payment_history (
    changed_at TIMESTAMP(6) NOT NULL,
    id UUID NOT NULL,
    payment_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'PAID', 'REFUNDED_CALL', 'REFUNDED')),
    reason TEXT,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_payments (
    amount NUMERIC(10,2),
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    paid_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    id UUID NOT NULL,
    order_id UUID NOT NULL UNIQUE,
    method VARCHAR(50) CHECK (method IN ('CREDIT_CARD', 'KAKAO_PAY', 'NAVER_PAY', 'BANK_TRANSFER', 'POINTS')),
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'PAID', 'REFUNDED_CALL', 'REFUNDED')),
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    update_by VARCHAR(255),
    user_id VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_reviews (
    rating INTEGER NOT NULL,
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    user_id BIGINT NOT NULL,
    id UUID NOT NULL,
    menu_id UUID,
    order_id UUID NOT NULL,
    comment TEXT,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_store_industry_categories
(
    industry_category_id UUID NOT NULL,
    store_id             UUID NOT NULL,
    PRIMARY KEY (industry_category_id, store_id)
);

CREATE TABLE IF NOT EXISTS p_store_update_requests (
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    id UUID NOT NULL,
    store_id UUID NOT NULL,
    phone VARCHAR(20) NOT NULL,
    status VARCHAR(50) CHECK (status IN ('PENDING', 'OPEN', 'CLOSED', 'SUSPENDED', 'HIDDEN', 'DELETE_REQUESTED', 'DELETE', 'UPDATED', 'COMPLETED', 'UPDATE_REQUESTED')),
    address VARCHAR(255) NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    image_url VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_stores (
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    user_id BIGINT NOT NULL,
    id UUID NOT NULL,
    location_category_id UUID,
    phone VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL CHECK (status IN ('PENDING', 'OPEN', 'CLOSED', 'SUSPENDED', 'HIDDEN', 'DELETE_REQUESTED', 'DELETE', 'UPDATED', 'COMPLETED', 'UPDATE_REQUESTED')),
    address VARCHAR(255) NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    image_url VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_user_addresses (
    is_default BOOLEAN NOT NULL,
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    update_at TIMESTAMP(6),
    user_address_id BIGINT,
    user_id BIGINT NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    id UUID NOT NULL,
    recipient_name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    address_name VARCHAR(255) NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    update_by VARCHAR(255),
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_users (
    create_at TIMESTAMP(6) NOT NULL,
    delete_at TIMESTAMP(6),
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    update_at TIMESTAMP(6),
    phone VARCHAR(20) NOT NULL UNIQUE,
    role VARCHAR(50) NOT NULL CHECK (role IN ('CUSTOMER', 'OWNER', 'MANAGER', 'MASTER')),
    name VARCHAR(100) NOT NULL,
    create_by VARCHAR(255) NOT NULL,
    delete_by VARCHAR(255),
    nickname VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    update_by VARCHAR(255),
    user_id VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS p_users_store_list
(
    user_id BIGINT NOT NULL,
    store_list_id UUID   NOT NULL UNIQUE
);
-- p_ai_interactions 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_ai_interactions
DROP CONSTRAINT IF EXISTS FK5vlpvrjx3jmbd6agsp8xei79n;
ALTER TABLE IF EXISTS p_ai_interactions
    ADD CONSTRAINT FK5vlpvrjx3jmbd6agsp8xei79n
    FOREIGN KEY (user_id)
    REFERENCES p_users;

-- p_location_categories 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_location_categories
DROP CONSTRAINT IF EXISTS FK9bv2wrjf0bf8299dunpajgslt;
ALTER TABLE IF EXISTS p_location_categories
    ADD CONSTRAINT FK9bv2wrjf0bf8299dunpajgslt
    FOREIGN KEY (parent_id)
    REFERENCES p_location_categories;

-- p_menu_images 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_menu_images
DROP CONSTRAINT IF EXISTS FKmsy00v3m2cv3q60h9qb31vc7l;
ALTER TABLE IF EXISTS p_menu_images
    ADD CONSTRAINT FKmsy00v3m2cv3q60h9qb31vc7l
    FOREIGN KEY (menu_id)
    REFERENCES p_menus;

-- p_menus 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_menus
DROP CONSTRAINT IF EXISTS FKjbcqptr51cpejgof4hhl3tild;
ALTER TABLE IF EXISTS p_menus
    ADD CONSTRAINT FKjbcqptr51cpejgof4hhl3tild
    FOREIGN KEY (store_id)
    REFERENCES p_stores;

-- p_order_items 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_order_items
DROP CONSTRAINT IF EXISTS FKquswcn84hdunm64xwjbrnc3mc;
ALTER TABLE IF EXISTS p_order_items
    ADD CONSTRAINT FKquswcn84hdunm64xwjbrnc3mc
    FOREIGN KEY (order_id)
    REFERENCES p_orders;

-- p_orders 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_orders
DROP CONSTRAINT IF EXISTS FK2ovpe5mpbxixmxehewq749c1w;
ALTER TABLE IF EXISTS p_orders
    ADD CONSTRAINT FK2ovpe5mpbxixmxehewq749c1w
    FOREIGN KEY (store_id)
    REFERENCES p_stores;

-- p_payment_history 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_payment_history
DROP CONSTRAINT IF EXISTS FKki5clm6yyr6cdb1cvl4yhcu91;
ALTER TABLE IF EXISTS p_payment_history
    ADD CONSTRAINT FKki5clm6yyr6cdb1cvl4yhcu91
    FOREIGN KEY (payment_id)
    REFERENCES p_payments;

-- p_payments 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_payments
DROP CONSTRAINT IF EXISTS FKpum2urwkoivajjk0e6rmiiewu;
ALTER TABLE IF EXISTS p_payments
    ADD CONSTRAINT FKpum2urwkoivajjk0e6rmiiewu
    FOREIGN KEY (order_id)
    REFERENCES p_orders;

-- p_reviews 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_reviews
DROP CONSTRAINT IF EXISTS FK64sdl884uhb78rbdj6e0r2bg2;
ALTER TABLE IF EXISTS p_reviews
    ADD CONSTRAINT FK64sdl884uhb78rbdj6e0r2bg2
    FOREIGN KEY (menu_id)
    REFERENCES p_menus;

ALTER TABLE IF EXISTS p_reviews
DROP CONSTRAINT IF EXISTS FK7encho0muaa8kkbi4a574o61i;
ALTER TABLE IF EXISTS p_reviews
    ADD CONSTRAINT FK7encho0muaa8kkbi4a574o61i
    FOREIGN KEY (order_id)
    REFERENCES p_orders;

ALTER TABLE IF EXISTS p_reviews
DROP CONSTRAINT IF EXISTS FKl1ykwws0f8df9asse8sjid65t;
ALTER TABLE IF EXISTS p_reviews
    ADD CONSTRAINT FKl1ykwws0f8df9asse8sjid65t
    FOREIGN KEY (user_id)
    REFERENCES p_users;

-- p_store_industry_categories 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_store_industry_categories
DROP CONSTRAINT IF EXISTS FKpipvw0j26x3pgoc8gnbfpt04j;
ALTER TABLE IF EXISTS p_store_industry_categories
    ADD CONSTRAINT FKpipvw0j26x3pgoc8gnbfpt04j
    FOREIGN KEY (industry_category_id)
    REFERENCES p_industry_categories;

ALTER TABLE IF EXISTS p_store_industry_categories
DROP CONSTRAINT IF EXISTS FKfthwel3e8oq7ov0elq90spl8q;
ALTER TABLE IF EXISTS p_store_industry_categories
    ADD CONSTRAINT FKfthwel3e8oq7ov0elq90spl8q
    FOREIGN KEY (store_id)
    REFERENCES p_stores;

-- p_store_update_requests 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_store_update_requests
DROP CONSTRAINT IF EXISTS FKogqxpnpwpv3h1u5fe1gmmh0x9;
ALTER TABLE IF EXISTS p_store_update_requests
    ADD CONSTRAINT FKogqxpnpwpv3h1u5fe1gmmh0x9
    FOREIGN KEY (store_id)
    REFERENCES p_stores;

-- p_stores 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_stores
DROP CONSTRAINT IF EXISTS FKqkuwo8i1bp172m1rric3iyo76;
ALTER TABLE IF EXISTS p_stores
    ADD CONSTRAINT FKqkuwo8i1bp172m1rric3iyo76
    FOREIGN KEY (location_category_id)
    REFERENCES p_location_categories;

ALTER TABLE IF EXISTS p_stores
DROP CONSTRAINT IF EXISTS FK5vd0ybsa7hi7x2di2wsdvysqg;
ALTER TABLE IF EXISTS p_stores
    ADD CONSTRAINT FK5vd0ybsa7hi7x2di2wsdvysqg
    FOREIGN KEY (user_id)
    REFERENCES p_users;

-- p_user_addresses 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_user_addresses
DROP CONSTRAINT IF EXISTS FKb5a7me9wp5d9xt2rcqs0jx1sn;
ALTER TABLE IF EXISTS p_user_addresses
    ADD CONSTRAINT FKb5a7me9wp5d9xt2rcqs0jx1sn
    FOREIGN KEY (user_id)
    REFERENCES p_users;

ALTER TABLE IF EXISTS p_user_addresses
DROP CONSTRAINT IF EXISTS FK2vaftumvq8vdud7oq4wikaayd;
ALTER TABLE IF EXISTS p_user_addresses
    ADD CONSTRAINT FK2vaftumvq8vdud7oq4wikaayd
    FOREIGN KEY (user_address_id)
    REFERENCES p_users;

-- p_users_store_list 테이블 외래 키 추가
ALTER TABLE IF EXISTS p_users_store_list
DROP CONSTRAINT IF EXISTS FKpd2n5ivf2aehw1a13j6qh5eup;
ALTER TABLE IF EXISTS p_users_store_list
    ADD CONSTRAINT FKpd2n5ivf2aehw1a13j6qh5eup
    FOREIGN KEY (store_list_id)
    REFERENCES p_stores;

ALTER TABLE IF EXISTS p_users_store_list
DROP CONSTRAINT IF EXISTS FKatlojwe674y7dakama0sy1433;
ALTER TABLE IF EXISTS p_users_store_list
    ADD CONSTRAINT FKatlojwe674y7dakama0sy1433
    FOREIGN KEY (user_id)
    REFERENCES p_users;
