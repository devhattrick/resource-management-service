-- =====================================================================
-- Resource Management Platform - Initial Schema (Domain A-D)
-- PostgreSQL 16. Enums stored as varchar + CHECK. PK uuid.
-- =====================================================================

-- ============================ DOMAIN D =============================
-- Access Control
-- ===================================================================
CREATE TABLE app_users (
    id            uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    employee_code varchar(20)  NOT NULL UNIQUE,
    email         varchar(150) NOT NULL UNIQUE,
    full_name     varchar(150) NOT NULL,
    password_hash varchar(100) NOT NULL,
    is_active     boolean      NOT NULL DEFAULT true,
    created_at    timestamptz  NOT NULL DEFAULT now(),
    updated_at    timestamptz  NOT NULL DEFAULT now(),
    version       bigint       NOT NULL DEFAULT 0
);

CREATE TABLE app_roles (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code        varchar(30)  NOT NULL UNIQUE,
    name        varchar(100) NOT NULL,
    description text,
    is_active   boolean      NOT NULL DEFAULT true
);

CREATE TABLE app_permissions (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code        varchar(50)  NOT NULL UNIQUE,
    name        varchar(100) NOT NULL,
    description text
);

CREATE TABLE user_roles (
    user_id uuid NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    role_id uuid NOT NULL REFERENCES app_roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE role_permissions (
    role_id       uuid NOT NULL REFERENCES app_roles(id) ON DELETE CASCADE,
    permission_id uuid NOT NULL REFERENCES app_permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- ===================== MASTER DATA (Domain A) ======================
CREATE TABLE job_roles (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code        varchar(30)  NOT NULL UNIQUE,
    name        varchar(100) NOT NULL,
    description text,
    is_active   boolean      NOT NULL DEFAULT true
);

CREATE TABLE skill_categories (
    id         uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code       varchar(30)  NOT NULL UNIQUE,
    name       varchar(100) NOT NULL,
    sort_order smallint     NOT NULL DEFAULT 0,
    is_active  boolean      NOT NULL DEFAULT true
);

CREATE TABLE skills (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    category_id uuid         NOT NULL REFERENCES skill_categories(id),
    code        varchar(50)  NOT NULL UNIQUE,
    name        varchar(100) NOT NULL,
    description text,
    is_active   boolean      NOT NULL DEFAULT true
);
CREATE INDEX idx_skills_category ON skills(category_id);

CREATE TABLE languages (
    id        uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code      varchar(10) NOT NULL UNIQUE,
    name      varchar(50) NOT NULL,
    is_active boolean     NOT NULL DEFAULT true
);

CREATE TABLE proficiency_levels (
    level       smallint PRIMARY KEY CHECK (level BETWEEN 1 AND 5),
    name        varchar(30) NOT NULL,
    description text
);

-- ===================== DEVELOPER (Domain A) ========================
CREATE TABLE developer_profiles (
    id                     uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id                uuid        NOT NULL UNIQUE REFERENCES app_users(id),
    seniority              varchar(20) CHECK (seniority IN ('JUNIOR','MIDDLE','SENIOR','LEAD')),
    total_experience_years numeric(4,1) NOT NULL DEFAULT 0,
    capacity_percent       smallint    NOT NULL DEFAULT 100 CHECK (capacity_percent BETWEEN 0 AND 100),
    profile_status         varchar(20) NOT NULL DEFAULT 'ACTIVE' CHECK (profile_status IN ('ACTIVE','INACTIVE')),
    note                   text,
    created_at             timestamptz NOT NULL DEFAULT now(),
    updated_at             timestamptz NOT NULL DEFAULT now(),
    version                bigint      NOT NULL DEFAULT 0
);

CREATE TABLE developer_roles (
    id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    developer_id uuid    NOT NULL REFERENCES developer_profiles(id) ON DELETE CASCADE,
    job_role_id  uuid    NOT NULL REFERENCES job_roles(id),
    is_primary   boolean NOT NULL DEFAULT false,
    UNIQUE (developer_id, job_role_id)
);

CREATE TABLE developer_languages (
    id              uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    developer_id    uuid     NOT NULL REFERENCES developer_profiles(id) ON DELETE CASCADE,
    language_id     uuid     NOT NULL REFERENCES languages(id),
    listening_level smallint NOT NULL CHECK (listening_level BETWEEN 1 AND 5),
    speaking_level  smallint NOT NULL CHECK (speaking_level  BETWEEN 1 AND 5),
    reading_level   smallint NOT NULL CHECK (reading_level   BETWEEN 1 AND 5),
    writing_level   smallint NOT NULL CHECK (writing_level   BETWEEN 1 AND 5),
    UNIQUE (developer_id, language_id)
);

-- ===================== GOVERNANCE (Domain B) =======================
CREATE TABLE change_requests (
    id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    developer_id   uuid        NOT NULL REFERENCES developer_profiles(id),
    requested_by   uuid        NOT NULL REFERENCES app_users(id),
    request_type   varchar(20) NOT NULL DEFAULT 'SKILL_CHANGE' CHECK (request_type IN ('SKILL_CHANGE')),
    status         varchar(20) NOT NULL DEFAULT 'DRAFT'
                     CHECK (status IN ('DRAFT','SUBMITTED','APPROVED','REJECTED','CANCELLED')),
    note           text,
    reviewed_by    uuid        REFERENCES app_users(id),
    reviewed_at    timestamptz,
    review_comment text,
    submitted_at   timestamptz,
    created_at     timestamptz NOT NULL DEFAULT now(),
    updated_at     timestamptz NOT NULL DEFAULT now(),
    version        bigint      NOT NULL DEFAULT 0
);
CREATE INDEX idx_cr_developer ON change_requests(developer_id);
CREATE INDEX idx_cr_status    ON change_requests(status);

CREATE TABLE change_request_items (
    id                uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    change_request_id uuid        NOT NULL REFERENCES change_requests(id) ON DELETE CASCADE,
    target_type       varchar(20) NOT NULL CHECK (target_type IN ('SKILL','LANGUAGE','ROLE')),
    action            varchar(10) NOT NULL CHECK (action IN ('ADD','UPDATE','REMOVE')),
    target_ref_id     uuid,
    payload           jsonb       NOT NULL DEFAULT '{}'::jsonb,
    item_status       varchar(20) NOT NULL DEFAULT 'PENDING' CHECK (item_status IN ('PENDING','APPROVED','REJECTED')),
    created_at        timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX idx_cri_request ON change_request_items(change_request_id);

-- Approved skills (values pending approval live in change_request_items)
CREATE TABLE developer_skills (
    id                       uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    developer_id             uuid         NOT NULL REFERENCES developer_profiles(id) ON DELETE CASCADE,
    skill_id                 uuid         NOT NULL REFERENCES skills(id),
    proficiency_level        smallint     NOT NULL CHECK (proficiency_level BETWEEN 1 AND 5),
    is_main_stack            boolean      NOT NULL DEFAULT false,
    years_of_experience      numeric(4,1),
    last_used_date           date,
    approved_by              uuid         REFERENCES app_users(id),
    approved_at              timestamptz,
    source_change_request_id uuid         REFERENCES change_requests(id),
    created_at               timestamptz  NOT NULL DEFAULT now(),
    updated_at               timestamptz  NOT NULL DEFAULT now(),
    version                  bigint       NOT NULL DEFAULT 0,
    UNIQUE (developer_id, skill_id)
);
CREATE INDEX idx_devskill_skill ON developer_skills(skill_id, proficiency_level);

-- ===================== PROJECT (Domain C) ==========================
CREATE TABLE projects (
    id          uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    code        varchar(30)  NOT NULL UNIQUE,
    name        varchar(150) NOT NULL,
    description text,
    status      varchar(20)  NOT NULL DEFAULT 'DRAFT'
                  CHECK (status IN ('DRAFT','OPEN','STAFFED','IN_PROGRESS','CLOSED')),
    start_date  date,
    end_date    date,
    created_by  uuid         REFERENCES app_users(id),
    created_at  timestamptz  NOT NULL DEFAULT now(),
    updated_at  timestamptz  NOT NULL DEFAULT now(),
    version     bigint       NOT NULL DEFAULT 0,
    CHECK (end_date IS NULL OR start_date IS NULL OR end_date >= start_date)
);

CREATE TABLE project_skill_requirements (
    id                    uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id            uuid         NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    skill_id              uuid         NOT NULL REFERENCES skills(id),
    min_proficiency_level smallint     NOT NULL CHECK (min_proficiency_level BETWEEN 1 AND 5),
    is_mandatory          boolean      NOT NULL DEFAULT true,
    weight                numeric(4,2) NOT NULL DEFAULT 1.0,
    UNIQUE (project_id, skill_id)
);

CREATE TABLE project_role_requirements (
    id             uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id     uuid         NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    job_role_id    uuid         NOT NULL REFERENCES job_roles(id),
    required_count smallint     NOT NULL DEFAULT 1 CHECK (required_count >= 1),
    min_seniority  varchar(20)  CHECK (min_seniority IN ('JUNIOR','MIDDLE','SENIOR','LEAD')),
    is_mandatory   boolean      NOT NULL DEFAULT true,
    weight         numeric(4,2) NOT NULL DEFAULT 1.0,
    UNIQUE (project_id, job_role_id)
);

CREATE TABLE project_language_requirements (
    id           uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id   uuid         NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    language_id  uuid         NOT NULL REFERENCES languages(id),
    min_level    smallint     NOT NULL CHECK (min_level BETWEEN 1 AND 5),
    is_mandatory boolean      NOT NULL DEFAULT false,
    weight       numeric(4,2) NOT NULL DEFAULT 1.0,
    UNIQUE (project_id, language_id)
);

CREATE TABLE project_assignments (
    id                 uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id         uuid        NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    developer_id       uuid        NOT NULL REFERENCES developer_profiles(id),
    job_role_id        uuid        REFERENCES job_roles(id),
    allocation_percent smallint    NOT NULL CHECK (allocation_percent BETWEEN 1 AND 100),
    start_date         date        NOT NULL,
    end_date           date        NOT NULL,
    status             varchar(20) NOT NULL DEFAULT 'PLANNED'
                         CHECK (status IN ('PLANNED','ACTIVE','COMPLETED','CANCELLED')),
    assigned_by        uuid        REFERENCES app_users(id),
    created_at         timestamptz NOT NULL DEFAULT now(),
    updated_at         timestamptz NOT NULL DEFAULT now(),
    version            bigint      NOT NULL DEFAULT 0,
    CHECK (end_date >= start_date)
);
CREATE INDEX idx_assign_availability ON project_assignments(developer_id, status, start_date, end_date);
CREATE INDEX idx_assign_project      ON project_assignments(project_id);

-- ===================== AUDIT (Domain B) ============================
CREATE TABLE audit_events (
    id            bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_type    varchar(50) NOT NULL,
    entity_type   varchar(50) NOT NULL,
    entity_id     uuid        NOT NULL,
    actor_user_id uuid        REFERENCES app_users(id),
    before_data   jsonb,
    after_data    jsonb,
    occurred_at   timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX idx_audit_entity ON audit_events(entity_type, entity_id);

-- =====================================================================
-- SEED DATA
-- =====================================================================
INSERT INTO proficiency_levels(level, name, description) VALUES
  (1,'Novice','เพิ่งเริ่ม ต้องมีคนช่วย'),
  (2,'Beginner','ทำงานพื้นฐานได้ภายใต้การ review'),
  (3,'Competent','ทำงานทั่วไปได้ด้วยตนเอง'),
  (4,'Proficient','ทำงานซับซ้อนได้ และช่วย review ผู้อื่น'),
  (5,'Expert','เชี่ยวชาญ กำหนดมาตรฐาน/สอนผู้อื่นได้');

INSERT INTO app_roles(code, name, description) VALUES
  ('ADMIN','Administrator','ผู้ดูแลระบบ'),
  ('RESOURCE_MANAGER','Resource Manager','อนุมัติ skill และจัดสรร resource'),
  ('DEVELOPER','Developer','ผู้ใช้งานทั่วไป ยื่นคำขอแก้ไข profile ตนเอง');

INSERT INTO app_permissions(code, name, description) VALUES
  ('SKILL_APPROVE','Approve skill change','อนุมัติ/ปฏิเสธ change request'),
  ('PROJECT_MANAGE','Manage projects','สร้าง/แก้ไข project และ requirement'),
  ('ASSIGNMENT_MANAGE','Manage assignments','จัดสรร dev เข้า project'),
  ('MASTER_MANAGE','Manage master data','จัดการ skill/role/language/category'),
  ('MATCHING_VIEW','View matching','ค้นหา dev ที่เหมาะกับ project'),
  ('PROFILE_EDIT_OWN','Edit own profile','ยื่นคำขอแก้ไข profile ตนเอง');

-- ADMIN -> all permissions
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM app_roles r CROSS JOIN app_permissions p WHERE r.code = 'ADMIN';

-- RESOURCE_MANAGER
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM app_roles r JOIN app_permissions p
  ON p.code IN ('SKILL_APPROVE','PROJECT_MANAGE','ASSIGNMENT_MANAGE','MASTER_MANAGE','MATCHING_VIEW')
WHERE r.code = 'RESOURCE_MANAGER';

-- DEVELOPER
INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id FROM app_roles r JOIN app_permissions p
  ON p.code IN ('PROFILE_EDIT_OWN')
WHERE r.code = 'DEVELOPER';
