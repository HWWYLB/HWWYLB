DROP DATABASE IF EXISTS `ChatOL`;
CREATE DATABASE `ChatOL`;

USE `ChatOL`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `rid` int(10) NOT NULL AUTO_INCREMENT,
  `roomname` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `roominfo` varchar(255) NOT NULL,
  `ownerid` int(10) NOT NULL,
  `isEncrypt` ENUM("T","F") DEFAULT "F",
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `mid` int(10) NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `src` varchar(255) NOT NULL,
  `dest` varchar(255) NOT NULL,
  `rid` int(10) NOT NULL,
  `mtype` varchar(25) NOT NULL,
  `mtime` timestamp,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `useroom`;
CREATE TABLE `useroom` (
  `urid` int(10) NOT NULL AUTO_INCREMENT,
  `rid` int(10) NOT NULL,
  `uid` int(10) NOT NULL,
  `time` timestamp,
  PRIMARY KEY (`urid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;