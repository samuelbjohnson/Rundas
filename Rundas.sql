-- phpMyAdmin SQL Dump
-- version 3.2.5
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Dec 15, 2011 at 04:48 PM
-- Server version: 5.1.44
-- PHP Version: 5.3.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `Rundas`
--

-- --------------------------------------------------------

--
-- Table structure for table `Games`
--

DROP TABLE IF EXISTS `Games`;
CREATE TABLE `Games` (
  `gameId` int(11) NOT NULL AUTO_INCREMENT,
  `homeTeamId` int(11) NOT NULL,
  `awayTeamId` int(11) NOT NULL,
  `gameDate` date NOT NULL,
  `homeScore` int(11) NOT NULL DEFAULT '-1',
  `awayScore` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`gameId`),
  UNIQUE KEY `homeTeamId` (`homeTeamId`,`awayTeamId`,`gameDate`),
  KEY `homeTeamId_2` (`homeTeamId`),
  KEY `awayTeamId` (`awayTeamId`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=807 ;

-- --------------------------------------------------------

--
-- Table structure for table `NcaaPlayer`
--

DROP TABLE IF EXISTS `NcaaPlayer`;
CREATE TABLE `NcaaPlayer` (
  `teamId` int(11) NOT NULL,
  `uniformNumber` varchar(3) COLLATE utf8_unicode_ci NOT NULL,
  `lastName` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `firstName` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `position` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `year` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ncaaId` int(6) NOT NULL,
  PRIMARY KEY (`ncaaId`),
  UNIQUE KEY `ncaaId` (`ncaaId`),
  UNIQUE KEY `teamId_2` (`teamId`,`uniformNumber`),
  KEY `teamId` (`teamId`),
  KEY `teamId_3` (`teamId`,`uniformNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `NcaaTeam`
--

DROP TABLE IF EXISTS `NcaaTeam`;
CREATE TABLE `NcaaTeam` (
  `ncaaTeamId` int(11) NOT NULL COMMENT 'Used by Ncaa in their stats',
  `ncaaTeamName` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Used by Ncaa',
  PRIMARY KEY (`ncaaTeamId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `PlayerStats`
--

DROP TABLE IF EXISTS `PlayerStats`;
CREATE TABLE `PlayerStats` (
  `playerId` int(11) NOT NULL,
  `gameDate` date NOT NULL,
  `netRushingYards` int(11) DEFAULT NULL,
  `totalPassingYards` int(11) DEFAULT NULL,
  `totalReceivingYards` int(11) DEFAULT NULL,
  PRIMARY KEY (`playerId`,`gameDate`),
  KEY `playerId` (`playerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `Games`
--
ALTER TABLE `Games`
  ADD CONSTRAINT `Games_ibfk_1` FOREIGN KEY (`homeTeamId`) REFERENCES `NcaaTeam` (`ncaaTeamId`) ON DELETE NO ACTION ON UPDATE CASCADE,
  ADD CONSTRAINT `Games_ibfk_2` FOREIGN KEY (`awayTeamId`) REFERENCES `NcaaTeam` (`ncaaTeamId`) ON DELETE NO ACTION ON UPDATE CASCADE;

--
-- Constraints for table `NcaaPlayer`
--
ALTER TABLE `NcaaPlayer`
  ADD CONSTRAINT `NcaaPlayer_ibfk_1` FOREIGN KEY (`teamId`) REFERENCES `NcaaTeam` (`ncaaTeamId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `PlayerStats`
--
ALTER TABLE `PlayerStats`
  ADD CONSTRAINT `PlayerStats_ibfk_1` FOREIGN KEY (`playerId`) REFERENCES `NcaaPlayer` (`ncaaId`) ON DELETE CASCADE ON UPDATE CASCADE;
