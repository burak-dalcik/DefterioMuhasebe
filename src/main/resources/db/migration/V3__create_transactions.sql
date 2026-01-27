CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    subtype VARCHAR(50) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL DEFAULT 'TRY',
    date DATE NOT NULL,
    description VARCHAR(500),
    category_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    contact_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_transaction_category FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transaction_contact FOREIGN KEY (contact_id) REFERENCES contacts(id)
);

CREATE INDEX idx_transactions_type ON transactions(type);
CREATE INDEX idx_transactions_subtype ON transactions(subtype);
CREATE INDEX idx_transactions_date ON transactions(date);
CREATE INDEX idx_transactions_category ON transactions(category_id);
CREATE INDEX idx_transactions_account ON transactions(account_id);
CREATE INDEX idx_transactions_contact ON transactions(contact_id);
