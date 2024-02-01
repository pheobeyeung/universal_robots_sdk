

# FROM scratch
FROM universalrobots/ursim_e-series
# FROM ubuntu:18.04 
#CMD ["bash"]
ENV HOME=/sdk-1.14.0
ENV URTOOL_ROOT=/opt/urtool-3.0 \
    URTOOL_TARGET=i686-unknown-linux-gnu- \
    PATH="${PATH}:/opt/urtool-3.0/bin"
ENV URSIM=/ursim 
RUN apt-get -y update \
&& apt-get -y upgrade \
&& apt-get install -y nodejs \
&& apt-get install -y dialog \
&& apt-get install -y ant \
&& apt-get install -y unzip \
&& apt-get install -y maven \
&& apt-get install -y xdg-utils \
&& apt-get install -y sshpass \
&& apt-get install -y git \
&& apt-get clean \
&& rm -rf /var/lib/apt/lists/* 

RUN curl https://urplus-developer-site.s3.eu-west-1.amazonaws.com/sdk/sdk-1.14.0.zip -o sdk-1.14.0.zip \
&& unzip sdk-1.14.0.zip -d /sdk-1.14.0 \
&& rm sdk-1.14.0.zip \
&& cd /sdk-1.14.0 \
&& yes | ./install.sh
