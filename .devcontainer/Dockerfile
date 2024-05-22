FROM --platform=linux/amd64 debian:bullseye

# Set Version Information
ARG VERSION
ARG URSIM
LABEL build_version="URSim Version: ${VERSION}"
LABEL description="Simulator for Universal Robots e-Series"

RUN dpkg --add-architecture i386; \
    apt-get update; \
    # install 32 bit packages
    apt-get install -y \
        libcurl4:i386 \
        runit:i386 \ 
        lib32gcc-s1 

# Install git, supervisor, VNC, & X11 packages
RUN set -ex; \
    apt-get update; \
    apt-get install -y \
      git \
      net-tools \
      novnc \
      supervisor \
      x11vnc \
      htop \
      openssh-server \
      xvfb \
      curl \
      nano \
      psmisc \
      procps \
      build-essential \
      sshpass \
      dialog \
      ant \
      maven \
      xdg-utils \
      unzip \
      sudo


RUN set -ex; \
    curl https://www.python.org/ftp/python/2.7.18/Python-2.7.18.tgz -o python-2.7.18.tgz; \
    tar -xzf /python-2.7.18.tgz; \
    cd Python-2.7.18; \
    ./configure --prefix=/usr --enable-shared; \
    make && make install; \
    cd .. && update-alternatives --install /usr/bin/python python /usr/bin/python2.7 20; \
    update-alternatives --set python /usr/bin/python2.7; \
    apt-get remove -y build-essential

RUN set -x; \
    curl https://packages.baidu.com/app/jdk-8/jdk-8u11-linux-x64.tar.gz -o jdk-8u11-linux-x64.tar.gz; \
    mkdir /usr/lib/jvm; \
    tar -zxvf jdk-8u11-linux-x64.tar.gz -C /usr/lib/jvm/; \
    update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk1.8.0_11/bin/java 10000

ARG USERNAME=psxdev
ARG USER_UID=1001
ARG USER_GID=$USER_UID
RUN set -ex; \
    groupadd --gid $USER_GID $USERNAME; \
    useradd --uid $USER_UID --gid $USER_GID -m $USERNAME; \
    echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME; \
    chmod 0440 /etc/sudoers.d/$USERNAME
        
#USER $USERNAME

RUN set -ex; \
    curl https://s3-eu-west-1.amazonaws.com/ur-support-site/219685/URSim_Linux-5.16.0.127960.tar.gz --insecure -o URSim.tar.gz; \
  #  curl https://nexus3.ur-update.dk/repository/maven-standard-release-hosted/com/ur/ursim/package-g5/12.7.960/package-g5-12.7.960.tar.gz --insecure -o URSim.tar.gz; \
    tar xvzf URSim.tar.gz; \
    rm URSim.tar.gz; \
    #if the URSim is downloaded from Nexus it is compressed twice
    if [ -f URSim*.tar.gz ]; then \
        tar xvzf URSim*.tar.gz ; \
        rm URSim*.tar.gz; \
    fi; \    
    mv /ursim* /ursim

RUN set -ex; \
    DEPENDENCY_EXIST=$(dpkg -I /ursim/ursim-dependencies/libxmlrpc-c-ur_1.33.14_amd64.deb | grep Depends | grep lib32gcc1); \
    if [ "test${DEPENDENCY_EXIST}" != "test" ]; then \
        dpkg-deb -R /ursim/ursim-dependencies/libxmlrpc-c-ur_1.33.14_amd64.deb /ursim/unpacked-libxmlrpc; \
        cd /ursim ; \
        sed -i 's|lib32gcc1 (>= 1:4.1.1)|lib32gcc-s1 (>= 4.1.1)|g' unpacked-libxmlrpc/DEBIAN/control ; \
        dpkg-deb -b  unpacked-libxmlrpc ursim-dependencies/libxmlrpc-c-ur_1.33.14_amd64.deb ;  \
        rm -r unpacked-libxmlrpc; \
    fi

