FROM universalrobots/ursim_e-series
SHELL ["/bin/bash", "-i", "-c"]
ENV URTOOL_ROOT=/opt/urtool-3.0 \
    URTOOL_TARGET=i686-unknown-linux-gnu- \
    PATH="${PATH}:/opt/urtool-3.0/bin"
ENV URSIM=/ursim 

ARG url=https://s3-eu-west-1.amazonaws.com/ur-support-site/115819/sdk-1.18.0.zip
ARG sdk_version=sdk-1.18.0

RUN set -ex; \
    apt-get -y update; \
    apt-get install -y build-essential maven unzip dialog ant sshpass curl git bash; \
    apt-get clean; \
    rm -rf /var/lib/apt/lists/* 

ARG USERNAME=ur-dev
ARG USER_UID=1001
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && apt-get update \
    && apt-get install -y sudo \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

USER $USERNAME

RUN set -ex; \
    sudo curl $url -o $sdk_version.zip; \
    sudo unzip $sdk_version.zip -d /$sdk_version; \
    sudo rm $sdk_version.zip; \
    cd /$sdk_version; \
    sudo yes | ./install.sh; \
    sudo chmod a+rwx /ursim

CMD ["/bin/bash", "-i"]
