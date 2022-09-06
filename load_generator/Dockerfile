FROM androidsdk/android-31

ARG github_username
ARG github_password

WORKDIR /

RUN echo no | avdmanager create avd -n opbeans_emulator --abi google_apis/x86_64 -k "system-images;android-31;google_apis;x86_64"

COPY requirements.txt ./

RUN apt update
RUN yes | apt install python3-pip
RUN pip3 install --no-cache-dir -r requirements.txt

RUN git clone https://${github_username}:${github_password}@github.com/elastic/opbeans-android.git
RUN git clone https://${github_username}:${github_password}@github.com/elastic/apm-agent-android.git

WORKDIR /apm-agent-android
RUN git checkout plain-asm-instrumented

WORKDIR /

ENV ANDROID_AVD_HOME=/opt/android-sdk-linux/.android/avd

COPY . .

CMD ["python3", "-m", "run"]