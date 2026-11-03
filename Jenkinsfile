
pipeline {
    agent any

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                sh '''
                    python3 -m venv venv
                    . venv/bin/activate
                    pip install --upgrade pip
                    pip install -r requirements.txt
                '''
            }
        }

        stage('Run Unit Tests') {
            steps {
                sh '''
                    . venv/bin/activate
                    pytest -v --junitxml=test-results.xml
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
    }

    post {
        always {
            junit allowEmptyResults: true,
                  testResults: 'test-results.xml'

            archiveArtifacts artifacts: 'test-results.xml,build-info.txt',
                             allowEmptyArchive: true,
                             fingerprint: true

            sh 'rm -rf venv'
        }

        success {
            echo 'Pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check build logs.'
        }
    }
}
