package com.effective.mobile.tskmngmntsystm.service;

import com.effective.mobile.tskmngmntsystm.dto.CommentRq;
import com.effective.mobile.tskmngmntsystm.dto.CommentRs;
import com.effective.mobile.tskmngmntsystm.dto.TaskServiceResponse;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {

    TaskServiceResponse<CommentRs> createComment(CommentRq commentRq);

    TaskServiceResponse<CommentRs> deleteComment(Long id);
}
