
INSERT INTO user_info (role, email, password, created_at, updated_at) VALUES
('ROLE_ADMIN', 'admin@test.com', 'password123', NOW(), NOW()),
('ROLE_USER', 'user1@test.com', 'password123', NOW(), NOW()),
('ROLE_USER', 'user2@test.com', 'password123', NOW(), NOW()),
('ROLE_USER', 'user3@test.com', 'password123', NOW(), NOW());


INSERT INTO task (title, description, status, priority, author_id, performer_id, created_at, updated_at) VALUES
('Task 1', 'Description for Task 1', 'IN_PROGRESS', 'HIGH', 1, 2, NOW(), NOW()),
('Task 2', 'Description for Task 2', 'COMPLETED', 'MEDIUM', 1, 3, NOW(), NOW()),
('Task 3', 'Description for Task 3', 'PENDING', 'LOW', 1, 4, NOW(), NOW()),
('Task 4', 'Description for Task 4', 'IN_PROGRESS', 'MEDIUM', 1, 3, NOW(), NOW());


INSERT INTO comment (text, author_id, task_id, created_at, updated_at) VALUES
('This is a comment on Task 1 by User 2', 2, 1, NOW(), NOW()),
('Another comment on Task 1 by User 3', 1, 1, NOW(), NOW()),
('Comment on Task 2 by User 3', 3, 2, NOW(), NOW()),
('Comment on Task 3 by User 4', 4, 3, NOW(), NOW()),
('Additional comment on Task 4 by User 3', 3, 4, NOW(), NOW());
