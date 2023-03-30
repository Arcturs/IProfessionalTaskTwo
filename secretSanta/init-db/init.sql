CREATE TABLE group_info (
    id BIGINT PRIMARY KEY GENERATED AS IDENTITY,
    name TEXT NOT NULL,
    description TEXT
);

CREATE TABLE participant (
    id BIGINT PRIMARY KEY GENERATED AS IDENTITY,
    name TEXT NOT NULL,
    wish TEXT,
    recipient BIGINT REFERENCES participant(id) ON DELETE CASCADE
);

CREATE TABLE group_participant (
    id BIGINT PRIMARY KEY GENERATED AS IDENTITY,
    group_id BIGINT REFERENCES group_info(id) ON DELETE CASCADE,
    participant_id BIGINT REFERENCES participant(id) ON DELETE CASCADE
);