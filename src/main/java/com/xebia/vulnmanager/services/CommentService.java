package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.comments.Comment;
import com.xebia.vulnmanager.models.generic.GenericResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private GenericReportService reportService;

    @Autowired
    public CommentService(final GenericReportService genRepository) {
        this.reportService = genRepository;
    }

    public Optional<List<Comment>> getCommentsOfGenericResult(long reportId, long resultId) {
        Optional<GenericResult> report = this.reportService.getReportByGenericId(reportId, resultId);

        if (report.isPresent()) {
            return Optional.of(report.get().getComments());
        }
        return  Optional.empty();
    }

}
