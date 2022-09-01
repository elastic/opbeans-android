# DO NOT RUN IT DIRECTLY, run the "run_loader.sh" file instead, which is located in the root
# dir of this project.
import os
import subprocess

from jproperties import Properties


def log(message, *args):
    print("[LOADER] - " + message.format(*args))


def run_command(command, from_dir=os.getcwd()):
    log("Running command: {}", command)
    with subprocess.Popen(command.split(), stdout=subprocess.PIPE, cwd=from_dir, bufsize=1,
                          universal_newlines=True) as p:
        for line in p.stdout:
            print(line, end='')

    if p.returncode != 0:
        raise subprocess.CalledProcessError(p.returncode, p.args)


def fetch_agent():
    print("Fetching APM Agent Android")
    run_command("git clone git@github.com:elastic/apm-agent-android.git")

    # Todo remove after https://github.com/elastic/apm-agent-android/issues/6 is closed:
    run_command("git checkout plain-asm-instrumented", "apm-agent-android")


def build_agent():
    print("Building APM Agent")
    run_command("./gradlew publishToMavenLocal", "./apm-agent-android")


def get_agent_version():
    configs = Properties()
    with open('apm-agent-android/gradle.properties', 'rb') as properties:
        configs.load(properties)

    return configs.get("version").data


def set_opbeans_agent_version(agent_version):
    print("Setting agent version: {}".format(agent_version))
    with open('../gradle.properties', 'r+b') as properties:
        opbeans_prop = Properties()
        opbeans_prop.load(properties)

        opbeans_prop["agent_version"] = agent_version

        properties.seek(0)
        properties.truncate(0)
        opbeans_prop.store(properties)


def run_tests():
    print("Running UI tests")
    run_command("./gradlew connectedAndroidTest", "..")


def clean_up():
    print("Cleaning up")
    run_command("rm -rf apm-agent-android")


def main():
    fetch_agent()
    build_agent()
    set_opbeans_agent_version(get_agent_version())
    run_tests()
    clean_up()


if __name__ == "__main__":
    main()
