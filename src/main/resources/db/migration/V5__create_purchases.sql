CREATE TABLE purchases (
    id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    date DATE NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'TRY',
    note VARCHAR(1000),
    total DECIMAL(19, 2) NOT NULL,
    transaction_id BIGINT NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_purchase_supplier FOREIGN KEY (supplier_id) REFERENCES contacts(id),
    CONSTRAINT fk_purchase_account FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT fk_purchase_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE INDEX idx_purchases_supplier ON purchases(supplier_id);
CREATE INDEX idx_purchases_account ON purchases(account_id);
CREATE INDEX idx_purchases_date ON purchases(date);

CREATE TABLE purchase_lines (
    id BIGSERIAL PRIMARY KEY,
    purchase_id BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL,
    quantity DECIMAL(19, 2) NOT NULL,
    unit_price DECIMAL(19, 2) NOT NULL,
    line_total DECIMAL(19, 2) NOT NULL,
    category_id BIGINT NOT NULL,
    CONSTRAINT fk_purchase_line_purchase FOREIGN KEY (purchase_id) REFERENCES purchases(id) ON DELETE CASCADE,
    CONSTRAINT fk_purchase_line_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_purchase_lines_purchase ON purchase_lines(purchase_id);
CREATE INDEX idx_purchase_lines_category ON purchase_lines(category_id);
