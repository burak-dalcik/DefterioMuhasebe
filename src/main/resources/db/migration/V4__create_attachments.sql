CREATE TABLE attachments (
    id BIGSERIAL PRIMARY KEY,
    owner_type VARCHAR(50) NOT NULL,
    owner_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size BIGINT NOT NULL,
    storage_key VARCHAR(500) NOT NULL UNIQUE,
    uploaded_at TIMESTAMP NOT NULL,
    uploaded_by VARCHAR(255) NOT NULL
);

CREATE INDEX idx_attachments_owner ON attachments(owner_type, owner_id);