# Install URSim
RUN set -ex; \
    cd /ursim ; \
    cat install.sh; \
    sed -i 's|apt-get -y install|apt-get -qy install --no-install-recommends|g' install.sh ; \
    sed -i 's|tty -s|(exit 0)|g' install.sh ; \
    sed -i 's|needToInstallJava$|(exit 0)|g' install.sh ; \
    if [ -d programs.UR30 ]; then \
        sed -i '/for TYPE in UR3 UR5 UR10 UR16 UR20 UR30/,$ d' install.sh; \
        sed -i -e '$a copyRuntimeLibraries' install.sh; \
    elif [ -d programs.UR20 ]; then \
        sed -i '/for TYPE in UR3 UR5 UR10 UR16 UR20/,$ d' install.sh ; \
        sed -i -e '$a copyRuntimeLibraries' install.sh; \
    elif [ -d programs.UR16 ]; then \
        sed -i '/for TYPE in UR3 UR5 UR10 UR16/,$ d' install.sh; \
    else \
        sed -i '/for TYPE in UR3 UR5 UR10/,$ d' install.sh; \
    fi ; \
    # runit and libcurl is installed above
    sed -i 's|sudo apt-get -y install runit||g' install.sh ; \
    sed -i 's|libcurl3||g' install.sh  ; \
    sed -i 's|pkexec ||g' install.sh ; \
    sed -i 's|lib32gcc1 ||g' install.sh ; \
    sed -i 's|sudo ||g' install.sh ; \
    if [ -f ursim-certificate-check.sh ]; then \
        sed -i 's|sudo ||g' ursim-certificate-check.sh; \
    fi ; \
    cat install.sh ; \
    ./install.sh

RUN set -ex; \
    sed -i 's|HOME=$SCRIPT_DIR $SCRIPT_DIR/URControl &>$SCRIPT_DIR/URControl.log &|HOME=$SCRIPT_DIR $SCRIPT_DIR/URControl -r &>$SCRIPT_DIR/URControl.log &|g' /ursim/starturcontrol.sh; \
     rm -rf /tmp/* /var/lib/apt/lists/* /var/tmp/*; \
     mkdir /urcaps; \
     cp /usr/share/java/* /usr/lib/jni; \
    export PATH=$PATH:/usr/lib/jvm/jdk1.8.0_11/bin; 

RUN  set -ex; \
    mkdir /var/run/sshd ; \
    useradd -m -s /bin/bash ur ; \
    echo 'ur:easybot' | chpasswd ; \
    sed -i 's/PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config ; \
    sed 's@session\s*required\s*pam_loginuid.so@session optional pam_loginuid.so@g' -i /etc/pam.d/sshd ; \
    chown ur /ursim/.urcaps

# Setup demo environment variables
ENV HOME=/ursim \
    DEBIAN_FRONTEND=noninteractive \
    LANG=en_US.UTF-8 \
    LANGUAGE=en_US.UTF-8 \
    LC_ALL=C.UTF-8 \
    DISPLAY=:1 \
    DISPLAY_WIDTH=1080 \
    DISPLAY_HEIGHT=800 \
    TZ="Europe/Copenhagen" \
    VERSION=${VERSION} \
    ROBOT_MODEL=UR5 \
    PATH="$PATH:/ursim/usr/bin" \
    LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/opt/urtool-3.0/lib" 

COPY . /app

RUN set -x; \
     curl https://urplus-developer-site.s3.eu-west-1.amazonaws.com/sdk/sdk-1.14.0.zip -o sdk-1.14.0.zip; \
     unzip sdk-1.14.0.zip -d /sdk-1.14.0; \ 
     rm sdk-1.14.0.zip; \
    cd sdk-1.14.0; \
    no | ./install.sh

ENTRYPOINT ["/app/entrypoint.sh"]
EXPOSE 22 6080 5900 502 29999 30001-30004 30020 50001-50003