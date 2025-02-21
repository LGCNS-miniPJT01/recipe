package recipe.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import recipe.entity.Recipe;
import recipe.entity.Report;
import recipe.entity.Report.ReportStatus;
import recipe.entity.User;
import recipe.repository.RecipeRepository;
import recipe.repository.ReportRepository;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	//관리자만 신고 조회
	@Override
	public List<Report> getAllPendingReports(User admin) {
		if (!admin.isAdmin()) {
            throw new SecurityException("관리자만 신고 목록을 조회할 수 있습니다.");
        }
		return reportRepository.findByStatus(Report.ReportStatus.PENDING);
	}

	//관리자의 신고처리
	@Override
	@Transactional
	public Report updateReportStatus(User admin, Long reportId, ReportStatus status) {
        if (!admin.isAdmin()) {
            throw new SecurityException("관리자만 신고를 처리할 수 있습니다.");
        }

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신고입니다."));
        report.setStatus(status);
        
        if (status == ReportStatus.RESOLVED) {
            Recipe recipe = report.getRecipe();
            recipe.setDeletedYn(true);
            recipeRepository.save(recipe);
        }

        
        return reportRepository.save(report);
    }

}
