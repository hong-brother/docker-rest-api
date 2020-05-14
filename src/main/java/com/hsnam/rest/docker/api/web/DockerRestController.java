package com.hsnam.rest.docker.api.web;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.hsnam.rest.docker.api.domain.DockerInfoDomain;
import com.hsnam.rest.docker.api.service.DockerRestService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@Slf4j
@RestController
public class DockerRestController {

    @Autowired
    private DockerRestService dockerRestService;

    /**
     * Get Contianer List
     * @return Container List
     */
    @ApiOperation(value = "Get Container List", notes = "Docker Host Container List")
    @ApiResponses({@ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping("container/list")
    public ResponseEntity containerList(){
        List<Container> containerList = dockerRestService.containerList();
        if(containerList !=null){
            log.info("Container List = {}"+ containerList.toString());
            return new ResponseEntity(containerList, HttpStatus.OK);
        }else{
            log.info("Host Docker is Error");
            return new ResponseEntity("Host Docker is Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of containerList

    /**
     * Get Container Status Using Container Name
     * @param containerName Container Name
     * @return
     */
    @ApiOperation(value = "Get Container Status Using Container Name", notes = "Container 상태 조회(Name) Like 조건")
    @ApiResponses({@ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping("containerName/{containerName}/status")
    public ResponseEntity containerStatusUsingName(@ApiParam(value = "Target Docker Container Name", required = true)@PathVariable("containerName") String containerName){
        List<Container> targetContainer = dockerRestService.containerStatusUsingName(containerName);
        if(targetContainer !=null && targetContainer.size() >0){
            log.info("target Container Status={}", targetContainer);
            return new ResponseEntity(targetContainer, HttpStatus.OK);
        }else{
            log.info("Invalid ContainerName");
            return new ResponseEntity("Invalid ContainerName",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of containerStatusUsingName

    /**
     * Get Container Status Using Container ID
     * @param containerID Container ID
     * @return
     */
    @ApiOperation(value = "Get Container Status Using Container ID", notes = "Container 상태 조회(ID)")
    @ApiResponses({@ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping("containerID/{containerID}/status")
    public ResponseEntity containerStatusUsingID(@ApiParam(value = "Target Docker Container ID", required = true) @PathVariable("containerID") String containerID){
        List<Container> targetContainer = dockerRestService.containerStatusUsingID(containerID);
        if(targetContainer !=null && targetContainer.size() >0){
            log.info("target Container Status={}", targetContainer);
            return new ResponseEntity(targetContainer, HttpStatus.OK);
        }else{
            log.info("Invalid Container ID");
            return new ResponseEntity("Invalid Container ID",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of containerStatusUsingID

    /**
     * Get Container Inspect
     * @param container Container ID or Name
     * @return
     */
    @ApiOperation(value = "Get Container Inspect", notes = "Container 정보 조회")
    @ApiResponses({@ApiResponse(code = 200, message = "Success"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping("{container}/inspect")
    public ResponseEntity inspectContainer(@ApiParam(value = "Target Container Name or ID", required = true)@PathVariable("container") String container){
        InspectContainerResponse response = dockerRestService.inspectContainer(container);
        if(response !=null){
            log.info("Inspect Container = {}" ,response);
            return new ResponseEntity(response, HttpStatus.OK);
        }else{
            log.info("Invalid ContainerID");
            return new ResponseEntity("Invalid ContainerID",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of inspectContainer

    /**
     * Restart Container
     * @param container Container ID or Name
     * @return
     */
    @ApiOperation(value = "Restart Container", notes = "Container 재시작")
    @ApiResponses({@ApiResponse(code = 204, message = "NO CONTENT"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping("{container}/restart")
    public ResponseEntity containerRestart(@ApiParam(value = "Target Container Name or ID", required = true)@PathVariable("container") String container){
        String result = dockerRestService.containerAction(container, "restart");
        if(result ==null){
            log.info("Restart Container.");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else{
            log.info(result);
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of containerRestart

    /**
     * Stop Container
     * @param container Container ID
     * @return
     */
    @ApiOperation(value = "Stop Container", notes = "Container 종료")
    @ApiResponses({@ApiResponse(code = 204, message = "NO CONTENT"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping("{container}/stop")
    public ResponseEntity containerStop(@ApiParam(value = "Target Container Name or ID", required = true)@PathVariable("container") String container){
        String result = dockerRestService.containerAction(container, "stop");
        if(result ==null){
            log.info("Stop Container.");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else{
            log.info(result);
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of containerStop

    /**
     * Start Container
     * @param container Container ID
     * @return
     */
    @ApiOperation(value = "Start Container", notes = "Container 시작")
    @ApiResponses({@ApiResponse(code = 204, message = "NO CONTENT"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping("{container}/start")
    public ResponseEntity containerStart(@ApiParam(value = "Target Container Name or ID", required = true)@PathVariable("container") String container){
        String result = dockerRestService.containerAction(container, "start");
        if(result == null){
            log.info("Start Container.");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else{
            log.info(result);
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of containerStart

    /**
     * Kill Container
     * @param container Container Id
     * @return
     */
    @ApiOperation(value = "Kill Container", notes = "Container 강제 종료")
    @ApiResponses({@ApiResponse(code = 204, message = "NO CONTENT"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping("{container}/kill")
    public ResponseEntity containerKill(@ApiParam(value = "Target Container Name or ID", required = true)@PathVariable("container") String container){
        String result = dockerRestService.containerAction(container, "kill");
        if(result == null){
            log.info("Start Container.");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else{
            log.info(result);
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove Container
     * @param container Container ID
     * @return
     */
    @ApiOperation(value = "Remove Container", notes = "Container 제거")
    @ApiResponses({@ApiResponse(code = 204, message = "NO CONTENT"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping("{container}/remove")
    public ResponseEntity containerRemove(@ApiParam(value = "Target Container Name or ID", required = true)@PathVariable("container") String container){
        String result = dockerRestService.containerAction( container, "remove");
        if(result == null){
            log.info("Remove Container.");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }else{
            log.info(result);
            return new ResponseEntity(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of containerRemove

    /**
     * Create Container
     * @param dockerModel Docker Model
     * @return
     */
    @ApiOperation(value = "Create Container", notes = "Container 생성")
    @ApiResponses({@ApiResponse(code = 204, message = "NO CONTENT"), @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping("container/create")
    public ResponseEntity containerCreate(@ApiParam(value = "Create Container Information", required = true) @RequestBody DockerInfoDomain dockerModel){
        log.info("Create Container ={}", dockerModel);
        String containerId = dockerRestService.createContainer(dockerModel);
        if(containerId !=null){
            log.info("create Container ={}" + dockerModel.toString());
            return new ResponseEntity(containerId, HttpStatus.NO_CONTENT);
        }else{
            log.info("Can't not Create container");
            return new ResponseEntity("Can't not Create container",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end of containerCreate
}
