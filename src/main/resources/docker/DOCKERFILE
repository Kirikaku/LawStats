FROM openjdk:8-jdk

RUN set -ex \
      && DEBIAN_FRONTEND=noninteractive \
      && apt-get update \
      && apt-get install -y --no-install-recommends apt-utils locales maven git \
      && sed -i -e 's/# en_US.UTF-8 UTF-8/en_US.UTF-8 UTF-8/' /etc/locale.gen \
      && dpkg-reconfigure --frontend=noninteractive locales \
      && update-locale LANG=en_US.UTF-8 \
      && apt-get clean
ENV LANG en_US.UTF-8

RUN cd /
RUN git clone https://github.com/Kirikaku/LawStats.git
WORKDIR /LawStats
RUN mvn clean compile
RUN chmod a+x src/main/resources/preprocessing/pdftotext

EXPOSE 8080
CMD mvn spring-boot:run
