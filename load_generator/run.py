import argparse
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


def run_tests(args):
    log("Running UI tests")
    command = "./gradlew connectedAndroidTest -Pexporter_endpoint={} -Popbeans_endpoint={}".format(
        args.exporterEndpoint, args.opbeansEndpoint)

    if args.exporterAuthToken is not None:
        command = command + " -Pexporter_auth_token={}".format(args.exporterAuthToken)
    if args.opbeansAuthToken is not None:
        command = command + " -Popbeans_auth_token={}".format(args.opbeansAuthToken)

    run_command(command, "./opbeans-android")


def none_or_str(value):
    if value == 'None':
        return None
    return value


def parse_arguments():
    parser = argparse.ArgumentParser()
    parser.add_argument('--exporter-endpoint', dest='exporterEndpoint')
    parser.add_argument('--exporter-auth-token', dest='exporterAuthToken', type=none_or_str,
                        default=None)
    parser.add_argument('--opbeans-endpoint', dest='opbeansEndpoint')
    parser.add_argument('--opbeans-auth-token', dest='opbeansAuthToken', type=none_or_str,
                        default=None)
    return parser.parse_args()


def main():
    args = parse_arguments()
    build_agent()
    set_opbeans_agent_version(get_agent_version())
    run_tests(args)


if __name__ == "__main__":
    main()
