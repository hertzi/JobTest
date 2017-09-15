node {
    stage ('checkout'){
        checkout scm
    }
    
    stage ('package'){
        withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
            sh "mvn clean package -Dmaven.test.skip=true"
        }
    }
    
    stage ('test'){
        withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
            sh "mvn test"
        }
    }
    
    stage ('verify'){
        withEnv(["PATH+MAVEN=${tool 'M3'}/bin"]) {
            sh "mvn verify"
        }
    }
}