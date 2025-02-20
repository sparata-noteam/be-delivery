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