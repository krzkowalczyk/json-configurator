import groovy.json.JsonSlurperClassic

@NonCPS
def jsonParse(def json) {
    new groovy.json.JsonSlurperClassic().parseText(json)
}

repository['url'] = 'https://github.com/krzkowalczyk/json-configurator.git'
repository['branch'] = 'devel'
json_file = './fnetagent.json'


node {
  git branch: repository['branch'], url: repository['url']
  echo 'Hello World'

    def config =  jsonParse(readFile("config.json"))
    echo config.key.inspect()
    def version = config["iis_app"]["fnetagent"]

}

input message: '', parameters: [choice(choices: '1.0\n1.1\n1.2\n1.3', description: '', name: 'fnetagent')]
