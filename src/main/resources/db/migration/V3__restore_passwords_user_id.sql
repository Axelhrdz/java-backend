-- V2 removed ownership information; existing rows have no recoverable owner.
-- New passwords receive the authenticated user's ID from the application.
ALTER TABLE passwords
    ADD COLUMN user_id VARCHAR(36) REFERENCES users (id);
