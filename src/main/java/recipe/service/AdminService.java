package recipe.service;

import java.util.List;

import recipe.entity.Report;
import recipe.entity.User;
import recipe.entity.Report.ReportStatus;

public interface AdminService {
	List<Report> getAllPendingReports(User admin); 
	Report updateReportStatus(User admin, Long reportId, ReportStatus status);
}
