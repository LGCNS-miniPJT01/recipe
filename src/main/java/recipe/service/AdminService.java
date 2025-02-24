package recipe.service;

import java.util.List;

import recipe.entity.Report;
import recipe.entity.User;
import recipe.entity.Report.ReportStatus;

public interface AdminService {
	List<Report> getAllPendingReports(); 
	Report updateReportStatus(Long reportId, ReportStatus status);
	User suspendUser(Long userId);
	User activateUser(Long userId);
	List<User> getAllUsers();
}
