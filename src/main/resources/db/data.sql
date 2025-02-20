INSERT INTO p_industry_categories
(id, create_at, create_by, update_at, update_by, name)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '한식'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '중식'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '일식'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '양식'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '디저트'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '패스트푸드'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '베이커리'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '브런치'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '샌드위치'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '스테이크'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '타파스'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '소고기 구이'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '해산물'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '치킨'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '피자'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '리조또'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '버거'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '국수'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '샐러드'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '파스타'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '오므라이스'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '카레'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '크레페'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '아이스크림'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '스무디'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '프리타타'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '치즈케이크');

INSERT INTO p_location_categories
(id, create_at, create_by, update_at, update_by, name, type, parent_id)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '서울시', '시', NULL);

WITH 서울시_id AS (
    SELECT id FROM public.p_location_categories WHERE name = '서울시' LIMIT 1
    ),
    종로구_id AS (
INSERT INTO public.p_location_categories
(id, create_at, create_by, update_at, update_by, name, type, parent_id)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '종로구', '구', (SELECT id FROM 서울시_id))
    RETURNING id
    ),
    중구_id AS (
INSERT INTO public.p_location_categories
(id, create_at, create_by, update_at, update_by, name, type, parent_id)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '중구', '구', (SELECT id FROM 서울시_id))
    RETURNING id
    )
INSERT INTO public.p_location_categories
(id, create_at, create_by, update_at, update_by, name, type, parent_id)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '용산구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '성동구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '광진구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '동대문구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '중랑구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '서대문구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '마포구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '양천구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '강서구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '구로구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '금천구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '영등포구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '동작구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '관악구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '서초구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '강남구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '송파구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '강동구', '구', (SELECT id FROM 서울시_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '광화문', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '세종로', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '인사동', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '청운효자동', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '장사동', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '신문로', '동', (SELECT id FROM 중구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '필동', '동', (SELECT id FROM 중구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '남산동', '동', (SELECT id FROM 중구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '사직동', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '청진동', '동', (SELECT id FROM 중구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '무교동', '동', (SELECT id FROM 중구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '내자동', '동', (SELECT id FROM 중구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '서린동', '동', (SELECT id FROM 중구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '태평로1가', '동', (SELECT id FROM 중구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '장충동', '동', (SELECT id FROM 중구_id));

