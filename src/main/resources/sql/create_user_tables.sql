CREATE TABLE `user` (
  user_id            INT          NOT NULL AUTO_INCREMENT,
  email              VARCHAR(256) NOT NULL,
  password           VARCHAR(256),
  role               VARCHAR(50)  NOT NULL DEFAULT 'ROLE_USER',
  created_date       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_modified_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id),
  UNIQUE KEY uq_email (email)
);

CREATE TABLE `user_oauth` (
  oauth_id         INT          NOT NULL AUTO_INCREMENT,
  user_id          INT          NOT NULL,
  provider         VARCHAR(50)  NOT NULL,
  provider_user_id VARCHAR(256) NOT NULL,
  PRIMARY KEY (oauth_id),
  UNIQUE KEY uq_provider (provider, provider_user_id),
  FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);