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

-- Dumping structure for table dce.tbl_professorship
CREATE TABLE IF NOT EXISTS `tbl_professorship` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `COURSE_CODE` int(11) NOT NULL,
  `TEACHER_CODE` int(11) NOT NULL,
  `DEGREE_CODE` int(11) NOT NULL,
  `EXECUTION_YEAR` int(11) NOT NULL,
  `SEMESTER` smallint(6) NOT NULL,
  `LECTURED_HOURS` float DEFAULT NULL,
  `RESPONSIBLE_FOR_COURSE` bit(1) NOT NULL,
  `BEGIN_DATE` date NOT NULL DEFAULT '0000-00-00',
  `END_DATE` date DEFAULT '0000-00-00',
  PRIMARY KEY (`ID`),
  KEY `FK_tbl_professorship_tbl_teacher` (`TEACHER_CODE`),
  CONSTRAINT `tbl_professorship_ibfk_1` FOREIGN KEY (`TEACHER_CODE`) REFERENCES `tbl_teacher` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
