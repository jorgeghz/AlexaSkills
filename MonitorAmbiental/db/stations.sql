CREATE TABLE `stations_waqi` (
  `stationId` varchar(30) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  `url` varchar(150) DEFAULT NULL,
  `frecuency` float DEFAULT NULL,
  `alias` varchar(45) DEFAULT NULL,
  `postalcode` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`stationId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
