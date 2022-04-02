pipeline {
     agent any

     stages {
         stage('Acceptance Tests') {
             steps {
                 sh 'ls -la'
                 sh "mvn clean test"
             }
         }
     }
}
