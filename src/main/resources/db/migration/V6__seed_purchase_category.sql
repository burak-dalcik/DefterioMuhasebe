INSERT INTO categories (type, name, created_at, updated_at)
SELECT 'EXPENSE', 'Alım (Purchase)', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (
    SELECT 1 FROM categories WHERE type = 'EXPENSE' AND name = 'Alım (Purchase)'
);
