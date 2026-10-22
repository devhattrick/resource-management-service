-- Developer profile governance: a permission to manage OTHERS' profiles.
-- (Owners edit their own profile via PROFILE_EDIT_OWN; skills still go through change requests.)
INSERT INTO app_permissions(code, name, description) VALUES
  ('PROFILE_MANAGE', 'Manage developer profiles', 'สร้าง/แก้ไข developer profile ของผู้อื่น');

INSERT INTO role_permissions(role_id, permission_id)
SELECT r.id, p.id
FROM app_roles r
JOIN app_permissions p ON p.code = 'PROFILE_MANAGE'
WHERE r.code IN ('ADMIN', 'RESOURCE_MANAGER');
