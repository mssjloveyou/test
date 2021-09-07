From centos

RUN mkdir /usr/app

ADD jdk-8u281-linux-x64.tar.gz /usr/local/

RUN mv /usr/local/jdk1.8.0_281 /usr/local/jdk

COPY target/solo-0.0.1-SNAPSHOT.jar /usr/app/app.jar

ENV JAVA_HOME /usr/local/jdk

ENV PATH $JAVA_HOME/bin:$PATH
ENV JAVA_TOOL_OPTIONS -Xdebug -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000
RUN yum install openssh-server -y
RUN yum install -y openssh-clients
RUN yum install -y expect
RUN yum install -y net-tools ncurses
RUN echo "root:root"|chpasswd
RUN ssh-keygen -q -t rsa -b 2048 -f /etc/ssh/ssh_host_rsa_key -N ''
RUN ssh-keygen -q -t ecdsa -f /etc/ssh/ssh_host_ecdsa_key -N ''
RUN ssh-keygen -t dsa -f /etc/ssh/ssh_host_ed25519_key -N ''
RUN ssh-keygen -f ~/.ssh/id_rsa -t rsa -N ''
EXPOSE 3306

ENTRYPOINT ["java","-jar","/usr/app/app.jar"]
#CMD ["/usr/sbin/sshd","-D" ]

