#!/usr/bin/env groovy

import groovy.json.JsonSlurperClassic

@NonCPS
def jsonParse(def json) {
    new groovy.json.JsonSlurperClassic().parseText(json)
}

repository = [url:'https://github.com/krzkowalczyk/json-configurator.git', branch:'devel']
json_file = './sample/fnetagent.json'
def appsVersions = [:]
def inputFields = [:]
dataSource = [repository_url:'https://github.com/krzkowalczyk/json-configurator.git', branch:'devel', file:'sample/releases.json', loopElement:'versions', value: 'key']

node('worker') {
  git branch: repository['branch'], url: repository['url']

  echo 'Hello World'
    def releases
    def config =  jsonParse(readFile(json_file))
    for ( e in config['iis_app'] ) {
      print "key = ${e.key}, value = ${e.value}"
      releases = jsonParse(readFile("./sample/${e.key}-releases.json"))


      inputFields[e.key] = [$class: 'choice', choices: releases[dataSource['loopElement']].keySet().join('\n'), description: '', name: 'env']
    }
    def version = config["iis_app"]["fnetagent"]
    echo version.toString()

    echo inputFields.inspect()

    def userInput = input(
     id: 'userInput', message: 'Let\'s promote?', parameters: [
      inputFields.entrySet()
    ])
    echo ("Env: "+userInput['env'])
    echo ("Target: "+userInput['target'])

}

// input message: '', parameters: [
//   choice(choices: '1.0\n1.1\n1.2\n1.3', description: '', name: 'fnetagent'),
//   choice(choices: '1.0\n1.1\n1.2\n1.3', description: '', name: 'fnetagent')
// ]



// stage '\u2776 promotion'
//
// def inpt = [$class: 'TextParameterDefinition', defaultValue: 'uat', description: 'Environment', name: 'env']
//
// def userInput = input(
//  id: 'userInput', message: 'Let\'s promote?', parameters: [
//  [$class: 'TextParameterDefinition', defaultValue: 'uat', description: 'Environment', name: 'env'],
//  [$class: 'TextParameterDefinition', defaultValue: 'uat1', description: 'Target', name: 'target']
// ])
// echo ("Env: "+userInput['env'])
// echo ("Target: "+userInput['target'])
//
// and the result will be