-- INSERT INTO p_stores (
--     id,
--     create_at,
--     delete_at,
--     update_at,
--     user_id,
--     location_category_id,
--     phone,
--     status,
--     address,
--     create_by,
--     delete_by,
--     image_url,
--     name,
--     update_by
-- )
-- VALUES
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 1, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1111-1111', 'PENDING',
--     '테헤란로 101', 'MASTER', NULL, 'https://example.com/image1.jpg', 'Store 1', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 2, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-2222-2222', 'PENDING',
--      '올림픽로 202', 'MASTER', NULL, NULL, 'Store 2', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 3, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-3333-3333', 'PENDING',
--      '명동길 303', 'MASTER', NULL, 'https://example.com/image3.jpg', 'Store 3', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 4, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-4444-4444', 'PENDING',
--      '세종대로 404', 'MASTER', NULL, NULL, 'Store 4', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 5, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-5555-5555', 'PENDING',
--      '천호대로 505', 'MASTER', NULL, 'https://example.com/image5.jpg', 'Store 5', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 6, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-6666-6666', 'PENDING',
--      '반포대로 606', 'MASTER', NULL, NULL, 'Store 6', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 7, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-7777-7777', 'PENDING',
--      '디지털로 707', 'MASTER', NULL, 'https://example.com/image7.jpg', 'Store 7', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 8, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-8888-8888', 'PENDING',
--      '사당로 808', 'MASTER', NULL, NULL, 'Store 8', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 9, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-9999-9999', 'PENDING',
--      '월드컵로 909', 'MASTER', NULL, 'https://example.com/image9.jpg', 'Store 9', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 10, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1010-1010', 'PENDING',
--      '길음로 1010', 'MASTER', NULL, NULL, 'Store 10', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 11, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1111-1112', 'PENDING',
--      '공항대로 1111', 'MASTER', NULL, 'https://example.com/image11.jpg', 'Store 11', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 12, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1212-1212', 'PENDING',
--      '대학로 1212', 'MASTER', NULL, NULL, 'Store 12', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 13, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1313-1313', 'PENDING',
--      '선릉로 1313', 'MASTER', NULL, 'https://example.com/image13.jpg', 'Store 13', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 14, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1414-1414', 'PENDING',
--      '청계천로 1414', 'MASTER', NULL, NULL, 'Store 14', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 15, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1515-1515', 'PENDING',
--      '가락로 1515', 'MASTER', NULL, 'https://example.com/image15.jpg', 'Store 15', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 16, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1616-1616', 'PENDING',
--      '신촌로 1616', 'MASTER', NULL, NULL, 'Store 16', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 17, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1717-1717', 'PENDING',
--      '공항로 1717', 'MASTER', NULL, 'https://example.com/image17.jpg', 'Store 17', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 18, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1818-1818', 'PENDING',
--      '상봉로 1818', 'MASTER', NULL, NULL, 'Store 18', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 19, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-1919-1919', 'PENDING',
--      '은평로 1919', 'MASTER', NULL, 'https://example.com/image19.jpg', 'Store 19', NULL),
--     (uuid_generate_v4(), NOW(), NULL, NOW(), 20, (SELECT uuid FROM location_category WHERE type = '동' LIMIT 1), '010-2020-2020', 'PENDING',
--      '한남대로 2020', 'MASTER', NULL, NULL, 'Store 20', NULL);
--
-- INSERT INTO p_store_industry_categories (industry_category_id, store_id)
-- VALUES
--     ('90620839-7fcf-41e6-8ffb-feed695bb2a5', '308931a3-c5e7-4c5f-b051-abb471ad153f'),
--     ('98ffd51f-eba5-4dc1-a945-a35ac20c89e2', 'f2c57a5a-1a96-411d-8ed8-667cf6a9fc70'),
--     ('3afb504f-1372-4a3f-b164-56f533d2147c', '6354281b-968d-401e-9bf2-c2a673b00ed7'),
--     ('198b81fb-91fa-4d22-9460-d535650f937f', '88b13b54-744c-4cb3-bdeb-fd5ea4674b30'),
--     ('ecbc828b-83cf-43e1-b9e6-7e3755f4015e', '84a83688-eec4-4313-b997-0f1084279473'),
--     ('03c6491c-d6a4-441d-a9af-cb1f5bef5a3a', '95e9da77-9bb0-464e-8921-872031862af8'),
--     ('290688d5-ced0-44a2-b8a3-d83566931226', '88f14f70-c6da-4cf8-b2b5-fc915bd484b8'),
--     ('93fd3a08-154c-40e4-93f1-df28ed2bfbfa', 'c3eab34f-bd6e-414a-a3eb-fba13c36db2b'),
--     ('44a6cf5f-c3d0-4095-b762-f516cb5d0d2a', '44db621a-385c-432f-9a69-bf0b73213f2c'),
--     ('59618ed4-cbd5-41aa-a840-9b91faef353c', '2ae0c1a0-998f-489e-8be2-7a6003d0c2d8'),
--     ('673650d8-0936-4e0d-b23d-f3a209adcd94', '28671199-1542-4bdb-9a47-ae759c458f0e'),
--     ('6b174cbc-49ac-4bce-a9a6-cce3b34126a5', '33c602c4-bfd4-4a57-9d4f-0bc6f0b449e7'),
--     ('bd62aa58-d3df-401f-bda4-d7cf8d9863d8', '32e94b78-fa65-4ac5-a3da-4d4edc729a1f'),
--     ('a457b111-e600-4848-99d6-1c6c903c829f', 'c9d66d35-5685-4635-ad29-fdbba6a4e94d'),
--     ('b3182857-d92e-4442-a5dd-ec71d98a9fbc', '9a175398-a8d3-4658-baf3-6a82e78183a6'),
--     ('6216c493-66ed-4a69-be65-f0839f0e363c', 'fe16a581-96c4-4ec3-baee-da0c6c5e4f3c'),
--     ('902c93ed-0c7a-4d6c-b5ed-25e91db5b7a7', 'f09b0b1b-52a7-43bc-a848-1003fa2f77ea'),
--     ('c1f7c705-2a17-48be-ba44-138a1f0799b5', 'ed7c9b3d-f0df-422a-9175-a4f5617c2131'),
--     ('4a8082f9-63be-4509-94e1-ab0737987d85', 'fa9f77db-b4f9-486f-ab51-f083ced0953b'),
--     ('958b3d1f-e33d-4978-ae66-b83d9d0341dc', 'a21972d9-3e05-4eca-b6b8-e316cb15b024'),
--     ('4e1d44ec-e0a4-459b-959f-c42dc71a16da', '95e9da77-9bb0-464e-8921-872031862af8'),
--     ('995c360d-cfc8-4ce4-a05f-a26bd9baf217', '32e94b78-fa65-4ac5-a3da-4d4edc729a1f'),
--     ('93c53eb6-4b5c-4734-8c06-5003a82ecc3f', '9a175398-a8d3-4658-baf3-6a82e78183a6'),
--     ('42c1ff14-f805-4924-9007-0314bf5e2e3d', 'fe16a581-96c4-4ec3-baee-da0c6c5e4f3c'),
--     ('d0061432-b038-4590-ad86-dbb88e76367f', 'f09b0b1b-52a7-43bc-a848-1003fa2f77ea'),
--     ('3d65d824-efc5-4206-a6e1-33ec0ee66e34', 'ed7c9b3d-f0df-422a-9175-a4f5617c2131'),
--     ('fe9eb861-58f1-4d3c-956b-e0e1cb128d5a', 'fa9f77db-b4f9-486f-ab51-f083ced0953b')
--     ON CONFLICT (industry_category_id, store_id) DO NOTHING;

