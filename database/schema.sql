CREATE TABLE company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    domain VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `user` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    email VARCHAR(320) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    joined_at DATETIME NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES company(id)
);

CREATE TABLE activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_by BIGINT,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NULL,
    CONSTRAINT fk_activity_company FOREIGN KEY (company_id) REFERENCES company(id),
    CONSTRAINT fk_activity_creator FOREIGN KEY (created_by) REFERENCES `user`(id)
);

CREATE TABLE `action` (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    activity_id BIGINT NULL, -- optional link to activity
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_by BIGINT,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_action_company FOREIGN KEY (company_id) REFERENCES company(id),
    CONSTRAINT fk_action_activity FOREIGN KEY (activity_id) REFERENCES activity(id),
    CONSTRAINT fk_action_creator FOREIGN KEY (created_by) REFERENCES `user`(id)
);

CREATE TABLE user_activity (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    activity_id BIGINT NOT NULL,
    joined_at DATETIME NOT NULL,
    left_at DATETIME NULL,
    role VARCHAR(50) NULL,
    CONSTRAINT fk_ua_user FOREIGN KEY (user_id) REFERENCES `user`(id),
    CONSTRAINT fk_ua_activity FOREIGN KEY (activity_id) REFERENCES activity(id)
);


CREATE TABLE user_action (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    action_id BIGINT NOT NULL,
    performed_at DATETIME NOT NULL,
    metadata JSON NULL,
    CONSTRAINT fk_ua2_user FOREIGN KEY (user_id) REFERENCES `user`(id),
    CONSTRAINT fk_ua2_action FOREIGN KEY (action_id) REFERENCES `action`(id)
);

CREATE TABLE rating (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    from_user_id BIGINT NOT NULL,
    to_user_activity_id BIGINT NOT NULL,
    score TINYINT NOT NULL,
    comment TEXT,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_rating_from FOREIGN KEY (from_user_id) REFERENCES `user`(id),
    CONSTRAINT fk_rating_to_activity FOREIGN KEY (to_user_activity_id) REFERENCES user_activity(id)
);