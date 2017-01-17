import groovy.json.JsonSlurperClassic

@NonCPS
def jsonParse(def json) {
    new groovy.json.JsonSlurperClassic().parseText(json)
}

repository = [url:'https://github.com/krzkowalczyk/json-configurator.git', branch:'devel']
json_file = './fnetagent.json'


node {
  git branch: repository['branch'], url: repository['url']
  echo 'Hello World'

    def config =  jsonParse(readFile(json_file))
    for ( e in config['iis_app'] ) {
      print "key = ${e.key}, value = ${e.value}"
    }
    def version = config["iis_app"]["fnetagent"]
    echo version

}

input message: '', parameters: [choice(choices: '1.0\n1.1\n1.2\n1.3', description: '', name: 'fnetagent')]
