DROP TABLE IF EXISTS `public_holiday`;
CREATE TABLE `public_holiday` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `name_kh` varchar(255) DEFAULT NULL,
  `date_value` timestamp DEFAULT NULL,
  `state` bigint(11) DEFAULT NULL,
  `version` bigint(11) DEFAULT NULL,
  `createdDate` timestamp NOT NULL DEFAULT  '0000-00-00 00:00:00',
  `createdBy` varchar(100) NULL,
  `updatedDate` timestamp NOT NULL DEFAULT  CURRENT_TIMESTAMP,
  `updatedBy` varchar(100) NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB;

