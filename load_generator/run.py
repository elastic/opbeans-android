# DO NOT RUN IT DIRECTLY, run the "run_load_generator.sh" file instead, which is located in the root
# dir of this project.
import os
import subprocess

from jproperties import Properties


def log(message, *args):
    print("[LOAD_GENERATOR] - " + message.format(*args))


def run_command(command, from_dir=os.getcwd()):
    log("Running command: {}", command)
    with subprocess.Popen(command, stdout=subprocess.PIPE, cwd=from_dir, bufsize=1,
                          universal_newlines=True, shell=True) as p:
        for line in p.stdout:
            print(line, end='')

    if p.returncode != 0:
        raise subprocess.CalledProcessError(p.returncode, p.args)


def build_agent():
    log("Building APM Agent")
    run_command("./gradlew publishToMavenLocal", "./apm-agent-android")


def get_agent_version():
    configs = Properties()
    with open('apm-agent-android/gradle.properties', 'rb') as properties:
        configs.load(properties)

    return configs.get("version").data


def set_opbeans_agent_version(agent_version):
    log("Setting agent version: {}", agent_version)
    with open('opbeans-android/gradle.properties', 'r+b') as properties:
        opbeans_prop = Properties()
        opbeans_prop.load(properties)

        opbeans_prop["agent_version"] = agent_version

        properties.seek(0)
        properties.truncate(0)
        opbeans_prop.store(properties)


def run_tests():
    log("Running UI tests")
    run_command("./gradlew connectedAndroidTest", "./opbeans-android")


def main():
    build_agent()
    set_opbeans_agent_version(get_agent_version())
    run_tests()


if __name__ == "__main__":
    main()
