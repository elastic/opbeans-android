FROM androidsdk/android-31

ARG github_username
ARG github_password
ARG exporter_auth_token=None
ARG opbeans_auth_token=None
ARG exporter_endpoint=http://10.0.2.2:8200
ARG opbeans_endpoint=http://10.0.2.2:3000
ENV EXPORTER_AUTH_TOKEN=${exporter_auth_token}
ENV EXPORTER_ENDPOINT=${exporter_endpoint}
ENV OPBEANS_AUTH_TOKEN=${opbeans_auth_token}
ENV OPBEANS_ENDPOINT=${opbeans_endpoint}

WORKDIR /

COPY requirements.txt ./

RUN apt update
RUN yes | apt install python3-pip
RUN pip3 install --no-cache-dir -r requirements.txt

RUN git clone https://${github_username}:${github_password}@github.com/elastic/opbeans-android.git
RUN git clone https://${github_username}:${github_password}@github.com/elastic/apm-agent-android.git

WORKDIR /apm-agent-android
RUN git checkout plain-asm-instrumented

WORKDIR /

COPY . .

CMD python3 -m run --exporter-endpoint ${EXPORTER_ENDPOINT} --exporter-auth-token ${EXPORTER_AUTH_TOKEN} --opbeans-endpoint ${OPBEANS_ENDPOINT} --opbeans-auth-token ${OPBEANS_AUTH_TOKEN}