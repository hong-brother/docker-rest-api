package com.hsnam.rest.docker.api.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.jaxrs.JerseyDockerCmdExecFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * com.hsnam.rest.docker.api.config
 * Docker Configuration
 * @author hsnam
 */
@Slf4j
@Configuration
public class DockerConfig {

    private final Environment env;

    private String dockerHostURL;

    public DockerConfig(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init(){
        this.dockerHostURL = env.getProperty("docker.host.protocol") + env.getProperty("docker.host.ip") + ":"+env.getProperty("docker.host.port");
        log.info("Docker Host = {}", this.dockerHostURL);
    }//end of init

    @Bean
    public DockerClient dockerClientList(){
        DockerClient client = null;
        try{
            DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost(this.dockerHostURL)
                    .withDockerTlsVerify(false)
                    .withDockerCertPath("/home/user/.docker/certs")
                    .withDockerConfig("/home/user/.docker")
                    .build();

            client= DockerClientBuilder.getInstance(dockerClientConfig)
                    .withDockerCmdExecFactory(
                            //Client 환경설정
                            new JerseyDockerCmdExecFactory()
                                    .withReadTimeout(15000)
                                    .withConnectTimeout(10000)
                                    .withMaxTotalConnections(100)
                                    .withMaxPerRouteConnections(10)
                    )
                    .build();
            if(client.versionCmd().exec() !=null){
                log.info("Docker Version="+client.versionCmd().exec().getVersion());
            }else{
                throw new DockerClientException("There is a problem with the Docker host.");
            }
        }catch (DockerClientException exp){
            log.error(exp.getMessage());
            client = null;
        }catch (NotFoundException not){
            log.error(not.getMessage());
            client = null;
        }catch (DockerException docker){
            log.error(docker.getMessage());
            client = null;
        }
        return client;
    }//end of dockerClient
}
