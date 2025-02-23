INSERT INTO p_industry_categories
(id, create_at, create_by, update_at, update_by, name)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '한식'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '중식'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '일식'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '양식'),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '디저트')
ON CONFLICT (name) DO NOTHING;

-- 서울시 데이터 삽입
INSERT INTO p_location_categories
(id, create_at, create_by, update_at, update_by, name, type, parent_id)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '서울시', '시', NULL)
ON CONFLICT (name) DO NOTHING
RETURNING id;

-- 종로구 데이터 삽입
WITH 서울시_id AS (
    SELECT id FROM p_location_categories WHERE name = '서울시' AND type = '시' LIMIT 1
)
INSERT INTO p_location_categories
(id, create_at, create_by, update_at, update_by, name, type, parent_id)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '종로구', '구', (SELECT id FROM 서울시_id))
ON CONFLICT (name) DO NOTHING
RETURNING id;

-- 동 데이터 삽입
WITH 종로구_id AS (
    SELECT id FROM p_location_categories WHERE name = '종로구' AND type = '구' LIMIT 1
)
INSERT INTO p_location_categories
(id, create_at, create_by, update_at, update_by, name, type, parent_id)
VALUES
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '광화문', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '세종로', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '청진동', '동', (SELECT id FROM 종로구_id)),
    (uuid_generate_v4(), CURRENT_TIMESTAMP, 'MASTER', CURRENT_TIMESTAMP, 'MASTER', '내자동', '동', (SELECT id FROM 종로구_id))
ON CONFLICT (name) DO NOTHING;