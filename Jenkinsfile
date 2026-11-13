
pipeline {
    agent any

    options {
        timestamps()
    }

    environment {
        APP_NAME = 'devops-todo-app'
        IMAGE_TAG = "devops-todo-app:${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Java Version') {
            steps {
                sh 'java -version'
            }
        }

        stage('Build and Test Java Maven App') {
            steps {
                sh '''
                    chmod +x java-todo-app/todo-app/mvnw
                    java-todo-app/todo-app/mvnw -B \
                      -f java-todo-app/todo-app/pom.xml clean verify
                '''
            }
        }

        stage('Install Python Dependencies') {
            steps {
                sh '''
                    python3 -m venv venv
                    . venv/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                '''
            }
        }

        stage('Run Python Unit Tests') {
            steps {
                sh '''
                    . venv/bin/activate
                    pytest -v --junitxml=test-results.xml
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t ${APP_NAME}:latest -t ${IMAGE_TAG} .
                '''
            }
        }

        stage('Generate Build Information') {
            steps {
                sh '''
                    echo "Application: DevOps Todo App" > build-info.txt
                    echo "Build Number: ${BUILD_NUMBER}" >> build-info.txt
                    echo "Git Commit: ${GIT_COMMIT}" >> build-info.txt
                '''
            }
        }

        stage('Archive Java JAR') {
            steps {
                archiveArtifacts artifacts: 'java-todo-app/todo-app/target/*.jar',
                                 fingerprint: true
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true,
                  testResults: 'test-results.xml'

            junit allowEmptyResults: true,
                  testResults: 'java-todo-app/todo-app/target/surefire-reports/TEST-*.xml'

            archiveArtifacts artifacts: 'test-results.xml,build-info.txt',
                             allowEmptyArchive: true,
                             fingerprint: true

            sh 'rm -rf venv'
        }

        success {
            echo 'Python and Java Maven pipelines completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check the stage logs.'
        }
    }
}
