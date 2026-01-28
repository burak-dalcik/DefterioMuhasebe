CREATE TABLE budgets (
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL,
    year INT NOT NULL,
    month INT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_budget_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT uq_budget_category_year_month UNIQUE (category_id, year, month)
);

CREATE INDEX idx_budgets_category ON budgets(category_id);
CREATE INDEX idx_budgets_year_month ON budgets(year, month);

