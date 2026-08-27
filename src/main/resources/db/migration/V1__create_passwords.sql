CREATE TABLE passwords (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL REFERENCES users (id),
    title VARCHAR(255) NOT NULL,
    secret TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
)