ALTER TABLE passwords
ADD CONSTRAINT uq_passwords_user_title UNIQUE (user_id, title);