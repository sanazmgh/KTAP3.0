DROP DATABASE `ServerDatabase`;

CREATE DATABASE `ServerDatabase`;

CREATE TABLE `ServerDatabase`.`users`
(
    `id`                        BIGINT              NOT NULL        AUTO_INCREMENT,
    `name`                      VARCHAR(64)         NOT NULL,
    `lastName`                  VARCHAR(64)         NOT NULL,
    `username`                  VARCHAR(64)         NOT NULL,
    `password`                  VARCHAR(64)         NOT NULL,
    `bio`                       VARCHAR(64)         NOT NULL,
    `email`                     VARCHAR(64)         NOT NULL,
    `visibleEmail`              BOOL                NOT NULL,
    `phone`                     VARCHAR(64),
    `visiblePhone`              VARCHAR(64),
    `dateOfBirth`               BIGINT,
    `visibleDateOfBirth`        BOOL                NOT NULL,
    `lastSeen`                  BIGINT              NOT NULL,
    `lastSeenMode`              INT                 NOT NULL,
    `isActive`                  BOOL                NOT NULL,
    `private`                   BOOL                NOT NULL,
    `imageSTR`                  LONGTEXT            NOT NULL,
    `messengerID`               INT                 NOT NULL,
    `tweets`                    JSON                NOT NULL,
    `likedTweets`               JSON                NOT NULL,
    `requested`                 JSON                NOT NULL,
    `followers`                 JSON                NOT NULL,
    `followings`                JSON                NOT NULL,
    `blocked`                   JSON                NOT NULL,
    `muted`                     JSON                NOT NULL,
    `systemNotifications`       JSON                NOT NULL,
    `requestsNotifications`     JSON                NOT NULL,
    `deleted`                   BOOL                NOT NULL,

    PRIMARY KEY (`id`)
);

CREATE TABLE `ServerDatabase`.`tweets`
(
    `id`                        BIGINT      NOT NULL    AUTO_INCREMENT,
    `userID`                    BIGINT      NOT NULL,
    `retweetFromUserId`         BIGINT      NOT NULL,
    `tweetText`                 LONGTEXT    NOT NULL,
    `tweetImageSTR`             LONGTEXT    NOT NULL,
    `dateTweeted`               BIGINT      NOT NULL,
    `commented`                 BOOL        NOT NULL,
    `noOfReports`               INT         NOT NULL,
    `comments`                  JSON        NOT NULL,
    `likes`                     JSON        NOT NULL,
    `retweets`                  JSON        NOT NULL,
    `deleted`                   BOOL        NOT NULL,

    PRIMARY KEY (`id`),
    FOREIGN KEY (`userID`) REFERENCES users (`id`)
);

CREATE TABLE `ServerDatabase`.`notifications`
(
    `notifID`          BIGINT      NOT NULL   AUTO_INCREMENT,
    `senderID`         BIGINT      NOT NULL,
    `text`             LONGTEXT    NOT NULL,

    PRIMARY KEY (`notifID`),
    FOREIGN KEY (`senderID`) REFERENCES users (`id`)
);

CREATE TABLE `ServerDatabase`.`messengers`
(
    `messengerID`      BIGINT      NOT NULL   AUTO_INCREMENT,
    `userId`           BIGINT      NOT NULL,
    `groups`           JSON        NOT NULL,
    `lists`            JSON        NOT NULL,
    `savedMessages`    BIGINT      NOT NULL,

    PRIMARY KEY (`messengerID`),
    FOREIGN KEY (`userId`) REFERENCES users (`id`)
);

CREATE TABLE `ServerDatabase`.`messages`
(
    `messageID`          BIGINT      NOT NULL   AUTO_INCREMENT,
    `senderId`           BIGINT      NOT NULL,
    `senderImageSTR`     LONGTEXT    NOT NULL,
    `text`               LONGTEXT    NOT NULL,
    `extraInfo`          LONGTEXT    NOT NULL,
    `forwarded`          BOOL        NOT NULL,
    `imageSTR`           LONGTEXT    NOT NULL,
    `deleted`            BOOL        NOT NULL,
    `status`             INT         NOT NULL,
    `schedule`           BIGINT      NOT NULL,

    PRIMARY KEY (`messageID`),
    FOREIGN KEY (`senderId`) REFERENCES users (`id`)
);

CREATE TABLE `ServerDatabase`.`lists`
(
    `listID`      BIGINT        NOT NULL   AUTO_INCREMENT,
    `name`        VARCHAR(64)   NOT NULL,
    `members`     JSON          NOT NULL,

    PRIMARY KEY (`listID`)
);

CREATE TABLE `ServerDatabase`.`groups`
(
    `groupID`               BIGINT        NOT NULL   AUTO_INCREMENT,
    `name`                  VARCHAR(64)   NOT NULL,
    `members`               JSON          NOT NULL,
    `messages`              JSON          NOT NULL,
    `scheduledMessages`     JSON          NOT NULL,

    PRIMARY KEY (`groupID`)
);