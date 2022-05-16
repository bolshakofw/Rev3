CREATE TABLE user_profile
(
    uuid     UUID NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_userprofile PRIMARY KEY (uuid)
);