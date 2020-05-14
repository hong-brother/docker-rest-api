package com.hsnam.rest.docker.api.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.ConflictException;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.*;
import com.hsnam.rest.docker.api.config.DockerConfig;
import com.hsnam.rest.docker.api.domain.DockerInfoDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
@Slf4j
@Service
public class DockerRestService {

    @Autowired
    private DockerClient dockerClient;

    /**
     * Get Container List
     * @return
     */
    public List<Container> containerList(){
        try{
            return dockerClient.listContainersCmd().withShowAll(true).exec();
        }catch (DockerClientException client){
            log.error(client.getMessage());
            return null;
        }
    }//end of containerList

    /**
     * Get Container Status Using Name
     * @param containerName Container Name
     * @return Container
     */
    public List<Container> containerStatusUsingName(String containerName){
        try{
            Collection<String> nameFilter = new ArrayList<String>();
            nameFilter.add(containerName);
            if(inspectContainer(containerName) !=null){
                return dockerClient.listContainersCmd().withShowAll(true).withNameFilter(nameFilter).exec();
            }else{
                log.info("Container Not Found.");
                return null;
            }
        }catch (DockerClientException client){
            log.error("DockerClientException="+client.getMessage());
            return null;
        }catch (DockerException docker){
            log.error("DockerException="+docker.getMessage());
            return null;
        }catch (NoClassDefFoundError exp){
            log.error("NoClassDefFoundError="+exp.getMessage());
            return null;
        }
    }//end of containerStatusUsingName

    /**
     * Get Container Status Using Container ID
     * @param containerId Container ID
     * @return Container List
     */
    public List<Container> containerStatusUsingID(String containerId){
        try{
            Collection<String> idFilter = new ArrayList<String>();
            idFilter.add(containerId);
            if(inspectContainer(containerId) !=null){
                return dockerClient.listContainersCmd().withShowAll(true).withIdFilter(idFilter).exec();
            }else{
                log.info("Container Not Found.");
                return null;
            }

        }catch (DockerClientException client){
            log.error(client.getMessage());
            return null;
        }catch (DockerException docker){
            log.error(docker.getMessage());
            return null;
        }catch (NoClassDefFoundError exp){
            log.error(exp.getMessage());
            return null;
        }
    }//end of containerStatusUsingID

    /**
     * Get Container Using Container Name or ID
     * @param container
     * @return
     */
    public InspectContainerResponse inspectContainer(String container){
        InspectContainerResponse inspectContainerResponse = null;
        try{
            inspectContainerResponse =  dockerClient.inspectContainerCmd(container).exec();
            return inspectContainerResponse;
        }catch (NotFoundException not){
            log.info(not.getMessage());
            return null;
        }
    }//end of InspectContainerResponse

    /**
     * Container Action
     * @param container Container ID OR Name
     * @param action Action(start, stop, restart, remove)
     */
    public String containerAction(String container, String action){
        try{
            InspectContainerResponse response =inspectContainer(container);
            if(response !=null){
                log.info("Container Status={}", response.getState());
                switch (action){
                    case "start":
                        log.info("Container Start = {}", container);
                        if(!response.getState().getRunning() ==true){
                            dockerClient.startContainerCmd(container).exec();
                        }else {
                            throw new DockerClientException("The container has already been running.");
                        }
                        break;
                    case "stop":
                        log.info("Container Stop = {}", container);
                        if(!response.getState().getRunning() ==false){
                            dockerClient.stopContainerCmd(container).exec();
                        }else{
                            throw new DockerClientException("The container has already been shut down.");
                        }
                        break;
                    case "restart":
                        log.info("Container restart = {}", container);
                        if(!response.getState().getRestarting() ==true){
                            dockerClient.restartContainerCmd(container).exec();
                        }else{
                            throw new DockerClientException("The container has already been restarting.");
                        }
                        break;
                    case "remove":
                        log.info("Container remove = {}", container);
                        if(!response.getState().getRunning() ==true){
                            dockerClient.removeContainerCmd(container).exec();
                        }else{
                            throw new DockerClientException("You must shut down the container.");
                        }
                        break;
                    case "kill":
                        log.info("Container Kill = {}", container);
                        if(!response.getState().getRunning() ==false){
                            dockerClient.killContainerCmd(container).exec();
                        }else{
                            throw new DockerClientException("The container must be running.");
                        }
                        break;
                    default:
                        throw new DockerClientException("The command is wrong.");
                }//end of switch
                return null; //정상 케이스
            }else{
                throw new DockerClientException("Invalid Container Name or ID");
            }
        }catch (DockerClientException docker){
            return docker.toString();
        }
    }//end of containerAction

    /**
     * Crteate Container
     * @param dockerInfoDomain Create Container Domain
     * @return Container ID
     */
    public String createContainer(DockerInfoDomain dockerInfoDomain){
        try{
            ExposedPort exposedPort = ExposedPort.tcp(7070); // bind Port
            Ports portBindings = new Ports();
            portBindings.bind(exposedPort, Ports.Binding.bindPort(dockerInfoDomain.getTargetServerPort()));
            /**
             * TODO - 향후 Docker Image Update 변경시 이미지 사용하는 부분도 유동적으로 변경 할것.
             */
            CreateContainerResponse container = dockerClient.createContainerCmd("registry.ust21.kr/smart-api:1.0")
                    .withExposedPorts(exposedPort)
                    .withName(dockerInfoDomain.getCreateContainerName())
                    .withEnv("APP_KEY="+dockerInfoDomain.getAppKey())
                    .withHostConfig(new HostConfig()
                            .withRestartPolicy(RestartPolicy.alwaysRestart())
                            .withPortBindings(portBindings))
                    .exec();
            if(container.getId() !=null){
                dockerClient.startContainerCmd(container.getId()).exec();
                return container.getId();
            }else {
                return null;
            }
        }catch (NotFoundException not){
            log.error("NotFoundException="+not.getMessage());
            return null;
        }catch (DockerClientException doc){
            log.error("DockerClientException="+doc.getMessage());
            return null;
        }catch (ConflictException conflict){
            log.error("ConflictException="+conflict.getMessage());
            return null;
        }
    }//end of createContainer
}
