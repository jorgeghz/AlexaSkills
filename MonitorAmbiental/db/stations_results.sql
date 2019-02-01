CREATE TABLE `waqi_station_results` (
  `id` varchar(30) DEFAULT NULL,
  `result` json DEFAULT NULL,
  `timestamp` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `state` varchar(45) DEFAULT NULL,
  `alias` varchar(45) DEFAULT NULL,
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
