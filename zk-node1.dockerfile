FROM centos:7.4.1708
LABEL maintainer="udaya.chathuranga@thealtria.com"

RUN yum -y install sudo

RUN yum -y install java-1.8.0-openjdk

RUN yum -y install wget 
RUN wget -q http://archive.apache.org/dist/zookeeper/zookeeper-3.5.6/apache-zookeeper-3.5.6-bin.tar.gz
RUN tar -xzf apache-zookeeper-3.5.6-bin.tar.gz -C /opt

WORKDIR /opt
RUN ln -s apache-zookeeper-3.5.6-bin ./zookeeper
COPY zk-node1/zoo.cfg /opt/zookeeper/conf

RUN groupadd zookeeper
RUN useradd -g zookeeper zookeeper

#RUN chown -R zookeeper:zookeeper /opt/zookeeper/*

#set root passwd
RUN echo "root":"root" | chpasswd

ENV PATH="/opt/zookeeper/bin:${PATH}"

CMD ["/opt/zookeeper/bin/zkServer.sh", "start-foreground"]