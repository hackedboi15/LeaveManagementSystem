-- Insert sample employees
INSERT IGNORE INTO employees (name, email, department, joining_date, annual_leave_balance) VALUES
('Rahul Sharma', 'rahul.sharma@company.com', 'IT', '2024-01-15', 24),
('Priya Patel', 'priya.patel@company.com', 'HR', '2024-02-01', 24),
('Amit Kumar', 'amit.kumar@company.com', 'FINANCE', '2024-03-10', 24),
('Sneha Gupta', 'sneha.gupta@company.com', 'MARKETING', '2024-01-20', 24),
('Vikram Singh', 'vikram.singh@company.com', 'IT', '2024-04-05', 24);

-- Insert sample leave requests
INSERT IGNORE INTO leave_requests (employee_id, start_date, end_date, days, reason, status, created_at, updated_at) VALUES
(1, '2024-12-20', '2024-12-22', 3, 'Personal work', 'PENDING', NOW(), NOW()),
(2, '2024-12-25', '2024-12-27', 3, 'Festival celebration', 'APPROVED', NOW(), NOW()),
(3, '2025-01-10', '2025-01-12', 3, 'Medical checkup', 'PENDING', NOW(), NOW());
