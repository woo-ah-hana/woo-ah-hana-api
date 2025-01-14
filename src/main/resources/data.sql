CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-1234-5678', 'root1234!','김미강','010-1234-5678', '15904815655249', '하나은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-2445-7209', 'root1234!','김상현','010-2445-7209', '15904714122054', '하나은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-3515-6678', 'root1234!','김채운','010-3515-6678', '2272015114962', '우리은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-4838-2972', 'root1234!','윤영헌','010-4838-2972', '2281512623481', '우리은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-5112-3496', 'root1234!','최선정','010-5112-3496', '356060995233', '신한은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-6056-5121', 'root1234!','함형주','010-6056-5121', '356060743668', '신한은행');

INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-7234-4499', 'root1234!','안유진','010-7234-4499', '15904330519247', '하나은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-8411-1056', 'root1234!','김가을','010-8411-1056', '15907152411278', '하나은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-9134-3778', 'root1234!','장원영','010-9134-3778', '2261055612854', '신한은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-1865-5221', 'root1234!','김레이','010-1865-5221', '2261081574421', '신한은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-2845-6651', 'root1234!','김지원','010-2845-6651', '356060389541', '우리은행');
INSERT INTO MEMBER (id, username, password, name, phone_number, account_number, account_bank) VALUES (uuid_generate_v4(), '010-3109=1753', 'root1234!','이현서','010-3109=1753', '356060382210', '우리은행');

INSERT INTO COMMUNITY (id, manager_id, name, account_number, credits, fee, fee_period) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '김미강'), '맛집탐방', '15902219250391', 10, 100000, 11);
INSERT INTO COMMUNITY (id, manager_id, name, account_number, credits, fee, fee_period) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '안유진'), '떠나요 여행', '15902219250392', 10, 200000, 15);

INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '김미강'), (SELECT id FROM COMMUNITY WHERE name = '맛집탐방'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '김상현'), (SELECT id FROM COMMUNITY WHERE name = '맛집탐방'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '김채운'), (SELECT id FROM COMMUNITY WHERE name = '맛집탐방'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '윤영헌'), (SELECT id FROM COMMUNITY WHERE name = '맛집탐방'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '최선정'), (SELECT id FROM COMMUNITY WHERE name = '맛집탐방'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '함형주'), (SELECT id FROM COMMUNITY WHERE name = '맛집탐방'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '안유진'), (SELECT id FROM COMMUNITY WHERE name = '떠나요 여행'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '김가을'), (SELECT id FROM COMMUNITY WHERE name = '떠나요 여행'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '장원영'), (SELECT id FROM COMMUNITY WHERE name = '떠나요 여행'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '김레이'), (SELECT id FROM COMMUNITY WHERE name = '떠나요 여행'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '김지원'), (SELECT id FROM COMMUNITY WHERE name = '떠나요 여행'));
INSERT INTO MEMBERSHIP (id, member_id, community_id) VALUES (uuid_generate_v4(), (SELECT id FROM MEMBER WHERE name = '이현서'), (SELECT id FROM COMMUNITY WHERE name = '떠나요 여행'));

INSERT INTO PLAN (id, community_id, title, start_date, end_date, category, locations, member_ids) VALUES (uuid_generate_v4(), (SELECT id FROM COMMUNITY WHERE name = '맛집탐방'), '수리고등학교 동창 강릉 여행', '2025-01-15 10:00:00', '2025-01-16 10:00:00', '여행', '["동화가든", "카페 툇마루", "경포대"]', '["김미강", "김상현", "김채운", "윤영헌", "최선정", "함형주"]' );

INSERT INTO PLAN (id, community_id, title, start_date, end_date, category, locations, member_ids) VALUES (uuid_generate_v4(), (SELECT id FROM COMMUNITY WHERE name = '맛집탐방'), '성수 나들이', '2025-01-16 10:00:00', '2025-01-17 10:00:00', '정기 모임', '["자연도소금빵", "소문난성수감자탕", "올리브영성수점"]', '["김상현", "김채운", "윤영헌"]' );
INSERT INTO PLAN (id, community_id, title, start_date, end_date, category, locations, member_ids) VALUES (uuid_generate_v4(), (SELECT id FROM COMMUNITY WHERE name = '떠나요 여행'), '다같이 부산 여행', '2025-01-20 10:00:00', '2025-01-22 10:00:00', '여행', '["태종대", "광안리해수욕장", "감천문화마을"]', '["안유진", "김가을", "장원영", "김레이", "김지원", "이현서"]');
INSERT INTO PLAN (id, community_id, title, start_date, end_date, category, locations, member_ids) VALUES (uuid_generate_v4(), (SELECT id FROM COMMUNITY WHERE name = '떠나요 여행'), '한강공원 런닝', '2025-01-24 10:00:00', '2025-01-24 10:00:00', '운동', '["뚝섬한강공원", "망원한강공원"]', '["안유진", "김가을", "장원영", "김레이"]');