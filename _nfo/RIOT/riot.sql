-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 22. Jun 2016 um 18:43
-- Server-Version: 10.1.10-MariaDB
-- PHP-Version: 7.0.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- NEU (ungetestet)
CREATE USER IF NOT EXISTS 'riot'@'localhost' IDENTIFIED BY 'riot';

--
-- Datenbank: `riot`
--
CREATE DATABASE IF NOT EXISTS `riot` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
-- NEU (ungetestet)
GRANT ALL PRIVILEGES ON riot.* TO 'riot'@'%' WITH GRANT OPTION;

USE `riot`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `calendarentries`
--

DROP TABLE IF EXISTS `calendarentries`;
CREATE TABLE `calendarentries` (
  `id` int(11) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime DEFAULT NULL,
  `allDayEvent` bit(1) NOT NULL,
  `description` text,
  `location` varchar(200) DEFAULT NULL,
  `title` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `calendarentries`
--

TRUNCATE TABLE `calendarentries`;
--
-- Daten für Tabelle `calendarentries`
--

INSERT INTO `calendarentries` (`id`, `startTime`, `endTime`, `allDayEvent`, `description`, `location`, `title`) VALUES
(1, '2015-01-19 10:00:00', '2015-01-19 11:00:00', b'0', 'Description 1', 'Vaihingen', 'Standup Meeting 1'),
(2, '2015-02-19 10:00:00', '2015-02-19 11:00:00', b'0', 'Description 2', 'Vaihingen', 'Standup Meeting 2'),
(3, '2015-03-19 10:00:00', '2015-03-19 11:00:00', b'0', 'Description 3', 'Vaihingen', 'Standup Meeting 3');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `configuration`
--

DROP TABLE IF EXISTS `configuration`;
CREATE TABLE `configuration` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `configKey` varchar(100) NOT NULL,
  `configValue` text NOT NULL,
  `dataType` varchar(16) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `configuration`
--

TRUNCATE TABLE `configuration`;
--
-- Daten für Tabelle `configuration`
--

INSERT INTO `configuration` (`id`, `configKey`, `configValue`, `dataType`) VALUES
(1, 'um_hashIterations', '200000', 'Integer'),
(2, 'um_pwValidator_minLength', '6', 'Integer'),
(3, 'um_pwValidator_maxLength', '20', 'Integer'),
(4, 'um_pwValidator_numberCount', '1', 'Integer'),
(5, 'um_pwValidator_specialCharsCount', '1', 'Integer'),
(6, 'um_pwValidator_lowerCaseCharsCount', '1', 'Integer'),
(7, 'um_pwValidator_upperCaseCharCount', '1', 'Integer'),
(8, 'um_pwValidator_allowedSpecialChars', '"][?\\/<~#`''!@$%^&§*()+-=}"|:;,>{"', 'String'),
(9, 'um_maxLoginRetries', '5', 'Integer'),
(10, 'um_authTokenValidTime', '10800', 'Integer');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `contacts`
--

DROP TABLE IF EXISTS `contacts`;
CREATE TABLE `contacts` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `firstName` varchar(50) NOT NULL,
  `lastName` varchar(50) NOT NULL,
  `email` varchar(256) DEFAULT NULL,
  `phoneNumber` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `contacts`
--

TRUNCATE TABLE `contacts`;
--
-- Daten für Tabelle `contacts`
--

INSERT INTO `contacts` (`id`, `firstName`, `lastName`, `email`, `phoneNumber`) VALUES
(1, 'John', 'Doe', 'john.doe@example.com', '+4915177777777'),
(2, 'Mary', 'Doe', 'mary.doe@example.com', '+4915188888888'),
(3, 'Hans', 'Doe', 'hans.doe@example.com', '+4915188888889');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `notifications`
--

DROP TABLE IF EXISTS `notifications`;
CREATE TABLE `notifications` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `userID` bigint(20) UNSIGNED NOT NULL,
  `thingID` bigint(20) UNSIGNED DEFAULT NULL,
  `name` varchar(30) NOT NULL,
  `severity` varchar(30) NOT NULL,
  `titleKey` varchar(128) NOT NULL,
  `messageKey` varchar(128) NOT NULL,
  `time` datetime NOT NULL,
  `dismissed` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `notifications`
--

TRUNCATE TABLE `notifications`;
-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `notification_arguments`
--

DROP TABLE IF EXISTS `notification_arguments`;
CREATE TABLE `notification_arguments` (
  `notificationID` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(128) NOT NULL,
  `val` varchar(512) NOT NULL,
  `valType` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `notification_arguments`
--

TRUNCATE TABLE `notification_arguments`;
-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `permissions`
--

DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `permissionValue` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `permissions`
--

TRUNCATE TABLE `permissions`;
--
-- Daten für Tabelle `permissions`
--

INSERT INTO `permissions` (`id`, `permissionValue`) VALUES
(4, 'thing:*'),
(1, 'thing:1:*'),
(7, 'thing:2:*'),
(3, 'thing:3:read'),
(2, 'thing:create'),
(5, 'x'),
(6, 'y');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `propertyvalues`
--

DROP TABLE IF EXISTS `propertyvalues`;
CREATE TABLE `propertyvalues` (
  `thingID` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(128) NOT NULL,
  `val` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `propertyvalues`
--

TRUNCATE TABLE `propertyvalues`;
-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `roles`
--

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `roleName` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `roles`
--

TRUNCATE TABLE `roles`;
--
-- Daten für Tabelle `roles`
--

INSERT INTO `roles` (`id`, `roleName`) VALUES
(4, 'admin'),
(3, 'Good'),
(1, 'Master'),
(2, 'Robot');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `roles_permissions`
--

DROP TABLE IF EXISTS `roles_permissions`;
CREATE TABLE `roles_permissions` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `permissionID` bigint(20) UNSIGNED NOT NULL,
  `roleID` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `roles_permissions`
--

TRUNCATE TABLE `roles_permissions`;
--
-- Daten für Tabelle `roles_permissions`
--

INSERT INTO `roles_permissions` (`id`, `permissionID`, `roleID`) VALUES
(1, 1, 1),
(4, 2, 4),
(3, 3, 3),
(2, 5, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ruleparameters`
--

DROP TABLE IF EXISTS `ruleparameters`;
CREATE TABLE `ruleparameters` (
  `ruleID` bigint(20) UNSIGNED NOT NULL,
  `name` varchar(128) NOT NULL,
  `val` varchar(256) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `ruleparameters`
--

TRUNCATE TABLE `ruleparameters`;
-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `rules`
--

DROP TABLE IF EXISTS `rules`;
CREATE TABLE `rules` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `type` varchar(256) NOT NULL,
  `ownerID` bigint(20) DEFAULT NULL,
  `name` varchar(256) NOT NULL,
  `status` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `rules`
--

TRUNCATE TABLE `rules`;
-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `things`
--

DROP TABLE IF EXISTS `things`;
CREATE TABLE `things` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `type` varchar(256) NOT NULL,
  `ownerID` bigint(20) DEFAULT NULL,
  `name` varchar(256) DEFAULT NULL,
  `parentID` bigint(20) UNSIGNED DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `things`
--

TRUNCATE TABLE `things`;
--
-- Daten für Tabelle `things`
--

INSERT INTO `things` (`id`, `type`, `ownerID`, `name`, `parentID`) VALUES
(1, 'de.uni_stuttgart.riot.thing.house.coffeemachine.CoffeeMachine', 1, 'Coffemachine', NULL);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `things_users`
--

DROP TABLE IF EXISTS `things_users`;
CREATE TABLE `things_users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `thingID` bigint(20) UNSIGNED NOT NULL,
  `userID` bigint(20) UNSIGNED NOT NULL,
  `canRead` tinyint(1) NOT NULL DEFAULT '0',
  `canControl` tinyint(1) NOT NULL DEFAULT '0',
  `canExecute` tinyint(1) NOT NULL DEFAULT '0',
  `canDelete` tinyint(1) NOT NULL DEFAULT '0',
  `canShare` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `things_users`
--

TRUNCATE TABLE `things_users`;
--
-- Daten für Tabelle `things_users`
--

INSERT INTO `things_users` (`id`, `thingID`, `userID`, `canRead`, `canControl`, `canExecute`, `canDelete`, `canShare`) VALUES
(1, 1, 1, 1, 1, 1, 1, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tokens`
--

DROP TABLE IF EXISTS `tokens`;
CREATE TABLE `tokens` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `userID` bigint(20) UNSIGNED NOT NULL,
  `tokenValue` varchar(100) NOT NULL,
  `refreshtokenValue` varchar(100) NOT NULL,
  `valid` tinyint(1) NOT NULL,
  `issueTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expirationTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `tokens`
--

TRUNCATE TABLE `tokens`;
--
-- Daten für Tabelle `tokens`
--

INSERT INTO `tokens` (`id`, `userID`, `tokenValue`, `refreshtokenValue`, `valid`, `issueTime`, `expirationTime`) VALUES
(1, 1, 'token1', 'token1R', 1, '2016-06-20 08:15:07', '2024-10-19 08:23:54'),
(2, 2, 'token2', 'token2R', 1, '2016-06-20 08:15:07', '2024-10-19 08:23:54'),
(3, 3, 'token3', 'token3R', 0, '2016-06-20 08:15:07', '2024-10-19 08:23:54'),
(4, 1, '6gqkcd4u5caaskgfsuuh57ea2', 'ls7cpv0522sgo7tai0e1rr754q', 1, '2016-06-22 16:23:40', '2016-06-22 19:40:20');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tokens_roles`
--

DROP TABLE IF EXISTS `tokens_roles`;
CREATE TABLE `tokens_roles` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `tokenID` bigint(20) UNSIGNED NOT NULL,
  `roleID` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `tokens_roles`
--

TRUNCATE TABLE `tokens_roles`;
--
-- Daten für Tabelle `tokens_roles`
--

INSERT INTO `tokens_roles` (`id`, `tokenID`, `roleID`) VALUES
(1, 1, 1),
(4, 1, 3),
(2, 2, 2),
(3, 3, 4),
(5, 4, 4);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(50) NOT NULL,
  `hashedPassword` varchar(256) NOT NULL,
  `passwordSalt` varchar(256) NOT NULL,
  `hashIterations` int(11) NOT NULL,
  `loginAttemptCount` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `users`
--

TRUNCATE TABLE `users`;
--
-- Daten für Tabelle `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `hashedPassword`, `passwordSalt`, `hashIterations`, `loginAttemptCount`) VALUES
(1, 'Yoda', 'yoda@force.org', 'yPYMjqXzWOPKaAKNJXfEw7Gu3EnckZmoWUuEhOqz/7IqGd4Ub+3/X3uANlO0mkIOqIMhxhUi/ieU1KZt2BK+eg==', '108bgacl42gihhrdlfcm8e6ls6gm2q45q00boauiv5kkduf1ainv', 200000, 0),
(2, 'R2D2', 'r2d2@rob.org', 'xGWcc3nOCCgWwtRoo/2pu2/nynq1wCGuDKz8SwDtbWI+Jmv4UHNEFOK0RFLxfWUEfCsRfgspbKKdfMxY6/Ndwg==', '3uuc05ili6e8j18lqehv1piugel2bteviv6m351q1rvnn7e49gk', 200000, 0),
(3, 'Vader', 'vader@sith.org', 'catB93X7ygpJ1NjjFQlgXicAc1JUNGiZeZ0OOL95gsR8xZXLaer3EY/IXtDKFCL9Ye6RZfaILPF6FINQcgNpEg==', 're46m591d4el6t3d9ljq52itve8ml7jmf5c6i1pniuie6qqe0t', 200000, 0);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users_permissions`
--

DROP TABLE IF EXISTS `users_permissions`;
CREATE TABLE `users_permissions` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `userID` bigint(20) UNSIGNED NOT NULL,
  `permissionID` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `users_permissions`
--

TRUNCATE TABLE `users_permissions`;
--
-- Daten für Tabelle `users_permissions`
--

INSERT INTO `users_permissions` (`id`, `userID`, `permissionID`) VALUES
(2, 1, 2),
(1, 1, 4),
(4, 1, 7),
(3, 3, 3);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `users_roles`
--

DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles` (
  `id` bigint(20) UNSIGNED NOT NULL,
  `userID` bigint(20) UNSIGNED NOT NULL,
  `roleID` bigint(20) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- TRUNCATE Tabelle vor dem Einfügen `users_roles`
--

TRUNCATE TABLE `users_roles`;
--
-- Daten für Tabelle `users_roles`
--

INSERT INTO `users_roles` (`id`, `userID`, `roleID`) VALUES
(1, 1, 4),
(3, 2, 2),
(2, 3, 1);

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `calendarentries`
--
ALTER TABLE `calendarentries`
  ADD PRIMARY KEY (`id`);

--
-- Indizes für die Tabelle `configuration`
--
ALTER TABLE `configuration`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `configKey` (`configKey`);

--
-- Indizes für die Tabelle `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indizes für die Tabelle `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `thingID` (`thingID`),
  ADD KEY `userID` (`userID`);

--
-- Indizes für die Tabelle `notification_arguments`
--
ALTER TABLE `notification_arguments`
  ADD PRIMARY KEY (`notificationID`,`name`);

--
-- Indizes für die Tabelle `permissions`
--
ALTER TABLE `permissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `permissionValue` (`permissionValue`);

--
-- Indizes für die Tabelle `propertyvalues`
--
ALTER TABLE `propertyvalues`
  ADD PRIMARY KEY (`thingID`,`name`);

--
-- Indizes für die Tabelle `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `roleName` (`roleName`);

--
-- Indizes für die Tabelle `roles_permissions`
--
ALTER TABLE `roles_permissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `permissionID` (`permissionID`,`roleID`),
  ADD KEY `roleID` (`roleID`);

--
-- Indizes für die Tabelle `ruleparameters`
--
ALTER TABLE `ruleparameters`
  ADD PRIMARY KEY (`ruleID`,`name`);

--
-- Indizes für die Tabelle `rules`
--
ALTER TABLE `rules`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indizes für die Tabelle `things`
--
ALTER TABLE `things`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `parentID` (`parentID`);

--
-- Indizes für die Tabelle `things_users`
--
ALTER TABLE `things_users`
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `thingID` (`thingID`),
  ADD KEY `userID` (`userID`);

--
-- Indizes für die Tabelle `tokens`
--
ALTER TABLE `tokens`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `tokenValue` (`tokenValue`),
  ADD UNIQUE KEY `refreshtokenValue` (`refreshtokenValue`),
  ADD KEY `userID` (`userID`);

--
-- Indizes für die Tabelle `tokens_roles`
--
ALTER TABLE `tokens_roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `tokenID` (`tokenID`,`roleID`),
  ADD KEY `roleID` (`roleID`);

--
-- Indizes für die Tabelle `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indizes für die Tabelle `users_permissions`
--
ALTER TABLE `users_permissions`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `userID` (`userID`,`permissionID`),
  ADD KEY `permissionID` (`permissionID`);

--
-- Indizes für die Tabelle `users_roles`
--
ALTER TABLE `users_roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`),
  ADD UNIQUE KEY `userID` (`userID`,`roleID`),
  ADD KEY `roleID` (`roleID`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `calendarentries`
--
ALTER TABLE `calendarentries`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `configuration`
--
ALTER TABLE `configuration`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- AUTO_INCREMENT für Tabelle `contacts`
--
ALTER TABLE `contacts`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `permissions`
--
ALTER TABLE `permissions`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT für Tabelle `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT für Tabelle `roles_permissions`
--
ALTER TABLE `roles_permissions`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT für Tabelle `rules`
--
ALTER TABLE `rules`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT für Tabelle `things`
--
ALTER TABLE `things`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `things_users`
--
ALTER TABLE `things_users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT für Tabelle `tokens`
--
ALTER TABLE `tokens`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT für Tabelle `tokens_roles`
--
ALTER TABLE `tokens_roles`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT für Tabelle `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT für Tabelle `users_permissions`
--
ALTER TABLE `users_permissions`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT für Tabelle `users_roles`
--
ALTER TABLE `users_roles`
  MODIFY `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `notifications_ibfk_1` FOREIGN KEY (`thingID`) REFERENCES `things` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `notifications_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `notification_arguments`
--
ALTER TABLE `notification_arguments`
  ADD CONSTRAINT `notification_arguments_ibfk_1` FOREIGN KEY (`notificationID`) REFERENCES `notifications` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `propertyvalues`
--
ALTER TABLE `propertyvalues`
  ADD CONSTRAINT `propertyvalues_ibfk_1` FOREIGN KEY (`thingID`) REFERENCES `things` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `roles_permissions`
--
ALTER TABLE `roles_permissions`
  ADD CONSTRAINT `roles_permissions_ibfk_1` FOREIGN KEY (`roleID`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `roles_permissions_ibfk_2` FOREIGN KEY (`permissionID`) REFERENCES `permissions` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `ruleparameters`
--
ALTER TABLE `ruleparameters`
  ADD CONSTRAINT `ruleparameters_ibfk_1` FOREIGN KEY (`ruleID`) REFERENCES `rules` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `things`
--
ALTER TABLE `things`
  ADD CONSTRAINT `things_ibfk_1` FOREIGN KEY (`parentID`) REFERENCES `things` (`id`) ON DELETE SET NULL;

--
-- Constraints der Tabelle `things_users`
--
ALTER TABLE `things_users`
  ADD CONSTRAINT `things_users_ibfk_1` FOREIGN KEY (`thingID`) REFERENCES `things` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `things_users_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `tokens`
--
ALTER TABLE `tokens`
  ADD CONSTRAINT `tokens_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `tokens_roles`
--
ALTER TABLE `tokens_roles`
  ADD CONSTRAINT `tokens_roles_ibfk_1` FOREIGN KEY (`roleID`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tokens_roles_ibfk_2` FOREIGN KEY (`tokenID`) REFERENCES `tokens` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `users_permissions`
--
ALTER TABLE `users_permissions`
  ADD CONSTRAINT `users_permissions_ibfk_1` FOREIGN KEY (`permissionID`) REFERENCES `permissions` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `users_permissions_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints der Tabelle `users_roles`
--
ALTER TABLE `users_roles`
  ADD CONSTRAINT `users_roles_ibfk_1` FOREIGN KEY (`roleID`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `users_roles_ibfk_2` FOREIGN KEY (`userID`) REFERENCES `users` (`id`) ON DELETE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
