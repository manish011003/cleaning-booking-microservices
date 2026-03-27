pipeline {
    agent any

    tools {
        jdk 'JDK25'
        maven 'Maven-3.9.11'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        timeout(time: 60, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    parameters {
        booleanParam(name: 'RUN_CLEAN', defaultValue: true, description: 'Run clean before build/test')
        booleanParam(name: 'RUN_VERIFY', defaultValue: false, description: 'Run verify (includes failsafe) instead of test')
        booleanParam(name: 'RUN_SONAR', defaultValue: false, description: 'Run Sonar analysis for booking/professional services')
    }

    environment {
        MAVEN_OPTS = '-Xmx2048m -Djava.awt.headless=true'
        COMMON_GOALS = 'install -DskipTests'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Shared Module') {
            steps {
                script {
                    runMaven('common', buildGoals(env.COMMON_GOALS))
                }
            }
        }

        stage('Run Service Tests') {
            failFast true
            parallel {
                stage('professional-service') {
                    steps {
                        script {
                            runMaven('professional-service', buildGoals(testGoals()))
                        }
                    }
                }
                stage('booking-service') {
                    steps {
                        script {
                            runMaven('booking-service', buildGoals(testGoals()))
                        }
                    }
                }
                stage('api-gateway') {
                    steps {
                        script {
                            runMaven('api-gateway', buildGoals(testGoals()))
                        }
                    }
                }
                stage('discovery-server') {
                    steps {
                        script {
                            runMaven('discovery-server', buildGoals(testGoals()))
                        }
                    }
                }
                stage('config-server') {
                    steps {
                        script {
                            runMaven('config-server', buildGoals(testGoals()))
                        }
                    }
                }
            }
        }

        stage('Sonar Analysis (Optional)') {
            when {
                expression { return params.RUN_SONAR }
            }
            steps {
                script {
                    // Runs only for the modules that already define sonar properties/plugins.
                    runMaven('professional-service', buildGoals('sonar:sonar -DskipTests'))
                    runMaven('booking-service', buildGoals('sonar:sonar -DskipTests'))
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, keepLongStdio: true, testResults: '**/target/surefire-reports/*.xml, **/target/failsafe-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: '**/target/site/jacoco/**, **/target/surefire-reports/**, **/target/failsafe-reports/**'
        }
        unsuccessful {
            echo 'Pipeline failed. Check failed module stage and reports.'
        }
    }
}

def testGoals() {
    return params.RUN_VERIFY ? 'verify' : 'test'
}

def buildGoals(String goals) {
    return params.RUN_CLEAN ? "clean ${goals}" : goals
}

def runMaven(String moduleDir, String goals) {
    dir(moduleDir) {
        if (isUnix()) {
            if (fileExists('mvnw')) {
                sh "chmod +x mvnw && ./mvnw ${goals}"
            } else {
                sh "mvn ${goals}"
            }
            return
        }

        if (fileExists('mvnw.cmd')) {
            bat "mvnw.cmd ${goals}"
        } else {
            bat "mvn ${goals}"
        }
    }
}
