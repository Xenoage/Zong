-- phpMyAdmin SQL Dump
-- version 3.3.2deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 11. November 2011 um 13:57
-- Server Version: 5.1.41
-- PHP-Version: 5.3.2-1ubuntu4.10

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `tileserver`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `audio`
--

CREATE TABLE IF NOT EXISTS `audio` (
  `doc_id` int(11) NOT NULL COMMENT 'ID of the document (table docs)',
  `format` enum('OGG','MP3') NOT NULL COMMENT 'The audio format, which must be OGG or MP3',
  `audio` mediumblob NOT NULL COMMENT 'The sampled audio file'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='List of all stored audio files';

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `cursors`
--

CREATE TABLE IF NOT EXISTS `cursors` (
  `doc_id` int(11) NOT NULL COMMENT 'ID of the document (table docs)',
  `cursors` mediumtext NOT NULL COMMENT 'The cursor positions'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='List of the cursor positions for each file';

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `docs`
--

CREATE TABLE IF NOT EXISTS `docs` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID of the document',
  `url` varchar(255) NOT NULL COMMENT 'URL of the document',
  `public_id` varchar(36) NOT NULL COMMENT 'Public ID of the document, which is known by the client',
  `pages` int(11) NOT NULL COMMENT 'Number of pages in this document',
  `last_access` int(11) NOT NULL COMMENT 'UNIX timestamp of last access to this document',
  PRIMARY KEY (`id`),
  UNIQUE KEY `public_id` (`public_id`),
  UNIQUE KEY `url` (`url`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COMMENT='List of documents which are currently stored' AUTO_INCREMENT=8 ;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `pages`
--

CREATE TABLE IF NOT EXISTS `pages` (
  `doc_id` int(11) NOT NULL COMMENT 'ID of the document (table docs)',
  `page` int(11) NOT NULL COMMENT '0-based page index',
  `width` float NOT NULL COMMENT 'Width of the page in mm',
  `height` float NOT NULL COMMENT 'Height of the page in mm',
  PRIMARY KEY (`doc_id`,`page`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Page formats';

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `scaledpages`
--

CREATE TABLE IF NOT EXISTS `scaledpages` (
  `doc_id` int(11) NOT NULL COMMENT 'ID of the document (table docs)',
  `page` int(11) NOT NULL COMMENT '0-based page index',
  `scaling` int(11) NOT NULL COMMENT 'Scaling factor, multiplied with factor 10000',
  `tilesxcount` int(11) NOT NULL COMMENT 'Number of tiles in horizontal direction',
  `tilesycount` int(11) NOT NULL COMMENT 'Number of tiles in vertical direction',
  `widthpx` int(11) NOT NULL COMMENT 'The width of the page in px',
  `heightpx` int(11) NOT NULL COMMENT 'The height of the page in px',
  PRIMARY KEY (`doc_id`,`page`,`scaling`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='More information about scaled pages';

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tiles`
--

CREATE TABLE IF NOT EXISTS `tiles` (
  `doc_id` int(11) NOT NULL COMMENT 'ID of the document (table docs)',
  `page` int(11) NOT NULL COMMENT '0-based index of the page',
  `scaling` int(11) NOT NULL COMMENT 'Scaling factor, multiplied with factor 10000',
  `x` int(11) NOT NULL COMMENT '0-based horizontal tile coordinate',
  `y` int(11) NOT NULL COMMENT '0-based vertical tile coordinate',
  `image` blob NOT NULL COMMENT 'The tile image in PNG format',
  PRIMARY KEY (`doc_id`,`page`,`scaling`,`x`,`y`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='List of all stored tiles';
