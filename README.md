# Smart-Mrn Docker-api

![docker logo](https://www.sauru.so/assets/logos/docker-horizontal.800.png)

**Smart MRN Docker API**
Host Docker의 Remote API를 통하여 컨테이너 기동(Start, Stop, Restart) 및 관리(Create, Remove) 한다.

## Host Docker 정보 
    - Client: Docker Engine - Community
       Version:           19.03.5
       API version:       1.40
       Go version:        go1.12.12
       Git commit:        633a0ea
       Built:             Wed Nov 13 07:25:41 2019
       OS/Arch:           linux/amd64
       Experimental:      false
      
    - Server: Docker Engine - Community
       Engine:
        Version:          19.03.5
        API version:      1.40 (minimum version 1.12)
        Go version:       go1.12.12
        Git commit:       633a0ea
        Built:            Wed Nov 13 07:24:18 2019
        OS/Arch:          linux/amd64
        Experimental:     false
       containerd:
        Version:          1.2.10
        GitCommit:        b34a5c8af56e510852c35414db4c1f4fa6172339
       runc:
        Version:          1.0.0-rc8+dev
        GitCommit:        3e425f80a8c931f88e6d94a8c831b9d5aa481657
       docker-init:
        Version:          0.18.0
        GitCommit:        fec3683
        
    - Remote API
       http://127.0.0.1:4243
    
## Host Docker Service 기동
    - Start 
        sudo systemctl start docker
    - Stop
        sudo systemctl stop docker
    - restart
        sudo systemctl restart docker
        
## Docker API Service 목록
    - List Container
    - Status Container
    - Inspect Container
    - Stop Container
    - Restart Container
    - Start Container
    - Remove Container
    - Create Container

## How To Use Docker
    - Version 정보 조회
        docker version
    - Docker Container 정보 조회
        docker ps -a
    - Docker 해당 Container 조회
        docker inspect [container ID Or Name] 
    - Docker Container 시작
        docker start [container ID Or Name]
    - Docker Container 종료
        docker stop [container ID Or Name]
    - Docker Container 재시작
        docker restart [container ID Or Name]
    - Docker Container 삭제 (필수적으로 Container가 종료된 상태에서 시행되어야함.)
        docker rm [container ID Or Name]
    - Docker Container 발행
        docker run --name 'docker-api' -e 'APP_KEY=UUID' -p 8080:8080 -it [imageID Or imageName]
    - Docker Container 접속(exec)
        docker exec -it [ContainerId Or ContainerName] /bin/bash
    - Docker Container 접속 해지
        Ctrl + p and Ctrl + q 
    - Docker Images 조회
        docker images
    - Docker Images 삭제 (필수적으로 images에서 발행된 컨테이너가 존재하지 않아야함.)
        docker rmi [imageID Or ImageName]
    
## Swagger API
    - URI http://localhost:8081/docker-api/swagger-ui.html#/

## License
