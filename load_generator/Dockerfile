FROM androidsdk/android-31

ARG github_username
ARG github_password
ARG exporter_auth_token
ARG opbeans_auth_token
ARG exporter_endpoint=http://10.0.2.2:8200
ARG opbeans_endpoint=http://10.0.2.2:3000

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

CMD ["python3", "-m", "run",
        "--exporter-endpoint", "${exporter_endpoint}",
        "--exporter-auth-token", "${exporter_auth_token}",
        "--opbeans-endpoint", "${opbeans_endpoint}",
        "--opbeans-auth-token", "${opbeans_auth_token}"
   ]