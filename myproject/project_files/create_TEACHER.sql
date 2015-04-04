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

-- Dumping structure for table dce.tbl_teacher
CREATE TABLE IF NOT EXISTS `tbl_teacher` (
  `ID` int(11) NOT NULL,
  `FULL_NAME` varchar(255) DEFAULT NULL,
  `CATEGORY` varchar(255) DEFAULT NULL,
  `MILITARY_SITUATION` int(11) DEFAULT NULL,
  `ID_NUMBER` varchar(255) DEFAULT NULL,
  `CONTRACT` varchar(255) DEFAULT NULL,
  `BEGIN_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `E_MAIL` varchar(255) DEFAULT NULL,
  `MOBILE_PHONE` varchar(255) DEFAULT NULL,
  `SCIENTIFIC_FIELD` varchar(255) DEFAULT NULL,
  `ACADEMIC_DEGREE` varchar(255) DEFAULT NULL,
  `CATEGORY_DATE` date DEFAULT NULL,
  `IN_CHARGE` bit(1) DEFAULT NULL,
  `CATEGORY_GROUP_ID` int(11) DEFAULT NULL,
  `MASTER_DEGREE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `FULL_NAME` (`FULL_NAME`),
  UNIQUE KEY `UK_hnob2nev319wgeimwvan5as2h` (`FULL_NAME`),
  KEY `FK_3tum6jj9sjsf53h4aj2x8t77m` (`CATEGORY_GROUP_ID`),
  CONSTRAINT `tbl_teacher_ibfk_1` FOREIGN KEY (`CATEGORY_GROUP_ID`) REFERENCES `tbl_category` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- Data exporting was unselected.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
