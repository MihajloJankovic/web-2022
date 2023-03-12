package com.ftn.Pharmacy.Project.service.implementation;

import com.ftn.Pharmacy.Project.dao.impl.Commentdao;
import com.ftn.Pharmacy.Project.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private gradeservice dao;
    @Autowired
    private Commentdao ComDao;


    public List<Comment> getComments()
    {
        return ComDao.getComments();
    }
    public Comment getComment(Long id)
    {
        return ComDao.getComment(id);
    }
    public int save(Comment com)
    {
        dao.save((long) com.getMedicine(),com.getGrade());
        return ComDao.save(com);
    }
}
