package io.trelo.ppmtool.services;

import io.trelo.ppmtool.domain.Backlog;
import io.trelo.ppmtool.domain.Project;
import io.trelo.ppmtool.domain.ProjectTask;
import io.trelo.ppmtool.exceptions.ProjectNotFoundException;
import io.trelo.ppmtool.repositories.BacklogRepository;
import io.trelo.ppmtool.repositories.ProjectRepository;
import io.trelo.ppmtool.repositories.ProjectTaskRepository;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectService projectService;

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);
            projectTask.setBacklog(backlog);
            Integer BacklogSequence = backlog.getPTSequence();
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);
            projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            if(projectTask.getStatus()=="" || projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            if(projectTask.getPriority()==null || projectTask.getPriority()==0){
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);

    }

    public Iterable<ProjectTask> findBacklogById(String id, String username) {

        projectService.findProjectByIdentifier(id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTProjectSequence(String backlog_id, String pt_id, String username){

        projectService.findProjectByIdentifier(backlog_id,username);



        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if(projectTask==null){
            throw new ProjectNotFoundException("Project task " + pt_id + " not found");
        }

        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project task " + pt_id + " doesn't exist in project: " + backlog_id);
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTProjectSequence(backlog_id,pt_id, username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);
    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTProjectSequence(backlog_id,pt_id, username);
        projectTaskRepository.delete(projectTask);
    }
}
