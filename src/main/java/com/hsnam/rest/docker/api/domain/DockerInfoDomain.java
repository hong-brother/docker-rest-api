package com.hsnam.rest.docker.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Docker Domain
 */
@Data @ToString
public class DockerInfoDomain implements Serializable {
    private static final long serialVersionUID = -7638223927373031679L;
    /** target Docker Remote API **/
    @ApiModelProperty(value = "타켓 서버 PORT", required = true, example = "8082")
    private int targetServerPort;
    @ApiModelProperty(value = "생성 컨테이너 이름", required = true, example = "docker-container")
    private String createContainerName;
    @ApiModelProperty(value = "생성 분야 Key", required = true, example = "atom")
    private String appKey;
}
