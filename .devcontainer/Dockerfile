FROM debian:bullseye

RUN set -ex; \
    apt-get update -y; \
    apt-get install -y \
    bash \
    git \
    net-tools \
    novnc \
    supervisor \
    x11vnc \
    xterm \
    xvfb \
    curl \
    nano \
    psmisc \
    procps \
    build-essential \
    unzip \
    nodejs \
    dialog \
    ant \
    maven \
    xdg-utils \
    sshpass \
    openssh-server \
    git; \
    apt-get clean; \
    rm -rf /var/lib/apt/lists/* 


RUN set -x; \
    curl https://urplus-developer-site.s3.eu-west-1.amazonaws.com/sdk/sdk-1.14.0.zip -o sdk-1.14.0.zip; \
    unzip sdk-1.14.0.zip -d /sdk-1.14.0; \ 
    rm sdk-1.14.0.zip; \
    cd sdk-1.14.0; \
    no | ./install.sh; \
    mkdir /var/run/sshd



# RUN set -ex; \
#     curl https://www.python.org/ftp/python/2.7.18/Python-2.7.18.tgz -o python-2.7.18.tgz; \
#     tar -xzf /python-2.7.18.tgz; \
#     cd Python-2.7.18; \
#     ./configure --prefix=/usr --enable-shared; \
#     make && make install; \
#     cd .. && update-alternatives --install /usr/bin/python python /usr/bin/python2.7 20; \
#     update-alternatives --set python /usr/bin/python2.7

    # Setup demo environment variables
ENV DEBIAN_FRONTEND=noninteractive \
    LANG=en_US.UTF-8 \
    LANGUAGE=en_US.UTF-8 \
    LC_ALL=C.UTF-8 \
    LD_LIBRARY_PATH="$LD_LIBRARY_PATH:/opt/urtool-3.0/lib" \
    URTOOL_ROOT=/opt/urtool-3.0 \
    URTOOL_TARGET=i686-unknown-linux-gnu- \
    PATH="${PATH}:/opt/urtool-3.0/bin"

COPY . /app
CMD ["/app/entrypoint.sh"]
EXPOSE 22