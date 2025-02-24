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
import recipe.repository.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Autowired
	private RecipeRepository recipeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	//관리자만 신고 조회
	@Override
	public List<Report> getAllPendingReports() {
		return reportRepository.findByStatus(Report.ReportStatus.PENDING);
	}

	//관리자의 신고처리
	@Override
	@Transactional
	public Report updateReportStatus(Long reportId, ReportStatus status) {
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

	@Override
	@Transactional
	public User suspendUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
		user.setSuspended(true);
		return userRepository.save(user);
	}

	@Override
	@Transactional
	public User activateUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
		user.setSuspended(false);
		return userRepository.save(user);
	}

	@Override
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	
}
