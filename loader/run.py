import os
import subprocess


def run_command(command, from_dir=os.getcwd()):
    print("Running command: {}".format(command))
    process = subprocess.Popen(command.split(), cwd=from_dir, stdout=subprocess.PIPE)
    output, error = process.communicate()
    if error:
        raise ValueError("Command '{}' failed: {}".format(command, error))
    return output


def fetch_agent():
    print("Fetching APM Agent Android")
    run_command("git clone git@github.com:elastic/apm-agent-android.git")


def build_agent():
    print("Building APM Agent")
    run_command("./gradlew :android-plugin:jar", "./apm-agent-android")



fetch_agent()
build_agent()
