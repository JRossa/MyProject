-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               5.5.5-10.0.14-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             8.3.0.4694
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Dumping structure for table dce.tbl_role
DROP TABLE IF EXISTS `tbl_role`;
CREATE TABLE IF NOT EXISTS `tbl_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(50) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- Dumping data for table dce.tbl_role: ~4 rows (approximately)
DELETE FROM `tbl_role`;
/*!40000 ALTER TABLE `tbl_role` DISABLE KEYS */;
INSERT INTO `tbl_role` (`ID`, `DESCRIPTION`, `NAME`) VALUES
	(1, 'ROLE_ADMIN', 'Administração'),
	(2, 'ROLE_USER', 'Utilizador (Verificação)'),
	(3, 'ROLE_TEACHER', 'Docentes'),
	(4, 'ROLE_STUDENT', 'Alunos');
/*!40000 ALTER TABLE `tbl_role` ENABLE KEYS */;


-- Dumping structure for table dce.tbl_user
DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE IF NOT EXISTS `tbl_user` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATION_DATE` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `CREATION_USER` varchar(50) NOT NULL DEFAULT '0',
  `ENABLED` bit(1) NOT NULL DEFAULT b'1',
  `FIRST_NAME` varchar(50) NOT NULL DEFAULT '',
  `LAST_NAME` varchar(50) NOT NULL DEFAULT '',
  `MODIFICATION_DATE` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `MODIFICATION_USER` varchar(50) NOT NULL DEFAULT '',
  `PASSWORD` varchar(20) NOT NULL DEFAULT '',
  `RND_PASSWORD` varchar(50) NOT NULL DEFAULT '',
  `STUDENT_ID` int(11) NOT NULL DEFAULT '-1',
  `USER_ID` int(11) NOT NULL DEFAULT '-1',
  `USER` varchar(50) NOT NULL,
  `ROLE_ID` int(11) NOT NULL DEFAULT '-1',
  `TEACHER_ID` int(11) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `USER` (`USER`),
  KEY `FK_tbl_user_tbl_role` (`ROLE_ID`),
  KEY `FK_tbl_user_tbl_teacher` (`TEACHER_ID`),
  CONSTRAINT `FK_tbl_user_tbl_role` FOREIGN KEY (`ROLE_ID`) REFERENCES `tbl_role` (`ID`),
  CONSTRAINT `FK_tbl_user_tbl_teacher` FOREIGN KEY (`TEACHER_ID`) REFERENCES `tbl_teacher` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- Dumping data for table dce.tbl_user: ~0 rows (approximately)
DELETE FROM `tbl_user`;
/*!40000 ALTER TABLE `tbl_user` DISABLE KEYS */;
INSERT INTO `tbl_user` (`ID`, `CREATION_DATE`, `CREATION_USER`, `ENABLED`, `FIRST_NAME`, `LAST_NAME`, `MODIFICATION_DATE`, `MODIFICATION_USER`, `PASSWORD`, `RND_PASSWORD`, `STUDENT_ID`, `USER_ID`, `USER`, `ROLE_ID`, `TEACHER_ID`) VALUES
	(3, '0000-00-00 00:00:00', '0', b'1', '', '', '0000-00-00 00:00:00', '', 'admin', '', -1, -1, 'admin', 1, 146),
	(5, '0000-00-00 00:00:00', '0', b'1', '', '', '0000-00-00 00:00:00', '', 'user', '', -1, -1, 'user', 2, 146),
	(6, '0000-00-00 00:00:00', '0', b'1', '', '', '0000-00-00 00:00:00', '', 'teacher', '', -1, -1, 'teacher', 3, 146);
/*!40000 ALTER TABLE `tbl_user` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
