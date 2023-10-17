package com.br.sgs.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.br.sgs.models.ClientEngagementLogModel;

public interface ClientEngagementLogRepository extends JpaRepository<ClientEngagementLogModel, UUID>{
	
	boolean existsByClientClientIdAndCompanyCompanyId(UUID clientId, UUID companyId);
	boolean existsByClientClientIdAndDtCreatedAndCompanyCompanyId(UUID clientId, LocalDate dtCreated, UUID companyId);
	ClientEngagementLogModel save(ClientEngagementLogModel clientEngagementLog);
	
	
	@Query(value = "select status, count(*) quantidade \r\n"
			+ "	from sgsapi.client_engagement_log  \r\n"
			+ "   where company_id = ? \r\n"
			+ "   and dt_created between ? and ? "
			+ " group by \r\n "
			+ "   status", nativeQuery = true)
	List<Object[]> getSumarizedClientEngagementByCompanyIdAndDtCreatedBetween(String companyId, LocalDate now,
			LocalDate minusYears);
	
	/*@Query(value = "select\r\n"
			+ "    status,\r\n"
			+ "    CONCAT(DATE_FORMAT(dt_created, '%Y-%m'), '-01') AS anoMes,\r\n"
			+ "    COUNT(*) AS quantidade\r\n"
			+ "from\r\n"
			+ "    sgsapi.client_engagement_log \r\n"
			+ "where company_id = ? \r\n"
			+ "  and dt_created between ? and ?\r\n"
			+ "group by\r\n"
			+ "    status,\r\n"
			+ "    anoMes\r\n"
			+ "order by\r\n"
			+ "    anoMes, status ", nativeQuery = true)
	List<Object[]> getDetailedClientEngagementByCompanyIdAndDtCreatedBetween(String companyId, LocalDate now,
			LocalDate minusYears);  */
	
	
	
	@Query(value = "SELECT\r\n"
			+ "    allMonths.status AS status,\r\n"
			+ "    allMonths.monthNumber AS monthNumber,\r\n"
			+ "    IFNULL(COUNT(cel.status), 0) AS quantidade\r\n"
			+ "FROM (\r\n"
			+ "    SELECT 1 AS monthNumber, 'KNOWN' AS status\r\n"
			+ "    UNION SELECT 1, 'NEW'\r\n"
			+ "    UNION SELECT 2, 'KNOWN'\r\n"
			+ "    UNION SELECT 2, 'NEW'\r\n"
			+ "    UNION SELECT 3, 'KNOWN'\r\n"
			+ "    UNION SELECT 3, 'NEW'\r\n"
			+ "    UNION SELECT 4, 'KNOWN'\r\n"
			+ "    UNION SELECT 4, 'NEW'\r\n"
			+ "    UNION SELECT 5, 'KNOWN'\r\n"
			+ "    UNION SELECT 5, 'NEW'\r\n"
			+ "    UNION SELECT 6, 'KNOWN'\r\n"
			+ "    UNION SELECT 6, 'NEW'\r\n"
			+ "    UNION SELECT 7, 'KNOWN'\r\n"
			+ "    UNION SELECT 7, 'NEW'\r\n"
			+ "    UNION SELECT 8, 'KNOWN'\r\n"
			+ "    UNION SELECT 8, 'NEW'\r\n"
			+ "    UNION SELECT 9, 'KNOWN'\r\n"
			+ "    UNION SELECT 9, 'NEW'\r\n"
			+ "    UNION SELECT 10, 'KNOWN'\r\n"
			+ "    UNION SELECT 10, 'NEW'\r\n"
			+ "    UNION SELECT 11, 'KNOWN'\r\n"
			+ "    UNION SELECT 11, 'NEW'\r\n"
			+ "    UNION SELECT 12, 'KNOWN'\r\n"
			+ "    UNION SELECT 12, 'NEW'\r\n"
			+ ") AS allMonths\r\n"
			+ "LEFT JOIN (\r\n"
			+ "    SELECT\r\n"
			+ "        MONTH(dt_created) AS monthNumber,\r\n"
			+ "        IFNULL(status, 'UNKNOWN') AS status\r\n"
			+ "    FROM sgsapi.client_engagement_log\r\n"
			+ "    WHERE YEAR(dt_created) = ? \r\n"
			+ "        AND company_id = ? \r\n"
			+ ") AS cel\r\n"
			+ "ON allMonths.monthNumber = cel.monthNumber\r\n"
			+ "    AND allMonths.status = cel.status\r\n"
			+ "GROUP BY\r\n"
			+ "    allMonths.monthNumber, allMonths.status\r\n"
			+ "ORDER BY\r\n"
			+ "    allMonths.monthNumber, allMonths.status;", nativeQuery = true)
	List<Object[]> getDetailedClientEngagementByDtCreatedBetweenAndCompanyId(LocalDate now, String companyId);

}
