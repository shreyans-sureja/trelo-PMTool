package io.trelo.ppmtool.services;

import io.trelo.ppmtool.domain.Backlog;
import io.trelo.ppmtool.domain.Project;
import io.trelo.ppmtool.exceptions.ProjectIdException;
import io.trelo.ppmtool.repositories.BacklogRepository;
import io.trelo.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){

        try{
            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            if(project.getId() == null){
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if(project.getId() != null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
            }

            return projectRepository.save(project);
        }
        catch (Exception e){
            throw new ProjectIdException("Project Id " + project.getProjectIdentifier().toUpperCase() + " already exists.");
        }
    }

    public Project findProjectByIdentifier(String projectId){

        Project project =  projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException("Project doesn't exists.");
        }

        return project;
    }

    public Iterable<Project> findAllProject(){
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(project == null){
            throw new ProjectIdException(projectId + " This project-id doesn't exist");
        }

        projectRepository.delete(project);
    }
}
