CREATE EVENT IF NOT EXISTS auto_delete_station_results
ON SCHEDULE AT CURRENT_TIMESTAMP + INTERVAL 2 DAY
ON COMPLETION PRESERVE
DO
   DELETE  FROM monitorambientaldb.waqi_station_results 
	where timestamp < (NOW() - INTERVAL 2 DAY)
