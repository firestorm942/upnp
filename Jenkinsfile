pipeline {
  agent any
  stages {
    stage('Mvn') {
      steps {
        sh 'mvn package'
      }
    }
    stage('Move files') {
      steps {
        sh '''mv target/AutoUpnp*jar*.jar AutoUpnp.jar
mv AutoUpnpBungee/target/AutoUpnpBungee*jar*.jar AutoUpnpBungee.jar'''
      }
    }
    stage('Artifact') {
      steps {
        archiveArtifacts 'AutoUpnp.jar AutoUpnpBungee.jar'
      }
    }
  }
}