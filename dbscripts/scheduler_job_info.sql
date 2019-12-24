DROP TABLE IF EXISTS `scheduler_job_info`;
CREATE TABLE `scheduler_job_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cronExpression` varchar(255) DEFAULT NULL,
  `cronJob` bit(1) DEFAULT NULL,
  `jobClass` varchar(255) DEFAULT NULL,
  `jobGroup` varchar(255) DEFAULT NULL,
  `jobName` varchar(255) DEFAULT NULL,
  `repeatTime` bigint(20) DEFAULT NULL,
  `bashText` text DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB;

